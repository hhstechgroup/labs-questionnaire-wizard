package com.engagepoint.labs.wizard.test;

import java.io.Serializable;
import java.util.ArrayList;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * @author srijeeb
 *
 */
@Named("myBackingBean")
@SessionScoped
public class TestRadioBackingBean implements Serializable {

    private static final long serialVersionUID = -957611005891906772L;

    public TestRadioBackingBean() {
	populateTable2Values();
	populateTable3Values();
    }

    private ArrayList<TestVO> sampleTable2 = null;
    private ArrayList<TestVO> sampleTable3 = null;
    private String radSelectIndex2 = null;
    private String resultOfFirstTable = "";
    private String resultOfSecondTable = "";

    /**
     * @return
     */
    public ArrayList getSampleTable2() {
	return sampleTable2;
    }

    /**
     * @return
     */
    public ArrayList getSampleTable3() {
	return sampleTable3;
    }

    /**
     * @param list
     */
    public void setSampleTable2(ArrayList list) {
	sampleTable2 = list;
    }

    /**
     * @param list
     */
    public void setSampleTable3(ArrayList list) {
	sampleTable3 = list;
    }

    /**
     * @return
     */
    public String getRadSelectIndex2() {
	return radSelectIndex2;
    }

    public String selectRadio(String id)
    {
	System.out.println("BBBBBBBB");
	radSelectIndex2=id;
	return null;
    }

    /**
     * @param string
     */
    public void setRadSelectIndex2(String string) {
	System.out.println("AAAAAAAAA");
	radSelectIndex2 = string;
    }

    public String executeTest() {

	System.out.println("Radio Selected for Table 2: "
		+ getRadSelectIndex2());
	setFirstTableResultField();
//	System.out.println("Radio Selected for Table 3: ");
//	if (sampleTable3 != null) {
//	    for (int i = 0; i < sampleTable3.size(); i++) {
//		TestVO aTestVO = (TestVO) sampleTable3.get(i);
//		System.out.println("Row no :" + i + " Value : "
//			+ aTestVO.getRadAns());
//	    }
//	}
//	setSecondTableResultField();

	return null;
    }

    private void populateTable2Values() {
	sampleTable2 = new ArrayList<TestVO>();
	for (int i = 0; i < 3; i++) {
	    TestVO aTestVO = new TestVO();
	    aTestVO.setEmpName("Employee Name " + i);
	    aTestVO.setIndex(String.valueOf(i));
	    sampleTable2.add(aTestVO);
	}
    }

    private void populateTable3Values() {
	sampleTable3 = new ArrayList<TestVO>();
	for (int i = 0; i < 3; i++) {
	    TestVO aTestVO = new TestVO();
	    aTestVO.setEmpName("Employee Name " + i);
	    sampleTable3.add(aTestVO);
	}
    }

    private void setFirstTableResultField() {

	String radSelectedIndex = getRadSelectIndex2();
	int intRadSelectedIndex = -1;
	if (radSelectedIndex != null && radSelectedIndex.trim().length() > 0) {
	    intRadSelectedIndex = Integer.parseInt(radSelectedIndex);
	}
	String toPrint = "";
	if (intRadSelectedIndex != -1) {
	    toPrint = "You have selected index "
		    + radSelectedIndex
		    + " ("
		    + ((TestVO) sampleTable2.get(intRadSelectedIndex))
			    .getEmpName() + ") from First Table";
	    setResultOfFirstTable(toPrint);
	} else {
	    toPrint = "You have not selected any row from First Table";
	    ;
	    setResultOfFirstTable(toPrint);
	}
    }

    private void setSecondTableResultField() {

	String toPrint = "";
	if (sampleTable3 != null && sampleTable3.size() > 0) {
	    for (int i = 0; i < sampleTable3.size(); i++) {
		TestVO aTestVO = (TestVO) sampleTable3.get(i);
		String tempRadAns = "";
		if (aTestVO.getRadAns() == null) {
		    tempRadAns = "No Rating";
		} else {
		    tempRadAns = aTestVO.getRadAns();
		}
		toPrint += "[" + aTestVO.getEmpName() + "] Rating: "
			+ tempRadAns;

	    }
	    setResultOfSecondTable(toPrint);
	} else {
	    setResultOfSecondTable(toPrint);
	}

    }

    /**
     * @return
     */
    public String getResultOfFirstTable() {
	return resultOfFirstTable;
    }

    /**
     * @return
     */
    public String getResultOfSecondTable() {
	return resultOfSecondTable;
    }

    /**
     * @param string
     */
    public void setResultOfFirstTable(String string) {
	resultOfFirstTable = string;
    }

    /**
     * @param string
     */
    public void setResultOfSecondTable(String string) {
	resultOfSecondTable = string;
    }

}
