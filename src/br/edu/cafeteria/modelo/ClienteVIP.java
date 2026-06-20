package br.edu.cafeteria.modelo;

import br.edu.cafeteria.excecao.PontosInsuficientesException;

public class ClienteVIP extends Cliente {
    
    // 10 pontos de XP = R$ 1,00 de desconto
    public static final double TAXA_CONVERSAO = 10.0;

    public ClienteVIP(String nome, String cpf) {
        super(nome, cpf);
    }

    @Override
    public void adicionarXP(double valorGasto) {
        // Acumula 2 pontos de XP para cada R$ 1,00 gasto.
        this.saldoXP += valorGasto * 2.0;
    }

    public void pagarComXP(double valorTotal) {
        double xpNecessario = valorTotal * TAXA_CONVERSAO;
        if (this.saldoXP < xpNecessario) {
            throw new PontosInsuficientesException("Saldo de XP insuficiente. Necessário: " + xpNecessario + " XP, Atual: " + this.saldoXP + " XP.");
        }
        debitarXP(xpNecessario);
    }
}
