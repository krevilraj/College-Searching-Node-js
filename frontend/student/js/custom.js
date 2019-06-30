$(document).ready(function () {

    "use strict";

    $("#login").submit(function (event) {
        event.preventDefault();
        var login_form = $(this);
        var data = login_form.serialize();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/login',
            data: data,
            dataType: "json",
            beforeSend: function () {
                $("#btn_signin").html("Please Wait...");
            },
            success: function (response) {
                login_form[0].reset();
                if (response.response) {
                    document.getElementById("message").innerHTML = "<div class=\"alert alert-success\" role=\"alert\">\n" +
                        "  Success!!! We are redirecting..." +
                        "</div>";
                    setTimeout(function () {
                        if (response.response) {
                            if (typeof(Storage) !== "undefined") {
                                localStorage.setItem("access_token", response.token);
                                localStorage.setItem("username", response.username);
                                localStorage.setItem("admin_status", response.admin_status);
                                localStorage.setItem("id", response._id);
                            }
                            $('<a href="index.html" id="index"></a>').appendTo("body");
                            document.getElementById("index").click();
                        }
                    }, 3000);
                }
                else {
                    document.getElementById("message").innerHTML = "<div class=\"alert alert-danger\" role=\"alert\">\n" +
                        response.message +
                        "</div>";
                }


            },
            complete: function () {
                $("#btn_signin").html("Sign In");
            },
            error: function () {
            }
        })
    });

    $("#signup").submit(function (event) {
        event.preventDefault();
        var signup_form = $(this);
        var data = signup_form.serialize();
        $.ajax({
            type: 'POST',
            url: 'http://localhost:8080/register',
            data: data,
            dataType: "json",
            beforeSend: function () {
                $("#btn_signup").html("Please Wait...");
            },
            success: function (response) {

                if (response.response == "Registered Successfully") {
                    document.getElementById("message2").innerHTML = "<div class=\"alert alert-success\" role=\"alert\">\n" +
                        "Success !!! We are redirecting..." +
                        "</div>";
                    setTimeout(function () {
                        $('<a href="index.html" id="index"></a>').appendTo("body");
                        document.getElementById("index").click();
                    }, 5000);
                } else {
                    document.getElementById("message2").innerHTML = "<div class=\"alert alert-danger\" role=\"alert\">\n" +
                        response.response +
                        "</div>";
                }
                signup_form[0].reset();

            },
            complete: function () {
                $("#btn_signup").html("Register");
            },
            error: function () {
            }
        })
    });

    $("#contact_form").submit(function (event) {
        event.preventDefault();
        var u_form = $(this);

        var url = 'http://localhost:8080/sendContact';

        var data = u_form.serialize();

        $.ajax({
            type: 'POST',
            url: url,
            data: data,
            dataType: "json",
            beforeSend: function () {
                $("#submit").html("Please Wait...");
            },
            success: function (response) {

                if (response.response) {
                    u_form[0].reset();
                    document.getElementById("contact_message").innerHTML = "<div class=\"alert alert-success\" role=\"alert\">\n" +
                        "Success!!! We will call you soon" +
                        "</div>";
                }
                else {
                    document.getElementById("contact_message").innerHTML = "<div class=\"alert alert-danger\" role=\"alert\">\n" +
                        response.message +
                        "</div>";
                }
            },
            complete: function () {
                $("#submit").html("Enroll Now");
            },
            error: function () {
            }
        })
    });


    if (localStorage.getItem("access_token") != "undefined" && localStorage.getItem("access_token") != "") {
        if (localStorage.getItem("admin_status") == "true") {
            $(".student").remove();
        } else {
            $(".admin").remove();
        }
        $(".u_log").remove();
    }
    else {
        $(".admin").remove();
        $(".user").remove();
    }
    $("#logout").click(function () {
        localStorage.setItem("access_token", "");
        localStorage.setItem("admin", "");
        localStorage.setItem("username", "");
        localStorage.setItem("_id", "");
        $('<a href="index.html" id="index"></a>').appendTo("body");
        document.getElementById("index").click();
    });




});