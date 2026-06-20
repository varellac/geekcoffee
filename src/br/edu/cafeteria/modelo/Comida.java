package br.edu.cafeteria.modelo;

public class Comida extends Produto {
    private int tempoPreparo; // em minutos
    private boolean isVeganoSemGluten;

    public Comida(String codigo, String nome, double precoBase, int quantidadeEstoque, int tempoPreparo, boolean isVeganoSemGluten) {
        super(codigo, nome, precoBase, quantidadeEstoque);
        this.tempoPreparo = tempoPreparo;
        this.isVeganoSemGluten = isVeganoSemGluten;
    }

    public int getTempoPreparo() { return tempoPreparo; }
    public void setTempoPreparo(int tempoPreparo) { this.tempoPreparo = tempoPreparo; }

    public boolean isVeganoSemGluten() { return isVeganoSemGluten; }
    public void setVeganoSemGluten(boolean veganoSemGluten) { isVeganoSemGluten = veganoSemGluten; }
}
