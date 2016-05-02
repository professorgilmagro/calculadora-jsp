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
     * Enum para gerir as constantes para os operadores para cálculo das partes
     * envolvidas
     */
    public enum operator {
        ADDITION("+"),
        SUBTRACTION("-"),
        MULTIPLICATION("×"),
        DIVISION("÷");
        
        private final String sign;
        
        /**
         * Construtor do enum
         * Define o operador com base nas constantes definidas
         * 
         * @param value 
         */
        operator(String value){ 
            this.sign = value;
        }
        
        /**
         * Retorna o sinal da operacao definida na constante
         * 
         * @return String
         */
        public String getSign(){ 
            return sign; 
        }
    }
    
    /**
     * Recebe o tipo de operação matemática
     */
    private String operador = "" ;
    
    /**
     * Recebe um inteiro para o numerador
     */
    private Integer numerador ;
    
    /**
     * Recebe um inteiro para o denominador
     */
    private Integer denominador ;
    
    /**
     * Flag para indicar se a expressão é válida para ser calculada
     */
    private Boolean valid ;
    
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
     * Construtor para receber o numerador, denominador e a operação
     * 
     * @param numerador
     * @param denominador 
     * @param operacao 
     */
    public Fracao(Integer numerador, Integer denominador, String operacao){
        this.numerador = numerador;
        this.denominador = denominador;
        this.operador = operacao;
    }
     
    /**
     * Adiciona a fração à coleção de frações que compõe o cálculo
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
     * Efetua o cálculo da fração
     * As frações serão solucionadas em pares, seguindo a prioridade conforme
     * a sequência: Divisão, multiplicação, soma e subtração.
     * A medida que as partes que compoẽm o cálculo são solucionadas, a
     * expressão vai sendo reduzida até a obtenção do resultado final.
     */
    protected void calculate(){
        if( ! this.isValid() ) {
            this.result = new Fracao(0, 1) ;
            return ;
        }
        
        /**
         * Se não há itens agregrados à esta fração, logo a fração é composta
         * por um único termo, ou seja, ela mesma
         */
        if ( this.fracs.isEmpty() ){
            this.result = this;
        }
        
        /**
         * Nesta primeira iteração, efetuamos os cálculos de divisão e carregamos
         * as frações existentes numa nova lista para não modificar a original.
         * As partes que compõem o cálculo, são solucionados em pares e vão sendo
         * diluídos até a obtenção do resultado final
         */
        ArrayList<Fracao> fracParts = new ArrayList<Fracao>();
        
        fracParts.add(this);
        for (int i = 0; i < fracs.size() && fracs.size() > 0 ; i++) {
            Fracao curFrac = fracs.get(i);
            if(curFrac.getOperador().equals(operator.DIVISION.getSign())) {
                Fracao prevFrac = i > 0 ? fracs.get(i-1) : this;
                fracParts.set(i, prevFrac.dividir(curFrac));
                fracParts.remove(prevFrac);
                continue;
            }
            
            fracParts.add(curFrac);
        }
        
        /**
         * Nesta segunda iteração, efetuamos os cálculos de multiplicação.
         */
        for (int i = 0; i < fracParts.size() && fracParts.size() > 1; i++) {
            Fracao curFrac = fracParts.get(i);
            if( curFrac.getOperador().equals(operator.MULTIPLICATION.getSign())) {
                Fracao prevFrac = fracParts.get(i-1);
                fracParts.set(i, prevFrac.multiplicar(curFrac));
                fracParts.remove(prevFrac);
            }
        }
        
        /**
         * Nesta terceira iteração, efetuamos os cálculos de soma e a redução
         * dos termos.
         */
        for (int i = 0; i < fracParts.size() && fracParts.size() > 1; i++) {
            Fracao curFrac = fracParts.get(i);
            if( curFrac.getOperador().equals(operator.ADDITION.getSign())) {
                Fracao prevFrac = fracParts.get(i-1);
                fracParts.set(i, prevFrac.somar(curFrac));
                fracParts.remove(prevFrac);
            }
        }
        
        /**
         * Nesta quarta e última iteração, efetuamos os cálculos de subtação e a
         * redução dos termos.
         * Se tiver tudo certo, após a execução desta etapa, deverá restar apenas
         * um item que será o resultado do cálculo.
         */
        for (int i = 0; i < fracParts.size() && fracParts.size() > 1; i++) {
            Fracao curFrac = fracParts.get(i);
            if( curFrac.getOperador().equals(operator.SUBTRACTION.getSign())) {
                Fracao prevFrac = fracParts.get(i-1);
                fracParts.set(i, prevFrac.subtrair(curFrac));
                fracParts.remove(prevFrac);
            }
        }
        
        this.result = fracParts.get(0);
    }
    
    /**
    * Efetua a soma desta fração com a fração informada no parâmetro
    * 
     * @param frac  Fração a ser somada
     * 
     * @return Fracao
    */
    public Fracao somar( Fracao frac ){
        int newDenominador = this.MMC(this.getDenominador(), frac.getDenominador());
        int num1 = newDenominador/this.getDenominador() * this.getNumerador();
        int num2 = newDenominador/frac.getDenominador() * frac.getNumerador();
        int newNumerador = num1 + num2;
        
        return new Fracao(newNumerador, newDenominador, this.getOperador());
    }
    
    /**
    * Efetua a subtração desta fração com a fração informada no parâmetro
    * 
     * @param frac  Fração a ser subtraída
     * 
     * @return Fracao
    */
    public Fracao subtrair( Fracao frac ){
        int newDenominador = this.MMC(this.getDenominador(), frac.getDenominador());
        int num1 = newDenominador/this.getDenominador() * this.getNumerador();
        int num2 = newDenominador/frac.getDenominador() * frac.getNumerador();
        int newNumerador = num1 - num2;
        
        return new Fracao(newNumerador, newDenominador, this.getOperador());
    }
    
    /**
    * Efetua a divisão desta fração com a fração informada no parâmetro
    * Para tanto, multiplicamos a primeira fração pelo inverso da segunda
    * 
     * @param frac  Fração a ser multiplicada
     * 
     * @return Fracao
    */
    public Fracao multiplicar( Fracao frac ){
        int newNumerador = this.getNumerador() * frac.getNumerador();
        int newDenominador = this.getDenominador() * frac.getDenominador();
        return new Fracao(newNumerador, newDenominador, this.getOperador());
    }
    
    /**
    * Efetua a divisão desta fração com a fração informada no parâmetro
    * Para tanto, multiplicamos a primeira fração pelo inverso da segunda
    * 
    * @param frac Fração a ser utilizada como divisor deste objeto
    * 
    * @return Fracao
    */
    public Fracao dividir( Fracao frac ){
        int newNumerador = this.getNumerador() * frac.getDenominador();
        int newDenominador = this.getDenominador() * frac.getNumerador();
        return new Fracao(newNumerador, newDenominador, this.getOperador());
    }
    
    /**
     * Verifica se a fração, bem como as frações embutidas, são válidas
     * Por ora, apenas validamos se o denominador é Zero(0)
     * 
     * @return Boolean
     */
    private Boolean isValid(){
        if( this.valid != null ){
            return this.valid;
        }
        
        // verifica a fração deste objeto
        this.valid = true ;
        String errorMessage = "Ops! Apenas números inteiros maiores que zero são aceitos para numerador e denominador.";
        if ( this.getDenominador() == 0 || this.getNumerador()== 0 ){
            this.warnings.add(errorMessage);
            this.valid = false ;
        }
        
        // verifica as demais frações que compõem o cálculo são válidas
        if( this.valid && ! this.fracs.isEmpty() ) {
            for (Fracao frac : fracs) {
               if ( frac.getDenominador() == 0 || frac.getNumerador() == 0 ){
                    this.warnings.add(errorMessage);
                    this.valid = false ;
                    break;
                } 
            }
        }
        
        return this.valid ;
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
        Fracao rst = this.getResult();
        
        if ( ! this.getWarnings().isEmpty() ) {
            return this.types;
        }
        
        //Unitária: o numerador é igual a 1 e o denominador é um inteiro positivo.
        if ( rst.getNumerador() == 1 && rst.getDenominador() > 0 && rst.getDenominador() % 1 == 0 ) {
            rst.types.add("Unitária");
        }
        
        //Aparente: O numerador é múltiplo ao denominador
        if ( rst.getNumerador() % rst.getDenominador() == 0 ) {
            rst.types.add("Aparente") ;
        }
        
        //Equivalente: Mantêm a mesma proporção de outra fração
         if ( rst.isEquivalentType(rst.getNumerador(), rst.getDenominador()) ) {
            rst.types.add("Equivalente");
        }
        
        //Própria: O numerador é menor que o denominador
        if ( rst.getNumerador() < rst.getDenominador() ) {
            rst.types.add("Própria") ;
        }
        else {
            rst.types.add("Imprópria") ;
        }
        
        //Irredutível: o numerador e o denominador são primos entre si, não permitindo simplificação
        if ( rst.isPrimeNumbers(rst.getNumerador(), rst.getDenominador()) ) {
            rst.types.add("Irredutível");
        }
        
        //Decimal: O denominador é uma potência de 10
        if ( rst.getDenominador() % 10 == 0 ) {
            rst.types.add("Decimal") ;
        }
        
        return rst.types;
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
        return this.MDC(a, b) > 1 || this.MDC(b, a) > 1  ;
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
        if ( result == null && valid == null ) {
            this.calculate();
        }
        
        return result;
    }
    
    /**
     * Retorna a fração simplificada do resultado
     * Para tanto, basta dividi-los pelo máximo divisor comum (MDC) entre eles, 
     * obtendo-se uma fração que, além de manter a proporção da original, 
     * é do tipo irredutível:
     * 
     * @return Fracao
     */
    public Fracao getSimplifiedResult() {
        if ( result == null && valid == null ) {
            this.calculate();
        }
        
        if( ! this.isValid() ){
            return this.result ;
        }
        
        // Uma fração pode ser simplificada quando numerador e denominador não 
        // são primos entre si
        if( this.isPrimeNumbers(result.getNumerador(), result.getDenominador())){
            return result;
        }
        
        int mdc = this.MDC(result.denominador, result.getNumerador());
        int newNumerador = result.getNumerador()/mdc;
        int newDenominador = result.getDenominador()/mdc;
        return new Fracao(newNumerador, newDenominador);
    }
    
    /**
     * Retorna a fração resultante do cálculo em formato MathTeX amigável
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
    public String getDecimalResult(){
        return this.getRealResult().toString().replace(".", ",");
    }
    
    /**
     * Retorna o resultado da fração em ponto flutuante
     * 
     * @return Double
     */
    public Double getRealResult() {
         if( ! this.isValid() ){
            return 0.0 ;
        }
         
        BigDecimal bigNumerador = new BigDecimal(this.getResult().getNumerador().toString());
        BigDecimal bigDivisor = new BigDecimal(this.getResult().getDenominador().toString());
        return bigNumerador.divide(bigDivisor, 16, RoundingMode.HALF_UP).doubleValue();
    }
    
    /**
     * Retorna o operador de relação com o objeto 
     * 
     * @return 
     */
    public String getOperador() {
        return operador;
    }
    
    /**
     * Define o operador de cálculo para este objeto 
     * 
     * @param operador
     */
    public void setOperador(String operador) {
        this.operador = operador;
    }
}
