<%@ page pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Sign in</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
	</head>
	<body>
		<nav class="navbar navbar-dark bg-dark navbar-expand shadow-sm border-bottom" style="height:55px;">
	   	</nav>
		<div class="container d-flex justify-content-center align-items-center">
			<div style="margin-top:50px">
				<h2>Inscription</h2>
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
					      for (Exception e : (List<Exception>) request.getAttribute ("errors")) {
				    	  	out.println (String.format ("<p class=\"text-danger\">%s</p>", e.getMessage ()));
					      }
					   }
					%>
				</form>
			</div>
		</div>
	</body>
</html>