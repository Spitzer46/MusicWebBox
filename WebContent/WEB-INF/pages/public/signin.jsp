<%@ page pageEncoding="UTF-8" %>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sign in</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
	</head>
	<body>
		<div class="container" style="padding: 17vh 25vw 0 25vw;">
			<h2>Incription</h2>
			<form method="post" action="sign_in">
				<div class="form-row" style="padding-left:0;">
					<div class="form-group col-md-6 col-sm-6 col-xs-6">
						<input type="text" class="form-control" placeholder="Prénom" name="firstname">
					</div>
					<div class="form-group col-md-6 col-sm-6 col-xs-6">
						<input type="text" class="form-control" placeholder="Nom" name="lastname">
					</div>
				</div>
				<div class="form-group col-md-12 col-sm-12 col-xs-12" style="padding:0;">
					<input type="text" class="form-control" placeholder="Entrez vôtre pseudonyme" name="username">
				</div>
				<div class="form-group col-md-12 col-sm-12 col-xs-12" style="padding:0;">
					<input type="email" class="form-control" placeholder="Entrez vôtre email" name="email">
				</div>
				<div class="form-group col-md-12 col-sm-12 col-xs-12" style="padding: 0;">
					<input type="password" class="form-control" placeholder="Entrez vôtre mot de passe" name="password">
				</div>
				<div class="form-group col-md-12 col-sm-12 col-xs-12" style="padding: 0;">
					<input type="password" class="form-control" placeholder="Confirmation de vôtre mot de passe" name="confirmPassword">
				</div>
				<input type="submit" class="btn btn-primary" value="Creation du compte">
				<% if ((boolean) request.getAttribute ("errorSignIn")) {
				      for (String msg : (List<String>) request.getAttribute ("errors")) {
			    	  	out.println (String.format ("<p class=\"text-danger\">%s</p>", msg));
				      }
				   }
				%>
			</form>
		</div>
  		<script src="/SandboxWebApp/inc/js/jquery.min.js"></script>
  		<script src="/SandboxWebApp/inc/js/bootstrap.min.js"></script>
	</body>
</html>