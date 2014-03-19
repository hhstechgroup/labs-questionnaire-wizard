package com.engagepoint.labs.wizard.export;

import com.engagepoint.labs.wizard.bean.WizardForm;
import com.engagepoint.labs.wizard.bean.WizardPage;
import com.engagepoint.labs.wizard.bean.WizardTopic;
import com.engagepoint.labs.wizard.questions.*;
import com.engagepoint.labs.wizard.values.objects.Range;
import super_binding.*;

import java.util.*;

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
            if(wizardPage.isIgnored()){
                continue;
            }
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
            if(wizardTopic.isIgnored()){
                continue;
            }
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
            if(wizardQuestion.isIgnored()){
                continue;
            }
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
        switch (wizardQuestion.getQuestionType()) {
            case TEXT:
                defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion));
                break;
            case PARAGRAPHTEXT:
                defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion));
                break;
            case CHOOSEFROMLIST:
                defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion));
                question.setOptions(getQuestionOptions((DropDownQuestion) wizardQuestion));
                break;
            case CHECKBOX:
                defaultAnswers.getDefaultAnswer().addAll(getListValueAnswer(wizardQuestion));
                question.setOptions(getQuestionOptions((CheckBoxesQuestion) wizardQuestion));
                break;
            case RANGE:
                defaultAnswers.getDefaultAnswer().addAll(getListRangeAnswer(wizardQuestion));
                break;
            case MULTIPLECHOICE:
                defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion));
                question.setOptions(getQuestionOptions((MultipleChoiseQuestion) wizardQuestion));
                break;
            case DATE:
                defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion));
                break;
            case TIME:
                defaultAnswers.getDefaultAnswer().add(getTextValueAnswer(wizardQuestion));
                break;
            case GRID:
                defaultAnswers.getDefaultAnswer().addAll(getListGridAnswers((GridQuestion) wizardQuestion));
                break;
            case FILEUPLOAD:
                break;
            default:
                break;
        }
        return defaultAnswers;
    }

    private List<String> getListGridAnswers(GridQuestion gridQuestion) {
        List<String> answerList = new ArrayList<>(gridQuestion.getRows().size());
        StringBuilder linesBuilder = new StringBuilder();
        Map<String, Boolean> answersMap = ((com.engagepoint.labs.wizard.values.objects.Grid) gridQuestion.getAnswer().getValue()).getValues();
        Set keySet = answersMap.keySet();
        Iterator keysIterator = keySet.iterator();
        int valuesInLine = 0;
        boolean lastIterator = false;
        while (keysIterator.hasNext() || lastIterator) {
            if (valuesInLine < gridQuestion.getColumns().size()) {
                String key = keysIterator.next().toString();
                valuesInLine++;
                linesBuilder.append(answersMap.get(key));
                linesBuilder.append(",");
                lastIterator = true;
            } else {
                lastIterator = false;
                answerList.add(linesBuilder.toString());
                valuesInLine = 0;
                linesBuilder.setLength(0);
            }
        }
        return answerList;
    }

    private List<String> getListRangeAnswer(WizardQuestion wizardQuestion) {
        List<String> answerList = new ArrayList<>(2);
        com.engagepoint.labs.wizard.values.objects.Range range = (Range) wizardQuestion.getAnswer().getValue();
        answerList.add(String.valueOf(range.getStart()));
        answerList.add(String.valueOf(range.getEnd()));
        return answerList;
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
        if (null == wizardQuestion.getAnswer()) {
            return "";
        } else {
            return wizardQuestion.getAnswer().getValue().toString();
        }
    }

    private Options getQuestionOptions(MultipleChoiseQuestion wizardQuestion) {
        Options options = new Options();
        options.getOption().addAll(wizardQuestion.getOptionsList());
        return options;
    }

    private List getListValueAnswer(WizardQuestion wizardQuestion) {
        if (null == wizardQuestion.getAnswer()) {
            return new ArrayList();
        } else {
            return (List) wizardQuestion.getAnswer().getValue();
        }
    }
}
