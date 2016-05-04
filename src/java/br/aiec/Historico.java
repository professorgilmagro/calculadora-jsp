/*
 * Página de controle da página de Histórico
 */
package br.aiec;

import br.aiec.helpers.History;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página de controle da página de Histórico
 * 
 * @author GRA (Anne, Gilmar Ricardo)
 */
@WebServlet(name = "Historico", urlPatterns = {"/historico", "/historico/remover"})
public class Historico extends HttpServlet {
    
    /**
     * Constante para ação de remoção de histórico
     */
    public final static String ACTION_REMOVE = "remove";
    
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            History history = History.getInstance(request);
            if ( request.getParameter("action") != null && request.getParameter("action").equals(ACTION_REMOVE) && request.getParameter("idx") != null ) {
                int idx = Integer.parseInt( request.getParameter("idx") ) ;
                history.remove(idx);
            }
            
           request.setAttribute("historico", history);
           request.getRequestDispatcher("historico.jsp").include(request, response);
        } 
        finally {
            out.close();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
