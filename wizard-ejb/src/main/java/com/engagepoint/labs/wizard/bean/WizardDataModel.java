package com.engagepoint.labs.wizard.bean;

import com.engagepoint.labs.wizard.questions.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: andrii.sotnyk
 * Date: 2/4/14
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class WizardDataModel {
    private ArrayList<ArrayList<Integer>> wizardScreenIDs;
    private HashMap<Integer, Group> wizardScreens;
    private int lastGroupID = 0;

    public int getPageCount() {
        return wizardScreenIDs.size();
    }

    public void addPage() {
        wizardScreenIDs.add(new ArrayList<Integer>());
    }

    public int getGroupPerPageCount(int pageNumber) {
        return wizardScreenIDs.get(pageNumber).size();
    }

    public void addGroup(int pageNumber, Group newGroup) {
        wizardScreenIDs.get(pageNumber).add(lastGroupID);
        wizardScreens.put(lastGroupID, newGroup);
        lastGroupID++;
    }

    public int getGroupID(int pageNumber, int groupNumber) {
        return wizardScreenIDs.get(pageNumber).get(groupNumber);
    }

    public String getTitle(int groupID) {
        return wizardScreens.get(groupID).getTitle();
    }

    public List<Integer> getQuestionIDsByPage(int groupID) {
        List<Integer> questionIDs = new ArrayList<>();
        List<Question> questionList = wizardScreens.get(groupID).getQuestions();
        for(Question question : questionList) {
            questionIDs.add((int)question.getId());
        }
        return questionIDs;
    }






}
