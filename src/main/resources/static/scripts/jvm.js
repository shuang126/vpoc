let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        let topic = '/topic/gc/new';
        stompClient.subscribe(topic, function (greeting) {
            showGreeting(JSON.parse(greeting.body));
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showGreeting(message) {
    console.log('got: ' + message);
}

$(function () {
    connect();
    init_heap();
    // new_objects();
});