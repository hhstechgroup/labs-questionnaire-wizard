/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import java.util.List;

/**
 *
 * @author artem.pylypenko
 */
public class WizardDocument {

    private List<WizardForm> formList;

    public List<WizardForm> getFormList() {
        return formList;
    }

    public void setFormList(List<WizardForm> formList) {
        this.formList = formList;
    }

}
