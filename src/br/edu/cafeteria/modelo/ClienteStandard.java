package br.edu.cafeteria.modelo;

public class ClienteStandard extends Cliente {

    public ClienteStandard(String nome, String cpf) {
        super(nome, cpf);
    }

    @Override
    public void adicionarXP(double valorGasto) {
        // Acumula 1 ponto de XP para cada R$ 1,00 gasto.
        this.saldoXP += valorGasto * 1.0;
    }
}
