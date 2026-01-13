let ws;
const userId = '내학번';     // 로그인 세션에서 가져오는 값
const friendId = '상대학번'; // 매칭된 상대방 학번

function connectChat() {
    ws = new WebSocket("ws://localhost:8080/chat");
    ws.onopen = () => { console.log("웹소켓 연결됨"); };
    ws.onmessage = (event) => {
        // 메시지 수신
        document.getElementById('chatArea').innerHTML += '<div>' + event.data + '</div>';
    };
}

function sendMessage() {
    const msg = document.getElementById('chatInput').value;
    // 서버에서 {to, msg} JSON 형태로 받는다고 가정
    ws.send(JSON.stringify({ to: friendId, msg: msg }));
}

window.onload = connectChat;