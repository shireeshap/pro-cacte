/**
 * @author David Coffey - http://www.davidcoffey.us
 */
var frameHeight = 201; //frame height of your image sprite in pixels
var btn_status = "closed";
var ypos = 0;
var currentFrame = 0;
var timeCount = 0;
var browser = navigator.appName;

if (browser == 'Microsoft Internet Explorer') {
    var frameRate = 40; //desired frame rate in milliseconds for IE
}
else {
    var frameRate = 50; //desired frame rate in milliseconds for all other browsers
}

function activate_button(){
    switch (btn_status) {
        case "closed":
            animateToOpen();
            btn_status = "open";
            break;
        case "open":
            btn_status = "active";
            break;
    }
}

function animateToOpen(){
    var button = document.getElementById("submit_btn");
    currentFrame++;
    timeCount++;
    ypos = currentFrame * -frameHeight;
    button.style.backgroundPosition = '0px ' + ypos + 'px';
    if (btn_status == "active") {
        btn_down();
    }
    if (currentFrame <= 24) {
        setTimeout(animateToOpen, frameRate);
    }
    else {
        goBackwards()
    }
}

function goBackwards(){
    var button = document.getElementById("submit_btn");
    currentFrame--;
    timeCount++;
    ypos = currentFrame * -frameHeight;
    button.style.backgroundPosition = '0px ' + ypos + 'px';
    
    if (timeCount >= 200) {
        closeUp();
    }
    else {
        if (btn_status == "active") {
            btn_down();
        }
        if (currentFrame >= 16) {
            setTimeout(goBackwards, frameRate);
        }
        else {
            animateToOpen();
        }
    }
}

function btn_down(){
    var button = document.getElementById("submit_btn");
    currentFrame = 25;
    timeCount = 0;
    ypos = currentFrame * -frameHeight;
    button.style.backgroundPosition = '0px ' + ypos + 'px';
    if (browser == 'Microsoft Internet Explorer') {
        button.attachEvent('onmouseup', mouseup);
    }
    else {
        button.addEventListener('mouseup', mouseup, false);
    }
}

function mouseup(){
    btn_status = "open";
    var button = document.getElementById("submit_btn");
    currentFrame = 24;
    ypos = currentFrame * -frameHeight;
    button.style.backgroundPosition = '0px ' + ypos + 'px';
    //put link to actual SUBMIT action here
	document.myForm.submit();
}

function closeUp(){
    var button = document.getElementById("submit_btn");
    btn_status = "closed";
    currentFrame--;
    timeCount = 0;
    ypos = currentFrame * -frameHeight;
    button.style.backgroundPosition = '0px ' + ypos + 'px';
    if (currentFrame > 0) {
        setTimeout(closeUp, frameRate);
    }
    
}
