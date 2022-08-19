package ru.netology.testmode.data;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import lombok.val;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    // спецификация для переиспользования настроек в разных запросах
    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();
    private static final Faker faker = new Faker(new Locale("en"));

    private DataGenerator() {
    }

//      Требования
//      Для создания клиента нужно делать запрос вида:
//      POST /api/system/users
//      Content-Type: application/json
//      {
//              "login": "vasya",
//              "password": "password",
//              "status": "active"
//      }
//      Возможные значения поля статус:* "active"-пользователь активен,* "blocked"-пользователь заблокирован
//      В случае успешного создания пользователя возвращается код 200
//      При повторной передаче пользователя с таким же логином будет выполнена перезапись данных пользователя

    private static void sendRequest(RegistrationDto user) {

        Gson gson = new Gson();

        given()
            .spec(requestSpec)
            .body(gson.toJson(user))
            .when()
            .post("/api/system/users")
            .then()
            .statusCode(200);
    }


    public static String getRandomLogin() {
        String login = faker.name().firstName();
        return login;
    }

    public static String getRandomPassword() {
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {
        }

        public static RegistrationDto getUser(String status) {
            RegistrationDto user = new RegistrationDto(getRandomLogin(), getRandomPassword(), status);
            return user;
        }

        public static RegistrationDto getRegisteredUser(String status) {
            RegistrationDto registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }
    }

    @Value
    public static class RegistrationDto {
        String login;
        String password;
        String status;
    }
}