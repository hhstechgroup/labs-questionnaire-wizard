package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.BasicQuestion;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/4/14
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */

public class Group {
    private String title;
    private List<BasicQuestion> questions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<BasicQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<BasicQuestion> questions) {
        this.questions = questions;
    }
}
