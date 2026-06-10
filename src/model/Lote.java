package model;

import java.time.LocalDate;

public class Lote extends Entidade {
    private int codLote;
    private long numLote;
    private LocalDate dataLote;
    private int qtdaEnviado;
    private Vacina vacina;

    public Lote() {}

    public Lote(int codLote, long numLote, LocalDate dataLote, int qtdaEnviado, Vacina vacina) {
        this.codLote = codLote;
        this.numLote = numLote;
        this.dataLote = dataLote;
        this.qtdaEnviado = qtdaEnviado;
        this.vacina = vacina;
    }

    @Override
    public int getCodigo() { return codLote; }

    @Override
    public String getNome() { return "Lote #" + numLote; }

    public int getCodLote() { return codLote; }
    public void setCodLote(int codLote) { this.codLote = codLote; }
    public long getNumLote() { return numLote; }
    public void setNumLote(long numLote) { this.numLote = numLote; }
    public LocalDate getDataLote() { return dataLote; }
    public void setDataLote(LocalDate dataLote) { this.dataLote = dataLote; }
    public int getQtdaEnviado() { return qtdaEnviado; }
    public void setQtdaEnviado(int qtdaEnviado) { this.qtdaEnviado = qtdaEnviado; }
    public Vacina getVacina() { return vacina; }
    public void setVacina(Vacina vacina) { this.vacina = vacina; }
}
