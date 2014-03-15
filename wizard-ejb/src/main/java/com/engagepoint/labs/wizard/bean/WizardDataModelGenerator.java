/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import super_binding.DefaultAnswers;
import super_binding.Group;
import super_binding.GroupsOfQuestions;
import super_binding.Page;
import super_binding.Pages;
import super_binding.Question;
import super_binding.QuestionnaireForm;
import super_binding.QuestionnaireForms;
import super_binding.Questions;
import super_binding.Rule;
import super_binding.Rules;

import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.questions.DropDownQuestion;
import com.engagepoint.labs.wizard.questions.FileUploadQuestion;
import com.engagepoint.labs.wizard.questions.GridQuestion;
import com.engagepoint.labs.wizard.questions.MultipleChoiseQuestion;
import com.engagepoint.labs.wizard.questions.RangeQuestion;
import com.engagepoint.labs.wizard.questions.TextAreaQuestion;
import com.engagepoint.labs.wizard.questions.TextQuestion;
import com.engagepoint.labs.wizard.questions.TimeQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.values.DateValue;
import com.engagepoint.labs.wizard.values.GridValue;
import com.engagepoint.labs.wizard.values.ListTextValue;
import com.engagepoint.labs.wizard.values.RangeValue;
import com.engagepoint.labs.wizard.values.TextValue;
import com.engagepoint.labs.wizard.values.objects.Grid;
import com.engagepoint.labs.wizard.values.objects.Range;

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
    private List<String> defaultAnswers;
    private List<Rule> ruleList;
    private static final Logger LOGGER = Logger
	    .getLogger(WizardDataModelGenerator.class);

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
	    wizardPage.setPageName(page.getPageName());
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
			DateQuestion.DATE_FORMAT);
		try {
		    date = formatter.parse(defaultAnswers.get(0));
		} catch (ParseException e) {
		    LOGGER.warn(e.getMessage());
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
			TimeQuestion.TIME_FORMAT);
		try {
		    time = formatter.parse(defaultAnswers.get(0));
		} catch (ParseException e) {
		    LOGGER.warn(e.getMessage());
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
	    List<String> rows = xmlQuestion.getGrid().getRows().getRow();
	    List<String> columns = xmlQuestion.getGrid().getColumns()
		    .getColumn();

	    gridQuestion.setColumns(columns);
	    gridQuestion.setRows(rows);
	    if (checkDefaultAnswer(xmlQuestion)) {
		GridValue gridDefaults = new GridValue();
		gridDefaults.setValue(new Grid(rows, columns, defaultAnswers,
			gridQuestion.getId()));
		gridQuestion.setDefaultAnswer(gridDefaults);
		gridQuestion.setAnswer(gridDefaults);
	    }
	    wizardQuestion = gridQuestion;
	    break;
	case MULTIPLECHOICE:
	    MultipleChoiseQuestion multipleChoiseQuestion = new MultipleChoiseQuestion();
	    multipleChoiseQuestion.setOptionsList(xmlQuestion.getOptions()
		    .getOption());
	    if (checkDefaultAnswer(xmlQuestion)) {
		TextValue multipleChoiceDefaults = new TextValue();
		multipleChoiceDefaults.setValue(defaultAnswers.get(0));
		multipleChoiseQuestion.setDefaultAnswer(multipleChoiceDefaults);
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
		range.setStart(Integer.parseInt(xmlQuestion.getDefaultAnswers()
			.getDefaultAnswer().get(0)));
		range.setEnd(Integer.parseInt(xmlQuestion.getDefaultAnswers()
			.getDefaultAnswer().get(1)));
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
	if (checkRules(xmlQuestion)) {
	    wizardQuestion.setRules(ruleList);
	    wizardQuestion.setIgnored(true);
	}
	return wizardQuestion;
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

    private boolean checkRules(Question xmlQuestion) {
	Rules rules = xmlQuestion.getRules();
	List<Rule> rule = null;
	if (rules != null) {
	    rule = rules.getRule();
	}
	if (rule != null && !rule.isEmpty()) {
	    xmlQuestion.getRules().getRule();
	    List<Rule> correctRuleList = getCorrectRuleList(xmlQuestion
		    .getRules());
	    if (correctRuleList.isEmpty()) {
		return false;
	    }
	    this.ruleList = correctRuleList;
	    return true;
	}
	return false;
    }

    private List<Rule> getCorrectRuleList(Rules rules) {
	List<Rule> correctRuleList = new ArrayList<>();
	for (Rule rule : rules.getRule()) {
	    if (rule.getMethod() != null && !rule.getMethod().isEmpty()
		    && rule.getParentId() != null
		    && !rule.getParentId().isEmpty()) {
		correctRuleList.add(rule);
	    }
	}
	return correctRuleList;
    }
}