package com.engagepoint.labs.wizard.test;

public class TestVO {

    private String empName = null;
    private String index = "-1";
    private Boolean selected=false;
    private String radAns = null;

    public String getEmpName() {
	return empName;
    }

    public void setEmpName(String string) {
	empName = string;
    }

    public String getIndex() {
	System.out.println("IIIIIIIIIIIIII");
	return index;
    }

    public void setIndex(String i) {
	StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
	StackTraceElement e = stacktrace[2];//maybe this number needs to be corrected
	String methodName = e.getMethodName();
	System.out.println(methodName);
	index = i;
    }

    public String getRadAns() {
	return radAns;
    }

    public void setRadAns(String ans) {
	radAns = ans;
    }

    public Boolean getSelected() {
	return selected;
    }

    public void setSelected(Boolean selected) {
	this.selected = selected;
    }
}
