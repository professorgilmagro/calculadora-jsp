<%@page import="java.util.ArrayList"%>
<form id="fraction_calculator" method="post" class="calculator" action="<%= request.getContextPath() %>/calculadora">
    <%
        String categoria = (String) request.getAttribute("categoria");
        String result = (String) request.getAttribute("resultado");
        ArrayList<String> avisos = (ArrayList<String>) request.getAttribute("avisos");
    %>
    
   <div class="bg-calc">
    <table class="calc-board">
        <caption>
            <a class="active" href="#" >Resultado</a>
            <a class="inative" href="#" >Tipo</a>
        </caption>
        <tbody>
            <tr>
                <td colspan="5">
                    <input name="num1" id="calc-value1" type="hidden" value="<%= result == null ? "" : result %>">
                    <input name="num2" id="calc-value2" type="hidden">
                </td>
            </tr>
            <tr class="visor">
                <td colspan="4" class="content" >
                    <div class="tipo-fracao"><%= categoria == null ? "" : categoria %></div>
                    <div id="calc-screen"><%= result == null ? "" : result %></div>
                    <ul class="warnings">
                    <%
                    if( avisos != null ) {
                        for (String aviso : avisos) {
                           out.print( "<li>" + aviso + "</li>" );
                        }
                    }
                    %>
                    </ul>
                </td>
            </tr>
            <tr>
                <td><button id="btn-clean-memory" type="button">CE</button></td>
                <td><button id="btn-clean" type="button">C</button></td>
                <td><button type="button" id="btn-backspace" ><img src="assets/images/back-backspace.png"></button></td>
                <td><button type="button" id="btn-divisor" ><img src="assets/images/divisor-branco.png"></button></td>
            </tr>

            <tr>
                <td><button type="button" >7</button></td>
                <td><button type="button" >8</button></td>
                <td><button type="button" >9</button></td>
                <td><button type="button" disabled="disabled" >x</button></td>
            </tr>
             <tr>
                <td><button type="button" >4</button></td>
                <td><button type="button" >5</button></td>
                <td><button type="button" >6</button></td>
                <td><button type="button" disabled="disabled">-</button></td>
            </tr>
            <tr>
                <td><button type="button" >1</button></td>
                <td><button type="button" >2</button></td>
                <td><button type="button" >3</button></td>
                <td><button type="button" disabled="disabled">+</button></td>
            </tr>
             <tr>
                 <td><button type="button">0</button></td>
                <td><button type="button" id="btn-decimal" >,</button></td>
                <td><button type="button" id="btn-percent" >%</button></td>
                <td><button type="submit" id="btn-calculate" >=</button></td>
            </tr>
        </tbody>
    </table>
   </div>
</form>