package com.engagepoint.labs.wizard.model.data;

public class Question {
    private String name = "Question";

    public Question(int p_id, int gr_id, int id) {
	setName(getName() + id + 1);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }
}
