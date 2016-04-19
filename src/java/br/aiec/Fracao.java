/*
 * Centraliza os métodos comuns para uma fração matemática
 */
package br.aiec;

/**
 *
 * @author Grupo GRA (Anne, Gilmar e Ricardo) <aiec.br>
 */
public class Fracao {
    /**
     * Recebe um inteiro para o numerador
     */
    private Integer numerador ;
    
    /**
     * Recebe um inteiro para o denominador
     */
    private Integer denominador ;
    
    /**
     * Construtor para receber um objeto do tipo fração
     * 
     * @param fracao 
     */
    public Fracao(Fracao fracao){
        this.denominador = fracao.getDenominador();
        this.numerador = fracao.getNumerador();
    }
    
    /**
     * Construtor para receber o numerador e denominador
     * 
     * @param numerador
     * @param denominador 
     */
    public Fracao(Integer numerador, Integer denominador){
        this.numerador = numerador;
        this.denominador = denominador;
    }
    
    /**
     * Devolve o numerador da fração
     * 
     * @return Integer
     */
    public Integer getNumerador() {
        return numerador;
    }
    
    /**
     * Define o numerador da fração
     * 
     * @param numerador
     */
    public void setNumerador(Integer numerador) {
        this.numerador = numerador;
    }
    
    /**
     * Devolve o denominador da fração
     * 
     * @return Integer
     */
    public Integer getDenominador() {
        return denominador;
    }
    
    /**
     * Define o denominador da fração
     * 
     * @param denominador
     */
    public void setDenominador(Integer denominador) {
        this.denominador = denominador;
    }
}
