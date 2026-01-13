function doMatch() {
  const year = document.getElementById('schoolYear').value;
  const major = document.getElementById('major').value;

  // 유효성 검사 추가 (비었을 때 안내)
  if (!year || !major) {
    alert('학년과 전공을 모두 선택해주세요.');
    return;
  }

  fetch(`/api/matches/create?schoolYear=${encodeURIComponent(year)}&major=${encodeURIComponent(major)}`, {
    method: 'POST'
  })
    .then(async res => {
      if (!res.ok) {
        let msg = '서버 오류: ' + res.status;
        try {
          const data = await res.json();
          if (data.error) msg = data.error;
        } catch (e) {}
        throw new Error(msg);
      }
      return res.json();
    })
    .then(data => {
      if (data.matchId) {
        window.location.href = '/dm/' + data.matchId;
      } else {
        alert('매칭 결과가 없습니다');
      }
    })
    .catch(err => {
      alert(err.message);
    });
}
