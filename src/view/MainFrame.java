package view;

import dao.*;
import model.*;
import util.Conexao;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainFrame extends JFrame {

    private static final Color COR_PRIMARIA   = new Color(0, 102, 153);
    private static final Color COR_SECUNDARIA = new Color(0, 153, 204);
    private static final Color COR_PERIGO     = new Color(180, 30, 30);
    private static final Color COR_SUCESSO    = new Color(30, 130, 60);
    private static final Color COR_FUNDO      = new Color(245, 248, 252);
    private static final Color COR_BRANCO     = Color.WHITE;
    private static final Color COR_PRETO      = Color.BLACK;
    private static final Font  FONTE_TITULO   = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  FONTE_LABEL    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONTE_BOLD     = new Font("Segoe UI", Font.BOLD, 13);
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JTabbedPane abas;

    public MainFrame() {
        setTitle("VacinePlus — Sistema de Vacinação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 720);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COR_FUNDO);
        construirUI();
    }

    private void construirUI() {
        JPanel cabecalho = new JPanel(new BorderLayout());
        cabecalho.setBackground(COR_PRIMARIA);
        cabecalho.setBorder(new EmptyBorder(16, 24, 16, 24));
        JLabel titulo = new JLabel("VacinePlus");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(COR_BRANCO);
        JLabel sub = new JLabel("Sistema de Registro de Vacinação");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(new Color(200, 230, 255));
        cabecalho.add(titulo, BorderLayout.WEST);
        cabecalho.add(sub, BorderLayout.EAST);

        abas = new JTabbedPane();
        abas.setFont(FONTE_BOLD);
        abas.setBackground(COR_FUNDO);
        abas.addTab("Registrar Aplicação", criarPainelRegistro());
        abas.addTab("Histórico",            criarPainelHistorico());
        abas.addTab("Gerenciar",            criarPainelGerenciar());

        setLayout(new BorderLayout());
        add(cabecalho, BorderLayout.NORTH);
        add(abas, BorderLayout.CENTER);
    }

    private JPanel criarPainelRegistro() {
        JPanel painel = new JPanel(new BorderLayout(0, 16));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(COR_BRANCO);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            new EmptyBorder(24, 24, 24, 24)));

        GridBagConstraints gbc = gbc();

        JComboBox<Paciente>   cbPaciente   = new JComboBox<>();
        JComboBox<Vacina>     cbVacina     = new JComboBox<>();
        JComboBox<Funcionario> cbFuncionario = new JComboBox<>();
        JComboBox<Lote>       cbLote       = new JComboBox<>();
        JComboBox<PostoSaude> cbPosto      = new JComboBox<>();
        JLabel lblValidade = new JLabel(" ");
        lblValidade.setFont(FONTE_LABEL);

        carregarCombos(cbPaciente, cbVacina, cbFuncionario, cbLote, cbPosto);

        cbVacina.addActionListener(e -> {
            Vacina v = (Vacina) cbVacina.getSelectedItem();
            if (v != null) {
                String texto = "Validade: " + (v.getValidade() != null ? v.getValidade().format(FMT) : "N/A");
                if (v.estaVencida()) {
                    lblValidade.setForeground(COR_PERIGO);
                    lblValidade.setText(" " + texto + " — VENCIDA");
                } else {
                    lblValidade.setForeground(COR_SUCESSO);
                    lblValidade.setText(" " + texto + " — Válida");
                }
                atualizarLotes(v, cbLote);
            }
        });

        adicionarCampo(card, gbc, 0, "Paciente:",       cbPaciente);
        adicionarCampo(card, gbc, 1, "Vacina:",         cbVacina);
        gbc.gridx = 1; gbc.gridy = 2; card.add(lblValidade, gbc);
        adicionarCampo(card, gbc, 3, "Lote:",           cbLote);
        adicionarCampo(card, gbc, 4, "Funcionário:",    cbFuncionario);
        adicionarCampo(card, gbc, 5, "Posto de Saúde:", cbPosto);

        JButton btnSalvar = botao("Registrar Aplicação", COR_PRIMARIA);
        JButton btnLimpar = botao("Limpar", new Color(120, 120, 130));
        btnSalvar.addActionListener(e -> registrarAplicacao(cbPaciente, cbVacina, cbFuncionario, cbLote, cbPosto));
        btnLimpar.addActionListener(e -> carregarCombos(cbPaciente, cbVacina, cbFuncionario, cbLote, cbPosto));

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setBackground(COR_BRANCO);
        botoes.add(btnLimpar);
        botoes.add(btnSalvar);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 8, 4, 8);
        card.add(botoes, gbc);

        painel.add(card, BorderLayout.NORTH);
        return painel;
    }


    private JPanel criarPainelHistorico() {
        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(20, 24, 20, 24));

        String[] colunas = {"Atendimento.", "Paciente", "Vacina", "Lote", "Aplicado por:", "Posto"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabela = estilizarTabela(new JTable(modelo));
        JComboBox<PostoSaude> cbPostoTotal = new JComboBox<>();
        cbPostoTotal.setFont(FONTE_LABEL);
        cbPostoTotal.setPreferredSize(new Dimension(260, 32));
        JLabel lblTotalPosto = new JLabel("Total: 0");
        lblTotalPosto.setFont(FONTE_BOLD);
        carregarPostosTotal(cbPostoTotal, lblTotalPosto);
        cbPostoTotal.addActionListener(e -> atualizarTotalPosto(cbPostoTotal, lblTotalPosto));

        JButton btnAtualizar = botao("Atualizar", COR_SECUNDARIA);
        btnAtualizar.addActionListener(e -> {
            carregarHistorico(modelo);
            atualizarTotalPosto(cbPostoTotal, lblTotalPosto);
        });

        JPanel topo = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        topo.setBackground(COR_FUNDO);
        topo.add(new JLabel("Posto:"));
        topo.add(cbPostoTotal);
        topo.add(lblTotalPosto);
        topo.add(btnAtualizar);

        painel.add(topo, BorderLayout.NORTH);
        painel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        carregarHistorico(modelo);
        return painel;
    }


    private JPanel criarPainelGerenciar() {
        JPanel painel = new JPanel(new BorderLayout(0, 0));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JTabbedPane subAbas = new JTabbedPane();
        subAbas.setFont(FONTE_BOLD);
        subAbas.setBackground(COR_FUNDO);
        subAbas.addTab("Vacinas",   criarSubPainelVacinas());
        subAbas.addTab("Pacientes", criarSubPainelPacientes());

        painel.add(subAbas, BorderLayout.CENTER);
        return painel;
    }


    private JPanel criarSubPainelVacinas() {
        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(16, 0, 0, 0));

        String[] colunas = {"Cód.", "Nome", "Fabricante", "Qtd.", "Validade", "Ativa"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = estilizarTabela(new JTable(modelo));


        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(COR_BRANCO);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            new EmptyBorder(16, 20, 16, 20)));

        GridBagConstraints gbc = gbc();

        JTextField tfNomeVacina   = campo(200);
        JTextField tfNomeFab      = campo(200);
        JTextField tfQuantidade   = campo(80);
        JTextField tfValidade     = campo(100);
        tfValidade.setToolTipText("dd/MM/yyyy");
        JCheckBox  cbAtiva        = new JCheckBox("Ativa", true);
        cbAtiva.setBackground(COR_BRANCO);
        cbAtiva.setFont(FONTE_LABEL);

        JLabel lblAviso = new JLabel(" ");
        lblAviso.setFont(FONTE_LABEL);

        adicionarCampo(formCard, gbc, 0, "Nome da Vacina:",  tfNomeVacina);
        adicionarCampo(formCard, gbc, 1, "Fabricante:",      tfNomeFab);
        adicionarCampo(formCard, gbc, 2, "Quantidade:",      tfQuantidade);
        adicionarCampo(formCard, gbc, 3, "Validade (dd/MM/yyyy):", tfValidade);

        gbc.gridx = 1; gbc.gridy = 4; formCard.add(cbAtiva, gbc);
        gbc.gridx = 1; gbc.gridy = 5; formCard.add(lblAviso, gbc);

        JButton btnCadastrar = botao("Cadastrar Vacina", COR_SUCESSO);
        JButton btnExcluir   = botao("Excluir Selecionada", COR_PERIGO);
        JButton btnAtualizar = botao("Atualizar", COR_SECUNDARIA);

        btnCadastrar.addActionListener(e -> {
            try {
                String nomeVacina = tfNomeVacina.getText().trim();
                String nomeFab    = tfNomeFab.getText().trim();
                String qtdStr     = tfQuantidade.getText().trim();
                String valStr     = tfValidade.getText().trim();

                if (nomeVacina.isEmpty() || nomeFab.isEmpty() || qtdStr.isEmpty() || valStr.isEmpty()) {
                    lblAviso.setForeground(COR_PERIGO);
                    lblAviso.setText("Preencha todos os campos.");
                    return;
                }

                int quantidade = Integer.parseInt(qtdStr);
                LocalDate validade = LocalDate.parse(valStr, FMT);

                CrudDAO dao = new CrudDAO();
                int codVacina = dao.proximoCodigoVacina();
                int codFab    = dao.proximoCodigoFabricante();

                dao.inserirViaProc(codFab, nomeFab, codVacina, nomeVacina, quantidade, validade, cbAtiva.isSelected());

                lblAviso.setForeground(COR_SUCESSO);
                lblAviso.setText("Vacina cadastrada com sucesso.");
                tfNomeVacina.setText(""); tfNomeFab.setText("");
                tfQuantidade.setText(""); tfValidade.setText("");
                cbAtiva.setSelected(true);
                carregarTabelaVacinas(modelo);
                

            } catch (NumberFormatException ex) {
                lblAviso.setForeground(COR_PERIGO);
                lblAviso.setText("Quantidade deve ser um número inteiro.");
            } catch (Exception ex) {
                lblAviso.setForeground(COR_PERIGO);
                lblAviso.setText(ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha < 0) {
                JOptionPane.showMessageDialog(this, "Selecione uma vacina na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int cod = (int) modelo.getValueAt(linha, 0);
            String nome = (String) modelo.getValueAt(linha, 1);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Excluir a vacina \"" + nome + "\"?\nIsso removerá os lotes vinculados.", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            try {
                new CrudDAO().excluirVacina(cod);
                carregarTabelaVacinas(modelo);
            } catch (SQLException ex) {
                mostrarErro("Erro ao excluir: " + ex.getMessage());
            }
        });

        btnAtualizar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha < 0) {
                carregarTabelaVacinas(modelo);
                return;
            }
            try {
                int cod           = (int)    modelo.getValueAt(linha, 0);
                String nome       =          tfNomeVacina.getText().trim();
                int quantidade    = Integer.parseInt(tfQuantidade.getText().trim());
                LocalDate val     = LocalDate.parse(tfValidade.getText().trim(), FMT);
                boolean ativa     = cbAtiva.isSelected();

                new CrudDAO().atualizarVacina(cod, nome, quantidade, val, ativa, tfNomeFab.getText().trim());
                carregarTabelaVacinas(modelo);
            } catch (NumberFormatException ex) {
                mostrarErro("Quantidade deve ser um número inteiro.");
            } catch (Exception ex) {
                mostrarErro(ex.getMessage());
            }
        });

        JPanel barraDir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        barraDir.setBackground(COR_FUNDO);
        barraDir.add(btnAtualizar);
        barraDir.add(btnExcluir);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setBackground(COR_FUNDO);
        centro.add(barraDir, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setBackground(COR_BRANCO);
        botoes.add(btnCadastrar);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 4, 8);
        formCard.add(botoes, gbc);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formCard, centro);
        split.setResizeWeight(0.42);
        split.setBorder(null);
        split.setBackground(COR_FUNDO);

        painel.add(split, BorderLayout.CENTER);
        carregarTabelaVacinas(modelo);
        return painel;
    }


    private JPanel criarSubPainelPacientes() {
        JPanel painel = new JPanel(new BorderLayout(0, 12));
        painel.setBackground(COR_FUNDO);
        painel.setBorder(new EmptyBorder(16, 0, 0, 0));

        String[] colunas = {"Cód.", "Nome", "CPF", "Nascimento", "Sexo", "Cidade", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tabela = estilizarTabela(new JTable(modelo));

        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBackground(COR_BRANCO);
        formCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 230, 240), 1),
            new EmptyBorder(16, 20, 16, 20)));

        GridBagConstraints gbc = gbc();

        JTextField tfNome       = campo(220);
        JTextField tfCpf        = campo(130);
        JTextField tfNasc       = campo(100);
        tfNasc.setToolTipText("dd/MM/yyyy");
        String[] sexos = {"Masculino", "Feminino", "Outro"};
        JComboBox<String> cbSexo = new JComboBox<>(sexos);
        cbSexo.setFont(FONTE_LABEL);
        JTextField tfEndereco   = campo(220);
        JTextField tfCidade     = campo(160);
        JTextField tfEstado     = campo(40);
        JTextField tfCep        = campo(80);

        JLabel lblAviso = new JLabel(" ");
        lblAviso.setFont(FONTE_LABEL);

        adicionarCampo(formCard, gbc, 0, "Nome:",           tfNome);
        adicionarCampo(formCard, gbc, 1, "CPF:",            tfCpf);
        adicionarCampo(formCard, gbc, 2, "Nascimento (dd/MM/yyyy):", tfNasc);
        adicionarCampo(formCard, gbc, 3, "Sexo:",           cbSexo);
        adicionarCampo(formCard, gbc, 4, "Endereço:",       tfEndereco);
        adicionarCampo(formCard, gbc, 5, "Cidade:",         tfCidade);
        adicionarCampo(formCard, gbc, 6, "Estado (UF):",    tfEstado);
        adicionarCampo(formCard, gbc, 7, "CEP:",            tfCep);

        gbc.gridx = 1; gbc.gridy = 8; formCard.add(lblAviso, gbc);

        JButton btnCadastrar = botao("Cadastrar Paciente", COR_SUCESSO);
        JButton btnExcluir   = botao("Excluir Selecionado", COR_PERIGO);
        JButton btnAtualizar = botao("Atualizar", COR_SECUNDARIA);

        btnCadastrar.addActionListener(e -> {
            try {
                String nome = tfNome.getText().trim();
                String cpf  = tfCpf.getText().trim();
                String nasc = tfNasc.getText().trim();
                String sexo = (String) cbSexo.getSelectedItem();

                if (nome.isEmpty() || cpf.isEmpty() || nasc.isEmpty()) {
                    lblAviso.setForeground(COR_PERIGO);
                    lblAviso.setText("Nome, CPF e Nascimento são obrigatórios.");
                    return;
                }

                LocalDate dataNasc = LocalDate.parse(nasc, FMT);
                CrudDAO aux = new CrudDAO();
                int cod = aux.proximoCodigoPaciente();

                Paciente p = new Paciente(cod, cpf, nome, dataNasc, sexo,
                    tfEndereco.getText().trim(), tfCidade.getText().trim(),
                    tfEstado.getText().trim().toUpperCase(), tfCep.getText().trim());

                aux.inserirPaciente(p);
                lblAviso.setForeground(COR_SUCESSO);
                lblAviso.setText("✓ Paciente cadastrado com sucesso.");
                tfNome.setText(""); tfCpf.setText(""); tfNasc.setText("");
                tfEndereco.setText(""); tfCidade.setText(""); tfEstado.setText(""); tfCep.setText("");
                carregarTabelaPacientes(modelo);

            } catch (Exception ex) {
                lblAviso.setForeground(COR_PERIGO);
                lblAviso.setText(ex.getMessage());
            }
        });

        btnExcluir.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha < 0) {
                JOptionPane.showMessageDialog(this, "Selecione um paciente na tabela.", "Atenção", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int cod = (int) modelo.getValueAt(linha, 0);
            String nome = (String) modelo.getValueAt(linha, 1);
            int confirm = JOptionPane.showConfirmDialog(this,
                "Excluir o paciente \"" + nome + "\"?\nOs registros de aplicação vinculados também serão removidos.",
                "Confirmar exclusão", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION) return;
            try {
                new CrudDAO().excluirPaciente(cod);
                carregarTabelaPacientes(modelo);
            } catch (SQLException ex) {
                mostrarErro("Erro ao excluir: " + ex.getMessage());
            }
        });

        btnAtualizar.addActionListener(e -> {
            int linha = tabela.getSelectedRow();
            if (linha < 0) {
                carregarTabelaPacientes(modelo);
                return;
            }
            try {
                int cod            = (int) modelo.getValueAt(linha, 0);
                String nome        = tfNome.getText().trim();
                String cpf         = tfCpf.getText().trim();
                LocalDate dataNasc = LocalDate.parse(tfNasc.getText().trim(), FMT);
                String sexo        = (String) cbSexo.getSelectedItem();
                String endereco    = tfEndereco.getText().trim();
                String cidade      = tfCidade.getText().trim();
                String estado      = tfEstado.getText().trim().toUpperCase();
                String cep         = tfCep.getText().trim();

                new CrudDAO().atualizarPaciente(cod, nome, cpf, dataNasc, sexo, endereco, cidade, estado, cep);
                carregarTabelaPacientes(modelo);
            } catch (Exception ex) {
                mostrarErro(ex.getMessage());
            }
        });

        JPanel barraDir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        barraDir.setBackground(COR_FUNDO);
        barraDir.add(btnAtualizar);
        barraDir.add(btnExcluir);

        JPanel centro = new JPanel(new BorderLayout(0, 8));
        centro.setBackground(COR_FUNDO);
        centro.add(barraDir, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabela), BorderLayout.CENTER);

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setBackground(COR_BRANCO);
        botoes.add(btnCadastrar);

        gbc.gridx = 0; gbc.gridy = 9; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 4, 8);
        formCard.add(botoes, gbc);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formCard, centro);
        split.setResizeWeight(0.55);
        split.setBorder(null);
        split.setBackground(COR_FUNDO);

        painel.add(split, BorderLayout.CENTER);
        carregarTabelaPacientes(modelo);
        return painel;
    }


    private void carregarTabelaVacinas(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            for (Vacina v : new CrudDAO().listarVacinas()) {
                modelo.addRow(new Object[]{
                    v.getCodVacina(),
                    v.getNome(),
                    v.getFabricante() != null ? v.getFabricante().getNome() : "",
                    v.getQuantidade(),
                    v.getValidade() != null ? v.getValidade().format(FMT) : "",
                    v.isAtiva() ? "Sim" : "Não"
                });
            }
        } catch (SQLException ex) { mostrarErro(ex.getMessage()); }
    }

    private void carregarTabelaPacientes(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            for (Paciente p : new CrudDAO().listarPacientes()) {
                modelo.addRow(new Object[]{
                    p.getCodPaciente(),
                    p.getNome(),
                    p.getCpf(),
                    p.getDataNascimento() != null ? p.getDataNascimento().format(FMT) : "",
                    p.getSexo(),
                    p.getCidade(),
                    p.getEstado()
                });
            }
        } catch (SQLException ex) { mostrarErro(ex.getMessage()); }
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints gbc, int linha, String rotulo, JComponent comp) {
        JLabel label = new JLabel(rotulo);
        label.setFont(FONTE_BOLD);
        label.setForeground(new Color(60, 70, 90));
        gbc.gridx = 0; gbc.gridy = linha; gbc.gridwidth = 1; gbc.weightx = 0.25;
        painel.add(label, gbc);
        comp.setFont(FONTE_LABEL);
        gbc.gridx = 1; gbc.weightx = 0.75;
        painel.add(comp, gbc);
    }

    private GridBagConstraints gbc() {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 8, 5, 8);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.WEST;
        return g;
    }

    private JTextField campo(int largura) {
        JTextField tf = new JTextField();
        tf.setPreferredSize(new Dimension(largura, 30));
        tf.setFont(FONTE_LABEL);
        return tf;
    }

    private JButton botao(String texto, Color cor) {
        JButton btn = new JButton(texto);
        btn.setFont(FONTE_BOLD);
        btn.setBackground(cor);
        btn.setForeground(COR_BRANCO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(200, 34));
        return btn;
    }

    private JTable estilizarTabela(JTable tabela) {
        tabela.setFont(FONTE_LABEL);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(FONTE_BOLD);
        tabela.getTableHeader().setBackground(COR_PRIMARIA);
        tabela.getTableHeader().setForeground(COR_PRETO);
        tabela.setSelectionBackground(new Color(210, 235, 255));
        tabela.setGridColor(new Color(230, 235, 245));
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return tabela;
    }

    @SuppressWarnings("unchecked")
    private void carregarCombos(JComboBox<Paciente> cbPac, JComboBox<Vacina> cbVac,
                                 JComboBox<Funcionario> cbFunc, JComboBox<Lote> cbLote,
                                 JComboBox<PostoSaude> cbPosto) {
        try {
            cbPac.removeAllItems();
            for (Paciente p : new PacienteDAO().listarTodos()) cbPac.addItem(p);
            cbVac.removeAllItems();
            for (Vacina v : new VacinaDAO().listarTodos()) cbVac.addItem(v);
            cbFunc.removeAllItems();
            for (Funcionario f : new FuncionarioDAO().listarTodos()) cbFunc.addItem(f);
            cbPosto.removeAllItems();
            for (PostoSaude ps : new PostoSaudeDAO().listarTodos()) cbPosto.addItem(ps);
            cbLote.removeAllItems();
            if (cbVac.getSelectedItem() != null)
                atualizarLotes((Vacina) cbVac.getSelectedItem(), cbLote);
        } catch (SQLException ex) { mostrarErro("Erro ao carregar dados: " + ex.getMessage()); }
    }

    private void atualizarLotes(Vacina vacina, JComboBox<Lote> cbLote) {
        try {
            cbLote.removeAllItems();
            for (Lote l : new LoteDAO().listarPorVacina(vacina.getCodVacina())) cbLote.addItem(l);
        } catch (SQLException ex) { mostrarErro("Erro ao carregar lotes: " + ex.getMessage()); }
    }

    private void registrarAplicacao(JComboBox<Paciente> cbPac, JComboBox<Vacina> cbVac,
                                     JComboBox<Funcionario> cbFunc, JComboBox<Lote> cbLote,
                                     JComboBox<PostoSaude> cbPosto) {
        Paciente   paciente    = (Paciente)   cbPac.getSelectedItem();
        Vacina     vacina      = (Vacina)     cbVac.getSelectedItem();
        Funcionario funcionario = (Funcionario) cbFunc.getSelectedItem();
        Lote       lote        = (Lote)       cbLote.getSelectedItem();
        PostoSaude posto       = (PostoSaude) cbPosto.getSelectedItem();

        if (paciente == null || vacina == null || funcionario == null || lote == null || posto == null) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            PacienteVacinaDAO dao = new PacienteVacinaDAO();
            int codigo = dao.proximoCodigo();
            dao.inserir(new PacienteVacina(codigo, paciente, vacina, funcionario, lote, posto));
            JOptionPane.showMessageDialog(this, "Aplicação registrada com sucesso!\nCódigo: " + codigo, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Vacina Vencida", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) { mostrarErro("Erro ao registrar: " + ex.getMessage()); }
    }

    private void carregarPostosTotal(JComboBox<PostoSaude> cbPosto, JLabel lblTotal) {
        try {
            cbPosto.removeAllItems();
            for (PostoSaude posto : new PostoSaudeDAO().listarTodos()) cbPosto.addItem(posto);
            atualizarTotalPosto(cbPosto, lblTotal);
        } catch (SQLException ex) {
            mostrarErro("Erro ao carregar postos: " + ex.getMessage());
        }
    }

    private void atualizarTotalPosto(JComboBox<PostoSaude> cbPosto, JLabel lblTotal) {
        PostoSaude posto = (PostoSaude) cbPosto.getSelectedItem();
        if (posto == null) {
            lblTotal.setText("Total: 0");
            return;
        }
        try {
            int total = new PostoSaudeDAO().totalAplicacoes(posto.getCodPosto());
            lblTotal.setText("Quantidade aplicadas por posto: " + total);
        } catch (SQLException ex) {
            mostrarErro("Erro ao carregar total: " + ex.getMessage());
        }
    }

    private void carregarHistorico(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            for (PacienteVacina pv : new PacienteVacinaDAO().listarTodos()) {
                modelo.addRow(new Object[]{
                    pv.getCodApli(), pv.getPaciente().getNome(), pv.getVacina().getNome(),
                    pv.getLote().getNumLote(), pv.getFuncionario().getNome(), pv.getPosto().getNome()
                });
            }
        } catch (SQLException ex) { mostrarErro("Erro ao carregar histórico: " + ex.getMessage()); }
    }

    private void mostrarErro(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}
