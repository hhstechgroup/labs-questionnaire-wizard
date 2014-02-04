package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import org.xml.sax.SAXException;
import super_binding.QuestionnaireForms;

import javax.xml.bind.JAXBException;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/4/14
 * Time: 2:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class WizardFactory {
    private XmlCustomParser parser;
    private String XMLPath;
    private WizardDataModel model;

    public WizardDataModel getWizardDataModel(String XMLPath) {
        try {
            QuestionnaireForms forms = parser.parseXML(XMLPath);
            fillModel(forms);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return model;
    }

    private void fillModel(QuestionnaireForms forms) {


    }
}
