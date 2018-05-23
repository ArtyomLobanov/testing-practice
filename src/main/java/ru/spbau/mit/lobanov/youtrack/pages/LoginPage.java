package ru.spbau.mit.lobanov.youtrack.pages;

import org.openqa.selenium.WebDriver;
import ru.spbau.mit.lobanov.youtrack.elements.WebButton;
import ru.spbau.mit.lobanov.youtrack.elements.WebTextField;

import static ru.spbau.mit.lobanov.youtrack.Utils.findElementForced;

public class LoginPage {
    private final WebButton loginButton;
    private final WebTextField loginTextField;
    private final WebTextField passwordTextField;

    private LoginPage(WebButton loginButton, WebTextField loginTextField, WebTextField passwordTextField) {
        this.loginButton = loginButton;
        this.loginTextField = loginTextField;
        this.passwordTextField = passwordTextField;
    }

    public void login(String login, String password) {
        loginTextField.setText(login);
        passwordTextField.setText(password);
        loginButton.click();

    }

    public static LoginPage open(WebDriver driver, String host) {
        driver.get(host + "/login");
        final WebButton loginButton = new WebButton(findElementForced(driver, "id_l.L.loginButton"));
        final WebTextField loginTextField = new WebTextField(findElementForced(driver, "id_l.L.login"));
        final WebTextField passwordTextField = new WebTextField(findElementForced(driver, "id_l.L.password"));
        return new LoginPage(loginButton, loginTextField, passwordTextField);
    }
}
