package com.fitpeo.base;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import com.fitpeo.utility.Logger;

public class BaseTest {
    protected RemoteWebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        Logger.log("Test setup complete. Browser launched.");
    }
    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
            Logger.log("Test completed. Browser closed.");
        }
    }
}
