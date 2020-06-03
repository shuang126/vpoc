let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/gc/new', function (data) {
            showNewObject(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/mark', function (data) {
            mark(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/copy', function (data) {
            showNewObject(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/sweep', function (data) {
            showNewObject(JSON.parse(data.body));
        });
    });
}

function showNewObject(obj) {
    console.log('showNewObject: ' + obj);
    eden_space.allocate_one_obj(obj.size);
}

function mark(obj) {
    console.log('mark: ' + obj);
    eden_space.mark(obj.id);
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

$(function () {
    connect();
    init_heap();
    // new_objects();
});