package com.fitpeo.utility;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ScreenshotUtil {

    private RemoteWebDriver driver;

    
    public ScreenshotUtil(RemoteWebDriver driver) {
        this.driver = driver;
    }

    /**
     * Captures a screenshot and saves it to the specified folder with a timestamped filename.
     */
    public void captureScreenshot(String folderPath, String screenshotName) throws IOException {
        // Take the screenshot
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        // Create a timestamp for the filename
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // Create the destination file
        File destination = new File(folderPath + File.separator + screenshotName + "_" + timestamp + ".png");

        // Copy the screenshot file to the destination
        FileUtils.copyFile(screenshot, destination);

        System.out.println("Screenshot saved at: " + destination.getAbsolutePath());
    }
}

