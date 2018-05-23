package ru.spbau.mit.lobanov.youtrack.elements;

import org.openqa.selenium.WebElement;

public class WebTextField {
    private final WebElement element;

    public WebTextField(WebElement element) {
        this.element = element;
    }

    public void setText(String text) {
        element.clear();
        element.sendKeys(text);
    }
}
