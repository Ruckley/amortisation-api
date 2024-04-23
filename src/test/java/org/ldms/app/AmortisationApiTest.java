package org.ldms.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.ldms.app.db.InstallmentRepo;
import org.ldms.app.db.ScheduleRepo;
import org.ldms.app.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AmortisationApiTest {
    static final MySQLContainer<?> MY_SQL_CONTAINER;

    @LocalServerPort
    private Integer port;
    @Autowired
    private ScheduleRepo scheduleRepo;
    @Autowired
    private InstallmentRepo installmentRepo;

    ObjectMapper objectMapper = new ObjectMapper();

    static {
        MY_SQL_CONTAINER = new MySQLContainer<>("mysql:latest");
        MY_SQL_CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        MY_SQL_CONTAINER.stop();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() {
        scheduleRepo.deleteAll();
        installmentRepo.deleteAll();
    }

    /**
     * Checks schedule and installments are created correctly
     * Table entry IDs are set to 1 for checks so test execution order doesn't effect equality checks
     *
     * @throws IOException
     */
    @Test
    void createScheduleCorrectly() throws IOException {

        TestObjects testObjects = new TestObjects();

        ScheduleRequest testReq = testObjects.getScheduleRequest();

        String requestJson = objectMapper.writeValueAsString(testReq);
        Schedule expectedSchedule = new Schedule(
                1,
                testReq.getName(),
                testReq.getAssetValue(),
                testReq.getDeposit(),
                testReq.getBalloonPayment(),
                testReq.getInfo(),
                new Money("1735.15"),
                new Money("821.79"),
                new Money("20821.79")
        );

        String resultJson = given()
                .contentType(ContentType.JSON)
                .body(requestJson)
                .when()
                .post("/amortisation/create_schedule")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().asString();


        // Check returned Schedule is correct
        Schedule resultSchedule = objectMapper.readValue(resultJson, Schedule.class);
        resultSchedule.setId(expectedSchedule.getId());
        Assertions.assertEquals(expectedSchedule, resultSchedule);

        List<Schedule> scheduleTableContents = scheduleRepo.findAll();
        List<Installment> installmentTableContents = installmentRepo.findAll();

        // Check Schedule created correctly
        Assertions.assertEquals(1, scheduleTableContents.size());
        Schedule resultTableSchedule = scheduleTableContents.get(0);
        // Set expectedSchedule ID to the created schedule ID
        expectedSchedule.setId(resultTableSchedule.getId());
        Assertions.assertEquals(expectedSchedule, resultTableSchedule);

        // Check returned Installments are correct
        // Set expectedInstallments to the created schedule ID
        List<Installment> expectedInstallments = new ArrayList<>(testObjects.getExpectedInstallments());
        for (Installment installment : expectedInstallments) {
            installment.setScheduleId(resultTableSchedule.getId());
        }
        Assertions.assertEquals(expectedInstallments, installmentTableContents);

        System.out.println(testObjects.getExpectedInstallments());

    }


    @Test
    void getSchedulesCorrectly() throws JsonProcessingException {

        Schedule expectedSchedule1 = new Schedule(
                "test",
                new Money("25000.00"),
                new Money("5000.00"),
                new Money("0.00"),
                "Additional information",
                new Money("1735.15"),
                new Money("821.79"),
                new Money("20821.79")
        );
        Schedule expectedSchedule2 = new Schedule(expectedSchedule1);
        expectedSchedule2.setAssetValue(new Money("100000"));
        Schedule expectedSchedule3 = new Schedule(expectedSchedule1);
        expectedSchedule3.setDepositAmount(new Money("2000"));

        List<Schedule> existingSchedules = List.of(expectedSchedule1, expectedSchedule2, expectedSchedule3);
        scheduleRepo.saveAll(existingSchedules);

        String resultJson = given()
                .when()
                .get("/amortisation/get_schedules")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().asString();

        Assertions.assertEquals(objectMapper.writeValueAsString(existingSchedules), resultJson);

    }

    @Test
    void getScheduleInfoCorrectly() throws IOException {

        Schedule expectedSchedule1 = new Schedule(
                "test1",
                new Money("25000.00"),
                new Money("5000.00"),
                new Money("0.00"),
                "Additional information",
                new Money("1700"),
                new Money("800"),
                new Money("21000")
        );

        // insert multiple schedules to check ony relevant one is returned
        Schedule expectedSchedule2 = new Schedule(expectedSchedule1);
        expectedSchedule2.setName("test2");
        expectedSchedule2.setAssetValue(new Money("999999"));
        Schedule expectedSchedule3 = new Schedule(expectedSchedule1);
        expectedSchedule2.setName("test3");
        expectedSchedule3.setAssetValue(new Money("2000"));


        List<Schedule> existingSchedules = List.of(expectedSchedule1, expectedSchedule2, expectedSchedule3);
        scheduleRepo.saveAll(existingSchedules);

        List<Installment> schedule1Installments = List.of(
                new Installment(1, 1, 1, expectedSchedule1.getPeriodPayment(), new Money("1610.15"), new Money("125.00"), new Money("18389.85")),
                new Installment(2, 1, 2, expectedSchedule1.getPeriodPayment(), new Money("1620.21"), new Money("114.94"), new Money("0"))
        );

        List<Installment> schedule2Installments = List.of(
                new Installment(3, 2, 1, expectedSchedule3.getPeriodPayment(), new Money("1610.15"), new Money("125.00"), new Money("88888")),
                new Installment(4, 2, 2, expectedSchedule3.getPeriodPayment(), new Money("1620.21"), new Money("114.94"), new Money("0"))
        );

        List<Installment> otherInstallments = List.of(
                new Installment(5, 3, 1, new Money("5000"), new Money("1630.34"), new Money("104.81"), new Money("15139.30")),
                new Installment(6, 3, 1, new Money("5000"), new Money("1630.34"), new Money("104.81"), new Money("15139.30"))
        );

        List<Installment> existingInstallments = new ArrayList<>(schedule1Installments);
        existingInstallments.addAll(schedule2Installments);
        existingInstallments.addAll(otherInstallments);

        installmentRepo.saveAll(existingInstallments);

        Amortisation expectedAmortisation1 = new Amortisation(expectedSchedule1, schedule1Installments);
        Amortisation expectedAmortisation2 = new Amortisation(expectedSchedule2, schedule2Installments);

        String resultJson1 = given()
                .when()
                .get("/amortisation/get_schedule_info?id=1")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().asString();

        Assertions.assertEquals(objectMapper.writeValueAsString(expectedAmortisation1), resultJson1);

        String resultJson2 = given()
                .when()
                .get("/amortisation/get_schedule_info?id=2")
                .then()
                .assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response().asString();

        Assertions.assertEquals(objectMapper.writeValueAsString(expectedAmortisation2), resultJson2);
    }

    @Test
    void createScheduleRouteHandlesMissingRequiredFields() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"name\"}}")
                .when()
                .post("/amortisation/create_schedule")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    void createScheduleRouteHandlesMalformedJson() {
        given()
                .contentType(ContentType.JSON)
                .body("{\"}\"}")
                .when()
                .post("/amortisation/create_schedule")
                .then()
                .assertThat()
                .statusCode(400);
    }

    private void sendIncorrectBodyRequest(String route, String body, Integer expectedStatus, String expectedBody) {
        String responseBody =
                given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/amortisation/" + route)
                .then()
                .assertThat()
                .statusCode(expectedStatus)
                .extract().response().asString();

        Assertions.assertEquals(expectedBody, responseBody);

    }

    @Test
    void createScheduleRouteHandlesNegAssetValue() throws JsonProcessingException {
        ScheduleRequest s = new ScheduleRequest("name", new Money("-25000"), new Money("5000"), new BigDecimal("0.075"), 12, new Money("0"), "info");
        sendIncorrectBodyRequest("create_schedule", objectMapper.writeValueAsString(s), 400, "Invalid request: AssetValue cannot be negative");
    }

    @Test
    void createScheduleRouteHandlesNegDepositValue() throws JsonProcessingException {
        ScheduleRequest s = new ScheduleRequest("name", new Money("25000"), new Money("-5000"), new BigDecimal("0.075"), 12, new Money("0"), "info");
        sendIncorrectBodyRequest("create_schedule", objectMapper.writeValueAsString(s), 400, "Invalid request: Deposit cannot be negative");
    }
    @Test
    void createScheduleRouteHandlesDepositLargerThanAssetValue() throws JsonProcessingException {
        ScheduleRequest s = new ScheduleRequest("name", new Money("25000"), new Money("105000"), new BigDecimal("0.075"), 12, new Money("0"), "info");
        sendIncorrectBodyRequest("create_schedule", objectMapper.writeValueAsString(s), 400, "Invalid request: deposit cannot be greater than assetValue");
    }

    void createScheduleRouteHandlesNumPaymentsessThan2() throws JsonProcessingException {
        ScheduleRequest s = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), 1, new Money("0"), "info");
        sendIncorrectBodyRequest("create_schedule", objectMapper.writeValueAsString(s), 400, "Amortisations must have at least 2 installments");
        ScheduleRequest s2 = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), -5, new Money("0"), "info");
        sendIncorrectBodyRequest("create_schedule", objectMapper.writeValueAsString(s2), 400, "Amortisations must have at least 2 installments");
    }

    @Test
    void createScheduleRouteHandlesNegativeBalloonPayment() throws JsonProcessingException {
        ScheduleRequest s = new ScheduleRequest("name", new Money("25000"), new Money("5000"), new BigDecimal("0.075"), 12, new Money("-500"), "info");
        sendIncorrectBodyRequest("create_schedule", objectMapper.writeValueAsString(s), 400, "Invalid request: Balloon Payment cannot be negative");
    }

    private void sendIncorrectGetRequest(String route, Integer expectedStatus, String expectedBody) {
        String responseBody =
                given()
                        .when()
                        .get("/amortisation/" + route)
                        .then()
                        .assertThat()
                        .statusCode(expectedStatus)
                        .extract().response().asString();

        Assertions.assertEquals(expectedBody, responseBody);

    }

    @Test
    void getSchedulesRouteHandlesNoSchedules() {
        sendIncorrectGetRequest("get_schedules", 404, "No schedules in DB");
    }

    @Test
    void getScheduleInfoRouteHandlesIncorrectId() {
        sendIncorrectGetRequest("get_schedule_info?id=22", 404, "Could not find schedule with ID: 22");
    }

    @Test
    void getScheduleInfoRouteHandlesMissingId() {
        given()
                .when()
                .get("/amortisation/get_schedule_info")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    void getScheduleInfoRouteHandlesMalformedId() {

        given()
                .when()
                .get("/amortisation/get_schedule_info?id=asdf")
                .then()
                .assertThat()
                .statusCode(400);
    }



}
