<%@ page pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Login</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
	</head>
	<body>
		<div class="container" style="padding:17vh 25vw 0 25vw;">
			<h2>Connexion</h2>
			<form method="post" action="login">
				<div class="form-group">
					<label for="pseudo">Pseudo</label>
					<input type="text" class="form-control" name="username" id="pseudoElem">
				</div>
				<div class="form-group">
					<label for="password">Mot de passe</label>
					<input type="password" class="form-control" name="password" id="passwordElem">
				</div>
				<input type="submit" class="btn btn-primary" value="Connection">
				<input type="button" class="btn btn-success" onclick="window.location.href='sign_in'" value="Incription">
			</form>
			<br>
			<% if ((boolean) request.getAttribute ("errorConnection")) {
			      for (String msg : (List<String>) request.getAttribute ("errors")) {
			    	  out.println (String.format ("<p class=\"text-danger\">%s</p>", msg));
			      }
			   }
			%>
		</div>
  		<script src="/SandboxWebApp/inc/js/jquery.min.js"></script>
  		<script src="/SandboxWebApp/inc/js/bootstrap.min.js"></script>
	</body>
</html>