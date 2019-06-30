$(document).ready(function(){
    $("#signout").click(function () {
        localStorage.setItem("access_token", "");
        localStorage.setItem("admin", "");
        localStorage.setItem("username", "");
        localStorage.setItem("_id", "");
        $('<a href="../student/index.html" id="index"></a>').appendTo("body");
        document.getElementById("index").click();
    });
    if(localStorage.getItem("access_token")=="undefined" || localStorage.getItem("access_token")=="" ){
        $( '<a href="../student/index.html" id="aa"></a>' ).appendTo( "body" );
        document.getElementById("aa").click();
    }
});