/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.WizardQuestion;
import com.engagepoint.labs.wizard.ruleExecutors.TopicRuleExecutor;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import super_binding.GroupRule;

import java.util.List;

/**
 * @author artem.pylypenko
 */
public class WizardTopic {

    private String id;
    private String groupTitle;
    private List<WizardQuestion> wizardQuestionList;
    private Integer topicNumber;
    private List<GroupRule> groupRuleList;
    private boolean ignored;
    public TopicRuleExecutor ruleExecutor;

    public WizardTopic() {
    }

    public List<GroupRule> getGroupRuleList() {
        return groupRuleList;
    }

    public void setGroupRuleList(List<GroupRule> groupRuleList) {
        this.groupRuleList = groupRuleList;
    }

    public TopicRuleExecutor getRuleExecutor() {
        return ruleExecutor;
    }

    public void setRuleExecutor(TopicRuleExecutor ruleExecutor) {
        this.ruleExecutor = ruleExecutor;
    }

    public boolean isIgnored() {

        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTopicNumber() {
        return topicNumber;
    }

    public void setTopicNumber(Integer topicNumber) {
        this.topicNumber = topicNumber;
    }

    public List<WizardQuestion> getWizardQuestionList() {
        return wizardQuestionList;
    }

    public void setWizardQuestionList(List<WizardQuestion> wizardQuestionList) {
        this.wizardQuestionList = wizardQuestionList;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public WizardQuestion findQuestionById(String questionId) {
        for (WizardQuestion question : wizardQuestionList) {
            if (question.getId().equals(questionId)) {
                return question;
            }
        }
        return null;
    }

    public boolean executeAllRules() {
        boolean change = false;
        if (groupRuleList != null) {
            for (GroupRule rule : groupRuleList) {
                ruleExecutor.setTopic(this);
                JexlEngine jexlEngine = new JexlEngine();
                Expression expression = jexlEngine.createExpression(String.format(rule.getMethod(), rule.getParentId()));
                JexlContext context = new MapContext();
                context.set("this", this);
                boolean result = (boolean) expression.evaluate(context);
                if (result) {
                    change = true;
                }
            }
        }
        return change;
    }

    public void resetTopic() {
        for (WizardQuestion question : wizardQuestionList) {
            question.resetAnswer();
        }

    }
}
