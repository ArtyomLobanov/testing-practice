package ru.spbau.mit.lobanov.youtrack;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import ru.spbau.mit.lobanov.youtrack.pages.LoginPage;
import ru.spbau.mit.lobanov.youtrack.pages.UsersPage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class YoutrackTests {

    private static final String host = "http://localhost:8080";

    @Test
    public void simpleUserCreationTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "user");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        assertTrue(page.getUsers().contains(login));
        page.deleteUserForName(login);
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void manyUserCreationTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final List<String> logins = new ArrayList<>();
        final Set<String> users = new HashSet<>(page.getUsers());
        for (int i = 0; i < 5; i++) {
            final String login = generateUserName(users, "user");
            final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
            dialog.setLogin(login);
            dialog.setPassword("password");
            dialog.setPasswordConfirm("password");
            dialog.createUser();
            Utils.sleep(200);
            page.refresh();
            logins.add(login);
            users.add(login);
        }
        assertTrue(page.getUsers().containsAll(logins));
        logins.forEach(login -> {
            page.deleteUserForName(login);
            Utils.sleep(200);
            page.refresh();
            assertFalse(page.getUsers().contains(login));
        });
        driver.quit();
    }

    @Test
    public void underscoreInNameTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "_user");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        assertTrue(page.getUsers().contains(login));
        page.deleteUserForName(login);
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void cancellationTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "_user");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.cancel();
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void mismatchedPasswordsTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "user");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password_2");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void emptyNameTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = "";
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void onlyDigitsInNameTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "0123");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        assertTrue(page.getUsers().contains(login));
        page.deleteUserForName(login);
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void spaceInNameTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "us er");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }

    @Test
    public void duplicateLoginTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()), "user");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        final List<String> snapshot1 = page.getUsers();
        assertTrue(snapshot1.contains(login));

        final UsersPage.CreateUserDialog anotherDialog = page.openCreateUserDialog();
        anotherDialog.setLogin(login);
        anotherDialog.setPassword("password");
        anotherDialog.setPasswordConfirm("password");
        anotherDialog.createUser();
        Utils.sleep(200);
        page.refresh();
        final List<String> snapshot2 = page.getUsers();
        assertTrue(snapshot2.contains(login));
        assertEquals(snapshot1.size(), snapshot2.size());

        page.deleteUserForName(login);
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));

        driver.quit();
    }

    /*
     * “ут довольно неожиданна€ ситауци€ происходит.
     * ѕохоже слишком длинный логин обрезаетс€, но об этом никак не сообщаетс€.
     */
    /*
    @Test
    public void longNameTest() throws Exception {
        final WebDriver driver = new FirefoxDriver();
        LoginPage.open(driver, host).login("root", "root");
        final UsersPage page = UsersPage.open(driver, host);
        final String login = generateUserName(new HashSet<>(page.getUsers()),
                "rtfhujiokrdtfghujirdtfghujiotrfghujioxrdctfghujiodrtfyguhjio");
        final UsersPage.CreateUserDialog dialog = page.openCreateUserDialog();
        dialog.setLogin(login);
        dialog.setPassword("password");
        dialog.setPasswordConfirm("password");
        dialog.createUser();
        Utils.sleep(200);
        page.refresh();
        System.out.println(page.getUsers());
        assertTrue(page.getUsers().contains(login));
        page.deleteUserForName(login);
        Utils.sleep(200);
        page.refresh();
        assertFalse(page.getUsers().contains(login));
        driver.quit();
    }
    */

    private String generateUserName(Set<String> users, String prefix) {
        for (int i = 0; ; i++) {
            final String login = prefix + i;
            if (!users.contains(login)) {
                return login;
            }
        }
    }
}