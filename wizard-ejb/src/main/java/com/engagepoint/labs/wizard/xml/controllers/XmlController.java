/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.controllers;

import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.bean.WizardDocument;
import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import org.xml.sax.SAXException;
import super_binding.QuestionnaireForms;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author artem
 */
@Named
@SessionScoped
public class XmlController implements Serializable {

    private final XmlCustomParser parser;
    private WizardDocument wizardDocument;


    public List<String> getXmlPathList() {
        return xmlPathList;
    }

    private List<String> xmlPathList;
    private File exportFile;

    public XmlController() {
        xmlPathList = new ArrayList<String>();
        xmlPathList.add("/XMLforWizard.xml");
        xmlPathList.add("/XMLforWizard2.xml");
       xmlPathList.add("/ExemplaryWelcome.xml");
       xmlPathList.add("/dependentQuestions.xml");
       xmlPathList.add("/prevTestTemplate.xml");
       // xmlPathList.add("/exportFile.xml");
        this.parser = new XmlCustomParser();
    }

    public WizardDocument readAllDeafultXmlFiles() throws SAXException, JAXBException {
        WizardDataModelGenerator generator = new WizardDataModelGenerator();
        List<QuestionnaireForms> formsList = new ArrayList<>();
        for (String xmlPath : xmlPathList) {
            formsList.add(parser.parseXML(xmlPath));
        }
        wizardDocument = generator.getWizardDocument(formsList);
        return wizardDocument;
    }

    public File getExportFileFromWizardForm(WizardForm wizardForm){
        exportFile = parser.parseWizardFormToXml(wizardForm);
        return exportFile;
    }
}
