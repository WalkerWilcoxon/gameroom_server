var stompClient = null;
var num = Math.floor(Math.random()*2)
function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}
var stompClient = null;
function connect() {
    stompClient = new window.StompJs.Client({
        webSocketFactory: function () {
            return new WebSocket("ws://coms-309-bs-7.cs.iastate.edu:8080/ws");
        }
    });
    stompClient.onConnect = function (frame) {
        frameHandler(frame)
    };
    stompClient.onWebsocketClose = function () {
        onSocketClose();
    };

    stompClient.activate();
}
function frameHandler(frame) {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/game/' + num, function (message) {
    	console.log(message);
        showGreeting(message.body);
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.publish({destination: "/app/game/" + num, body: $("#name").val()});
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}
function disconnect() {
    if (stompClient !== null) {
        stompClient.deactivate();
    }
    setConnected(false);
    console.log("Disconnected");
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});