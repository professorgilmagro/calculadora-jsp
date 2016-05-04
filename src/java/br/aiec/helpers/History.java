/*
 * Esta classe centraliza todos os metódos necessários para trabalhar com o
 * histórico de cálculos do sistema da calculadora de frações.
 * Faz uso de Sessão, mas futuramente pode ser implementado uso de banco de dados
 */
package br.aiec.helpers;

import br.aiec.Fracao;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Grupo GRA (Anne, Gilmar e Ricardo) <aiec.br>
 */
public final class History {
    private HttpSession _session ;
    private HttpServletRequest _request ;
   
     /**
     * Construtor da classe. 
     * 
     * @param request
     */
    public History(HttpServletRequest request){
        this.setRequest(request);
    }
    
    /**
     * Retorna uma instância desta classe estaticamente
     * 
     * @param request   Request de requisição
     * 
     * @return History
     */
    public static History getInstance(HttpServletRequest request){
        return new History(request);
    }
    
    /**
     * Define o HttpServletRequest correspondente
     * 
     * @param request
     */
    public void setRequest(HttpServletRequest request){
        this._request = request ;
        this._session = request.getSession();
    }
    
    /**
     * Permite adiciona um item de fração ao histórico
     * 
     * @param frac Fração a ser adicionada
     */
    public void add(Fracao frac){
        ArrayList<Fracao> items = this.getItems();
        items.add(frac);
        this._session.setAttribute( "historico" , items);
    }
    
    /**
     * Permite remover uma fração do histórico com base no objeto
     * 
     * @param frac Fração a ser removida
     * 
     * @return Boolean  Retorna se a remoção foi realizada com sucesso
     */
    public Boolean remove(Fracao frac){
        try {
            this.getItems().remove(frac);
            return true ;
        } catch (Exception e) {
            return false ;
        }
    }
    
    /**
     * Permite remover uma fração do histórico com base na posição do mesmo
     * 
     * @param index Posição na lista
     * 
     * @return Boolean  Retorna se a remoção foi realizada com sucesso
     */
    public Boolean remove(int index){
        try {
            this.getItems().remove(index);
            return true ;
        } catch (Exception e) {
            return false ;
        }
    }
    
    /**
     * Retorna uma lista de fração guardadas na sessão da aplicação
     * 
     * @return ArrayList
     */
    public ArrayList<Fracao> getItems(){
        if( this._session.isNew() || this._session.getAttribute("historico") == null ) {
            this._session.setMaxInactiveInterval(1800);
            return new ArrayList<Fracao>() ;
        }
        
        return (ArrayList<Fracao>) this._session.getAttribute("historico");
    }
    
    /**
     * Verifica se o histórico está vazio
     * 
     * @return Boolean
     */
    public Boolean isEmpty() {
        return this.getItems().isEmpty() ;
    }
}
