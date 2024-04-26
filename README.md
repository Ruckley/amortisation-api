# Amortisation API

This API provides endpoints for managing amortisation schedules.

## How to run tests

Please use maven to run tests. Not that the tests use testcontainers and so your docker engine wil need to be running.<br>
```mvn clean test```

## How to run
Please use the Makefile provided. This will create the jar and spin up the api and db containers
Api container exposes itself on localhost:8080<br>
```make all```

## Endpoints

### Create Amortisation Schedule
- **URL:** `/amortisation/create_schedule`
- **Method:** `POST`
- **Description:** Creates a new amortisation schedule based on the provided request data.
- **Request Body:** JSON object representing schedule request.
  - Request Body Example
  ```json
    {
      "name": "Sample Schedule",
      "assetValue": 20000,
      "deposit": 5000,
      "interestRate": 0.075,
      "numberPayments": 12,
      "balloonPayment": 0,
      "info": "Additional information"
     }
  ```
  
- **Response:** Returns the created amortisation schedule information.
  - Response example
  ```json
    {"id":1,"name":"Sample Schedule","assetValue":20000.00,"depositAmount":5000.00,"balloonPayment":0.00,"info":"Additional information here","periodPayment":1301.36,"totalInterest":616.35,"totalPayment":15616.35}
  ```

### Get All Amortisation Schedules
- **URL:** `/amortisation/get_schedules`
- **Method:** `GET`
- **Description:** Retrieves all existing amortisation schedules from the database.
- **Response:** Returns a list of all amortisation schedules.
  - Example response
  ```json
    [{"id":1,"name":"Sample Schedule","assetValue":20000.00,"depositAmount":5000.00,"balloonPayment":0.00,"info":"Additional information here","periodPayment":1301.36,"totalInterest":616.35,"totalPayment":15616.35},{"id":2,"name":"Sample Schedule","assetValue":20000.00,"depositAmount":5000.00,"balloonPayment":2000.00,"info":"Additional information here","periodPayment":1140.34,"totalInterest":684.16,"totalPayment":15684.16}]
    ```

### Get Amortisation Schedule Information
- **URL:** `/amortisation/get_schedule_info`
- **Method:** `GET`
- **Description:** Retrieves information about a specific amortisation schedule by its ID.
- **Query Parameters:**
    - `id` (required) - The ID of the amortisation schedule to retrieve.
- **Response:** Returns information about the requested amortisation schedule, including installments, otherwise returns a 404 error.
    -Example Response
    ```json
      {
        "schedule":{"id":1,"name":"Sample Schedule","assetValue":20000.00,"depositAmount":5000.00,"balloonPayment":0.00,"info":"Additional information here","periodPayment":1301.36,"totalInterest":616.35,"totalPayment":15616.35},
        "installments":[{"id":1,"scheduleId":1,"period":1,"payment":1301.36,"principle":1207.61,"interest":93.75,"balance":13792.39},{"id":2,"scheduleId":1,"period":2,"payment":1301.36,"principle":1215.16,"interest":86.20,"balance":12577.23},{"id":3,"scheduleId":1,"period":3,"payment":1301.36,"principle":1222.75,"interest":78.61,"balance":11354.48},{"id":4,"scheduleId":1,"period":4,"payment":1301.36,"principle":1230.39,"interest":70.97,"balance":10124.09},{"id":5,"scheduleId":1,"period":5,"payment":1301.36,"principle":1238.08,"interest":63.28,"balance":8886.01},{"id":6,"scheduleId":1,"period":6,"payment":1301.36,"principle":1245.82,"interest":55.54,"balance":7640.19},{"id":7,"scheduleId":1,"period":7,"payment":1301.36,"principle":1253.61,"interest":47.75,"balance":6386.58},{"id":8,"scheduleId":1,"period":8,"payment":1301.36,"principle":1261.44,"interest":39.92,"balance":5125.14},{"id":9,"scheduleId":1,"period":9,"payment":1301.36,"principle":1269.33,"interest":32.03,"balance":3855.81},{"id":10,"scheduleId":1,"period":10,"payment":1301.36,"principle":1277.26,"interest":24.10,"balance":2578.55},{"id":11,"scheduleId":1,"period":11,"payment":1301.36,"principle":1285.24,"interest":16.12,"balance":1293.31},{"id":12,"scheduleId":1,"period":12,"payment":1301.39,"principle":1293.31,"interest":8.08,"balance":0.00}]
      }
    ```
  
### Example requests

```
curl -X POST \
    http://localhost:8080/amortisation/create_schedule \
-H 'Content-Type: application/json' \
-d
    '{
    "name": "Sample Schedule",
    "assetValue": 20000,
    "deposit": 5000,
    "interestRate": 0.075,
    "numberPayments": 12,
    "balloonPayment": 0,
    "info": "Additional information"
    }'
```

```
curl -X GET http://localhost:8080/amortisation/get_schedules
```

```
curl -X GET http://localhost:8080/amortisation/get_schedule_info?id=1
```