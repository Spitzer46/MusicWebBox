<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Email d'activation</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
	</head>
	<body>
	    <nav class="navbar navbar-dark bg-dark navbar-expand shadow-sm border-bottom" style="height:55px;">
	   	</nav>
	    <div class="container-fluid" style="height:90vh;width:100%;padding:10px;background-color:#fff;">
	        <div class="row" style="margin-top:10%;">
	            <div class="col col-sm-8 col-lg-6 offset-sm-2 offset-lg-3">
	                <div class="card">
	                    <div class="card-body">
	                        <h4 class="card-title">Email envoyé</h4>
	                        <p class="card-text">Un email d'activation a été envoyé sur vôtre boite mail.&nbsp;</p>
	                        <a class="card-link" href="#" onclick="fetch ('/SandboxWebApp/email/sendemailsignin')">Renvoyer l'email</a>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	</body>
</html>