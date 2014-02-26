/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.values.DateValue;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.RangeValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.objects.Range;
import super_binding.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardDataModelGenerator {

    private WizardDocument wizardDocument;
    private List<WizardForm> wizardFormList;
    private List<WizardPage> wizardPageList;
    private List<WizardTopic> wizardTopicList;
    private List<WizardQuestion> wizardQuestionList;
    private int topicNumber;

    public WizardDataModelGenerator() {
    }

    public WizardDocument getWizardDocument(List<QuestionnaireForms> forms) {
        wizardDocument = new WizardDocument();
        wizardDocument.setFormList(getWizardForms(forms));
        return wizardDocument;
    }

    private List<WizardForm> getWizardForms(List<QuestionnaireForms> forms) {
        topicNumber = 1;
        wizardFormList = new ArrayList<>();
        WizardForm wizardForm;
        for (QuestionnaireForms questionnaireForms : forms) {
            List<QuestionnaireForm> questionalFormList = questionnaireForms.getQuestionnaireForm();
            for (QuestionnaireForm questionnaireForm : questionalFormList) {
                wizardForm = new WizardForm();
                wizardForm.setFormName(questionnaireForm.getFormName());
                wizardForm.setId(questionnaireForm.getFormId());
                wizardForm.setWizardPageList(getWizardPages(questionnaireForm.getPages()));
                wizardFormList.add(wizardForm);
            }
        }
        return wizardFormList;
    }

    private List<WizardPage> getWizardPages(Pages pages) {
        List<Page> pageList = pages.getPage();
        wizardPageList = new ArrayList<>();
        WizardPage wizardPage;
        for (Page page : pageList) {
            wizardPage = new WizardPage();
            wizardPage.setId(page.getPageId());
            wizardPage.setPageNumber(page.getPageNumber());
            wizardPage.setTopicList(getWizardQuestionGroups(page.getGroupsOfQuestions()));
            wizardPageList.add(wizardPage);
        }
        return wizardPageList;
    }

    private List<WizardTopic> getWizardQuestionGroups(GroupsOfQuestions questionsGroups) {
        List<Group> groupList = questionsGroups.getGroup();
        wizardTopicList = new ArrayList<>();
        WizardTopic wizardTopic;
        for (Group group : groupList) {
            wizardTopic = new WizardTopic();
            wizardTopic.setId(group.getGroupId());
            wizardTopic.setGroupTitle(group.getGroupName());
            wizardTopic.setWizardQuestionList(getWizardQuestions(group.getQuestions()));
            wizardTopic.setTopicNumber(topicNumber);
            wizardTopicList.add(wizardTopic);
            topicNumber++;
        }
        return wizardTopicList;
    }

    private List<WizardQuestion> getWizardQuestions(Questions questions) {
        List<Question> questionList = questions.getQuestion();
        wizardQuestionList = new ArrayList<>();
        for (Question question : questionList) {
            if (createWizardQuestionFromXmlQuestion(question) != null) {
                wizardQuestionList.add(createWizardQuestionFromXmlQuestion(question));
            }
        }
        return wizardQuestionList;
    }

    private WizardQuestion createWizardQuestionFromXmlQuestion(Question xmlQuestion) {
//        DependentQuestions dependentQuestions = xmlQuestion.getDependentQuestions(); // not supported yet
        WizardQuestion wizardQuestion = null;
        if (xmlQuestion.getQuestionType() == null) {
            return null;
        }
        switch (xmlQuestion.getQuestionType()) {
            case CHECKBOX:
                CheckBoxesQuestion checkBoxesQuestion = new CheckBoxesQuestion();
                checkBoxesQuestion.setOptionsList(xmlQuestion.getOptions().getOption());
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        ListTextValue checkboxDefaults = new ListTextValue();
                        checkboxDefaults.setValue(xmlQuestion.getDefaultAnswers().getDefaultAnswer());
                        checkBoxesQuestion.setDefaultAnswer(checkboxDefaults);
                    }
                }
                wizardQuestion = checkBoxesQuestion;
                break;

            case CHOOSEFROMLIST:
                DropDownQuestion dropDownQuestion = new DropDownQuestion();
                dropDownQuestion.setOptionsList(xmlQuestion.getOptions().getOption());
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        TextValue chooseFromListDefaults = new TextValue();
                        chooseFromListDefaults.setValue(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0));
                        dropDownQuestion.setDefaultAnswer(chooseFromListDefaults);
                    }
                }
                wizardQuestion = dropDownQuestion;
                break;
            case DATE:
                wizardQuestion = new DateQuestion();
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        DateValue dateDefaults = new DateValue();
                        dateDefaults.setValue(Date.valueOf(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0)));
                        wizardQuestion.setDefaultAnswer(dateDefaults);
                    }
                }
                break;
            case FILEUPLOAD:
                wizardQuestion = new FileUploadQuestion();
                break;
            case GRID:
                GridQuestion gridQuestion = new GridQuestion();
                gridQuestion.setColumns(xmlQuestion.getGrid().getColumns().getColumn());
                gridQuestion.setRows(xmlQuestion.getGrid().getRows().getRow());
                wizardQuestion = gridQuestion;
                break;
            case MULTIPLECHOICE:
                MultipleChoiseQuestion multipleChoiseQuestion = new MultipleChoiseQuestion();
                multipleChoiseQuestion.setOptionsList(xmlQuestion.getOptions().getOption());
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        TextValue multipleChoiceDefaults = new TextValue();
                        multipleChoiceDefaults.setValue(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0));
                        multipleChoiseQuestion.setDefaultAnswer(multipleChoiceDefaults);
                    }
                }
                wizardQuestion = multipleChoiseQuestion;
                break;
            case PARAGRAPHTEXT:
                wizardQuestion = new TextAreaQuestion();
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        TextValue paragraphDefaults = new TextValue();
                        paragraphDefaults.setValue(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0));
                        wizardQuestion.setDefaultAnswer(paragraphDefaults);
                    }
                }
                break;
            case RANGE:
                RangeQuestion rangeQuestion = new RangeQuestion();
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        RangeValue rangeDefaults = new RangeValue();
                        Range range = new Range();
                        range.setStart(Integer.parseInt(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0)));
                        range.setEnd(Integer.parseInt(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(1)));
                        rangeDefaults.setValue(range);
                        rangeQuestion.setDefaultAnswer(rangeDefaults);
                    }
                }
                rangeQuestion.setRange(xmlQuestion.getRange().getRangeBegin(), xmlQuestion.getRange().getRangeEnd());
                wizardQuestion = rangeQuestion;
                break;
            case TEXT:
                wizardQuestion = new TextQuestion();
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        TextValue textDefaults = new TextValue();
                        textDefaults.setValue(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0));
                        wizardQuestion.setDefaultAnswer(textDefaults);
                    }
                }
                break;
            case TIME:
                wizardQuestion = new TimeQuestion();
                if (xmlQuestion.getDefaultAnswers() != null) {
                    if (!xmlQuestion.getDefaultAnswers().getDefaultAnswer().isEmpty()) {
                        DateValue timeDefaults = new DateValue();
                        timeDefaults.setValue(Date.valueOf(xmlQuestion.getDefaultAnswers().getDefaultAnswer().get(0)));
                        wizardQuestion.setDefaultAnswer(timeDefaults);
                    }
                }
                break;
        }

        if (wizardQuestion != null) {
            wizardQuestion.setId(xmlQuestion.getQuestionId());
            wizardQuestion.setTitle(xmlQuestion.getQuestionTitle());
            wizardQuestion.setQuestionType(xmlQuestion.getQuestionType());
            wizardQuestion.setHelpText(xmlQuestion.getHelpText());
            wizardQuestion.setAnswerRequired(xmlQuestion.isAnswerRequired());
        }

        return wizardQuestion;
    }

}
