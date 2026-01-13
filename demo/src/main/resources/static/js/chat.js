let userId = '';
let friendId = 'opponentName';
let ws = null;

// 1. 페이지 로드 시 REST API로 내 정보, 상대 정보 받아오기
window.onload = function() {
  fetch('/api/chat/info') // 백엔드에서 내 userId, friendId 등 JSON으로 내려줘야 함
    .then(response => response.json())
    .then(info => {
      userId = info.myUserId;           // 예: 20240001
      friendId = info.opponentUserId;   // 예: 20240002

      // 상대방 프로필, 이름 등 화면에 동적 적용
      document.getElementById('opponentName').innerText = info.opponentName;
      document.getElementById('opponentProfile').src = info.opponentProfile || 'https://randomuser.me/api/portraits/men/45.jpg';

      // 내 정보가 필요하면 여기서 같이 할당

      // **웹소켓 연결도 정보 받아온 후에 실행해야 함!**
      connectChat();
    });
};

//
function connectChat() {
  ws = new WebSocket("ws://localhost:8080/chat"); // 서버와 주소 맞추기

  ws.onopen = () => {
    console.log("웹소켓 연결됨");
    // (선택) 서버에 최초 연결 알림 or 인증 메시지 보낼 수도 있음
    // ws.send(JSON.stringify({ type: "ENTER", userId }));
  };

  ws.onmessage = (event) => {
    // 서버에서 받는 메시지는 {from, to, msg, timestamp} 등으로 가정
    const data = JSON.parse(event.data);

    // 내 메시지/상대 메시지 구분
    if (data.from === userId) {
      showMessage(data.msg, 'me', data.timestamp);
    } else if (data.from === friendId) {
      showMessage(data.msg, 'you', data.timestamp);
    }
  };

  ws.onclose = () => {
    console.log("웹소켓 연결 종료");
  };

  ws.onerror = (err) => {
    console.error("웹소켓 에러", err);
  };
}

// 메시지 전송 함수
function sendMessage(event) {
  event.preventDefault();
  const input = document.getElementById('msgInput');
  const message = input.value.trim();
  if (!message || !ws || ws.readyState !== WebSocket.OPEN) return;

  // 서버에 메시지 전송 (보통 JSON으로 전송)
  ws.send(JSON.stringify({
    from: userId,
    to: friendId,
    msg: message
  }));

  showMessage(message, 'me'); // 화면에 내 메시지 추가
  input.value = '';
  document.getElementById('chatMessages').scrollTop = document.getElementById('chatMessages').scrollHeight;
}

// 메시지 출력
function showMessage(message, who, timestamp) {
  const chatMessages = document.getElementById('chatMessages');
  const msgRow = document.createElement('div');
  msgRow.className = 'message-row ' + (who === 'me' ? 'me' : 'you');

  // 시간 표시
  let timeStr = '';
  if (timestamp) {
    const date = new Date(timestamp);
    timeStr = date.getHours().toString().padStart(2,'0') + ':' + date.getMinutes().toString().padStart(2,'0');
  } else {
    const now = new Date();
    timeStr = now.getHours().toString().padStart(2,'0') + ':' + now.getMinutes().toString().padStart(2,'0');
  }

  msgRow.innerHTML = `<div class="message-bubble">${message}</div><div class="message-time">${timeStr}</div>`;
  chatMessages.appendChild(msgRow);
  chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 채팅 종료
function endChat() {
  if(confirm('채팅을 종료하고 매칭 페이지로 돌아가시겠습니까?')) {
    window.location.href = '/match';
  }
}
