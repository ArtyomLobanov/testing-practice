package ru.spbau.mit.lobanov.youtrack.elements;

import org.openqa.selenium.WebElement;

public class WebButton {
    private final WebElement element;

    public WebButton(WebElement element) {
        this.element = element;
    }

    public void click() {
        element.click();
    }
}
