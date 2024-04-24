var stompFailureCallback = function (error) {
    console.log('STOMP: ' + error);
    setTimeout(stompConnect, 10000);
    console.log('STOMP: Reconecting in 10 seconds');
};

function stompConnect() {
    console.log('STOMP: Attempting connection');
    // recreate the stompClient to use a new WebSocket
    stompClient = Stomp.over('ws://localhost:61612');
    stompClient.connect('login', 'password', stompSuccessCallback, stompFailureCallback);
}