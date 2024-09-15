import http from 'k6/http';
import { check, sleep } from 'k6';

export let options = {
  vus: 10, // virtual users
  duration: '1s', // test duration
};

export default function () {
  // Define the base URL for the API
  let baseURL = 'http://localhost:8080/api/accommodations';

  // Define query parameters
  let params = {
    //category: null,  // Example category
    checkInDate: '2024-07-10',  // Example check-in date
    checkOutDate: '2024-07-11',  // Example check-out date
    personNumber: 3  // Example number of persons
  };

  // Create the query string from parameters
  let queryString = Object.keys(params)
  .map(key => `${encodeURIComponent(key)}=${encodeURIComponent(params[key])}`)
  .join('&');

  // Send GET request to the API endpoint
  let res = http.get(`${baseURL}?${queryString}`);

  // Check the response status and output the result
  check(res, {
    'status was 200': (r) => r.status === 200,
    'response is not empty': (r) => r.json().length > 0,
  });

  // Add a sleep to simulate user think time
  sleep(1);
}
