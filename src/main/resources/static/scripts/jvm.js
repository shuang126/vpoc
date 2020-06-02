let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        let topic = '/topic/gc/new';
        stompClient.subscribe(topic, function (greeting) {
            showNewObjects(JSON.parse(greeting.body));
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

function showNewObjects(objects) {
    for (let i = 0; i < objects.length; i++) {
        let obj = objects[i];
        console.log('got: ' + obj);
        setTimeout(function () {
            eden_space.allocate_one_obj(obj.size);
        }, 100 * i);
    }
}

$(function () {
    connect();
    init_heap();
    // new_objects();
});