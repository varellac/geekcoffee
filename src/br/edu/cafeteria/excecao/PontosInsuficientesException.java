package br.edu.cafeteria.excecao;

public class PontosInsuficientesException extends RuntimeException {
    public PontosInsuficientesException(String mensagem) {
        super(mensagem);
    }
}
