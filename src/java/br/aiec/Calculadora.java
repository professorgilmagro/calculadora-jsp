package br.aiec;

/*
 * Página de controle da página de Calculadora
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Página de controle da Calculadora
 * 
 * @author GRA (Anne, Gilmar Ricardo)
 */
@WebServlet(name = "Calculadora", urlPatterns = {"/calculadora"})
public class Calculadora extends HttpServlet {
    
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
           request.setAttribute("resultado", "");
           request.setAttribute("tipos", new ArrayList<String>());
           request.getRequestDispatcher("calculadora.jsp").forward(request, response);
        } finally {
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
        response.setContentType("text/html;charset=UTF-8");
        String mathText = request.getParameter("mathText");
        
        Fracao result = this._getResult(mathText);
        request.setAttribute("tipos", result.getTypes());
        
        request.setAttribute("resultado", result.getPrettyMathResult());
        request.setAttribute("avisos", result.getWarnings());
        
       request.getRequestDispatcher("calculadora.jsp").forward(request, response);
    }
    
    /**
     * Prepara o objeto fracao para os calculos e retorna uma fracao de resultado
     * 
     * @param mathText  Composição textual do cálculo (fórmula)
     * @return Fracao
     */
    private Fracao _getResult( String mathText ){
        Fracao mainFrac = new Fracao();
        String[] fracs = mathText.split("\\+|-|×|÷");
        List list = Arrays.asList(fracs);
        
        for (String frac : fracs) {
            String[] numbers = frac.split("/");
            int numerador = Integer.parseInt(numbers[0]);
            int denominador = Integer.parseInt(numbers[1]);
            mainFrac.add( new Fracao(numerador, denominador) );
        }
        
        String result = mainFrac.getPrettyMathResult();
        
        return mainFrac;
    }
}
