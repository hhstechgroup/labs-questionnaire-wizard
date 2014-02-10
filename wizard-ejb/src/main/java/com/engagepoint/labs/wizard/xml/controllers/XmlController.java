/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.controllers;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
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

    public WizardDocument readAllDeafultXmlFiles(List<String> XMLpathList) throws SAXException, JAXBException {
        WizardDataModelGenerator generator = new WizardDataModelGenerator();
        List<QuestionnaireForms> formsList = new ArrayList<>();
        for (String xmlPath : XMLpathList) {
            formsList.add(parser.parseXML(xmlPath));
        }
        wizardDocument = generator.getWizardDocument(formsList);
        return wizardDocument;
    }
}
