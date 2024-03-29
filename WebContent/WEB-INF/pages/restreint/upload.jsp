<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Upload page</title>
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="/SandboxWebApp/inc/style/bootstrap.min.css">
  		<style type="text/css">
	  		.my-custom-scrollbar {
				position: relative;
				width: 100%;
				height: 100%;
				overflow: auto;
			}
			*::-webkit-scrollbar-track {
				border-left: 1px solid #ddd;
			}
			.table-wrapper-scroll-y {
				display: block;
			}
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
			#dropdragTag {
				position: relative;
				width: 100%;
				height: 100%;
				display: flex;
				justify-content: center;
				align-items: center;
			}
			#dropzoneBox {
				position: relative;
				background: #fff;
			}
  		</style>
	</head>
	<body>
	    <nav class="navbar navbar-light navbar-expand d-flex justify-content-between shadow-sm border-bottom" style="height:55px;background-color:#2a3439;">
	        <div class="container-fluid">
	            <div class="collapse navbar-collapse d-flex justify-content-between" id="navcol-1" style="padding:10px;">
	                <button class="btn btn-dark" type="button" onclick="window.location.href='player'" style="background-color:rgba(255,255,255,0);">Player</button>
	                <button class="btn btn-dark" type="button" onclick="window.location.href='logout'">Log out</button>
                </div>
	        </div>
	    </nav>
	    <div class="container-fluid" style="height:90vh;width:100%;padding:10px;background-color:#fff;">
	        <div class="row" style="height:100%;margin:0px;">
	            <div class="col-sm-10 col-md-8 offset-sm-1 offset-md-2" style="padding:0px;">
	                <div class="card rounded-lg" style="height:100%;width:100%;padding:0px;">
	                    <div class="card-header" style="height:8vh;"></div>
	                    <div class="card-body d-flex justify-content-center align-items-center rounded-lg" style="max-width:100%;height:70vh;padding:0;background-color:#fff;">
                 			<div class="table-wrapper-scroll-y my-custom-scrollbar table-hover" id="dropbox">
								<div id="dropdragTag">Drag and drop your tracks</div>
								<table class="table mb-0" di="dropzoneBox">
				    				<tbody id="dropboxBody"></tbody>
			  					</table>
							</div>
	                    </div>
	                    <div class="card-footer d-inline-block d-flex justify-content-between align-items-center" style="height:10%;padding:7px;">
	                    	<button class="btn btn-outline-primary" type="button" id="btnUpload">Upload</button>
	                        <div class="progress" style="width:50%;" id="progressBarContainer">
	                            <div class="progress-bar" style="width:0%;" id="progressBar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100"></div>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
    	<script type="text/javascript">
  			window.onload = function () {

  				"use strict";
  				
  				const audioContext = new (window.AudioContext || window.webkitAudioContext) ();
  				let uploading = false;
  				const listOfTracks = [];
  				const dropbox = document.getElementById ("dropbox");
  				const list = document.getElementById ("dropboxBody");
  				const progressBar = document.getElementById ("progressBar");
  				const progressBarContainer = document.getElementById ("progressBarContainer");
  				progressBarContainer.style.visibility = "hidden";
  				const dropdragTag = document.getElementById ("dropdragTag");
  				
  				dropbox.addEventListener("dragenter", e => {
  					e.stopPropagation ();
  					e.preventDefault ();
  				}, false);
  				
  				dropbox.addEventListener("dragover", e => {
  					e.stopPropagation ();
  					e.preventDefault ();
  				}, false);

  				dropbox.addEventListener("drop", e => {
	  				e.stopPropagation ();
	  				e.preventDefault ();
	  				const files = e.target.files || (e.dataTransfer && e.dataTransfer.files);
  				  	handler(files);
  				}, false);

  				function isPresent (track) {
  					for (const t of listOfTracks) {
  						if (t.name === track.name) return true;
  					}
  					return false;
  				}
  				
  				function handler(files) {
  				  	for (const file of files) {
	  				    if (!file.type.match(/^audio\//)) {
	  				    	console.error ("Element du type : " + file.type);
	  				    	continue;
	  				    }
	  				  	if (isPresent (file)) continue;
  				  		listOfTracks.push (file);
  				    	updateDisplay ();
  				    }
  				}
  				
  				function updateDisplay () {
  				    let html = "";
  				    for (let i = 0; i < listOfTracks.length; i++) {
  				    	const track = listOfTracks [i];
	  				  	html += `
	  				  		<tr style="">
	  				  			<th scope="row" style="width:5%">` + (i + 1) + `</th>
	  				  			<td style="width:90%">` + track.name + `</td>
	  				  			<td style="width:5%"><button class="btn btn-outline-danger btn-sm btn-sup" role="button">X</button></td>
	  				  		</tr>
	  				  	`;
  				    }
  				    list.innerHTML = html;
  				    const elems = document.getElementsByClassName ("btn-sup");
  				    for (const elem of elems) {
  				    	elem.onclick = function (e) { 
  				    		const ppe = elem.parentNode.parentNode;
  				    		const index = parseInt (ppe.getElementsByTagName ("th") [0].innerHTML) - 1;
  				    		listOfTracks.splice (index, 1);
  				    		updateDisplay ();
  				    	}
  				    }
  				    dropdragTag.style.display = listOfTracks.length ? "none" : "flex";
  				}
  				
  			    function uploadFile (track) {
  					return new Promise ((resolve, reject) => {
	  					const reader = new FileReader ();
  	  					reader.onload = (e) => {
  	  						audioContext.decodeAudioData (e.target.result).then (buffer => {
	  	  						const form = new FormData ();
	  		  					form.append ("file", track);
	  		  					form.append ("duration", buffer.duration);
	  		  					const xhr = new XMLHttpRequest ();
	  		  					xhr.upload.addEventListener ("load", () => {
	  		  						console.log ("load");
	  		  						progressBarContainer.style.visibility = "hidden";
	  		  					    progressBar.style.width = "0%";
	  		  						resolve ();
	  		  					});
	  		  					xhr.upload.addEventListener ("error", () => {
	  		  						console.log ("error");
	  		  						progressBar.className = "progress-bar bg-danger";
	  		  						setTimeout (() => {
	  		  							progressBarContainer.style.visibility = "hidden";
	  		  						    progressBar.style.width = "0%";
	  		  						    reject ();
	  		  						}, 1000);
	  		  					});
	  		  					xhr.upload.addEventListener ("progress", e => {
	  		  						const percent = (e.loaded / e.total * 100);
	  		  						progressBar.style.width = percent + "%";
	  		  						progressBar.setAttribute ("aria-valuenow", percent);
	  		  					});
	  		  					xhr.upload.addEventListener ("loadstart", () => {
	  		  				 	    console.log ("load start");
	  		  						progressBar.className = "progress-bar";
	  		  						progressBarContainer.style.visibility = "visible";
	  		  					});
	  		  					xhr.open ("POST", "upload", true);
	  		  					xhr.send (form);
  	  						});
  	  					}
  	  					reader.readAsArrayBuffer (track);
  					});
  				}
  				
  				async function uploadAllFiles (listOfTracks) {
  					while (listOfTracks.length) {
  						const track = listOfTracks.pop ();
  						try {
  							await uploadFile (track);
  						}
  						catch (e) {
  							console.error ("Erreur de téléchagement sur le titre : " + track.name);
  						}
  						updateDisplay ();
  					}
  					uploading = false;
  				}
  				
  				document.getElementById ("btnUpload").addEventListener ("click", e => {
  					if (!uploading) {
  						uploading = true;
  						uploadAllFiles (listOfTracks);
  					}
  				}, false);

  			}
  		</script>
	</body>
</html>