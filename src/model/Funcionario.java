package model;

public class Funcionario extends Entidade {
    private int codFuncionario;
    private String nome;
    private String cargo;
    private PostoSaude unidadePosto;

    public Funcionario() {}

    public Funcionario(int codFuncionario, String nome, String cargo, PostoSaude unidadePosto) {
        this.codFuncionario = codFuncionario;
        this.nome = nome;
        this.cargo = cargo;
        this.unidadePosto = unidadePosto;
    }

    @Override
    public int getCodigo() { return codFuncionario; }

    @Override
    public String getNome() { return nome; }

    public int getCodFuncionario() { return codFuncionario; }
    public void setCodFuncionario(int codFuncionario) { this.codFuncionario = codFuncionario; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }
    public PostoSaude getUnidadePosto() { return unidadePosto; }
    public void setUnidadePosto(PostoSaude unidadePosto) { this.unidadePosto = unidadePosto; }
}
