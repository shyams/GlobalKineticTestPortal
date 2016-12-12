
function hideDivs() {
    $("#loginPage").addClass("hidden");
    $("#usersLoggedIn").addClass("hidden");
    $("#registerPage").addClass("hidden");
    $("#successAlert").addClass("hidden");
}
$(document).ready(function () {
    var idleTime;
    hideDivs();
    $("#registerPage").removeClass("hidden");
    $("#loginPage").removeClass("hidden");
    var idleInterval = setInterval(timerIncrement, 60000); // 1 minute
    //Zero the idle timer on mouse movement.
    $(this).mousemove(function (e) {
        idleTime = 0;
    });
    $(this).keypress(function (e) {
        idleTime = 0;
    });
    function timerIncrement() {
        idleTime = idleTime + 1;
        if (idleTime >= 3) { // 3 minutes
            console.log("We logging you out from the app");
            var strJSON = '{"token":"' + $("a#logoutBtn").attr("sessionID") + '"}';
            $.post("TestWebservice", strJSON, function (data, status) {
                if (status === "success") {
                    var jsonData = JSON.parse(data);
                    hideDivs();
                    window.location.reload();
                }
            });
        }
    }
});


$("#register").click(function () {

    var phone = $("#phoneToRegister").val();
    var pass = $("#passwordToRegister").val();
    var user = $("#usernameToRegister").val();
    if (!isNaN(phone) && phone != "" && pass != "" && user != "") {
        $.ajax({
            url: 'TestWebservice',
            type: 'PUT',
            data: '{"username":"' + user + '","password":"' + pass + '","phone":"' + phone + '"}',
            success: function (data, status) {
                if (status === "success") {
                    hideDivs();
                    window.location.reload();
                }
            }
        });
    }
    return false;
});

$("#login").click(function () {
    var pass = $("#passwordToLogin").val();
    var user = $("#usernameToLogin").val();
    if (pass != "" && user != "") {
        var strJSON = '{"username":"' + user + '","password":"' + pass + '"}';
        $.post("TestWebservice", strJSON, function (data, status) {
            if (status === "success") {
                var jsonData = JSON.parse(data);
                hideDivs();
                if (jsonData.id == undefined) {
                    window.location.reload();
                } else if (jsonData.id != undefined && jsonData.id != null) {
                    $("#loggerDetails").html("Signed in as: " + jsonData.id);
                    $("a#logoutBtn").attr("sessionID", jsonData.token)
                    $("a#logoutBtn").attr("user", jsonData.id)
                    getLoggersDetails();
                }
            }
        });
    }
    return false;
});

function getLoggersDetails() {
    $.get("TestWebservice", function (data, status) {

        if (status === "success") {
            hideDivs();
            var jsonData = JSON.parse(data);
            var users = jsonData.users;
            $('#userTable tbody').html("");
            $('#loggedInFiveMin tbody').html("");
            for (var i = 0; i < users.length; i++) {
                var count = i + 1;
                var user = users[i];

                var phone = user.phone
                var name = user.id;
                var time = user.time;
                $("#usersLoggedIn").removeClass("hidden");

                $('#userTable tbody').append('<tr><th scope="row">' + count + '</th><td>' + name + '</td><td>' + phone + '</td></tr>');
                var date = new Date();
                var millisec = date.getTime();
                var minDiff = (time - millisec) / 60 / 1000; //in minutes
                if (minDiff >= -5) {
                    $('#loggedInFiveMin tbody').append('<tr><th scope="row">' + count + '</th><td>' + name + '</td><td>' + phone + '</td></tr>')
                }
                

            }
        }
    })
}

$("a#logoutBtn").click(function () {
    var strJSON = '{"token":"' + $("a#logoutBtn").attr("sessionID") + '","id":"'+$("a#logoutBtn").attr("user")+'"}';
    $.post("TestWebservice", strJSON, function (data, status) {
        if (status === "success") {
            var jsonData = JSON.parse(data);
            hideDivs();
            window.location.reload();
        }
    });
});

function checkUserLoggedIn() {
    if (($("a#logoutBtn").attr("sessionID")) != undefined) {
        getLoggersDetails();
    }
}
setInterval(checkUserLoggedIn, (10 * 1000));
