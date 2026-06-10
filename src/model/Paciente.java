package model;

import java.time.LocalDate;

public class Paciente extends Entidade {
    private int codPaciente;
    private String cpf;
    private String nome;
    private LocalDate dataNascimento;
    private String sexo;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;

    public Paciente() {}

    public Paciente(int codPaciente, String cpf, String nome, LocalDate dataNascimento,
                    String sexo, String endereco, String cidade, String estado, String cep) {
        this.codPaciente = codPaciente;
        this.cpf = cpf;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.sexo = sexo;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    @Override
    public int getCodigo() { return codPaciente; }

    @Override
    public String getNome() { return nome; }

    public int getCodPaciente() { return codPaciente; }
    public void setCodPaciente(int codPaciente) { this.codPaciente = codPaciente; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public void setNome(String nome) { this.nome = nome; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }
}
