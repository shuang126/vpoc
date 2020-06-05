let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/gc/new', function (data) {
            console.log('showNewObject: ' + data.body);
            eden_space.allocate_one_obj(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/mark', function (data) {
            console.log('mark: ' + data.body);
            eden_space.mark(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/copy', function (data) {
            console.log('copy: ' + data.body);
            eden_space.copy(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/sweep', function (data) {
            console.log('sweep: ' + data.body);
            eden_space.sweep(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/supervisor/pause', function (data) {
            console.log('pause: ' + data.body);
            pause(JSON.parse(data.body));
        });
    });
}

let debug_alert = "#debug_alert";
function pause(obj) {
    $(debug_alert).text(obj.msg);
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
});