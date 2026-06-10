package model;

import java.time.LocalDate;

public class Vacina extends Entidade {
    private int codVacina;
    private String nome;
    private int quantidade;
    private LocalDate validade;
    private boolean ativa;
    private Fabricante fabricante;

    public Vacina() {}

    public Vacina(int codVacina, String nome, int quantidade, LocalDate validade, boolean ativa, Fabricante fabricante) {
        this.codVacina = codVacina;
        this.nome = nome;
        this.quantidade = quantidade;
        this.validade = validade;
        this.ativa = ativa;
        this.fabricante = fabricante;
    }

    public boolean estaVencida() {
        return validade != null && LocalDate.now().isAfter(validade);
    }

    @Override
    public int getCodigo() { return codVacina; }

    @Override
    public String getNome() { return nome; }

    public int getCodVacina() { return codVacina; }
    public void setCodVacina(int codVacina) { this.codVacina = codVacina; }
    public void setNome(String nome) { this.nome = nome; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public LocalDate getValidade() { return validade; }
    public void setValidade(LocalDate validade) { this.validade = validade; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    public Fabricante getFabricante() { return fabricante; }
    public void setFabricante(Fabricante fabricante) { this.fabricante = fabricante; }
}
