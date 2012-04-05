<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<html>
 <tags:javascriptLink name="prototype"/>
 <tags:javascriptLink name="lightwindow"/>
 <tags:javascriptLink name="scriptaculous"/>
 
   <body>
 
		<video controls poster="../images/proctcaeposter.jpg"  autoplay width="640" height="480" >
			<source src="../images/Pro-CTCAE_mfull.mp4" type="video/mp4" />
			<source src="../images/Pro-CTCAE_mfull.webm" type="video/webm" />
			<source src="../images/Pro-CTCAE_mfull.theora.ogv" type="video/ogg" />

			<object type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" width="640" height="480">
				<param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
				<param name="allowFullScreen" value="true" />
				<param name="wmode" value="transparent" />
				<param name="flashVars" value="autostart=true&amp;controlbar=over&amp;image=https%3A%2F%2F${pageContext.request.serverName}:8443%2Fproctcae%2fimages%2Fproctcaeposter.jpg&amp;file=https%3A%2F%2F${pageContext.request.serverName}:${pageContext.request.serverPort}%2Fproctcae%2fimages%2FPro-CTCAE_mfull.mp4" />
				<img alt="PROCTCAE Help" src="/images/proctcaeposter.jpg" width="640" height="480" title="No video playback capabilities, please download the video below">
			</object>
		</video>
		
					
		<!-- worked on everything except IE8 on tablet
		<object type="application/x-shockwave-flash" data="../images/player.swf" width="640" height="480">
			<param name="movie" value="../images/player.swf" />
			<param name="allowFullScreen" value="true" />
			<param name="wmode" value="transparent" />
			<param name="flashVars" value="autostart=true&amp;controlbar=over&amp;image=..%2Fimages%2Fproctcaeposter.jpg&amp;file=Pro-CTCAE_mfull.mp4" />
							<param name="flashVars" value="config={'playlist':['..%2Fimages%2Fproctcaeposter.jpg',{'url':'https%3A%2F%2F10.10.10.143%3A8443%2Fproctcae%2Fimages%2FPro-CTCAE_mfull.mp4','autoPlay':true}]}" />
			
			<img alt="PROCTCAE Help" src="../images/proctcaeposter.jpg" width="640" height="480" title="No video playback capabilities, please download the video below">
		</object>
		
		works everywhgeree
		<object type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" width="640" height="360">
			<param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
			<param name="allowFullScreen" value="true" />
			<param name="wmode" value="transparent" />
			<param name="flashVars" value="config={'playlist':['http%3A%2F%2Fsandbox.thewikies.com%2Fvfe-generator%2Fimages%2Fbig-buck-bunny_poster.jpg',{'url':'http%3A%2F%2Fclips.vorwaerts-gmbh.de%2Fbig_buck_bunny.mp4','autoPlay':false}]}" />
			<img alt="Big Buck Bunny" src="http://sandbox.thewikies.com/vfe-generator/images/big-buck-bunny_poster.jpg" width="640" height="360" title="No video playback capabilities, please download the video below" />
		</object>
		
		
		-->
			
  </body>
</html>

