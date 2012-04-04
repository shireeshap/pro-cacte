<!DOCTYPE html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
 
   <body>
 
		<video controls poster="../images/proctcaeposter.jpg"  autoplay width="640" height="480" >
			<source src="../images/Pro-CTCAE_mfull.mp4" type="video/mp4" />
			<source src="../images/Pro-CTCAE_mfull.webm" type="video/webm" />
			<source src="../images/Pro-CTCAE_mfull.theora.ogv" type="video/ogg" />

			<object type="application/x-shockwave-flash" data="../images/player.swf" width="640" height="480">
				<param name="movie" value="../images/player.swf" />
				<param name="allowFullScreen" value="true" />
				<param name="wmode" value="transparent" />
				<param name="flashVars" value="autostart=true&amp;controlbar=over&amp;image=proctcaeposter.jpg&amp;file=Pro-CTCAE_mfull.mp4" />
				<img alt="PROCTCAE Help" src="../images/proctcaeposter.jpg" width="640" height="480" title="No video playback capabilities, please download the video below">
			</object>
		</video>
		
		
		   <!--
		<object type="application/x-shockwave-flash" data="../images/Pro-CTCAE_mfull.mp4" width="640" height="480">
				<param name="movie" value="../images/Pro-CTCAE_mfull.mp4" />
				<param name="allowFullScreen" value="true" />
				<param name="wmode" value="transparent" />
				<param name="flashVars" value="autostart=true&amp;controlbar=over&amp;image=..%2Fimages%2Fproctcaeposter.jpg&amp;file=%2Fimages%2FPro-CTCAE_mfull.mp4" />
				<img alt="PROCTCAE Help" src="../images/proctcaeposter.jpg" width="640" height="480" title="No video playback capabilities, please download the video below">
			</object>-->
  </body>
</html>

