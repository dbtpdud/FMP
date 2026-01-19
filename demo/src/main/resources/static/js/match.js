let matchBtn = null;

function doMatch() {
  const yearEl = document.getElementById('schoolYear');
  const majorEl = document.getElementById('major');
  const btn = document.getElementById('matchBtn');
  matchBtn = btn;

  const year = yearEl ? yearEl.value : "";
  const major = majorEl ? majorEl.value : "";

  if (!year || !major) {
    alert('학년과 전공을 모두 선택해주세요.');
    return;
  }

  if (btn) btn.disabled = true;

  fetch(`/api/matches/create?schoolYear=${encodeURIComponent(year)}&major=${encodeURIComponent(major)}`, {
    method: 'POST'
  })
    .then(async res => {
      if (res.status === 401) throw new Error('로그인이 필요해요. 다시 로그인 해주세요.');

      if (!res.ok) {
        let msg = '서버 오류: ' + res.status;
        try {
          const data = await res.json();
          if (data && data.error) msg = data.error;
        } catch (e) {}
        throw new Error(msg);
      }

      return res.json();
    })
    .then(data => {
      if (!data || !data.matchId) {
        alert('매칭 생성에 실패했어.');
        return;
      }

      // ✅ WAITING이어도 일단 채팅 페이지로 이동
      //    (chat.html에서 matchId로 폴링해서 MATCHED가 되면 채팅 열림)
      window.location.href = '/dm/' + data.matchId;
    })
    .catch(err => {
      alert(err.message);
    })
    .finally(() => {
      if (matchBtn) matchBtn.disabled = false;
    });
}