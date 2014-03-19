package com.engagepoint.labs.wizard.acceptancetest;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.getTextAndSuppressNextLineChar;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import net.thucydides.core.pages.Pages;

import net.thucydides.core.pages.WebElementFacade;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import com.engagepoint.acceptancetest.base.steps.JbehaveBaseSteps;

public class SimpleJbehaveSteps extends JbehaveBaseSteps {

    private UIBootstrapBasePage uIBootstrapBasePage;

    public SimpleJbehaveSteps(Pages pages) {
        super(pages);
        uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
    }

    @When("the user choose template with name '$name'")
    public void chooseTemplate(String name) {
        selectListBoxValue(name, "formid-default_xml");
        clickBySelector("formid-butt");
    }

    @When("user choose Page with text '$innerText'")
    public void choosePage(String innerText) {
        clickLinkByText(innerText);
    }

    @When("user choose Topic with text '$innerText'")
    public void chooseTopicByInnerText(String innerText) {
        clickLinkByText(innerText);
    }

    @When("choose drop-down with id '$id' and set value '$value'")
    public void dragDownNameChecker(String id, String value) {
        clickBySelector(id);
        WebElement webElement = uIBootstrapBasePage.getDriver().findElement(By.xpath("//*[@id=\"maincontentid-oio8en9\"]"));
        Select clickThis = new Select(webElement);
        clickThis.selectByValue(value);
    }

    @When("user click 'Next' button")
    public void clickNextButton() {
        clickOnButtonText("NEXT");
    }

    @Then("element id '$id' has text '$textContent'")
    public void checkElementInnerText(String id, String textContent) {
        assertThat(getTextAndSuppressNextLineChar(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id))), is(equalTo(textContent)));
    }

    @Then("check that current URL is '$url'")
    public void checkURL(String url) {
        assertThat(pages().getConfiguration().getBaseUrl(), is(equalTo(url)));
    }

    @Then("checkbox with id '$id' is checked")
    public void checkboxIsChecked(String id) {
        WebElement checkBox = uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id));
        Assert.assertTrue(checkBox.isSelected());
    }

    @Then("element with id '$id' is visible")
    public void elementIsVisible(String id) {
        WebElementFacade element = uIBootstrapBasePage.element(By.id(id));
        Assert.assertTrue(element.isCurrentlyVisible());
    }

    @Then("element with id '$id' is inVisible")
    public void elementIsInVisible(String id) {
        WebElementFacade element = uIBootstrapBasePage.element(By.id(id));
        Assert.assertFalse(element.isCurrentlyVisible());
    }
}