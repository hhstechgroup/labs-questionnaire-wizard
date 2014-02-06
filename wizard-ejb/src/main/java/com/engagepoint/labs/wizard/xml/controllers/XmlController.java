/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.controllers;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardDataModelGenerator;
import com.engagepoint.labs.wizard.questions.RangeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
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

    public XmlController() {
        this.parser = new XmlCustomParser();
    }

    public String readXML(String XMLpath) throws SAXException, JAXBException {
        WizardDataModelGenerator generator = new WizardDataModelGenerator();
        QuestionnaireForms questionnaireForms = parser.parseXML(XMLpath);
        WizardForm wizardDataModel = generator.getFirstWizardForm(questionnaireForms);

        List<WizardQuestion> wizarsdQuestionsList = wizardDataModel.getPageList().get(0).getTopicList().get(0).getWizardQuestionList();

        if (wizarsdQuestionsList.get(0) == null) {
            return "Q IS NULL !!!!!!!!!";
        }
        QType questionType = wizarsdQuestionsList.get(0).getQuestionType();
        if (questionType != null && questionType.equals(QType.RANGE)) {
            RangeQuestion q = (RangeQuestion) wizardDataModel.getPageList().get(0).getTopicList().get(0).getWizardQuestionList().get(0);
            return "Form Name " + wizardDataModel.getFormName() + "Range start" + q.getStartRange();
        }
        return "Not A  RANGE Q";
    }
}
