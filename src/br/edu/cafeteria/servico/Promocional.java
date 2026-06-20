package br.edu.cafeteria.servico;

import br.edu.cafeteria.modelo.Pedido;

public interface Promocional {
    double aplicarDesconto(Pedido pedido);
}
