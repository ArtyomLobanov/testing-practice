package ru.spbau.mit.lobanov.youtrack.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.spbau.mit.lobanov.youtrack.elements.WebButton;
import ru.spbau.mit.lobanov.youtrack.elements.WebTextField;

import java.util.ArrayList;
import java.util.List;

import static ru.spbau.mit.lobanov.youtrack.Utils.findElementForced;

public class UsersPage {
    private final WebDriver driver;
    private final String host;
    private WebButton createUserButton;

    private UsersPage(WebDriver driver, String host) {
        this.driver = driver;
        this.host = host;
        refresh();
    }

    public CreateUserDialog openCreateUserDialog() {
        createUserButton.click();
        final WebTextField login = new WebTextField(findElementForced(driver, "id_l.U.cr.login"));
        final WebTextField password = new WebTextField(findElementForced(driver, "id_l.U.cr.password"));
        final WebTextField confirmPassword = new WebTextField(findElementForced(driver, "id_l.U.cr.confirmPassword"));
        final WebButton create = new WebButton(findElementForced(driver, "id_l.U.cr.createUserOk"));
        final WebButton cancel = new WebButton(findElementForced(driver, "id_l.U.cr.createUserCancel"));
        return new CreateUserDialog(create, cancel, login, password, confirmPassword);
    }

    public List<String> getUsers() {
        final WebElement table = findElementForced(driver, "id_l.U.usersList.usersList");
        final WebElement tableBody = table.findElement(By.tagName("tbody"));
        final List<String> users = new ArrayList<>();
        for (WebElement panel : tableBody.findElements(By.tagName("tr"))) {
            final WebElement labelElement = panel.findElement(By.xpath(".//*[@cn='l.U.usersList.UserLogin.editUser']"));
            final String login = labelElement.getAttribute("title");
            users.add(login);
        }
        return users;
    }

    public boolean deleteUserForName(String name) {
        final WebElement table = findElementForced(driver, "id_l.U.usersList.usersList");
        final WebElement tableBody = table.findElement(By.tagName("tbody"));
        for (WebElement panel : tableBody.findElements(By.tagName("tr"))) {
            final WebElement labelElement = panel.findElement(By.xpath(".//*[@cn='l.U.usersList.UserLogin.editUser']"));
            final String login = labelElement.getAttribute("title");
            if (login.equals(name)) {
                   final List<WebElement> buttons = panel.findElements(By.xpath(".//*[@cn='l.U.usersList.deleteUser']"));
                   if (buttons.size() == 1) {
                       buttons.get(0).click();
                       driver.switchTo().alert().accept();
                       return true;
                   }
                   return false;
            }
        }
        return false;
    }

    public void refresh() {
        driver.get(host + "/users");
        createUserButton = new WebButton(findElementForced(driver, "id_l.U.createNewUser"));
    }

    public static UsersPage open(WebDriver driver, String host) {
        return new UsersPage(driver, host);
    }

    public class CreateUserDialog {
        private final WebButton createButton;
        private final WebButton cancelButton;
        private final WebTextField loginTextField;
        private final WebTextField passwordTextField;
        private final WebTextField passwordConfirmTextField;

        private CreateUserDialog(WebButton createButton, WebButton cancelButton, WebTextField loginTextField,
                                 WebTextField passwordTextField, WebTextField passwordConfirmTextField) {
            this.createButton = createButton;
            this.cancelButton = cancelButton;
            this.loginTextField = loginTextField;
            this.passwordTextField = passwordTextField;
            this.passwordConfirmTextField = passwordConfirmTextField;
        }

        public void setPassword(String password) {
            passwordTextField.setText(password);
        }

        public void setPasswordConfirm(String passwordConfirm) {
            passwordConfirmTextField.setText(passwordConfirm);
        }

        public void setLogin(String login) {
            loginTextField.setText(login);
        }

        public void createUser() {
            createButton.click();
        }

        public void cancel() {
            cancelButton.click();
        }
    }

    public class WebUserPanel {
        private final String login;
        private final WebButton deleteUser;

        private WebUserPanel(String login, WebButton deleteUser) {
            this.login = login;
            this.deleteUser = deleteUser;
        }

        public String getLogin() {
            return login;
        }

        public void delete() {
            if (deleteUser == null) {
                throw new RuntimeException("Unable to delete user: " + login);
            }
            deleteUser.click();
            driver.switchTo().alert().accept();
        }
    }
}
