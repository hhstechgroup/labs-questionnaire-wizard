package com.engagepoint.labs.wizard.acceptancetest;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import com.engagepoint.acceptancetest.base.steps.JbehaveBaseSteps;
import net.thucydides.core.Thucydides;
import net.thucydides.core.pages.Pages;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static com.engagepoint.acceptancetest.base.webelements.utils.WebElementsHelper.getTextAndSuppressNextLineChar;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleJbehaveSteps extends JbehaveBaseSteps {

    private UIBootstrapBasePage uIBootstrapBasePage;

    public SimpleJbehaveSteps(Pages pages) {
        super(pages);
        uIBootstrapBasePage = pages().get(UIBootstrapBasePage.class);
    }

    @When("the user choose template with name '$name'")
    public void chooseTemplate(String name) {
        selectListBoxValue(name, "j_idt21-default_xml");
//        clickBySelector("j_idt6-submit");
        clickBySelector("j_idt21-j_idt25");
    }


    @When("user choose Page with text '$innerText'")
    public void choosePage(String innerText) {
        clickLinkByText(innerText);
    }

    @When("user choose Topic with text '$innerText'")
    public void chooseTopicByInnerText(String innerText) {
        clickLinkByText(innerText);
    }

    @When("user click on logo")
    public void clickOnLogo() {
        clickBySelector("logo");
    }

    @Then("element id '$id' has text '$textContent'")
    public void checkElementInnerText(String id, String textContent) {
        assertThat(getTextAndSuppressNextLineChar(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id))), is(equalTo(textContent)));
    }

    @Then("check that current URL is '$url'")
    public void checkURL(String url) {
        assertThat(pages().getConfiguration().getBaseUrl(), is(equalTo(url)));
    }
}