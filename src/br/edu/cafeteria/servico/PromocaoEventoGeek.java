package br.edu.cafeteria.servico;

import br.edu.cafeteria.modelo.Bebida;
import br.edu.cafeteria.modelo.ItemPedido;
import br.edu.cafeteria.modelo.Pedido;

public class PromocaoEventoGeek implements Promocional {

    @Override
    public double aplicarDesconto(Pedido pedido) {
        double desconto = 0.0;
        
        for (ItemPedido item : pedido.getItens()) {
            // Polimorfismo e checagem de tipo em tempo de execução
            if (item.getProduto() instanceof Bebida) {
                // 10% de desconto no valor total daquele item (se for bebida)
                desconto += item.getValorTotal() * 0.10;
            }
        }
        
        return desconto;
    }
}
