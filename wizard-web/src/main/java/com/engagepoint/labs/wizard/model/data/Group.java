package com.engagepoint.labs.wizard.model.data;

import java.util.ArrayList;

public class Group {
    private String name = "Group";
    private ArrayList<Question> questions;

    private int count = 3 + (int) (Math.random() * ((10 - 3) + 1));

    public Group(int p_id, int id) {
	setName(getName() + id + 1);
	setQuestions(new ArrayList<Question>());

	for (int i = 0; i < count; i++) {
	    getQuestions().add(new Question(p_id, id, i));
	}
    }

    public ArrayList<Question> getQuestions() {
	return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
	this.questions = questions;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}