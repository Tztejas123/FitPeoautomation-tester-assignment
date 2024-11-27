package com.fitpeo.fitpeo;

import org.testng.annotations.Test;

import com.fitpeo.base.BaseTest;
import com.fitpeo.pages.HomePage;
import com.fitpeo.pages.RevenueCalculatorPage;

public class RevenueCalculatorTest extends BaseTest {

    @Test(priority = 1)
    public void testRevenueCalculatorFlow() {
        HomePage homePage = new HomePage(driver);
        RevenueCalculatorPage calculatorPage = new RevenueCalculatorPage(driver);

        homePage.navigateToHomePage();
        calculatorPage.navigateToRevenueCalculator();

        calculatorPage.interactWithSlider(82);
        calculatorPage.updateTextField("820");
        calculatorPage.selectCheckboxes(new String[]{"CPT-99091", "CPT-99453", "CPT-99454", "CPT-99474"});
        calculatorPage.validateReimbursementValue("$110700");
    }
}
