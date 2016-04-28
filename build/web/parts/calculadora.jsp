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
            
           <?xml version="1.0" encoding="UTF-8"?>

            <tr>
                <td colspan="5">
                    <input name="mathText" id="mathText" type="hidden" value="">
                    <input name="numerador[]" id="calc-value1" type="hidden" value="<%= result == null ? "" : result %>">
                    <input name="divisor[]" id="calc-value2" type="hidden">
                </td>
            </tr>
            <tr class="visor">
                <td colspan="4" class="content" >
                    <div class="tipo-fracao"><%= categoria == null ? "" : categoria %></div>
                    <div class="cursor-frac"><span class="active" >x</span><span class="line" >&nbsp;</span><span>y</span></div>
                    <div id="calc-screen">
                        `0`
                    </div>
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
                <td><button id="btn-clean" type="button">C</button></td>
                <td><button type="button" id="btn-backspace" ><img src="assets/images/back-backspace.png"></button></td>
                <td><button id="btn-toogle-cursor" type="button"><math><mrow><mfrac><mn><span class="active">a</span></mn><mn><span>b</span></mn></mfrac></mrow></math></button></td>
                <td><button type="button" class="btn-operator" >÷</button></td>
            </tr>
            <tr>
                <td><button type="button" >7</button></td>
                <td><button type="button" >8</button></td>
                <td><button type="button" >9</button></td>
                <td><button type="button" class="btn-operator">×</button></td>
            </tr>
             <tr>
                <td><button type="button" >4</button></td>
                <td><button type="button" >5</button></td>
                <td><button type="button" >6</button></td>
                <td><button type="button" class="btn-operator">-</button></td>
            </tr>
            <tr>
                <td><button type="button" >1</button></td>
                <td><button type="button" >2</button></td>
                <td><button type="button" >3</button></td>
                <td><button type="button" class="btn-operator">+</button></td>
            </tr>
             <tr>
                 <td><button type="button">0</button></td>
                <td><button type="button" id="btn-decimal" >,</button></td>
                <td><button type="button" id="btn-percent" >%</button></td>
                <td><button type="submit" id="btn-calculate">=</button></td>
            </tr>
        </tbody>
    </table>
   </div>
</form>
                    

