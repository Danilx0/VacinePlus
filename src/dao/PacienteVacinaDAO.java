package dao;

import model.*;
import util.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteVacinaDAO implements DAO<PacienteVacina> {

    @Override
    public void inserir(PacienteVacina obj) throws SQLException {
        Vacina vacina = obj.getVacina();
        if (vacina.estaVencida()) {
            throw new IllegalStateException(
                "Vacina '" + vacina.getNome() + "' está vencida (validade: " + vacina.getValidade() + "). Cadastro não permitido."
            );
        }

        Connection conn = Conexao.getConexao();
        String sql = "INSERT INTO paciente_vacina (cod_apli, cod_paciente, cod_vacina, cod_funcionario, cod_lote, cod_posto) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, obj.getCodApli());
            ps.setInt(2, obj.getPaciente().getCodPaciente());
            ps.setInt(3, obj.getVacina().getCodVacina());
            ps.setInt(4, obj.getFuncionario().getCodFuncionario());
            ps.setLong(5, obj.getLote().getCodLote());
            ps.setInt(6, obj.getPosto().getCodPosto());
            ps.executeUpdate();
        }

        String atualizarQuantidade = "UPDATE vacina SET quantidade = quantidade - 1 WHERE cod_vacina = ?";
        try (PreparedStatement ps = conn.prepareStatement(atualizarQuantidade)) {
            ps.setInt(1, obj.getVacina().getCodVacina());
            ps.executeUpdate();
        }
    }

    @Override
    public List<PacienteVacina> listarTodos() throws SQLException {
        List<PacienteVacina> lista = new ArrayList<>();
        String sql = "SELECT ROW_NUMBER() OVER () AS cod_apli, " +
                     "\"Paciente\" AS nome_paciente, " +
                     "\"Vacina\" AS nome_vacina, " +
                     "\"Aplicado por:\" AS nome_func, " +
                     "\"Lote\" AS num_lote, " +
                     "localidade AS nome_posto " +
                     "FROM apli_vacina_paciente";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapearView(rs));
            }
        }
        return lista;
    }

    public int proximoCodigo() throws SQLException {
        String sql = "SELECT COALESCE(MAX(cod_apli), 0) + 1 FROM paciente_vacina";
        try (Statement st = Conexao.getConexao().createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        }
        return 1;
    }

    private PacienteVacina mapearView(ResultSet rs) throws SQLException {
        Paciente paciente = new Paciente();
        paciente.setNome(rs.getString("nome_paciente"));

        Vacina vacina = new Vacina();
        vacina.setNome(rs.getString("nome_vacina"));

        Funcionario func = new Funcionario();
        func.setNome(rs.getString("nome_func"));

        Lote lote = new Lote();
        lote.setNumLote(rs.getLong("num_lote"));

        PostoSaude posto = new PostoSaude();
        posto.setNomePosto(rs.getString("nome_posto"));

        return new PacienteVacina(rs.getInt("cod_apli"), paciente, vacina, func, lote, posto);
    }
}
