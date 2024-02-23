import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    stages: [
        {duration: '1m', target: 5},
        {duration: '3m', target: 5},
        {duration: '1m', target: 7},
        {duration: '3m', target: 7},
        {duration: '1m', target: 0}
    ]
};

export default () => {
    const url = 'http://host.docker.internal:8080/api/records';
    const payload = JSON.stringify({
        action: "공부",
        memo: "Service Logic 작성",
        timeLogRequests: [
            {
                date: "2024-01-01",
                startTime: "09:00:00",
                endTime: "10:00:00"
            },
            {
                date: "2024-01-01",
                startTime: "11:00:00",
                endTime: "12:00:00"
            }
        ]
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(url, payload, params);
};