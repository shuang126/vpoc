import {minor_gc, init_heap} from "./jvm_svg.js"

let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/gc/new', function (data) {
            console.log('showNewObject: ' + data.body);
            minor_gc.new(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/mark', function (data) {
            console.log('mark: ' + data.body);
            minor_gc.mark(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/copy', function (data) {
            console.log('copy: ' + data.body);
            minor_gc.copy(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/sweep', function (data) {
            console.log('sweep: ' + data.body);
            minor_gc.sweep(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/gc/promotion', function (data) {
            console.log('promotion: ' + data.body);
            minor_gc.promotion(JSON.parse(data.body));
        });
        stompClient.subscribe('/topic/supervisor/pause', function (data) {
            console.log('pause: ' + data.body);
            $("#debug_alert").text(data.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

$(function () {
    connect();
    init_heap();
    $("#run").click(() => {
        let request = {
            "size": 1,
            "count": 1000,
            "delay": 100,
            "reset": true,
            "debug": false
        }
        $.postJSON("/jvm/gc/new", request,
            (data) => console.log("/jvm/gc/new: " + data));
    });
});