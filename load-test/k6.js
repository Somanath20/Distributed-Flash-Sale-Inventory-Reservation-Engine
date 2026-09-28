import http from 'k6/http';
import { check } from 'k6';
export const options = { vus: 50, duration: '15s' };
export default function () {
  const id = `${__VU}-${__ITER}`;
  const res = http.post('http://localhost:8080/api/reservations', JSON.stringify({sku:'SHOE-001',quantity:1,idempotencyKey:`load-${id}`}), {headers:{'Content-Type':'application/json'}});
  check(res, {'request returns expected status': r => [201,409,500].includes(r.status)});
}
