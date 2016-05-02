package br.aiec;

/*
 * Página de controle da página de Calculadora
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
           request.setAttribute("resultadoSimplificado", "");
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
        request.setCharacterEncoding("UTF-8");
        String mathText = request.getParameter("mathText");
        
        Fracao result = this._getResult(mathText);
        request.setAttribute("tipos", result.getTypes());
        
        request.setAttribute("resultado", result.getPrettyMathResult());
        request.setAttribute("resultadoSimplificado", result.getSimplifiedResult().getPrettyMathResult());
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
        String[] fracs = mathText.split("\\+|-|×|÷");
        List<String> ops = new ArrayList<String>();
        
        Matcher m = Pattern.compile("\\+|-|×|÷").matcher(mathText);
        while (m.find()) {
            ops.add(m.group(0));
        }
        
        if(ops.isEmpty()){
            
        }
        
        Fracao mainFrac = null ;
        for (int i = 0; i < fracs.length; i++) {
            String[] numbers = fracs[i].split("/");
            int numerador = Integer.parseInt(numbers[0]);
            int denominador = numbers.length == 2 ? Integer.parseInt(numbers[1]) : 1;
            
            // cria a fracao que vai gerir o cálculo com a primeira parte da equação
            if ( i == 0 ) {
                mainFrac = new Fracao(numerador, denominador);
                continue;
            }
            
            mainFrac.add( new Fracao(numerador, denominador, ops.get(i-1)) );
        }
        
        String result = mainFrac.getPrettyMathResult();
        
        return mainFrac;
    }
}
