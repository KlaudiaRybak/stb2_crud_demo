package pl.akademiaqa.employees;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

class CreateNewEmployeeTest {

    private static final String BASE_URL = "http://localhost:3000/employees";

    @Test
    void createNewEmployeeTest() {

        // od javy 15 json jest bardziej czytelny
        // wersja z response body jako String

        String body = "    {\n" +
                "      \"firstName\": \"Jeremiasz\",\n" +
                "      \"lastName\": \"Pampaliński\",\n" +
                "      \"username\": \"jpampalinski\",\n" +
                "      \"email\": \"jpampalinski@testerprogramuje.pl\",\n" +
                "      \"phone\": \"731-111-111\",\n" +
                "      \"website\": \"testerprogramuje.pl\",\n" +
                "      \"role\": \"qa\",\n" +
                "      \"type\": \"b2b\",\n" +
                "      \"address\": {\n" +
                "        \"street\": \"Ul. Sezamkowa\",\n" +
                "        \"suite\": \"8\",\n" +
                "        \"city\": \"Katowice\",\n" +
                "        \"zipcode\": \"12-345\"\n" +
                "      },\n" +
                "      \"company\": {\n" +
                "        \"companyName\": \"Akademia QA\",\n" +
                "        \"taxNumber\": \"531-1593-430\",\n" +
                "        \"companyPhone\": \"731-111-111\"\n" +
                "      }\n" +
                "    }";

        final Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(BASE_URL)
                .then()
                .extract()
                .response();

        Assertions.assertEquals(201, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Jeremiasz", json.getString("firstName"));
        Assertions.assertEquals("Pampaliński", json.getString("lastName"));
        Assertions.assertEquals("jpampalinski", json.getString("username"));
        Assertions.assertEquals("jpampalinski@testerprogramuje.pl", json.getString("email"));
    }

    @Test
    void createNewEmployeeV2Test() {

        // wersja z response body jako obiekt
        // konieczne dodanie dependency dla JSON jn Java

        //given - przygotowanie danych
        JSONObject address = new JSONObject();
        address.put("street", "Ul. Sezamkowa");
        address.put("suite", "8");
        address.put("city", "Katowice");
        address.put("zipcode", "12-3456");

        JSONObject company = new JSONObject();
        company.put("companyName", "Akademia QA");
        company.put("taxNumber", "531-1593-430");
        company.put("companyPhone", "731-111-11");

        JSONObject employee = new JSONObject();
        employee.put("firstName", "Jeremiasz");
        employee.put("lastName", "Pampaliński");
        employee.put("username", "jpampalinski");
        employee.put("email", "jpampalinski@testerprogramuje.pl");
        employee.put("phone", "731-111-111");
        employee.put("website", "testerprogramuje.pl");
        employee.put("role", "qa");
        employee.put("type", "b2b");
        employee.put("address", address);
        employee.put("company", company);

        //when - wysłanie requestu
        final Response response = given()
                .contentType(ContentType.JSON)
                .body(employee.toString())              // obiekt zamieniony na String
                .when()
                .post(BASE_URL)
                .then()
                .extract()
                .response();

        // then - sprawdzenie odpowiedzi
        Assertions.assertEquals(201, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Jeremiasz", json.getString("firstName"));
        Assertions.assertEquals("Pampaliński", json.getString("lastName"));
        Assertions.assertEquals("jpampalinski", json.getString("username"));
        Assertions.assertEquals("jpampalinski@testerprogramuje.pl", json.getString("email"));
    }
}
