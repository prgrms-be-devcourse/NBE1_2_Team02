import http from 'k6/http';
import { sleep } from 'k6';

// 테스트 설정
export const options = {
    stages: [
        { duration: '1m', target: 100 },  // 1분 동안 100명으로 증가
        { duration: '10s', target: 0 },       // 부하를 줄임
    ]
};

export default function () {
    // 요청을 보낼 URL
    const url = 'http://host.docker.internal:8080/concerts/redis/list';

    // GET 요청
    const response = http.get(url);

    // 응답 상태 코드 로그
    console.log(`Response status: ${response.status}`);

    // 요청 사이에 대기 시간
    sleep(1); // 1초 대기
}

