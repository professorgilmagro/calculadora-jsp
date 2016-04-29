/*
 * Centraliza os métodos comuns para uma fração matemática
 */
package br.aiec;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

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
     * Recebe uma instância da fração como resultado do cálculo
     */
    private Fracao result ;
    
    /**
     * Recebe a lista de avisos em caso de erro ou informação
     */
    private ArrayList<String> warnings = new ArrayList<String>();
    
    /**
     * Recebe a lista de tipos de classificação da fração
     */
    private ArrayList<String> types = new ArrayList<String>();
    
    /**
     * Recebe a lista de frações que serão utilizadas para o cálculo
     */
    private ArrayList<Fracao> fracs = new ArrayList<Fracao>();
    
    /**
     * Construtor para receber um objeto do tipo fração
     */
    public Fracao(){
    }
    
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
     * Adiciona a fração ao conjunto de frações para cálculo
     * 
     * @param frac
     * 
     * @return self
     */
    public Fracao add(Fracao frac){
        this.fracs.add(frac);
        return this;
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
    
    /**
     * Retorna os avisos disparados pela operação
     * 
     * @return ArrayList 
     */
    public ArrayList<String> getWarnings() {
        return warnings;
    }
    
    /**
     * Retorna os Tipos de Frações
     * 
     * @return ArrayList
     */
    public ArrayList<String> getTypes(){
        if ( ! this.getWarnings().isEmpty() ) {
            return this.types ;
        }
        
        //Unitária: o numerador é igual a 1 e o denominador é um inteiro positivo.
        if ( this.getNumerador() == 1 && this.getDenominador() > 0 && this.getDenominador() % 1 == 0 ) {
            this.types.add("Unitária");
        }
        
        //Aparente: O numerador é múltiplo ao denominador
        if ( this.getNumerador() % this.getDenominador() == 0 ) {
            this.types.add("Aparente") ;
        }
        
        //Equivalente: Mantêm a mesma proporção de outra fração
         if ( this.isEquivalentType(this.getNumerador(), this.getDenominador()) ) {
            this.types.add("Equivalente");
        }
        
        //Própria: O numerador é menor que o denominador
        if ( this.getNumerador() < this.getDenominador() ) {
            this.types.add("Própria") ;
        }
        else {
            this.types.add("Imprópria") ;
        }
        
        // Imprópria: O numerador é maior ou igual ao denominador
        if ( this.getNumerador() < this.getDenominador() ) {
            this.types.add("Própria") ;
        }
        
        //Irredutível: o numerador e o denominador são primos entre si, não permitindo simplificação
        if ( this.isPrimeNumbers(this.getNumerador(), this.getDenominador()) ) {
            this.types.add("Irredutível");
        }
        
        return this.types;
    }
    
    /**
     * Verifica se os números informados são considerados equivalentes
     * 
     * @param a  int Número
     * @param b  int Número
     * 
     * @return 
     */
    private Boolean isEquivalentType(int a, int b){
        return this.MDC(a, b) > 1 ;
    }
    
    /**
     * Verifica se os números informados são primos entre si
     * Dois números são primos entre si se o MDC deles for 1.
     * 
     * @param a  int Número
     * @param b  int Número
     * 
     * @return 
     */
    public Boolean isPrimeNumbers(int a, int b){
        return this.MDC(a, b) == 1 ;
    }
    
    /**
    * Calcula o MMC (Mínimo múltiplo comum) entre dois números
    * 
    * @param a  int Número
    * @param b  int Número
    * 
    * @return int
    */
    private int MMC(int a, int b) {
        int mmc = a;
        while ( mmc % a != 0 || mmc % b != 0 ) {
            mmc++;
        }

        return mmc;
    }
    
    /**
    * Calcula o MDC (Máximo divisor comum) entre dois números
    * 
    * @param a  Número
    * @param b  Número
    * 
    * @return int
    */
    private int MDC(int a, int b) {
        int mdc = a;
        while (a % mdc != 0 || b % mdc != 0 ) {
            mdc--;
        }

        return mdc;
    }
    
    /**
     * Retorna a fração de resultado correspondente
     * 
     * @return Fracao
     */
    public Fracao getResult() {
        if ( result == null ) {
            this.calculate();
        }
        
        return result;
    }
    
    /**
     * Calcula a divisão com base nos valores informados
     * 
     * @return Boolean
     */
    protected Boolean calculate(){
        if ( ! this.getWarnings().isEmpty() ) {
            this.result = null;
            return false;
        }
        
        this.result = this;
     
        return true ;
    }
    
    /**
     * Retorna a fração resultante do cálculo em formato amigável
     * Ex: 1/3
     * 
     * @return String
     */
    public String getPrettyMathResult(){
       if ( this.getResult().getDenominador().equals(1) ) {
           return this.getResult().getNumerador().toString();
       }
       
       return String.format("%d/%d", this.getResult().getNumerador(), this.getResult().getDenominador());
    }
    
    /**
     * Retorna o resultado da fração em ponto flutuante em formato amigável
     * Exemplo: 0,333333333
     * 
     * @return String
     */
    public String getPrettyRealResult(){
        return this.getRealResult().toString().replace(".", ",");
    }
    
    /**
     * Retorna o resultado da fração em ponto flutuante
     * 
     * @return Double
     */
    public Double getRealResult() {
        BigDecimal bigNumerador = new BigDecimal(this.getResult().getNumerador().toString());
        BigDecimal bigDivisor = new BigDecimal(this.getResult().getDenominador().toString());

       return bigNumerador.divide(bigDivisor, 16, RoundingMode.HALF_UP).doubleValue();
    }

}
