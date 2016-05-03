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
public class Historic {
    private HttpSession _session ;
    
    /**
     * Recebe a lista de items do historico de sessão
     */
    private ArrayList<Fracao> items = new ArrayList<Fracao>();
    
     /**
     * Construtor da classe. 
     * Pemite instanciar passando os valores (String) a serem calculados
     * 
     * @param request
     */
    public Historic(HttpServletRequest request){
        this._session = request.getSession();
    }
    
    public void Add(Fracao frac){
        this.items.add(frac);
        if( this._session.isNew() ) {
            this._session.setAttribute( "historico" , items);
        }
    }
}
