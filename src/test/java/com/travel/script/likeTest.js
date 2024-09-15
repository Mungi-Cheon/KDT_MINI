import http from 'k6/http';
import { check } from 'k6';
import { SharedArray } from 'k6/data';

// 사전에 준비된 사용자 목록을 로드합니다.
const users = new SharedArray('users', function () {
  return JSON.parse(open('./users.json'));
});

export const options = {
  vus: 100,
  duration: '1s',
};

export default function () {
  const userIndex = __VU - 1; // 가상 사용자 번호에 따라 사용자 선택
  const user = users[userIndex];
  const accommodationId = 1; // 테스트할 숙소 ID

  const payload = JSON.stringify({ accommodationId: accommodationId });
  const params = {
    headers: {
      'Content-Type': 'application/json',
      'mock-token': user.id, // access token 회피
    },
  };

  // 좋아요 클릭 요청
  const res = http.post(`http://localhost:8080/api/auth/like`, payload, params);

  // 결과 체크
  check(res, {
    'status is 200': (r) => r.status === 200,
    'liked is true': (r) => JSON.parse(r.body).liked === true,
  });
}
