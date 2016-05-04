package br.aiec;

/*
 * Página de controle da página de Calculadora
 */

import br.aiec.helpers.History;
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
           request.setAttribute("resultadoDecimal", "");
           request.setAttribute("tipos", new ArrayList<String>());
           request.setAttribute("historico", History.getInstance(request));
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
        try {
            response.setContentType("text/html;charset=UTF-8");
            request.setCharacterEncoding("UTF-8");
            String mathText = request.getParameter("mathText");

            Fracao result = this._getResultFromMathTeX(mathText, response);
            String simplificado = result.getSimplifiedResult().getPrettyMathResult();
            String resultado = result.getPrettyMathResult() ;
            if ( simplificado.equals(resultado) ) {
                simplificado = "";
            }
        
            /**
             * Se a fração é válida, logo não há avisos de erros
             * Então guardá-la-emos no histórico
             */
            if ( result.getWarnings().isEmpty() ) {
                History.getInstance(request).add(result);
            }
        
            request.setAttribute("resultado", resultado);
            request.setAttribute("resultadoSimplificado", simplificado);
            request.setAttribute("resultadoDecimal", result.getDecimalResult());
            request.setAttribute("expressao", result.getMathExpression());
            request.setAttribute("avisos", result.getWarnings());
            request.setAttribute("tipos", result.getTypes());
            request.setAttribute("historico", request.getSession().getAttribute("historico") );
        } 
        finally {
            request.getRequestDispatcher("calculadora.jsp").forward(request, response);
        }
    }
    
    /**
     * Popula o objeto fracao para os calculos e retorna uma fracao de resultado
     * 
     * @param mathText  Composição textual do cálculo
     * 
     * @return Fracao
     */
    private Fracao _getResultFromMathTeX( String mathText , HttpServletResponse response  ){
        String[] fracs = mathText.split("\\+|-|×|÷");
        List<String> operators = new ArrayList<String>();
        
        Matcher m = Pattern.compile("\\+|-|×|÷").matcher(mathText);
        while (m.find()) {
            operators.add(m.group(0));
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

            mainFrac.add( new Fracao(numerador, denominador, operators.get(i-1)) );
        }
        
        return mainFrac;
    }
}
