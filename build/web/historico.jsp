<%@page import="br.aiec.Historico"%>
<%@page import="br.aiec.helpers.History"%>
<%@page import="br.aiec.Fracao"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
	<head>
		<meta charset="utf-8">
		<meta content="width=device-width, initial-scale=1, maximum-scale=1" name="viewport">
		<meta name="description" content="Atividade da disciplina Programação Web (AIEC), cujo objetivo visa complementar os conhecimentos adquiridos pelos discentes no âmbito da disciplina pela abordagem dos conceitos estudados com o desenvolvimento de páginas estáticas em linguagem HTML." />
		<meta name="keywords" content="calculadora,fracao,matematica,conta">
		<link rel="icon" type="image/png" href="assets/images/calculator.png" />
		<link rel="stylesheet" type="text/css" href="assets/css/dataTables.css">
		<link rel="stylesheet" type="text/css" href="assets/css/base.css">
		<link rel="stylesheet" type="text/css" href="assets/css/historico.css">
		<link rel="stylesheet" type="text/css" href="assets/css/mobile.css" media="screen and (max-width: 768px)">
		<script type="text/javascript" src="assets/js/jquery-1.9.0.js" ></script>
		<script type="text/javascript" src="assets/js/dataTables.min.js" ></script>
                <script type="text/javascript" async src="https://cdn.mathjax.org/mathjax/latest/MathJax.js?config=AM_HTMLorMML-full.js"></script>
		<script type="text/javascript" src="assets/js/historic.js" ></script>
		<title>Páginas HTML Estáticas - Equipe</title>
	</head>
	<body class="equipe">
		<header class="main" >
			<h1><a href="index.html"><img src="assets/images/aiec-logo.png" class="logo"></a> Calculador de fração 1.0</h1>
			<%@ include file="/parts/menu.jsp" %>
		</header>
		<section class="content">
			<h1>Histórico</h1>
			<article class="historic" >
				<table id="tabela-historico">
					<caption>Histórico de cálculos</caption>
					<thead>
						<tr>
							<th>Expressão</th>
							<th>Resultado</th>
							<th>Simplificação</th>
							<th>Valor Decimal</th>
							<th>Classificação</th>
							<th>Remover</th>
						</tr>
					</thead>
					<tbody>
                                                <%
                                                    History historico = (History) request.getAttribute("historico");
                                                    if( historico != null && ! historico.isEmpty()){
                                                        for( int i = 0; i < historico.getItems().size(); i++ ) {
                                                            Fracao frac = historico.getItems().get(i);
                                                %>
						<tr>
							<td>`<%= frac.getMathExpression() %>`</td>
							<td>`<%= frac.getPrettyMathResult() %>`</td>
							<td>`<%= frac.getSimplifiedResult().getMathExpression() %>`</td>
							<td><%= frac.getDecimalResult() %></td>
							<td><%= frac.getTypes().toString() %></td>
                                                        <td><a onclick="return confirm('Tem certeza que deseja excluir este item?')" href="historico?action=<%= Historico.ACTION_REMOVE %>&idx=<%= i %>"><img src="assets/images/delete.png"></a></td>
						</tr>
                                                <%
                                                        }
                                                    }
                                                %>
					</tbody>
				</table>
			</article>
		</section>
		<%@ include file="/parts/footer.jsp" %>
	</body>
</html>