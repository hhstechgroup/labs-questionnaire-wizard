package com.engagepoint.labs.wizard.acceptancetest;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import com.engagepoint.acceptancetest.base.steps.JbehaveBaseSteps;
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

    @When("the user close popup with id '$id'")
    public void closeSmartPopup(String id) {
        uIBootstrapBasePage.element("//*[@id='" + id + "']//a[@href='#']").click();
    }

    @When("the user choose template with name '$name'")
    public void chooseTemplate(String name) {
        selectListBoxValue(name, "j_idt21-default_xml");
//        clickBySelector("j_idt6-submit");
        clickBySelector("j_idt21-j_idt25");
    }

    @Then("element id '$id' has text '$textContent'")
    public void checkElementInnerText(String id, String textContent) {
        assertThat(getTextAndSuppressNextLineChar(uIBootstrapBasePage.element(findVisibleElementAndGetSelector(id))), is(equalTo(textContent)));
    }

    public void clickAllPagesAndTopics(){

    }


}