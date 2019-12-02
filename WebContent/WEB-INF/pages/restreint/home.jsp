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
			*::-webkit-scrollbar-track {
				border-left: 1px solid #ddd;
			}
			*::-webkit-scrollbar-thumb {
				background: #2a3439;
				border-radius: 5px;
			}
			*::-webkit-scrollbar-thumb:hover {
				background: #43535b;
			}
			button {
				background-color: transparent;
				border: none;
			}
			.disable {
				display: none;
			}
			.enable {
				display: block;
			}
			#container {
				background: #2a3439;
			}
			button {
				background: transparent;
				border: none;
			}
			button:focus {
				outline: 0 !important;
			}
			#range {
			    -webkit-appearance: none;
			    -moz-apperance: none;
			    border-radius: 6px;
			    width: 75%;
			    height: 6px;
				background: linear-gradient(to right, #df7164 0%, #F5D0CC 0%);
			}
			#range::-moz-range-track {
			    border: none;
			    background: none;
			    outline: none;
			}
			#range:focus {
			    outline: none;
			    border: none;
			}
			#range::-webkit-slider-thumb {
			    -webkit-appearance: none !important;
			    background-color: #df7164;
			    height: 15px;
			    width: 15px;
			    border-radius: 50%;
			}
			#range::-moz-range-thumb {
			    -moz-appearance: none !important;
			    background-color: #df7164;
			    border: none;
			    height: 15px;
			    width: 15px;
			    border-radius: 50%;
			}
			#tracksList {
				position: relative;
				height: 100%;
				overflow: hidden;
				overflow-y: scroll;
				display: none;
			}
			#playlistTag {
				position: relative;
				width: 100%;
				height: 100%;
				display: flex;
				justify-content: center;
				align-items: center;
			}
		</style>
	</head>
	<body>  	
	    <nav class="navbar navbar-dark bg-dark navbar-expand d-flex justify-content-between shadow-sm border-bottom" style="height:55px;">
	        <div class="container-fluid">
	            <div class="collapse navbar-collapse d-flex justify-content-between" id="navcol-1" style="padding:10px;">
	            	<button class="btn btn-dark" type="button" style="background-color:rgba(255,255,255,0);" onclick="window.location.href='upload'">Upload</button>
	            	<button class="btn btn-dark" type="button" onclick="window.location.href='logout'">Log out</button>					
	            </div>
	        </div>
	    </nav>
	    <div class="container-fluid" style="height:90vh;width:100%;padding:10px;background-color:#fff;">
	        <div class="row" style="height:100%;margin:0px;">
	            <div class="col-sm-10 col-lg-8 col-xl-6 offset-sm-1 offset-lg-2 offset-xl-3" style="padding:0px;">
	                <div class="card rounded-lg" style="height:100%;width:100%;padding:0px;">
	                    <div class="card-body rounded-lg" style="width:100%;height:50vh;background-color:#fff;padding:0;">
	                    	<div id="playlistTag">No track exist on your playlist</div>
	                    	<ul class="list-group list-group-flush" id="tracksList"></ul>
	                    </div>
	                    <div class="card-footer" style="height:25vh;background-color:#2a3439;">
	                    	<audio id="audioPlayerTag"></audio>
	                    	<div class="text-light text-center overflow-hidden" id="titleTrack" style="width:100%;height:20px"></div>
							<div class="row d-flex justify-content-center pt-3">
								<input id="range" type="range" min="0" max="100" step="0.001", value="0">
							</div>
							<div class="row d-flex justify-content-center pt-3">
								<button class="mr-1" id="previousTrack">
									<img src="icons/previoustrack.png" width="30">
								</button>
								<button id="btnPlayStop">
									<img id="imgPlay" class="enable" src="icons/play.jpg" width="60">
									<img id="imgStop" class="disable" src="icons/stop.jpg" width="60">
								</button>
								<button class="ml-1" id="nextTrack">
									<img src="icons/nextrack.png" width="30">
								</button>
							</div>
	                    </div>
	                </div>
	            </div>
	        </div>
	    </div>
  		<script>
 			"use strict";
 			
  			let startId = 999999999;
  			let mediaSource = null;
  			let currentLine = null;
  			const titleTrack = document.getElementById ("titleTrack");
  			const tracksList = document.getElementById ("tracksList");
  			const playlitTag = document.getElementById ("playlistTag");
  			const audioPlayerTag = document.getElementById ("audioPlayerTag");
  			audioPlayerTag.addEventListener ("timeupdate", e => {
  				const elem = e.srcElement;
  				const percent = elem.currentTime / elem.duration * 100;
  				if (!isNaN (percent)) {
  					playerPanel.setSliderCursor (percent);
  				}
  			});
  			audioPlayerTag.addEventListener ("ended", () => {
  				nextTrack (currentLine);
  				console.log ("next track");
  			});

  			function post (url, data) {
  				const sp = new URLSearchParams ();
  				for (const key in data) {
  					sp.set (key, data [key]);
  				}
  				return fetch (url, {
  					method: "POST",
  				  	body: sp
  				});
  			}
  			
  			function loadChunkAndAppend (mediaSource, sourceBuffer) {
  				return new Promise (async (resolve) => {
	 				try {
	  					const resp = await fetch ("player/api/readChunk", { method: "POST" });
	  					const chunk = await resp.arrayBuffer ();
						if (chunk.byteLength === 0) {
							mediaSource.endOfStream ();
							resolve (false);
						}
						else {
							sourceBuffer.addEventListener ("updateend", () => resolve (true));
							sourceBuffer.addEventListener ("error", (err) => { throw err });
							sourceBuffer.appendBuffer (chunk);
						}
					}
					catch (err) {
						console.error (err);
					}
  				});
  			}
  			
  			async function nextChunk (sourceBuffer) {
  				try {
	  				const next = await loadChunkAndAppend (mediaSource, sourceBuffer);
	  				if (next) {
	  					nextChunk (sourceBuffer);
	  				}
  				}
  				catch (err) {
  					console.error (err);
  				}
  			}
  			
  			async function startPlaying (id) {
  				try {
  					const resp = await post ("player/api/loadtrack", { id: id });
					const trackInfos = await resp.json ();
					mediaSource = new MediaSource ();
					mediaSource.addEventListener ("sourceopen", () => {
						const sourceBuffer = mediaSource.addSourceBuffer ("audio/mpeg");
						mediaSource.duration = parseFloat (trackInfos.duration);	
					    nextChunk (sourceBuffer);
					});
					audioPlayerTag.src = URL.createObjectURL (mediaSource);
					playerPanel.play ();
  				}
  				catch (err) {
  					console.error (err);
  				}
  			}
  			
  			function trackClick (elem) {
				if (currentLine != null) {
 					currentLine.className = currentLine.className.replace (" bg-light shadow", "");
 					currentLine.style.zIndex = 0;
 				}
 				elem.className += " bg-light shadow";
 				elem.style.zIndex = 1000;
 				currentLine = elem;
 				titleTrack.innerHTML = elem.innerHTML;
 				startPlaying (parseInt (elem.id.match (/\d+$/) [0]));
  			}
  			
  			function createTrackLine (id, title) {
  				const elem = document.createElement ("li");
  				elem.id = "track#" + id;
  				elem.className = "d-block list-group-item list-group-item-action";
  				elem.innerHTML = title;
  				elem.onclick = () => trackClick (elem);
  				return elem;
  			} 
  			
  			async function loadTracks (limit) {
  				try  {
	  				const resp = await post ("player/api/nexttracks", { 
	  					start: startId,
	  					limit: limit
	  				});
					const tracks = await resp.json ();
					for (const track of tracks) {
						const elem = createTrackLine (track.id, track.title);
						tracksList.appendChild (elem);
					}
					startId = tracks.length ? tracks [tracks.length - 1].id : startId;
					if (tracksList.childNodes.length) {
						tracksList.style.display = "block";
						playlitTag.style.display = "none";
					}
					else {
						tracksList.style.display = "none";
						playlitTag.style.display = "flex";
					}
					return tracks.length;
  				}
  				catch (err) {
  					console.error (err);
  					return 0;
  				}
  			}
  			
  			function previousTrack (elem) {
  				const list = tracksList.childNodes;
  				for (let i = 1; i < list.length; i++) {
  					if (list [i] === elem) 
  						return list [i - 1];
  				}
  				return null;
  			}
  			
  			function nextTrack (elem) {
  				const list = tracksList.childNodes;
  				for (let i = 0; i < list.length; i++) {
  					if (list [i] === elem) {
  						if (i === list.length - 1) 
  							if (loadTracks (10) === 0) 
  								return null;
						return list [i + 1];
  					}
  				}
  				return null;
  			}

  			tracksList.addEventListener ("scroll", (e) => {
  				const elem = e.target;
				if (elem.scrollTop + elem.clientHeight >= elem.scrollHeight) {
					loadTracks (10);
				}
  			});
  			
	  		const playerPanel = {
				init () {
					this.iconPlay = document.getElementById ("imgPlay");
					this.iconStop = document.getElementById ("imgStop");
					this.btnPlayStop = document.getElementById ("btnPlayStop");
					this.range = document.getElementById ("range");
					this.previousTrack = document.getElementById ("previousTrack");
					this.nextTrack = document.getElementById ("nextTrack");
					//////////// setup event listener /////////////
					this.btnPlayStop.addEventListener ("click", (e) => this.switchPlayStop (e));
					this.range.addEventListener ("mousemove", (e) => this.slider (e));
					this.previousTrack.addEventListener ("click", (e) => this.previousTrackAction (e));
					this.nextTrack.addEventListener ("click", (e) => this.nextTrackAction (e));
				},
				play () {
					this.iconPlay.classList.replace ("enable", "disable");
					this.iconStop.classList.replace ("disable", "enable");
					audioPlayerTag.play ();
				},
				stop () {
					this.iconPlay.classList.replace ("disable", "enable");
					this.iconStop.classList.replace ("enable", "disable");
					audioPlayerTag.pause ();
				},
				switchPlayStop (e) {
					this.iconPlay.classList.contains ("enable") ? this.play () : this.stop ();
				},
				slider (e) {
					const slider = e.target;
					const val = (slider.value - slider.min) / (slider.max - slider.min);
					const percent = val * 100;
					this.range.style.background = "linear-gradient(to right, #df7164 " + percent + "%, #F5D0CC " + percent + "%)";
				},
				setSliderCursor (value) {
					this.range.value = value;	
					this.range.style.background = "linear-gradient(to right, #df7164 " + value + "%, #F5D0CC " + value + "%)";
				},
				previousTrackAction (e) {
					// console.log ("Previous track")
					if (currentLine != null) {
						const previous = previousTrack (currentLine);
						// console.log (previous);
						if (previous != null)
							trackClick (previous);
					}
				},
				nextTrackAction (e) {
					// console.log ("Next track");
					if (currentLine != null) {
						const next = nextTrack (currentLine);
						// console.log (next);
						if (next != null)
							trackClick (next);
					}
				}
			}
  			
	  		playerPanel.init ();
  			loadTracks (15);
  			
  		</script>
	</body>
</html>