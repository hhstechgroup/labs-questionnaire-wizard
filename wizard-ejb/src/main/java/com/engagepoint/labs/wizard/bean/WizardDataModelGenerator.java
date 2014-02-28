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

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardDataModelGenerator {

    public static final String DATE_FORMAT = "dd-MM-yyyy";
    public static final String TIME_FORMAT = "hh:mm";

    private WizardDocument wizardDocument;
    private List<WizardForm> wizardFormList;
    private List<WizardPage> wizardPageList;
    private List<WizardTopic> wizardTopicList;
    private List<WizardQuestion> wizardQuestionList;
    private int topicNumber;
    private List<String> defaultAnswers;

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
            List<QuestionnaireForm> questionalFormList = questionnaireForms
                    .getQuestionnaireForm();
            for (QuestionnaireForm questionnaireForm : questionalFormList) {
                wizardForm = new WizardForm();
                wizardForm.setFormName(questionnaireForm.getFormName());
                wizardForm.setId(questionnaireForm.getFormId());
                wizardForm.setWizardPageList(getWizardPages(questionnaireForm
                        .getPages()));
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
            wizardPage.setTopicList(getWizardQuestionGroups(page
                    .getGroupsOfQuestions()));
            wizardPageList.add(wizardPage);
        }
        return wizardPageList;
    }

    private List<WizardTopic> getWizardQuestionGroups(
            GroupsOfQuestions questionsGroups) {
        List<Group> groupList = questionsGroups.getGroup();
        wizardTopicList = new ArrayList<>();
        WizardTopic wizardTopic;
        for (Group group : groupList) {
            wizardTopic = new WizardTopic();
            wizardTopic.setId(group.getGroupId());
            wizardTopic.setGroupTitle(group.getGroupName());
            wizardTopic.setWizardQuestionList(getWizardQuestions(group
                    .getQuestions()));
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
                wizardQuestionList
                        .add(createWizardQuestionFromXmlQuestion(question));
            }
        }
        return wizardQuestionList;
    }

    private WizardQuestion createWizardQuestionFromXmlQuestion(
            Question xmlQuestion) {
        // DependentQuestions dependentQuestions =
        // xmlQuestion.getDependentQuestions(); // not supported yet
        WizardQuestion wizardQuestion = null;
        if (xmlQuestion.getQuestionType() == null) {
            return null;
        }
        switch (xmlQuestion.getQuestionType()) {
            case CHECKBOX:
                CheckBoxesQuestion checkBoxesQuestion = new CheckBoxesQuestion();
                checkBoxesQuestion.setOptionsList(xmlQuestion.getOptions()
                        .getOption());
                if (checkDefaultAnswer(xmlQuestion)) {
                    ListTextValue checkboxDefaults = new ListTextValue();
                    checkboxDefaults.setValue(defaultAnswers);
                    checkBoxesQuestion.setDefaultAnswer(checkboxDefaults);
                }
                wizardQuestion = checkBoxesQuestion;
                break;

            case CHOOSEFROMLIST:
                DropDownQuestion dropDownQuestion = new DropDownQuestion();
                dropDownQuestion.setOptionsList(xmlQuestion.getOptions()
                        .getOption());
                if (checkDefaultAnswer(xmlQuestion)) {
                    TextValue chooseFromListDefaults = new TextValue();
                    chooseFromListDefaults.setValue(defaultAnswers.get(0));
                    dropDownQuestion.setDefaultAnswer(chooseFromListDefaults);
                }
                wizardQuestion = dropDownQuestion;
                break;
            case DATE:
                wizardQuestion = new DateQuestion();
                Date date = null;
                if (checkDefaultAnswer(xmlQuestion)) {
                    DateValue dateDefault = new DateValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            DATE_FORMAT);
                    try {
                        date = formatter.parse(defaultAnswers.get(0));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dateDefault.setValue(date);
                    wizardQuestion.setDefaultAnswer(dateDefault);
                }
                break;
            case TIME:
                wizardQuestion = new TimeQuestion();
                Date time = null;
                if (checkDefaultAnswer(xmlQuestion)) {
                    DateValue timeDefault = new DateValue();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            WizardDataModelGenerator.TIME_FORMAT);
                    try {
                        time = formatter.parse(defaultAnswers.get(0));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    timeDefault.setValue(time);
                    wizardQuestion.setDefaultAnswer(timeDefault);
                }
                break;
            case FILEUPLOAD:
                wizardQuestion = new FileUploadQuestion();
                break;
            case GRID:
                GridQuestion gridQuestion = new GridQuestion();
                gridQuestion.setColumns(xmlQuestion.getGrid().getColumns()
                        .getColumn());
                gridQuestion.setRows(xmlQuestion.getGrid().getRows().getRow());
                wizardQuestion = gridQuestion;
                break;
            case MULTIPLECHOICE:
                MultipleChoiseQuestion multipleChoiseQuestion = new MultipleChoiseQuestion();
                multipleChoiseQuestion.setOptionsList(xmlQuestion.getOptions()
                        .getOption());
                if (checkDefaultAnswer(xmlQuestion)) {
                    TextValue multipleChoiceDefaults = new TextValue();
                    multipleChoiceDefaults.setValue(defaultAnswers.get(0));
                    multipleChoiseQuestion
                            .setDefaultAnswer(multipleChoiceDefaults);
                }
                wizardQuestion = multipleChoiseQuestion;
                break;
            case PARAGRAPHTEXT:
                wizardQuestion = new TextAreaQuestion();
                if (checkDefaultAnswer(xmlQuestion)) {
                    TextValue paragraphDefaults = new TextValue();
                    paragraphDefaults.setValue(defaultAnswers.get(0));
                    wizardQuestion.setDefaultAnswer(paragraphDefaults);
                }
                break;
            case RANGE:
                RangeQuestion rangeQuestion = new RangeQuestion();
                if (checkDefaultAnswer(xmlQuestion)) {
                    RangeValue rangeDefaults = new RangeValue();
                    Range range = new Range();
                    range.setStart(Integer.parseInt(xmlQuestion
                            .getDefaultAnswers().getDefaultAnswer().get(0)));
                    range.setEnd(Integer.parseInt(xmlQuestion
                            .getDefaultAnswers().getDefaultAnswer().get(1)));
                    rangeDefaults.setValue(range);
                    rangeQuestion.setDefaultAnswer(rangeDefaults);
                }
                rangeQuestion.setRange(xmlQuestion.getRange().getRangeBegin(),
                        xmlQuestion.getRange().getRangeEnd());
                wizardQuestion = rangeQuestion;
                break;
            case TEXT:
                wizardQuestion = new TextQuestion();
                if (checkDefaultAnswer(xmlQuestion)) {
                    TextValue textDefaults = new TextValue();
                    textDefaults.setValue(defaultAnswers.get(0));
                    wizardQuestion.setDefaultAnswer(textDefaults);
                }
                break;
        }
        wizardQuestion.setId(xmlQuestion.getQuestionId());
        wizardQuestion.setTitle(xmlQuestion.getQuestionTitle());
        wizardQuestion.setQuestionType(xmlQuestion.getQuestionType());
        wizardQuestion.setHelpText(xmlQuestion.getHelpText());
        wizardQuestion.setAnswerRequired(xmlQuestion.isAnswerRequired());
        return wizardQuestion;
    }

    private boolean checkDefaultAnswer(Question xmlQuestion) {
        if (xmlQuestion.getDefaultAnswers() != null
                && !xmlQuestion.getDefaultAnswers().getDefaultAnswer()
                .isEmpty()) {
            List<String> defaultAnswers = getAllCorrectDefaultAnswers(xmlQuestion.getDefaultAnswers());
            if (defaultAnswers.isEmpty()) {
                return false;
            }
            this.defaultAnswers = defaultAnswers;
            return true;
        }
        return false;
    }

    private List<String> getAllCorrectDefaultAnswers(DefaultAnswers defaultAnswers) {
        List<String> correctAnswersList = new ArrayList<>();
        for (String s : defaultAnswers.getDefaultAnswer()) {
            if (!s.isEmpty()) {
                correctAnswersList.add(s);
            }
        }
        return correctAnswersList;
    }
}
