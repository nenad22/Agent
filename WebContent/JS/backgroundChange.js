/**
 * Created by Dejan Ribic on 03-06-17.
 * Changes background dynamically.
 */

(function() {
    var urls = ['backgroundSeamlessBlue.png', 'backgroundSeamlessGreen.png'];

    function swap() {
    	var htmlPart = "url('";
    	var serverPart = "http://localhost:8080";
    	var folderPart = "/agent/Assets/";
    	var completeString = htmlPart + serverPart + folderPart;
        var stringBuild = (completeString + urls[Math.floor(Math.random()*urls.length)] + "')");
        
        //console.log(stringBuild);
        document.body.style.backgroundImage = stringBuild;
    }

    // Mozilla, Opera and webkit nightlies currently support this event
    if ( document.addEventListener ) {
        window.addEventListener( 'load', swap, false );
    // If IE event model is used
    } else if ( document.attachEvent ) {
        window.attachEvent( 'onload', swap );
    }
})();
