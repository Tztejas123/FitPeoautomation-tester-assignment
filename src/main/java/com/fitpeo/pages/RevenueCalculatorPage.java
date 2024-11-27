package com.fitpeo.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.fitpeo.utility.Logger;
import com.fitpeo.utility.WaitHelper;

public class RevenueCalculatorPage {
    private RemoteWebDriver driver;
    private WaitHelper waitHelper;

    private By sliderLocator = By.xpath("//input[@type='range']");
    private By textFieldLocator = By.xpath("//div[@class='MuiBox-root css-j7qwjs']//input");
    private By checkboxesLocator = By.xpath("//input[@type='checkbox']");
    private By reimbursementLocator = By.cssSelector("p:nth-child(4) p:nth-child(1)");
    private By updatedTextFieldLocator = By.xpath("//input[@type='number']");

    public RevenueCalculatorPage(RemoteWebDriver driver) {
        this.driver = driver;
        this.waitHelper = new WaitHelper(driver);
    }

    public void navigateToRevenueCalculator() {
        ((JavascriptExecutor) driver).executeScript("window.open('https://www.fitpeo.com/revenue-calculator', '_blank');");
        Logger.log("Navigated to Revenue Calculator in a new tab.");
    }

    public void interactWithSlider(int offset) {
        WaitHelper waitHelper = new WaitHelper(driver);
        WebElement slider = waitHelper.waitForVisibility(sliderLocator, 20); // Extend timeout
        if (slider == null) {
            Logger.logError("Slider element not found.");
            return;
        }
        new Actions(driver).clickAndHold(slider).moveByOffset(offset, 0).release().perform();
        Logger.log("Slider moved by offset: " + offset);
    }


    public void updateTextField(String value) {
        WebElement textField = waitHelper.waitForVisibility(textFieldLocator, 10);
        textField.clear();
        textField.sendKeys(value);
        Logger.log("Updated text field to: " + value);
    }

    public void selectCheckboxes(String[] labels) {
        List<WebElement> checkboxes = driver.findElements(checkboxesLocator);
        for (WebElement checkbox : checkboxes) {
            WebElement labelElement = checkbox.findElement(By.xpath("./ancestor::label/preceding-sibling::p"));
            String label = labelElement.getText();
            for (String target : labels) {
                if (label.equals(target) && !checkbox.isSelected()) {
                    checkbox.click();
                    Logger.log("Selected checkbox: " + target);
                }
            }
        }
    }

    public void validateReimbursementValue(String expectedValue) {
        String actualValue = waitHelper.waitForVisibility(reimbursementLocator, 10).getText();
        if (!expectedValue.equals(actualValue)) {
            Logger.logError("Reimbursement validation failed. Expected: " + expectedValue + ", Actual: " + actualValue);
        } else {
            Logger.log("Reimbursement value validated successfully: " + expectedValue);
        }
    }
}
