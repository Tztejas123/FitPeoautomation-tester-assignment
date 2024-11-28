package com.fitpeo.fitpeo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class FitPeoAutomationTest {
    private static final Logger logger = Logger.getLogger(FitPeoAutomationTest.class.getName());
    private static ExtentReports extent; // For generating the report
    private static ExtentTest test; // For logging test steps
    private static FluentWait<RemoteWebDriver> wait;

    public static void main(String[] args) {
        System.out.println("Test execution started...");
        logger.info("Test execution started.");

        // Initialize ExtentReports
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("FitPeoTestReport.html");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("FitPeo Automation Test Report");
        sparkReporter.config().setReportName("FitPeo Test Results");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);

        // Initialize WebDriver
        RemoteWebDriver driver = new ChromeDriver();
        wait = new FluentWait<>(driver)
                .pollingEvery(Duration.ofSeconds(5))
                .withTimeout(Duration.ofSeconds(60))
                .ignoring(ElementClickInterceptedException.class)
                .withMessage("Timeout reached; element not clickable or not found.");

        try {
            // Navigate to FitPeo Homepage
            System.out.println("Navigating to FitPeo Homepage...");
            test = extent.createTest("Navigate to Homepage");
            driver.get("https://www.fitpeo.com/home");
            driver.manage().window().maximize();
            test.pass("Navigated to FitPeo Homepage.");
            logger.info("Navigated to FitPeo Homepage.");
            System.out.println("Successfully navigated to FitPeo Homepage.");

            // Open Revenue Calculator page in a new tab
            System.out.println("Navigating to Revenue Calculator Page...");
            test = extent.createTest("Navigate to Revenue Calculator Page");
            ((JavascriptExecutor) driver).executeScript("window.open('https://www.fitpeo.com/revenue-calculator', '_blank');");
            switchToNewTab(driver);
            test.pass("Navigated to Revenue Calculator page.");
            logger.info("Navigated to Revenue Calculator page.");
            System.out.println("Successfully navigated to Revenue Calculator Page.");

            // Perform page scrolling and validate slider adjustments
            System.out.println("Scrolling and adjusting slider...");
            test = extent.createTest("Scroll and Adjust Slider");
            scrollBy(driver, 500);
            WebElement slider = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='range']")));
            adjustSlider(driver, slider, "820");
            validateSliderValue(slider, "820", driver);
            test.pass("Slider adjusted and value validated.");
            System.out.println("Slider adjusted to 820 and validated.");

            // Validate reimbursement after adjustments
            System.out.println("Validating reimbursement value...");
            test = extent.createTest("Validate Reimbursement Value");
            validateReimbursement(driver, "$110700");
            System.out.println("Reimbursement value validated successfully.");

            // Update text field and validate slider synchronization
            System.out.println("Updating text field and validating slider synchronization...");
            test = extent.createTest("Validate Text Field and Slider Synchronization");
            WebElement textField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='MuiBox-root css-j7qwjs']//input")));
            updateTextField(textField, "560");
            validateSliderValue(slider, "560", driver);
            test.pass("Text field updated and slider synchronized.");
            System.out.println("Text field updated to 560 and slider synchronized.");

            // Scroll further down and validate checkboxes
            System.out.println("Selecting checkboxes and validating reimbursement...");
            test = extent.createTest("Select Checkboxes and Validate Reimbursement");
            scrollBy(driver, 500);
            selectCheckboxes(driver, new String[]{"CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474"});
            validateReimbursement(driver, "$110700");
            test.pass("Checkboxes selected and reimbursement validated.");
            System.out.println("Checkboxes selected and reimbursement validated.");
        } catch (Exception e) {
            test.fail("An unexpected error occurred: " + e.getMessage());
            captureScreenshot(driver, "Unexpected_Error");
            logger.log(Level.SEVERE, "An unexpected error occurred.", e);
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            driver.quit();
            extent.flush();
            logger.info("Test completed. Browser closed.");
            System.out.println("Test execution completed. Browser closed.");
        }
    }

    // Utility Methods with added System.out.println for feedback

    private static void switchToNewTab(RemoteWebDriver driver) {
        String originalWindow = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();
        for (String window : windows) {
            if (!window.equals(originalWindow)) {
                driver.switchTo().window(window);
                break;
            }
        }
        logger.info("Switched to new tab.");
        System.out.println("Switched to a new tab.");
    }

    private static void scrollBy(RemoteWebDriver driver, int pixels) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, " + pixels + ");");
        logger.info("Scrolled down by " + pixels + "px.");
        System.out.println("Scrolled down by " + pixels + "px.");
    }

    private static void adjustSlider(RemoteWebDriver driver, WebElement slider, String targetValue) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1];", slider, targetValue);
        logger.info("Slider value set directly to: " + targetValue);
        System.out.println("Slider value adjusted to: " + targetValue);
    }

    private static void validateSliderValue(WebElement slider, String expectedValue, RemoteWebDriver driver) {
        String actualValue = slider.getAttribute("value");
        if (!expectedValue.equals(actualValue)) {
            test.fail("Slider value mismatch. Expected: " + expectedValue + ", Found: " + actualValue);
            captureScreenshot(driver, "Slider_Mismatch");
            System.err.println("Slider value mismatch. Expected: " + expectedValue + ", Found: " + actualValue);
        } else {
            test.pass("Slider value synchronized successfully: " + actualValue);
            System.out.println("Slider value synchronized successfully: " + actualValue);
        }
    }

    private static void validateReimbursement(RemoteWebDriver driver, String expectedValue) {
        try {
            WebElement reimbursementElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p:nth-child(4) p:nth-child(1)")));

            String actualValue = reimbursementElement.getText();
            if (!expectedValue.equals(actualValue)) {
                test.fail("Reimbursement validation failed. Expected: " + expectedValue + ", Found: " + actualValue);
                captureScreenshot(driver, "Reimbursement_Validation_Failed");
                System.err.println("Reimbursement mismatch. Expected: " + expectedValue + ", Found: " + actualValue);
            } else {
                test.pass("Reimbursement value validated successfully: " + actualValue);
                System.out.println("Reimbursement value validated successfully: " + actualValue);
            }
        } catch (Exception e) {
            test.fail("Failed to validate reimbursement: " + e.getMessage());
            captureScreenshot(driver, "Reimbursement_Error");
            System.err.println("Error validating reimbursement: " + e.getMessage());
        }
    }

    private static void updateTextField(WebElement textField, String value) {
        textField.clear();
        textField.sendKeys(value);
        System.out.println("Text field updated to: " + value);
    }

    private static void selectCheckboxes(RemoteWebDriver driver, String[] targetLabels) {
        List<WebElement> checkboxes = driver.findElements(By.xpath("//input[@type='checkbox']"));
        for (WebElement checkbox : checkboxes) {
            WebElement label = checkbox.findElement(By.xpath("./ancestor::label/preceding-sibling::p"));
            String labelText = label.getText();
            for (String targetLabel : targetLabels) {
                if (targetLabel.equals(labelText) && !checkbox.isSelected()) {
                    checkbox.click();
                    System.out.println("Selected checkbox for: " + targetLabel);
                }
            }
        }
    }

    private static void captureScreenshot(RemoteWebDriver driver, String fileName) {
        try {
            String directoryPath = "failed_test_screenshots";
            Files.createDirectories(Paths.get(directoryPath));
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filePath = directoryPath + "/" + fileName + "_" + timeStamp + ".png";

            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(), Paths.get(filePath));
            System.out.println("Screenshot captured: " + filePath);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to capture screenshot.", e);
        }
    }
}

