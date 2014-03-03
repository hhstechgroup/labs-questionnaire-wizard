package com.engagepoint.labs.wizard.export;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.questions.WizardQuestion;
import super_binding.*;

/**
 * Created by igor.guzenko on 2/28/14.
 */
public class QuestionaireFormConverter {
    private QuestionnaireForms questionnaireForms;

    public QuestionnaireForms conver(WizardForm wizardForm) {
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
            setQuestionPropertiesByType(question, wizardQuestion);
            questions.getQuestion().add(question);
        }

        return null;
    }

    private void setGeneralProperties(final Question question, final WizardQuestion wizardQuestion) {
        question.setAnswerRequired(wizardQuestion.isRequired());
        question.setHelpText(wizardQuestion.getHelpText());
        question.setQuestionId(wizardQuestion.getId());
        question.setQuestionTitle(wizardQuestion.getTitle());
        question.setQuestionType(wizardQuestion.getQuestionType());
    }

    private void setQuestionPropertiesByType(final Question question, WizardQuestion wizardQuestion) {
            switch (wizardQuestion.getQuestionType()){
                case TEXT:
                    question.setDefaultAnswers(getTextValueAnswer(wizardQuestion));
                    break;
                case PARAGRAPHTEXT:
                    break;
                case CHOOSEFROMLIST:
                    break;
                case CHECKBOX:
                    break;
                case RANGE:
                    break;
                case MULTIPLECHOICE:
                    break;
                case DATE:
                    break;
                case TIME:
                    break;
                case GRID:
                    break;
                case FILEUPLOAD:
                    break;
                default:
                    break;
            }

    }

    private DefaultAnswers getTextValueAnswer(WizardQuestion wizardQuestion) {
        return null;
    }


}
