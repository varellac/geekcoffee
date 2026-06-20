package br.edu.cafeteria.modelo;

public abstract class Cliente {
    private String nome;
    private String cpf;
    protected double saldoXP; // protected para facilitar acesso pelas subclasses se necessario, ou uso de getters/setters

    public Cliente(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
        this.saldoXP = 0.0;
    }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public double getSaldoXP() { return saldoXP; }
    
    // Método para abater XP
    public void debitarXP(double xp) {
        this.saldoXP -= xp;
    }

    // Método abstrato de ganho de XP a ser implementado
    public abstract void adicionarXP(double valorGasto);
}
