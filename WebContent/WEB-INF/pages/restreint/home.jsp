<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Home</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
		<style>
			*::-webkit-scrollbar {
				width:10px;	
			}
			*::-webkit-scrollbar-thumb {
				background: #2a3439;
				border-radius: 5px;
			}
			*::-webkit-scrollbar-thumb:hover {
				background: #43535b;
			}
		</style>
	</head>
	<body>  	
	    <nav class="navbar navbar-light navbar-expand d-flex justify-content-between shadow-sm border-bottom" style="height:55px;background-color:#2a3439;">
	        <div class="container-fluid">
	            <div class="collapse navbar-collapse d-flex justify-content-between" id="navcol-1" style="padding:10px;">
	            	<button class="btn btn-dark" type="button" style="background-color:rgba(255,255,255,0);" onclick="window.location.href='upload'">Upload</button>
	            	<button class="btn btn-dark" type="button" onclick="window.location.href=''">Log out</button>
	            </div>
	        </div>
	    </nav>
	    <div class="container-fluid" style="height:90vh;width:100%;padding:10px;background-color:#fff;">
	        <div class="row" style="height:100%;margin:0px;">
	            <div class="col-sm-10 col-md-6 offset-sm-1 offset-md-3" style="padding:0px;">
	                <div class="card rounded-lg" style="height:100%;width:100%;padding:0px;">
	                    <div class="card-body rounded-lg" style="width:100%;height:50vh;background-color:#fff;padding:0;">
	                    	<ul class="list-group list-group-flush" style="height:100%;overflow:hidden;overflow-y:scroll;" id="tracksList">
							</ul>
	                    </div>
	                    <div class="card-footer" style="height:25vh;background-color:#2a3439;">
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
	    <script src="/SandboxWebApp/inc/js/jquery.min.js"></script>
  		<script src="/SandboxWebApp/inc/js/bootstrap.min.js"></script>
  		<script>
  			"use strict";
  			
  			const tracksList = document.getElementById ("tracksList");
  			let startId = 9999999;
  			
  			function startRead (id) {
  				$.ajax ({
  					type: "POST",
  					url: "player/api/loadtrack",
  					data: {
  						id: id
  					},
  					success: () => {},
  					error: () => {}
  				});
  			}
  			
  			function createTrackLine (id, title) {
  				const elem = document.createElement ("li");
  				elem.id = "track#" + id;
  				elem.className = "list-group-item list-group-item-action";
  				elem.innerHTML = title;
  				elem.onclick = (e) => {
  					startRead (parseInt (e.target.id.match (/\d+$/) [0]));
  				}
  				return elem;
  			} 
  			
  			function loadTracks (limit) {
  				$.ajax ({
  					type: "POST",
  					url: "player/api/nexttracks",
  					data: {
  						start: startId,
  						limit: limit
  					},
  					success: (jsonTracks) => {
  						const tracks = JSON.parse (jsonTracks);
						for (const track of tracks) {
 							const elem = createTrackLine (track.id, track.title);
 							tracksList.appendChild (elem);
						}
						startId = tracks.length ? tracks [tracks.length - 1].id : startId;
  					},
  					error: () => {}
  				});
  			}
  		

  			tracksList.addEventListener ("scroll", (e) => {
  				const elem = e.target;
				if (elem.scrollTop + elem.clientHeight >= elem.scrollHeight) {
					loadTracks (10);
				}
  			});
  			loadTracks (15);
  			
  		</script>
	</body>
</html>