package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Config;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        //  попытка входа в личный кабинет с учётными данными зарегистрированного активного
        //  пользователя, для заполнения полей формы registeredUser
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $(".button").click();
        $("h2").shouldBe(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        //  попытка входа в личный кабинет незарегистрированного пользователя,
        //  для заполнения полей формы notRegisteredUser
        $("[data-test-id=login] .input__control").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(notRegisteredUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        //попытка входа заблокированного зарегитсрированного пользователя
        $("[data-test-id=login] .input__control").setValue(blockedUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(blockedUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        //  попытка входа в личный кабинет с неверным логином, для заполнения поля формы "Логин"
        //   переменная wrongLogin, "Пароль"  пользователя registeredUser
        $("[data-test-id=login] .input__control").setValue(wrongLogin);
        $("[data-test-id=password] .input__control").setValue(registeredUser.getPassword());
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        //  попытка входа в личный кабинет с неверным  паролем, для заполнения поля формы "Логин"
        //   registeredUser,  "Пароль" переменная wrongPassword
        $("[data-test-id=login] .input__control").setValue(registeredUser.getLogin());
        $("[data-test-id=password] .input__control").setValue(wrongPassword);
        $(".button").click();
        $("[data-test-id=error-notification] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.exactText("Ошибка! Неверно указан логин или пароль"));
    }
}