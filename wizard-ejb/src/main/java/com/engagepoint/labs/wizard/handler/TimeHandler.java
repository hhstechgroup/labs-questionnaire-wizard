package com.engagepoint.labs.wizard.handler;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import com.engagepoint.labs.wizard.questions.TimeQuestion;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Named("timeHandler")
@SessionScoped
public class TimeHandler implements Serializable {
    private static final long serialVersionUID = 7067726255061916341L;

    private Map<String, TimeQuestion> questions;

    private String currentQuestionID;

    @PostConstruct
    public void init() {
	questions = new LinkedHashMap<>();
	currentQuestionID = "";
    }

    public TimeHandler setTimeQuestionID(String questionID) {
	currentQuestionID = questionID;
	return this;
    }

    public Date getCurrentTimeQuestionValue() {
	return (Date) questions.get(currentQuestionID).getAnswer().getValue();
    }

    public void setCurrentTimeQuestionValue(Date date) {
	questions.get(currentQuestionID).getAnswer().setValue(date);
    }

    public Map<String, TimeQuestion> getQuestions() {
	return questions;
    }

    public void setQuestions(Map<String, TimeQuestion> questions) {
	this.questions = questions;
    }

    public String getCurrentQuestionID() {
	return currentQuestionID;
    }

    public void setCurrentQuestionID(String currentQuestionID) {
	this.currentQuestionID = currentQuestionID;
    }
}
