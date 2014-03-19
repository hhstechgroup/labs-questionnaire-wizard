/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.values.*;
import com.engagepoint.labs.wizard.values.objects.Grid;
import com.engagepoint.labs.wizard.values.objects.Range;
import org.apache.log4j.Logger;
import super_binding.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardDataModelGenerator {

    private int topicNumber;
    private List<String> defaultAnswers;
    private List<QuestionRule> questionRuleList;
    private List<GroupRule> groupRuleList;
    private List<PageRule> pageRuleList;
    private static final Logger LOGGER = Logger.getLogger(WizardDataModelGenerator.class.getName());

    public WizardDataModelGenerator() {
    }

    public WizardDocument getWizardDocument(List<QuestionnaireForms> forms) {
        WizardDocument wizardDocument = new WizardDocument();
        wizardDocument.setFormList(getWizardForms(forms));
        return wizardDocument;
    }

    private List<WizardForm> getWizardForms(List<QuestionnaireForms> forms) {
        topicNumber = 1;
        List<WizardForm> wizardFormList = new ArrayList<>();
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
        List<WizardPage> wizardPageList = new ArrayList<>();
        WizardPage wizardPage;
        for (Page page : pageList) {
            wizardPage = new WizardPage();
            wizardPage.setId(page.getPageId());
            wizardPage.setPageNumber(page.getPageNumber());
            wizardPage.setPageName(page.getPageName());
            wizardPage.setTopicList(getWizardQuestionGroups(page
                    .getGroupsOfQuestions()));
            if (checkPageRules(page)) {
                wizardPage.setPageRuleList(pageRuleList);
                wizardPage.setIgnored(true);
            }
            wizardPageList.add(wizardPage);
        }
        return wizardPageList;
    }

    private List<WizardTopic> getWizardQuestionGroups(
            GroupsOfQuestions questionsGroups) {
        List<Group> groupList = questionsGroups.getGroup();
        List<WizardTopic> wizardTopicList = new ArrayList<>();
        WizardTopic wizardTopic;
        for (Group group : groupList) {
            wizardTopic = new WizardTopic();
            wizardTopic.setId(group.getGroupId());
            wizardTopic.setGroupTitle(group.getGroupName());
            wizardTopic.setWizardQuestionList(getWizardQuestions(group
                    .getQuestions()));
            wizardTopic.setTopicNumber(topicNumber);
            if (checkGroupRules(group)) {
                wizardTopic.setGroupRuleList(groupRuleList);
                wizardTopic.setIgnored(true);
            }
            wizardTopicList.add(wizardTopic);
            topicNumber++;
        }
        return wizardTopicList;
    }

    private List<WizardQuestion> getWizardQuestions(Questions questions) {
        List<Question> questionList = questions.getQuestion();
        List<WizardQuestion> wizardQuestionList = new ArrayList<>();
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
        WizardQuestion wizardQuestion = null;
        if (xmlQuestion.getQuestionType() == null) {
            return null;
        }
        switch (xmlQuestion.getQuestionType()) {
            case CHECKBOX:
                CheckBoxesQuestion checkBoxesQuestion = getCheckBoxesQuestion(xmlQuestion);
                wizardQuestion = checkBoxesQuestion;
                break;
            case CHOOSEFROMLIST:
                DropDownQuestion dropDownQuestion = getDropDownQuestion(xmlQuestion);
                wizardQuestion = dropDownQuestion;
                break;
            case DATE:
                wizardQuestion = new DateQuestion();
                getDateDefaultAnswer(xmlQuestion, wizardQuestion);
                break;
            case TIME:
                wizardQuestion = new TimeQuestion();
                getTimeDefaultAnswer(xmlQuestion, wizardQuestion);
                break;
            case FILEUPLOAD:
                wizardQuestion = new FileUploadQuestion();
                break;
            case GRID:
                GridQuestion gridQuestion = getGridQuestion(xmlQuestion);
                wizardQuestion = gridQuestion;
                break;
            case MULTIPLECHOICE:
                MultipleChoiseQuestion multipleChoiseQuestion = getMultipleChoiseQuestion(xmlQuestion);
                wizardQuestion = multipleChoiseQuestion;
                break;
            case PARAGRAPHTEXT:
                wizardQuestion = new TextAreaQuestion();
                getParagraphText(xmlQuestion, wizardQuestion);
                break;
            case RANGE:
                RangeQuestion rangeQuestion = getRangeQuestion(xmlQuestion);
                wizardQuestion = rangeQuestion;
                break;
            case TEXT:
                wizardQuestion = new TextQuestion();
                getTextDefaultAnswer(xmlQuestion, wizardQuestion);
                break;
        }
        wizardQuestion.setId(xmlQuestion.getQuestionId());
        wizardQuestion.setTitle(xmlQuestion.getQuestionTitle());
        wizardQuestion.setQuestionType(xmlQuestion.getQuestionType());
        wizardQuestion.setHelpText(xmlQuestion.getHelpText());
        wizardQuestion.setAnswerRequired(xmlQuestion.isAnswerRequired());
        if (checkQuestionRules(xmlQuestion)) {
            wizardQuestion.setQuestionRuleList(questionRuleList);
            wizardQuestion.setIgnored(true);
        }
        return wizardQuestion;
    }

    private void getTimeDefaultAnswer(Question xmlQuestion, WizardQuestion wizardQuestion) {
        Date time = null;
        if (checkDefaultAnswer(xmlQuestion)) {
            DateValue timeDefault = new DateValue();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    TimeQuestion.TIME_FORMAT);
            try {
                time = formatter.parse(defaultAnswers.get(0));
            } catch (ParseException e) {
                LOGGER.warn("TimeParseException", e);
            }
            timeDefault.setValue(time);
            wizardQuestion.setDefaultAnswer(timeDefault);
        }
    }

    private void getDateDefaultAnswer(Question xmlQuestion, WizardQuestion wizardQuestion) {
        Date date = null;
        if (checkDefaultAnswer(xmlQuestion)) {
            DateValue dateDefault = new DateValue();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    DateQuestion.DATE_FORMAT);
            try {
                date = formatter.parse(defaultAnswers.get(0));
            } catch (ParseException e) {
                LOGGER.warn("DateParseException", e);
            }
            dateDefault.setValue(date);
            wizardQuestion.setDefaultAnswer(dateDefault);
        }
    }

    private void getTextDefaultAnswer(Question xmlQuestion, WizardQuestion wizardQuestion) {
        if (checkDefaultAnswer(xmlQuestion)) {
            TextValue textDefaults = new TextValue();
            textDefaults.setValue(defaultAnswers.get(0));
            wizardQuestion.setDefaultAnswer(textDefaults);
        }
    }

    private void getParagraphText(Question xmlQuestion, WizardQuestion wizardQuestion) {
        if (checkDefaultAnswer(xmlQuestion)) {
            TextValue paragraphDefaults = new TextValue();
            paragraphDefaults.setValue(defaultAnswers.get(0));
            wizardQuestion.setDefaultAnswer(paragraphDefaults);
        }
    }

    private RangeQuestion getRangeQuestion(Question xmlQuestion) {
        RangeQuestion rangeQuestion = new RangeQuestion();
        if (checkDefaultAnswer(xmlQuestion)) {
            RangeValue rangeDefaults = new RangeValue();
            Range range = new Range();
            range.setStart(Integer.parseInt(xmlQuestion.getDefaultAnswers()
                    .getDefaultAnswer().get(0)));
            range.setEnd(Integer.parseInt(xmlQuestion.getDefaultAnswers()
                    .getDefaultAnswer().get(1)));
            rangeDefaults.setValue(range);
            rangeQuestion.setDefaultAnswer(rangeDefaults);
        }
        rangeQuestion.setRange(xmlQuestion.getRange().getRangeBegin(),
                xmlQuestion.getRange().getRangeEnd());
        return rangeQuestion;
    }

    private CheckBoxesQuestion getCheckBoxesQuestion(Question xmlQuestion) {
        CheckBoxesQuestion checkBoxesQuestion = new CheckBoxesQuestion();
        checkBoxesQuestion.setOptionsList(xmlQuestion.getOptions()
                .getOption());
        if (checkDefaultAnswer(xmlQuestion)) {
            ListTextValue checkboxDefaults = new ListTextValue();
            checkboxDefaults.setValue(defaultAnswers);
            checkBoxesQuestion.setDefaultAnswer(checkboxDefaults);
        }
        return checkBoxesQuestion;
    }

    private DropDownQuestion getDropDownQuestion(Question xmlQuestion) {
        DropDownQuestion dropDownQuestion = new DropDownQuestion();
        dropDownQuestion.setOptionsList(xmlQuestion.getOptions()
                .getOption());
        if (checkDefaultAnswer(xmlQuestion)) {
            TextValue chooseFromListDefaults = new TextValue();
            chooseFromListDefaults.setValue(defaultAnswers.get(0));
            dropDownQuestion.setDefaultAnswer(chooseFromListDefaults);
        }
        return dropDownQuestion;
    }

    private MultipleChoiseQuestion getMultipleChoiseQuestion(Question xmlQuestion) {
        MultipleChoiseQuestion multipleChoiseQuestion = new MultipleChoiseQuestion();
        multipleChoiseQuestion.setOptionsList(xmlQuestion.getOptions()
                .getOption());
        if (checkDefaultAnswer(xmlQuestion)) {
            TextValue multipleChoiceDefaults = new TextValue();
            multipleChoiceDefaults.setValue(defaultAnswers.get(0));
            multipleChoiseQuestion.setDefaultAnswer(multipleChoiceDefaults);
        }
        return multipleChoiseQuestion;
    }

    private GridQuestion getGridQuestion(Question xmlQuestion) {
        GridQuestion gridQuestion = new GridQuestion();
        List<String> rows = xmlQuestion.getGrid().getRows().getRow();
        List<String> columns = xmlQuestion.getGrid().getColumns()
                .getColumn();
        boolean oneInRow = xmlQuestion.isGridOneInRow();
        boolean oneInCol = xmlQuestion.isGridOneInCol();
        gridQuestion.setId(xmlQuestion.getQuestionId());
        gridQuestion.setColumns(columns);
        gridQuestion.setRows(rows);
        gridQuestion.setOneInCol(oneInCol);
        gridQuestion.setOneInRow(oneInRow);

        if (checkDefaultAnswer(xmlQuestion)) {
            GridValue gridDefaults = new GridValue();
            int answerSize = gridQuestion.getRows().size() * gridQuestion.getColumns().size();
            gridDefaults.setValue(new Grid(gridQuestion.getId(),
                    defaultAnswers, answerSize));
            gridQuestion.setDefaultAnswer(gridDefaults);
            gridQuestion.setAnswer(gridDefaults);
        }
        return gridQuestion;
    }

    private boolean checkDefaultAnswer(Question xmlQuestion) {
        if (xmlQuestion.getDefaultAnswers() != null
                && !xmlQuestion.getDefaultAnswers().getDefaultAnswer()
                .isEmpty()) {
            List<String> defaultAnswers = getAllCorrectDefaultAnswers(xmlQuestion
                    .getDefaultAnswers());
            if (defaultAnswers.isEmpty()) {
                return false;
            }
            this.defaultAnswers = defaultAnswers;
            return true;
        }
        return false;
    }

    private List<String> getAllCorrectDefaultAnswers(
            DefaultAnswers defaultAnswers) {
        List<String> correctAnswersList = new ArrayList<>();
        for (String s : defaultAnswers.getDefaultAnswer()) {
            if (!s.isEmpty()) {
                correctAnswersList.add(s);
            }
        }
        return correctAnswersList;
    }

    private boolean checkQuestionRules(Question xmlQuestion) {
        QuestionRules rules = xmlQuestion.getQuestionRules();
        List<QuestionRule> questionRuleList = null;
        if (rules != null) {
            questionRuleList = rules.getQuestionRule();
        }
        if (questionRuleList != null && !questionRuleList.isEmpty()) {
            List<QuestionRule> correctRuleList = getCorrectQuestionRuleList(xmlQuestion
                    .getQuestionRules());
            if (correctRuleList.isEmpty()) {
                return false;
            }
            this.questionRuleList = correctRuleList;
            return true;
        }
        return false;
    }

    private boolean checkGroupRules(Group xmlGroup) {
        GroupRules rules = xmlGroup.getGroupRules();
        List<GroupRule> groupRuleList = null;
        if (rules != null) {
            groupRuleList = rules.getGroupRule();
        }
        if (groupRuleList != null && !groupRuleList.isEmpty()) {
            List<GroupRule> correctGroupRuleList = getCorrectGroupRuleList(xmlGroup
                    .getGroupRules());
            if (correctGroupRuleList.isEmpty()) {
                return false;
            }
            this.groupRuleList = correctGroupRuleList;
            return true;
        }
        return false;

    }

    private boolean checkPageRules(Page xmlPage) {
        PageRules rules = xmlPage.getPageRules();
        List<PageRule> pageRuleList = null;
        if (rules != null) {
            pageRuleList = rules.getPageRule();
        }
        if (pageRuleList != null && !pageRuleList.isEmpty()) {
            List<PageRule> correctPageRuleList = getCorrectPageRuleList(xmlPage
                    .getPageRules());
            if (correctPageRuleList.isEmpty()) {
                return false;
            }
            this.pageRuleList = pageRuleList;
            return true;
        }
        return false;

    }

    private List<QuestionRule> getCorrectQuestionRuleList(QuestionRules rules) {
        List<QuestionRule> correctQuestionRuleList = new ArrayList<>();
        for (QuestionRule rule : rules.getQuestionRule()) {
            if (rule.getMethod() != null && !rule.getMethod().isEmpty()
                    && rule.getParentId() != null
                    && !rule.getParentId().isEmpty()) {
                correctQuestionRuleList.add(rule);
            }
        }
        return correctQuestionRuleList;
    }

    private List<GroupRule> getCorrectGroupRuleList(GroupRules rules) {
        List<GroupRule> correctGroupRuleList = new ArrayList<>();
        for (GroupRule rule : rules.getGroupRule()) {
            if (rule.getMethod() != null && !rule.getMethod().isEmpty()
                    && rule.getParentId() != null
                    && !rule.getParentId().isEmpty()) {
                correctGroupRuleList.add(rule);
            }
        }
        return correctGroupRuleList;
    }

    private List<PageRule> getCorrectPageRuleList(PageRules rules) {
        List<PageRule> correctPageRuleList = new ArrayList<>();
        for (PageRule rule : rules.getPageRule()) {
            if (rule.getMethod() != null && !rule.getMethod().isEmpty()
                    && rule.getParentId() != null
                    && !rule.getParentId().isEmpty()) {
                correctPageRuleList.add(rule);
            }
        }
        return correctPageRuleList;
    }
}