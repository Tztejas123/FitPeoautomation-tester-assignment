package com.fitpeo.pages;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.fitpeo.utility.Logger;

public class HomePage {
    private RemoteWebDriver driver;

    public HomePage(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHomePage() {
        driver.get("https://www.fitpeo.com/home");
        Logger.log("Navigated to FitPeo Homepage.");
    }
}
