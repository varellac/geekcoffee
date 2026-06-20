package br.edu.cafeteria.app;

import br.edu.cafeteria.excecao.EstoqueInsuficienteException;
import br.edu.cafeteria.excecao.PontosInsuficientesException;
import br.edu.cafeteria.modelo.*;
import br.edu.cafeteria.servico.PromocaoEventoGeek;
import br.edu.cafeteria.servico.Promocional;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Bem-vindo a Byte & Brew ===");

        // 1. Cadastro e Gestão de Produtos (Simulação de CRUD)
        List<Produto> cardapio = new ArrayList<>();
        Comida leambasBread = new Comida("C001", "Lembas Bread", 15.0, 10, 5, true);
        Comida portalCake = new Comida("C002", "Portal Cake", 25.0, 5, 10, false);
        Bebida pocaoMana = new Bebida("B001", "Poção de Mana", 12.0, 20, "M", 0);
        Bebida cafeProgramador = new Bebida("B002", "Café do Programador", 18.0, 15, "G", 200);

        cardapio.add(leambasBread);
        cardapio.add(portalCake);
        cardapio.add(pocaoMana);
        cardapio.add(cafeProgramador);
        
        System.out.println("\n--- Cardápio Cadastrado ---");
        for (Produto p : cardapio) {
            System.out.println(p.getCodigo() + " - " + p.getNome() + " | R$ " + p.getPrecoBase() + " | Estoque: " + p.getQuantidadeEstoque());
        }

        // 2. Cadastro de Clientes
        ClienteStandard cliente1 = new ClienteStandard("Frodo Baggins", "111.222.333-44");
        ClienteVIP cliente2 = new ClienteVIP("Gandalf", "999.888.777-66");
        
        // Simular um saldo anterior para Gandalf
        cliente2.adicionarXP(25.0); // Gastou 25 antes, ganhou 50 XP (pois é VIP)
        System.out.println("\n--- Clientes Cadastrados ---");
        System.out.println("Cliente Standard: " + cliente1.getNome() + " | XP: " + cliente1.getSaldoXP());
        System.out.println("Cliente VIP: " + cliente2.getNome() + " | XP: " + cliente2.getSaldoXP());

        // 3. Registro e Fluxo de Vendas
        System.out.println("\n=== INICIANDO VENDA 1 (Cliente Casual) ===");
        Pedido pedido1 = new Pedido(); // Casual, sem cliente
        try {
            pedido1.adicionarItem(leambasBread, 2); // Polimorfismo de Sobrecarga
            pedido1.adicionarItem(pocaoMana); // Polimorfismo de Sobrecarga (default 1)
            
            double totalP1 = pedido1.calcularValorTotal();
            System.out.println("Pedido " + pedido1.getNumeroSequencial() + " total: R$ " + totalP1);
            pedido1.finalizarPedido();
            System.out.println("Venda 1 finalizada com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro na Venda 1: " + e.getMessage());
        }

        System.out.println("\n=== INICIANDO VENDA 2 (Testando Exceção de Estoque) ===");
        Pedido pedido2 = new Pedido(cliente1);
        try {
            pedido2.adicionarItem(portalCake, 10); // Estoque é apenas 5, vai dar erro
        } catch (EstoqueInsuficienteException e) {
            System.out.println("Exceção Capturada Corretamente: " + e.getMessage());
        }

        System.out.println("\n=== INICIANDO VENDA 3 (Cliente Standard c/ Promocao) ===");
        Pedido pedido3 = new Pedido(cliente1);
        try {
            pedido3.adicionarItem(cafeProgramador, 2); // R$ 36.00
            
            double totalBruto = pedido3.calcularValorTotal();
            System.out.println("Total Bruto Pedido " + pedido3.getNumeroSequencial() + ": R$ " + totalBruto);
            
            Promocional promo = new PromocaoEventoGeek();
            double desconto = promo.aplicarDesconto(pedido3); // Polimorfismo usando Interface e instanceof
            double totalLiquido = totalBruto - desconto;
            
            System.out.println("Desconto Dia do Orgulho Nerd (10% Bebidas): R$ " + desconto);
            System.out.println("Total a Pagar: R$ " + totalLiquido);
            
            pedido3.finalizarPedido(); // Cliente ganha XP sobre valor bruto? Vamos simular que é sobre o ganho real mas na implementação usamos o calcularValorTotal (bruto).
            System.out.println("Novo saldo de XP de " + cliente1.getNome() + ": " + cliente1.getSaldoXP());
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n=== INICIANDO VENDA 4 (Cliente VIP pagando com XP) ===");
        Pedido pedido4 = new Pedido(cliente2);
        try {
            pedido4.adicionarItem(pocaoMana, 1); // R$ 12.0
            double total = pedido4.calcularValorTotal();
            System.out.println("Total Pedido " + pedido4.getNumeroSequencial() + ": R$ " + total);
            System.out.println("Tentando pagar inteiramente com XP...");
            
            try {
                // R$ 12,00 custariam 120 XP (12 * 10). O VIP só tem 50 XP, vai dar erro!
                cliente2.pagarComXP(total); 
            } catch (PontosInsuficientesException e) {
                System.out.println("Exceção Capturada Corretamente: " + e.getMessage());
                System.out.println("Pagamento com dinheiro normal efetuado.");
                pedido4.finalizarPedido(); // Ganha mais XP pelo pagamento em dinheiro
                System.out.println("Novo saldo de XP de " + cliente2.getNome() + ": " + cliente2.getSaldoXP());
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
        System.out.println("\n=== VERIFICAÇÃO DE ESTOQUE APÓS VENDAS ===");
        System.out.println(leambasBread.getNome() + " | Estoque restante: " + leambasBread.getQuantidadeEstoque());
        System.out.println(pocaoMana.getNome() + " | Estoque restante: " + pocaoMana.getQuantidadeEstoque());
        System.out.println(cafeProgramador.getNome() + " | Estoque restante: " + cafeProgramador.getQuantidadeEstoque());
    }
}
