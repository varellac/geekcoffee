package br.edu.cafeteria.app;

import br.edu.cafeteria.excecao.EstoqueInsuficienteException;
import br.edu.cafeteria.excecao.PontosInsuficientesException;
import br.edu.cafeteria.modelo.*;
import br.edu.cafeteria.servico.PromocaoEventoGeek;
import br.edu.cafeteria.servico.Promocional;

import java.util.ArrayList;
import java.util.List;

public class Main {
    
    // Listas simulando o Banco de Dados (CRUD)
    private static List<Produto> repositorioProdutos = new ArrayList<>();
    private static List<Cliente> repositorioClientes = new ArrayList<>();
    private static List<Atendente> repositorioAtendentes = new ArrayList<>();
    private static List<Pedido> repositorioPedidos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=== Bem-vindo a Byte & Brew ===");

        System.out.println("\n--- DEMONSTRACAO DO CRUD ---");
        // 1. CREATE
        System.out.println("-> CADASTRANDO (CREATE)");
        Produto leambasBread = new Comida("C001", "Lembas Bread", 15.0, 10, 5, true);
        Produto portalCake = new Comida("C002", "Portal Cake", 25.0, 5, 10, false);
        Produto pocaoMana = new Bebida("B001", "Pocao de Mana", 12.0, 20, "M", 0);
        Produto cafeProgramador = new Bebida("B002", "Cafe do Programador", 18.0, 15, "G", 200);
        
        adicionarProduto(leambasBread);
        adicionarProduto(portalCake);
        adicionarProduto(pocaoMana);
        adicionarProduto(cafeProgramador);
        
        Cliente cliente1 = new ClienteStandard("Frodo Baggins", "111.222.333-44");
        Cliente cliente2 = new ClienteVIP("Gandalf", "999.888.777-66");
        adicionarCliente(cliente1);
        adicionarCliente(cliente2);
        
        Atendente atendente1 = new Atendente("Sam", "A001");
        repositorioAtendentes.add(atendente1);

        // 2. READ (Pesquisa)
        System.out.println("\n-> PESQUISANDO (READ)");
        Produto pPesquisado = buscarProduto("B001");
        if (pPesquisado != null) {
            System.out.println("Produto encontrado: " + pPesquisado.getNome());
        }

        // 3. UPDATE (Atualizacao)
        System.out.println("\n-> ATUALIZANDO (UPDATE)");
        System.out.println("Preco antes: R$ " + leambasBread.getPrecoBase());
        atualizarPrecoProduto("C001", 16.50);
        System.out.println("Preco depois (Lembas Bread): R$ " + buscarProduto("C001").getPrecoBase());

        // 4. DELETE (Remocao)
        System.out.println("\n-> REMOVENDO (DELETE)");
        removerProduto("C002"); // Remove Portal Cake
        if (buscarProduto("C002") == null) {
            System.out.println("Portal Cake removido com sucesso!");
        }
        
        // Re-adicionando para a simulacao de vendas funcionar como antes
        adicionarProduto(portalCake);
        
        // --- INICIANDO FLUXO DE VENDAS ---
        System.out.println("\n--- FLUXO DE VENDAS ---");
        
        // Simular um saldo anterior para Gandalf
        cliente2.adicionarXP(25.0); // Ganhou 50 XP (pois e VIP)
        
        System.out.println("\n=== INICIANDO VENDA 1 (Cliente Casual) ===");
        Pedido pedido1 = new Pedido(atendente1); // Com atendente, sem cliente
        try {
            pedido1.adicionarItem(leambasBread, 2); 
            pedido1.adicionarItem(pocaoMana); 
            
            double totalP1 = pedido1.calcularValorTotal();
            System.out.println("Pedido " + pedido1.getNumeroSequencial() + " total: R$ " + totalP1);
            pedido1.finalizarPedido();
            repositorioPedidos.add(pedido1);
            System.out.println("Venda 1 finalizada com sucesso pelo atendente: " + pedido1.getAtendente().getNome());
        } catch (Exception e) {
            System.out.println("Erro na Venda 1: " + e.getMessage());
        }

        System.out.println("\n=== INICIANDO VENDA 2 (Testando Excecao de Estoque) ===");
        Pedido pedido2 = new Pedido(atendente1, cliente1);
        try {
            pedido2.adicionarItem(portalCake, 10); // Estoque e apenas 5, vai dar erro
        } catch (EstoqueInsuficienteException e) {
            System.out.println("Excecao Capturada Corretamente: " + e.getMessage());
        }

        System.out.println("\n=== INICIANDO VENDA 3 (Cliente Standard c/ Promocao e XP Ajustado) ===");
        Pedido pedido3 = new Pedido(atendente1, cliente1);
        try {
            pedido3.adicionarItem(cafeProgramador, 2); 
            
            double totalBruto = pedido3.calcularValorTotal();
            System.out.println("Total Bruto Pedido " + pedido3.getNumeroSequencial() + ": R$ " + totalBruto);
            
            Promocional promo = new PromocaoEventoGeek();
            double desconto = promo.aplicarDesconto(pedido3); 
            double totalLiquido = totalBruto - desconto;
            
            System.out.println("Desconto Dia do Orgulho Nerd (10% Bebidas): R$ " + desconto);
            System.out.println("Total a Pagar: R$ " + totalLiquido);
            
            // Agora finalizamos o pedido passando o desconto para computar o XP sobre o valor pago real
            pedido3.finalizarPedido(desconto);
            repositorioPedidos.add(pedido3);
            
            System.out.println("Novo saldo de XP de " + cliente1.getNome() + ": " + cliente1.getSaldoXP());
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }

        System.out.println("\n=== INICIANDO VENDA 4 (Cliente VIP pagando com XP) ===");
        Pedido pedido4 = new Pedido(atendente1, cliente2);
        try {
            pedido4.adicionarItem(pocaoMana, 1); 
            double total = pedido4.calcularValorTotal();
            System.out.println("Total Pedido " + pedido4.getNumeroSequencial() + ": R$ " + total);
            System.out.println("Tentando pagar inteiramente com XP...");
            
            try {
                // R$ 12,00 custariam 120 XP (12 * 10). O VIP so tem 50 XP (pois ganhou no comeco), vai dar erro!
                ((ClienteVIP) cliente2).pagarComXP(total); 
            } catch (PontosInsuficientesException e) {
                System.out.println("Excecao Capturada Corretamente: " + e.getMessage());
                System.out.println("Pagamento com dinheiro normal efetuado.");
                pedido4.finalizarPedido(); // Ganha mais XP pelo pagamento em dinheiro
                repositorioPedidos.add(pedido4);
                System.out.println("Novo saldo de XP de " + cliente2.getNome() + ": " + cliente2.getSaldoXP());
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    // === Metodos Auxiliares para demonstrar o CRUD de Produtos ===
    public static void adicionarProduto(Produto p) {
        repositorioProdutos.add(p);
    }
    
    public static Produto buscarProduto(String codigo) {
        for (Produto p : repositorioProdutos) {
            if (p.getCodigo().equals(codigo)) return p;
        }
        return null; // Nao encontrou
    }
    
    public static void atualizarPrecoProduto(String codigo, double novoPreco) {
        Produto p = buscarProduto(codigo);
        if (p != null) {
            p.setPrecoBase(novoPreco);
        }
    }
    
    public static void removerProduto(String codigo) {
        Produto p = buscarProduto(codigo);
        if (p != null) {
            repositorioProdutos.remove(p);
        }
    }
    
    // === Metodos Auxiliares para demonstrar o CRUD de Clientes ===
    public static void adicionarCliente(Cliente c) {
        repositorioClientes.add(c);
    }
    
    public static Cliente buscarCliente(String cpf) {
        for (Cliente c : repositorioClientes) {
            if (c.getCpf().equals(cpf)) return c;
        }
        return null;
    }
    
    public static void atualizarNomeCliente(String cpf, String novoNome) {
        Cliente c = buscarCliente(cpf);
        if (c != null) {
            c.setNome(novoNome);
        }
    }
    
    public static void removerCliente(String cpf) {
        Cliente c = buscarCliente(cpf);
        if (c != null) {
            repositorioClientes.remove(c);
        }
    }
}
