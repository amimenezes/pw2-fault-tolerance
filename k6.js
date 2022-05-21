import exec from 'k6/execution';
import http from 'k6/http';
import { sleep } from 'k6';

export const options = {
    vus: 10,
    duration: '10s',
    thresholds: {
        // Como teste, os erros de HTTP devem ser menor do que 5%
	    http_req_failed: ['rate<0.05'],
	},
};

export default function () {
    http.get('http://localhost:8080/fault/bulkhead/' + exec.vu.idInTest);
    sleep(1);
}