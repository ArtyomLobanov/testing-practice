package ru.spbau.mit.lobanov.youtrack;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

public class Utils {
    public static WebElement findElementForced(WebDriver driver, String id) {
        final FluentWait<WebDriver> wait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(10))
                .pollingEvery(Duration.ofMillis(100))
                .ignoring(NoSuchElementException.class);
        return wait.until(x -> driver.findElement(By.id(id)));
    }

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
