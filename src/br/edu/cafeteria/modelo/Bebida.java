package br.edu.cafeteria.modelo;

public class Bebida extends Produto {
    private String tamanho; // P, M, G
    private int quantidadeCafeina; // em mg

    public Bebida(String codigo, String nome, double precoBase, int quantidadeEstoque, String tamanho, int quantidadeCafeina) {
        super(codigo, nome, precoBase, quantidadeEstoque);
        this.tamanho = tamanho;
        this.quantidadeCafeina = quantidadeCafeina;
    }

    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }

    public int getQuantidadeCafeina() { return quantidadeCafeina; }
    public void setQuantidadeCafeina(int quantidadeCafeina) { this.quantidadeCafeina = quantidadeCafeina; }
}
