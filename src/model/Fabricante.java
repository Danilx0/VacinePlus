package model;

public class Fabricante extends Entidade {
    private int codFabricante;
    private String nome;

    public Fabricante() {}

    public Fabricante(int codFabricante, String nome) {
        this.codFabricante = codFabricante;
        this.nome = nome;
    }

    @Override
    public int getCodigo() { return codFabricante; }

    @Override
    public String getNome() { return nome; }

    public int getCodFabricante() { return codFabricante; }
    public void setCodFabricante(int codFabricante) { this.codFabricante = codFabricante; }
    public void setNome(String nome) { this.nome = nome; }
}
