/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.controllers;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.questions.RangeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import super_binding.QType;
import super_binding.QuestionnaireForm;
import super_binding.QuestionnaireForms;

/**
 *
 * @author artem
 */
@Named
@SessionScoped
public class XmlController implements Serializable {

    private final XmlCustomParser parser;
    private WizardDocument wizardDocument;

    public XmlController() {
        this.parser = new XmlCustomParser();
    }

    public WizardDocument readXML(List<String> XMLpathList) throws SAXException, JAXBException {
        WizardDataModelGenerator generator = new WizardDataModelGenerator();
        List<QuestionnaireForms> formsList = new ArrayList<>();
        for (String xmlPath : XMLpathList) {
            formsList.add(parser.parseXML(xmlPath));
        }
        wizardDocument = generator.getWizardDocument(formsList);
        return wizardDocument;
    }

    public void getSelectedTemplate(String formID, WizardDocument wd, WizardForm wizardForm) {
        wizardDocument = wd;
        if (wizardForm != null && wizardDocument != null) {
            List<WizardForm> formList = wizardDocument.getFormList();
            if (formList == null || formList.isEmpty()) {
                return;
            }
            for (WizardForm f : formList) {
                if (f.getId().equals(formID)) {
                    System.out.println("INSIDE");
                    System.out.println("size " + formList.size());
                    System.out.println("ID = " + f.getId());
                    wizardForm.setFormName(f.getFormName());
                    wizardForm.setId(f.getId());
                    wizardForm.setPageList(f.getPageList());
                }
            }
        }
    }
}
