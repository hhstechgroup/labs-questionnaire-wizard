package com.engagepoint.labs.wizard.acceptancetest;

import com.engagepoint.acceptancetest.base.pages.UIBootstrapBasePage;
import com.engagepoint.acceptancetest.base.steps.JbehaveBaseSteps;
import net.thucydides.core.pages.Pages;
import org.jbehave.core.annotations.When;

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
    public void sss(String name) {
        selectListBoxValue(name, "j_idt6-default_xml");
        clickBySelector("j_idt6-submit");
    }


}