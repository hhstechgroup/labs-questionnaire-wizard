/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.xml.controllers;

import com.engagepoint.labs.wizard.bean.WizardDataModel;
import com.engagepoint.labs.wizard.xml.parser.XmlCustomParser;
import java.io.Serializable;
import java.util.List;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
//import super_binding.CheckboxesOptions;
//import super_binding.ChooseFromListOptions;
import super_binding.Columns;
import super_binding.DependentQuestion;
import super_binding.DependentQuestions;
import super_binding.Grid;
import super_binding.Group;
import super_binding.GroupsOfQuestions;
import super_binding.Options;
//import super_binding.MultipleChoiceOptions;
import super_binding.Page;
import super_binding.Pages;
import super_binding.Question;
import super_binding.QuestionnaireForm;
import super_binding.QuestionnaireForms;
import super_binding.Questions;
import super_binding.Range;
import super_binding.Rows;

/**
 *
 * @author artem
 */
@Named
@SessionScoped
public class XmlContrloller implements Serializable {

    private final XmlCustomParser parser;

    public XmlContrloller() {
        this.parser = new XmlCustomParser();
    }

    public String readXML(String XMLpath) throws SAXException, JAXBException {
        QuestionnaireForms questionnaireForms = parser.parseXML(XMLpath);
        QuestionnaireForm form = questionnaireForms.getQuestionnaireForm().get(0);
        return translateQestioanForm(form);
    }

    private String translateQestioanForm(QuestionnaireForm form) {

        System.out.println("********************");
        System.out.println(getFormInfo(form));

        return getFormInfo(form);
    }

    private String getFormInfo(QuestionnaireForm form) {
        String allInfo = "Form name: " + form.getFormName() + "<br/>";
        allInfo += getPagesInfo(form.getPages());
        return allInfo;
    }

    private String getPagesInfo(Pages pages) {
        String pagesInfo = "";
        List<Page> pageList = pages.getPage();

        for (Page page : pageList) {
            pagesInfo += "     Page Number: " + page.getPageNumber() + "<br/>";
            pagesInfo += getQuestionsGroupInfo(page.getGroupsOfQuestions());
        }

        return pagesInfo;
    }

    private String getQuestionsGroupInfo(GroupsOfQuestions groupsOfQuestions) {
        String questionsInfo = "";
        List<Group> groupList = groupsOfQuestions.getGroup();

        for (Group group : groupList) {
            questionsInfo += "            Group name: " + group.getGroupName() + "<br/>";
            questionsInfo += getQuestionsInfo(group.getQuestions());
        }

        return questionsInfo;
    }

    private String getQuestionsInfo(Questions questions) {
        String questionsInfo = "";
        List<Question> questionList = questions.getQuestion();

        for (Question question : questionList) {
            questionsInfo += "Answer required: " + question.getAnswerRequired() + "<br/>";
            questionsInfo += "Question ID: " + question.getQuestionId().toString() + "<br/>";
            questionsInfo += "Question title: " + question.getQuestionTitle() + "<br/>";
            questionsInfo += "Question type: " + question.getQuestionType() + "<br/>";
            questionsInfo += "Help text" + question.getHelpText() + "<br/>";
            questionsInfo += "Options" + getOptionsInfo(question.getOptions()) + "<br/>";
            questionsInfo += getDependentQuestionsInfo(question.getDependentQuestions());
            questionsInfo += getRangeInfo(question.getRange());
            questionsInfo += getGridInfo(question.getGrid());
        }

        return questionsInfo;
    }

    private String getOptionsInfo(Options options) {
        String questionsInfo = "";
        List<String> optionList = options.getOption();
        for (String option : optionList) {
            questionsInfo += "     Option: " + option + "<br/>";
        }
        return questionsInfo;
    }

    private String getDependentQuestionsInfo(DependentQuestions dependentQuestions) {
        String dependentQuestionInfo = "";
        List<DependentQuestion> dependentQuestionList = dependentQuestions.getDependentQuestion();

        for (DependentQuestion dependentQuestion : dependentQuestionList) {
            dependentQuestionInfo += "Dependent question ID: " + dependentQuestion.getQuestionId() + "\n\r";
            dependentQuestionInfo += "Dependent question required answer: "
                    + dependentQuestion.getRequiredAnswer() + "<br/>";
        }

        return dependentQuestionInfo;
    }

    private String getRangeInfo(Range range) {
        String rangeInfo = "";
        rangeInfo += "Range Begin: " + range.getRangeBegin() + "<br/>";
        rangeInfo += "Range End: " + range.getRangeEnd() + "<br/>";
        rangeInfo += "Range Value: " + range.getValue() + "<br/>";
        return rangeInfo;
    }

    private String getGridInfo(Grid grid) {
        String gridInfo = "";

        Columns columns = grid.getColumns();
        List<String> columnList = columns.getColumn();

        Rows rows = grid.getRows();
        List<String> rowlist = rows.getRow();

        for (String row : rowlist) {
            gridInfo += "Grid Row:" + row + "<br/>";
        }

        for (String column : columnList) {
            gridInfo += "Grid Column: " + column + "<br/>";
        }

        return gridInfo;
    }
}
