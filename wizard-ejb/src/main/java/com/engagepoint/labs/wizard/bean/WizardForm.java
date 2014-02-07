/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author artem.pylypenko
 */
@Named
@SessionScoped
public class WizardForm implements Serializable {

    private String id;
    private String formName;
    private List<WizardPage> pageList;

    public WizardForm() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public List<WizardPage> getPageList() {
        return pageList;
    }

    public void setPageList(List<WizardPage> pageList) {
        this.pageList = pageList;
    }

}
