import http from 'k6/http';
import {check, randomSeed, sleep} from 'k6';
import {Counter} from 'k6/metrics';

export const options = {
  vus: 10, // 동시 사용자 수
  duration: '30s', // 테스트 지속 시간
};

randomSeed(12345); // For consistent randomness across test runs

// 테스트 실패 수를 기록할 커스텀 카운터
export const errorCounter = new Counter('errors');

// 회원 ID 리스트
const ids = [
  '1',
  '2',
  '3',
  '4',
  '5',
  '6',
  '7',
  '8',
  '9',
  '10',
];

export default function () {
  const baseUrl = 'http://172.30.1.83:8080/api/reservation'; // 실제 API 엔드포인트로 변경

  // 각 VU가 고유한 이메일을 선택
  const vuIndex = __VU % ids.length; // VU 인덱스를 ids 배열의 길이로 나눈 나머지를 사용
  const id = ids[vuIndex];

  const payload = JSON.stringify({
    check_in_date: '2024-07-01', // 테스트 데이터
    check_out_date: '2024-07-02', // 테스트 데이터
    product_id: 1, // 테스트 데이터
    accommodation_id: 1, // 테스트 데이터
    person_number: 2, // 테스트 데이터
  });

  const fullUrl = `${baseUrl}?id=${encodeURIComponent(id)}`;

  const params = {
    headers: {
      'Content-Type': 'application/json',
      'mock-token': id, // access token 회피
    },
  };

  // 각 가상 사용자는 한 번의 POST 요청을 보냄
  const response = http.post(fullUrl, payload, params);

  // 응답 상태 코드가 200인지 확인
  const checkRes = check(response, {
    'is status 200': (r) => r.status === 200,
  });

  // 상태 코드가 200이 아니면 에러 카운터 증가
  if (!checkRes) {
    errorCounter.add(1);
  }

  // 요청 후 짧은 대기 시간
  sleep(1);
}
