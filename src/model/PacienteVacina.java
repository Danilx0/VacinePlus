package model;

public class PacienteVacina {
    private int codApli;
    private Paciente paciente;
    private Vacina vacina;
    private Funcionario funcionario;
    private Lote lote;
    private PostoSaude posto;

    public PacienteVacina() {}

    public PacienteVacina(int codApli, Paciente paciente, Vacina vacina,
                          Funcionario funcionario, Lote lote, PostoSaude posto) {
        this.codApli = codApli;
        this.paciente = paciente;
        this.vacina = vacina;
        this.funcionario = funcionario;
        this.lote = lote;
        this.posto = posto;
    }

    public int getCodApli() { return codApli; }
    public void setCodApli(int codApli) { this.codApli = codApli; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Vacina getVacina() { return vacina; }
    public void setVacina(Vacina vacina) { this.vacina = vacina; }
    public Funcionario getFuncionario() { return funcionario; }
    public void setFuncionario(Funcionario funcionario) { this.funcionario = funcionario; }
    public Lote getLote() { return lote; }
    public void setLote(Lote lote) { this.lote = lote; }
    public PostoSaude getPosto() { return posto; }
    public void setPosto(PostoSaude posto) { this.posto = posto; }
}
