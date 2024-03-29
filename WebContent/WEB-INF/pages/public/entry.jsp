<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Login</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
	</head>
	<body>
		<nav class="navbar navbar-dark fixed-top bg-dark navbar-expand shadow-sm border-bottom" style="height:55px;">
	   	</nav>
		<div class="container d-flex justify-content-center align-items-center vh-100">
			<div class="col-sm-12 col-lg-6 col-xs-4">
				<h3>Connexion</h3>
				<form method="post" action="login">
					<div class="form-group">
						<label for="pseudo">Pseudo</label>
						<input type="text" class="form-control" name="username" id="pseudoElem">
					</div>
					<div class="form-group">
						<label for="password">Mot de passe</label>
						<input type="password" class="form-control" name="password" id="passwordElem">
					</div>
					<div class="form-group">
						<a class="text-body" href="recoverpassword">Mot de passe oublié</a>
					</div>
					<input type="submit" class="btn btn-primary" value="Connexion">
					<input type="button" class="btn btn-success" onclick="window.location.href='sign_in'" value="Inscription">
				</form>
				<br>
				<% if ((boolean) request.getAttribute ("errorConnection")) {
				      for (Exception e : (List<Exception>) request.getAttribute ("errors")) {
				    	  out.println (String.format ("<p class=\"text-danger\">%s</p>", e.getMessage ()));
				      }
				   }
				%>
			</div>
		</div>
	</body>
</html>