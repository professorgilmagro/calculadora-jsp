/*
 * Esta classe centraliza todos os metódos necessários para trabalhar com o
 * histórico de cálculos do sistema da calculadora.
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
    
    public static History getInstance(HttpServletRequest request){
        return new History(request);
    }
    
    public void setRequest(HttpServletRequest request){
        this._request = request ;
        this._session = request.getSession();
    }
    
    public void add(Fracao frac){
        ArrayList<Fracao> items = this.getItems();
        items.add(frac);
        this._session.setAttribute( "historico" , items);
    }
    
    public ArrayList<Fracao> getItems(){
        if( this._session.isNew() || this._session.getAttribute("historico") == null ) {
            this._session.setMaxInactiveInterval(1800);
            return new ArrayList<Fracao>() ;
        }
        
        return (ArrayList<Fracao>) this._session.getAttribute("historico");
    }
    
    public Boolean isEmpty() {
        return this.getItems().isEmpty() ;
    }
}
