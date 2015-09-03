<!DOCTYPE html>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
 <tags:javascriptLink name="prototype"/>
 <tags:javascriptLink name="lightwindow"/>
 <tags:javascriptLink name="ga"/>
 
  <body>
	    <script type="text/javascript">
			 var canPlay = false;
			 var v = document.createElement('video');
			 if(v.canPlayType && v.canPlayType('video/mp4').replace(/no/, '')) {
			     canPlay = true;
			 }
		</script>
	
        <c:choose>
           <c:when test="${param.lang eq 'es'}">
                <c:set var="poster" value="proctcaeposterspanish.png" />
                <div id="a" style="display:none">
                    <video id="videoEsA" controls poster="https://${pageContext.request.serverName}/proctcae/images/${poster}"  autoplay="true" width="640" height="480" >
                        <source src="https://${pageContext.request.serverName}/proctcae/images/Pro-CTCAE_mfull_spanish.mp4" type="video/mp4" />
                        <source src="https://${pageContext.request.serverName}/proctcae/images/Pro-CTCAE_mfull_spanish.theora.ogv" type="video/ogg" />
                        <source src="https://${pageContext.request.serverName}/proctcae/images/Pro-CTCAE_mfull_spanish.webm" type="video/webm" />
        
                        <object type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" width="640" height="480">
                                <param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
                                <param name="allowFullScreen" value="true" />
                                <param name="wmode" value="transparent" />
                                <param name="flashVars" value="config={'playlist':['https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2F${poster}',{'url':'https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2FPro-CTCAE_mfull_spanish.mp4','autoPlay':true}]}" />
                                <img alt="PROCTCAE Help" src="../images/${poster}" width="640" height="480" title="No video playback capabilities, please download the video below">
                        </object>
                    </video>
               </div>
               
               <div id="b" style="display:none">
                    <object type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" width="640" height="480">
                            <param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
                            <param name="allowFullScreen" value="true" />
                            <param name="wmode" value="transparent" />
                            <param name="flashVars" value="config={'playlist':['https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2F${poster}',{'url':'https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2FPro-CTCAE_mfull_spanish.mp4','autoPlay':true}]}" />
                            <img alt="PROCTCAE Help" src="https://${pageContext.request.serverName}/proctcae/images/${poster}" width="640" height="480" title="No video playback capabilities, please download the video below">
                    </object> 
                </div>
        
                <p>
                    <strong><tags:message code="download.video"/>:</strong> <a href="../images/Pro-CTCAE_mfull_spanish.mp4">MP4 formato</a> | <a href="../images/Pro-CTCAE_mfull_spanish.theora.ogv">Ogg formato</a> | <a href="../images/Pro-CTCAE_mfull_spanish.webm">WebM formato</a>
                </p>                   
           </c:when>
           <c:otherwise>
                <c:set var="poster" value="proctcaeposter.jpg" />
		        <div id="a" style="display:none">
		            <video id="videoEnA" controls poster="https://${pageContext.request.serverName}/proctcae/images/${poster}"  autoplay="true" width="640" height="480" >
		                <source src="https://${pageContext.request.serverName}/proctcae/images/Pro-CTCAE_mfull.mp4" type="video/mp4" />
		                <source src="https://${pageContext.request.serverName}/proctcae/images/Pro-CTCAE_mfull.theora.ogv" type="video/ogg" />
		                <source src="https://${pageContext.request.serverName}/proctcae/images/Pro-CTCAE_mfull.webm" type="video/webm" />
		
		                <object type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" width="640" height="480">
		                        <param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
		                        <param name="allowFullScreen" value="true" />
		                        <param name="wmode" value="transparent" />
		                        <param name="flashVars" value="config={'playlist':['https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2F${poster}',{'url':'https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2FPro-CTCAE_mfull.mp4','autoPlay':true}]}" />
		                        <img alt="PROCTCAE Help" src="../images/${poster}" width="640" height="480" title="No video playback capabilities, please download the video below">
		                </object>
		            </video>
		       </div>
		       
		       <div id="b" style="display:none">
		            <object type="application/x-shockwave-flash" data="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" width="640" height="480">
		                    <param name="movie" value="http://releases.flowplayer.org/swf/flowplayer-3.2.1.swf" />
		                    <param name="allowFullScreen" value="true" />
		                    <param name="wmode" value="transparent" />
		                    <param name="flashVars" value="config={'playlist':['https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2F${poster}',{'url':'https%3A%2F%2F${pageContext.request.serverName}%2Fproctcae%2Fimages%2FPro-CTCAE_mfull.mp4','autoPlay':true}]}" />
		                    <img alt="PROCTCAE Help" src="https://${pageContext.request.serverName}/proctcae/images/${poster}" width="640" height="480" title="No video playback capabilities, please download the video below">
		            </object> 
		        </div>
		
		        <p>
		            <strong><tags:message code="download.video"/>:</strong> <a href="../images/Pro-CTCAE_mfull.mp4">MP4 format</a> | <a href="../images/Pro-CTCAE_mfull.theora.ogv">Ogg format</a> | <a href="../images/Pro-CTCAE_mfull.webm">WebM format</a>
		        </p>                 
           </c:otherwise>
       </c:choose>

		<script>
			//depending on the browser, either display div a or div b. 
			if(canPlay){
				$('a').style.display = '';
			} else {
				$('b').style.display = '';
				jQuery('#videoEnA').attr("autoplay", false);
				jQuery('#videoEsA').attr("autoplay", false);
			}
		</script>
			
  </body>
</html>

