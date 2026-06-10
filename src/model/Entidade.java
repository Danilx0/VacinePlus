package model;

public abstract class Entidade {
    public abstract int getCodigo();
    public abstract String getNome();

    @Override
    public String toString() {
        return getNome();
    }
}
