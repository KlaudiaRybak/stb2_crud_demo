package pl.akademiaqa.employees;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

class ReadEmployeesTest {

    // metodę testową należy oznaczyć zawsze jako @Test - informacja dla JUnit
    // pochodzi z pakietu org.junit.jupiter.api.Test - teraz JUnit wie, że ta metoda to metoda testowa
    // z klasy i metody usuwamy public - są to testy, będą tylko w tym konkretnym pakiecie, nie muszą być publiczne
    // JUnit wykonuje testy losowo, nie po kolei, testy powinny być niezależne od siebie
    // asercje pisane są przy pomocy JUnit z biblioteki junit.jupiter.api.Assertions

    private static final String BASE_URL = "http://localhost:3000/employees";

    @Test
    void readAllEmployeesTest() {

        final Response response = given() // import metody given (alt Enter - import) z biblioteki rest assured - io.restassured.RestAssured.given
                .when()
                .get(BASE_URL);// jako string podawany endpoint

        // dla .get utworzyć zmienną, którą otrzymamy po wysłaniu requestu
        // alt Enter > Introduce local variable - zwraca response, który przpisujemy do zmiennej Response (tak jak w postmanie)

        // wyświetlanie odpowiedzi na konsoli:
        // System.out.println(response.asString()); // - wyświetlenie całego body response na konsoli
        // response.prettyPrint(); // - wyświetlenie całego body response na konsoli
        response.prettyPeek(); // - wyświetlenie całego response na konsoli, czyli: body + headersy + status code


        // sprawdzenie statusu odpowiedzi
        Assertions.assertEquals(200, response.getStatusCode());

        // sprawdzenie czy odpowiedź nie jest pusta
        JsonPath json = response.jsonPath(); // aby dostać się do pól odpowiedzi należy response zamienić na JsonPath
        List<String> firstNames = json.getList("firstName"); // wyciągamy listę i przypisujemy do zmiennej typu List
        Assertions.assertTrue(firstNames.size() > 0); // System.out.println(firstNames);

    }

    // ----

    @Test
    void readOneEmployeeTest() {

        final Response response = given()
                .when()
                .get(BASE_URL +"/1"); // rozwiązanie nie jest rekomendowane przez Rest Assured (patrz: @Test readOneEmployeeWithPathVariableTest)

        JsonPath json = response.jsonPath(); // aby dostać się do pól odpowiedzi należy response zamienić na JsonPath
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("Bartek", json.getString("firstName"));
        Assertions.assertEquals("Czarny", json.getString("lastName"));
        Assertions.assertEquals("bczarny", json.getString("username"));
        Assertions.assertEquals("bczarny@testerprogramuje.pl", json.getString("email"));

        // wyciągnięcie jednej konkretnej wartości z odpowiedzi - json.getString()
        // jeżeli chcemy wyciągnąć listę wartości z odpowiedzi - json.getList() i przypisanie do zmiennej klasowej typu List

    }

    // ----

    @Test
    void readOneEmployeeWithPathVariableTest() {

        // zmienna path variable w Rest Assured to path param

        final Response response = given()
                .pathParams("id", 1) // przekazanie zmiennej path variable
                .when()
                .get(BASE_URL + "/{id}");

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Bartek", json.getString("firstName"));
        Assertions.assertEquals("Czarny", json.getString("lastName"));
        Assertions.assertEquals("bczarny", json.getString("username"));
        Assertions.assertEquals("bczarny@testerprogramuje.pl", json.getString("email"));
    }

    @Test
    void readOneEmployeeWithPathVariableV2Test() {

        // inna metoda - given when then + matchersy z Hamcrest

        given()
                .pathParams("id", 1)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(200)
                .body("firstName", Matchers.equalTo("Bartek")) // używanie matchersów z biblioteki Hamcrest
                .body("lastName", Matchers.equalTo("Czarny"))
                .body("username", Matchers.equalTo("bczarny"))
                .body("email", Matchers.equalTo("bczarny@testerprogramuje.pl"));
    }

    @Test
    void readOneEmployeeWithPathVariableV3Test() {

        // inna metoda - given when then

        final Response response = given()
                .pathParams("id", 1) // przekazanie zmiennej path variable
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .extract()
                .response();

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Bartek", json.getString("firstName"));
        Assertions.assertEquals("Czarny", json.getString("lastName"));
        Assertions.assertEquals("bczarny", json.getString("username"));
        Assertions.assertEquals("bczarny@testerprogramuje.pl", json.getString("email"));
    }

    // ----

    @Test
    void readEmployeesWithQueryParamsTest() {

        final Response response = given()
                .queryParam("firstName", "Bartek")
                .when()
                .get(BASE_URL);

        Assertions.assertEquals(200, response.getStatusCode());

        JsonPath json = response.jsonPath();
        Assertions.assertEquals("Bartek", json.getList("firstName").get(0));
        Assertions.assertEquals("Czarny", json.getList("lastName").get(0));
        Assertions.assertEquals("bczarny", json.getList("username").get(0));
        Assertions.assertEquals("bczarny@testerprogramuje.pl", json.getList("email").get(0));
    }
}
