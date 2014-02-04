package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.Question;

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
    private List<Question> questions;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
