let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        let topic = '/topic/gc/new';
        stompClient.subscribe(topic, function (greeting) {
            showNewObject(JSON.parse(greeting.body));
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

function showNewObject(obj) {
    console.log('showNewObject: ' + obj);
    eden_space.allocate_one_obj(obj.size);
}

$(function () {
    connect();
    init_heap();
    // new_objects();
});