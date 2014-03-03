package com.engagepoint.labs.wizard.export;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.questions.CheckBoxesQuestion;
import com.engagepoint.labs.wizard.questions.DropDownQuestion;
import com.engagepoint.labs.wizard.questions.MultipleChoiseQuestion;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import super_binding.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor.guzenko on 2/28/14.
 */
public class QuestionaireFormConverter {
    private QuestionnaireForms questionnaireForms;

    public QuestionnaireForms convert(WizardForm wizardForm) {
        if (wizardForm == null) {
            return null;
        }
        questionnaireForms = new QuestionnaireForms();
        QuestionnaireForm questionnaireForm = new QuestionnaireForm();
        questionnaireForm.setFormId(wizardForm.getId());
        questionnaireForm.setFormName(wizardForm.getFormName());
        questionnaireForm.setPages(getPages(wizardForm));
        questionnaireForms.getQuestionnaireForm().add(questionnaireForm);
        return questionnaireForms;

    }

    private Pages getPages(WizardForm wizardForm) {
        Pages pages = new Pages();
        Page page;
        for (WizardPage wizardPage : wizardForm.getWizardPageList()) {
            page = new Page();
            page.setPageId(wizardPage.getId());
            page.setPageNumber(wizardPage.getPageNumber());
            page.setGroupsOfQuestions(getGroupsOfQuestions(wizardPage));
            pages.getPage().add(page);
        }
        return pages;
    }

    private GroupsOfQuestions getGroupsOfQuestions(WizardPage wizardPage) {
        GroupsOfQuestions groupsOfQuestions = new GroupsOfQuestions();
        Group group;
        for (WizardTopic wizardTopic : wizardPage.getTopicList()) {
            group = new Group();
            group.setGroupId(wizardTopic.getId());
            group.setGroupName(wizardTopic.getGroupTitle());
            group.setQuestions(getQuestions(wizardTopic));
            groupsOfQuestions.getGroup().add(group);
        }
        return groupsOfQuestions;
    }

    private Questions getQuestions(WizardTopic wizardTopic) {
        Questions questions = new Questions();
        Question question;
        for (WizardQuestion wizardQuestion : wizardTopic.getWizardQuestionList()) {
            question = new Question();
            setGeneralProperties(question, wizardQuestion);
            question.setDefaultAnswers(getQuestionAnswersByType(question, wizardQuestion));
            questions.getQuestion().add(question);
        }

        return questions;
    }

    private void setGeneralProperties(final Question question, final WizardQuestion wizardQuestion) {
        question.setAnswerRequired(wizardQuestion.isRequired());
        question.setHelpText(wizardQuestion.getHelpText());
        question.setQuestionId(wizardQuestion.getId());
        question.setQuestionTitle(wizardQuestion.getTitle());
        question.setQuestionType(wizardQuestion.getQuestionType());
    }

    private DefaultAnswers getQuestionAnswersByType(final Question question, WizardQuestion wizardQuestion) {
            DefaultAnswers defaultAnswers = new DefaultAnswers();
            switch (wizardQuestion.getQuestionType()){
                case TEXT:
                    defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion).toString());
                    question.setDefaultAnswers(defaultAnswers);
                    break;
                case PARAGRAPHTEXT:
                    defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion).toString());
                    question.setDefaultAnswers(defaultAnswers);
                    break;
                case CHOOSEFROMLIST:
                    defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion).toString());
                    question.setOptions(getQuestionOptions((DropDownQuestion)wizardQuestion));
                    question.setDefaultAnswers(defaultAnswers);
                    break;
                case CHECKBOX:
                    List answersList = (ArrayList<String>)wizardQuestion.getAnswer().getValue();
                    defaultAnswers.getDefaultAnswer().addAll(answersList);
                    question.setOptions(getQuestionOptions((CheckBoxesQuestion)wizardQuestion));
                    question.setDefaultAnswers(defaultAnswers);
                    break;
                case RANGE:

                    break;
                case MULTIPLECHOICE:
                    defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion).toString());
                    question.setOptions(getQuestionOptions((MultipleChoiseQuestion)wizardQuestion));
                    question.setDefaultAnswers(defaultAnswers);

                    break;
                case DATE:
                    defaultAnswers.getDefaultAnswer().add(wizardQuestion.getAnswer().getValue().toString());
                    question.setDefaultAnswers(defaultAnswers);
                    break;
                case TIME:
                    defaultAnswers.getDefaultAnswer().add(wizardQuestion.getAnswer().getValue().toString());
                    break;
                case GRID:
                    break;
                case FILEUPLOAD:
                    break;
                default:
                    break;
            }
        return defaultAnswers;
    }

    private Options getQuestionOptions(DropDownQuestion wizardQuestion) {
        Options options = new Options();
        options.getOption().addAll(wizardQuestion.getOptionsList());
        return options;
    }

    private Options getQuestionOptions(CheckBoxesQuestion wizardQuestion) {
        Options options = new Options();
        options.getOption().addAll(wizardQuestion.getOptionsList());
        return options;
    }

    private String getTextValueAnswer(WizardQuestion wizardQuestion) {
        return wizardQuestion.getAnswer().getValue().toString();
    }

    private Options getQuestionOptions(MultipleChoiseQuestion wizardQuestion) {
        Options options = new Options();
        options.getOption().addAll(wizardQuestion.getOptionsList());
        return options;
    }
}
