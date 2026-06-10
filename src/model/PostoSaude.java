package model;

public class PostoSaude extends Entidade {
    private int codPosto;
    private String nomePosto;
    private String endereco;
    private String cidade;
    private String estado;
    private String telefonePosto;
    private boolean funcionamento;

    public PostoSaude() {}

    public PostoSaude(int codPosto, String nomePosto, String endereco, String cidade,
                      String estado, String telefonePosto, boolean funcionamento) {
        this.codPosto = codPosto;
        this.nomePosto = nomePosto;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.telefonePosto = telefonePosto;
        this.funcionamento = funcionamento;
    }

    @Override
    public int getCodigo() { return codPosto; }

    @Override
    public String getNome() { return nomePosto; }

    public int getCodPosto() { return codPosto; }
    public void setCodPosto(int codPosto) { this.codPosto = codPosto; }
    public void setNomePosto(String nomePosto) { this.nomePosto = nomePosto; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTelefonePosto() { return telefonePosto; }
    public void setTelefonePosto(String telefonePosto) { this.telefonePosto = telefonePosto; }
    public boolean isFuncionamento() { return funcionamento; }
    public void setFuncionamento(boolean funcionamento) { this.funcionamento = funcionamento; }
}
