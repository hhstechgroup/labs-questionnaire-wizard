package com.engagepoint.labs.wizard.questions;

import com.engagepoint.labs.wizard.values.Value;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import super_binding.QType;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.util.List;

/**
 * Created by igor.guzenko on 2/4/14.
 */
public abstract class WizardQuestion {

    protected String id;
    protected String title;
    protected QType questionType;
    protected String helpText;
    protected Boolean answerRequired;
    protected Boolean valid;
    protected List<String> rules;
    public boolean ignored;
    public RuleExecutor ruleExecutor;

    public RuleExecutor getRuleExecutor() {
        return ruleExecutor;
    }

    public void setRuleExecutor(RuleExecutor ruleExecutor) {
        this.ruleExecutor = ruleExecutor;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public List<String> getRules() {
        return rules;
    }

    public void setRules(List<String> rules) {
        this.rules = rules;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public abstract Value getAnswer();

    public abstract void setAnswer(Value answer);

    public abstract Value getDefaultAnswer();

    public abstract void setDefaultAnswer(Value defaultAnswer);

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText(String helpText) {
        this.helpText = helpText;
    }

    public boolean isRequired() {
        return answerRequired;
    }

    public void setAnswerRequired(Boolean answerRequired) {
        this.answerRequired = answerRequired;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public QType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QType questionType) {
        this.questionType = questionType;
    }

    public void executeAllRules() {
        if (rules != null) {
            for (String s : rules) {
                ruleExecutor.setQuestion(this);
                JexlEngine jexlEngine = new JexlEngine();
                Expression expression = jexlEngine.createExpression(s);
                JexlContext context = new MapContext();
                context.set("this", this);
                expression.evaluate(context);
            }
        }
    }
}
