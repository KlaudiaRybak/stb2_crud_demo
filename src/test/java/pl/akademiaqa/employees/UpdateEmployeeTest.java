package pl.akademiaqa.employees;

import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class UpdateEmployeeTest {

    // wykorzystanie java-faker

    private static Faker faker; // prywatne statyczne pole faker, o nazwie faker (musi być statyczne, ponieważ metoda również jest statyczna
    private String randomFirstName;
    private String randomLastName;
    private String randomEmail;

    @BeforeAll // metoda beforeAll uruchomi się przed wszystkimi testami; jeżeli używana jest adnotacja @beforeAll to metoda musi być statyczna
    static void beforeAll() {
        faker = new Faker(); // utworzenie nowego fakera, wywołując konstruktor
    }

    @BeforeEach // metoda beforeEach uruchomi się przed każdym testem; metoda nie musi być statyczna
    void beforeEach () {
        randomEmail = faker.internet().emailAddress();
        randomFirstName = faker.name().firstName();
        randomLastName = faker.name().lastName();
    }


    private static final String BASE_URL = "http://localhost:3000/employees";

    @Test
    void updateEmployeeTest() {

        String body = "    {\n" +
                "      \"id\": \"1\",\n" +
                "      \"firstName\": \"BartekUPDATE\",\n" +
                "      \"lastName\": \"CzarnyUPDATE\",\n" +
                "      \"username\": \"bczarny\",\n" +
                "      \"email\": \"bczarny@testerprogramuje.pl\",\n" +
                "      \"phone\": \"731-111-111\",\n" +
                "      \"website\": \"testerprogramuje.pl\",\n" +
                "      \"role\": \"qa\",\n" +
                "      \"type\": \"b2b\",\n" +
                "      \"address\": {\n" +
                "        \"street\": \"Ul. Sezamkowa\",\n" +
                "        \"suite\": \"8\",\n" +
                "        \"city\": \"Wrocław\",\n" +
                "        \"zipcode\": \"12-123\"\n" +
                "      },\n" +
                "      \"company\": {\n" +
                "        \"companyName\": \"Akademia QA\",\n" +
                "        \"taxNumber\": \"531-1593-430\",\n" +
                "        \"companyPhone\": \"731-111-111\"\n" +
                "      }\n" +
                "    }";

        final Response response = given()
                .contentType(ContentType.JSON)
                .pathParams("id", 1)
                .body(body)
                .when()
                .put(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("BartekUPDATE", json.getString("firstName"));
        Assertions.assertEquals("CzarnyUPDATE", json.getString("lastName"));
    }

    @Test
    void updateEmployeeV2Test() {

        // wersja bez stringa, utworzenie obiektu - konieczne dodanie dependency dla JSON jn Java

        JSONObject address = new JSONObject();
        address.put("street", "Ul. Sezamkowa");
        address.put("suite", "8");
        address.put("city", "Wrocław");
        address.put("zipcode", "12-123");

        JSONObject company = new JSONObject();
        company.put("companyName", "Akademia QA");
        company.put("taxNumber", "531-1593-430");
        company.put("companyPhone", "731-111-11");

        JSONObject employee = new JSONObject();
        employee.put("firstName", "BartekUPDATE");
        employee.put("lastName", "CzarnyUPDATE");
        employee.put("username", "bczarny");
        employee.put("email", "bczarny@testerprogramuje.pl");
        employee.put("phone", "731-111-111");
        employee.put("website", "testerprogramuje.pl");
        employee.put("role", "qa");
        employee.put("type", "b2b");
        employee.put("address", address);
        employee.put("company", company);

        final Response response = given()
                .contentType(ContentType.JSON)
                .pathParams("id", 1)
                .body(employee.toString())
                .when()
                .put(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("BartekUPDATE", json.getString("firstName"));
        Assertions.assertEquals("CzarnyUPDATE", json.getString("lastName"));
    }

    // ----

    @Test
    void updateEmployeeWithRandomDataTest() {

        JSONObject address = new JSONObject();
        address.put("street", "Ul. Sezamkowa");
        address.put("suite", "8");
        address.put("city", "Wrocław");
        address.put("zipcode", "12-123");

        JSONObject company = new JSONObject();
        company.put("companyName", "Akademia QA");
        company.put("taxNumber", "531-1593-430");
        company.put("companyPhone", "731-111-11");

        JSONObject employee = new JSONObject();
        employee.put("firstName", randomFirstName);
        employee.put("lastName", randomLastName);
        employee.put("username", "bczarny");
        employee.put("email", randomEmail);
        employee.put("phone", "731-111-111");
        employee.put("website", "testerprogramuje.pl");
        employee.put("role", "qa");
        employee.put("type", "b2b");
        employee.put("address", address);
        employee.put("company", company);

        final Response response = given()
                .contentType(ContentType.JSON)
                .pathParams("id", 1)
                .body(employee.toString())
                .when()
                .put(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals(randomFirstName, json.getString("firstName"));
        Assertions.assertEquals(randomLastName, json.getString("lastName"));
        Assertions.assertEquals(randomEmail, json.getString("email"));
    }

    // ----

    @Test
    void partialUpdateEmployeeTest() {

        String body = "    {\n" +
                "      \"email\": \"bczarny@akademiaqa.pl\"" +
                "      }";

        final Response response = given()
                .contentType(ContentType.JSON)
                .pathParams("id", 1)
                .body(body)
                .when()
                .patch(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("bczarny@akademiaqa.pl", json.getString("email"));
    }

    @Test
    void partialUpdateEmployeeV2Test() {

        JSONObject employee = new JSONObject();
        employee.put("email", "bczarny@akademiaqa.pl");

        final Response response = given()
                .contentType(ContentType.JSON)
                .pathParams("id", 1)
                .body(employee.toString())
                .when()
                .patch(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("bczarny@akademiaqa.pl", json.getString("email"));
    }

    @Test
    void partialUpdateEmployeeWithRandomDataTest() {

        JSONObject employee = new JSONObject();
        employee.put("email", randomEmail);

        final Response response = given()
                .contentType(ContentType.JSON)
                .pathParams("id", 1)
                .body(employee.toString())
                .when()
                .patch(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals(randomEmail, json.getString("email"));
    }
}
