import com.engagepoint.labs.wizard.questions.DateQuestion;
import com.engagepoint.labs.wizard.values.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static junit.framework.Assert.*;

public class TestValues {

    private GridValue gridValue;
    private FileValue fileValue;
    private ListTextValue listTextValue;
    private RangeValue rangeValue;
    private TextValue textValue;
    private DateValue dateValue;


    @Before
    public void initGrid() {
        gridValue = new GridValue();
        fileValue = new FileValue();
        listTextValue = new ListTextValue();
        rangeValue = new RangeValue();
        textValue = new TextValue();
        dateValue = new DateValue();
    }


    @Test(expected = ClassCastException.class)
    public void testSetGridValue() {
        gridValue.setValue(ListTextValue.class);
    }


    @Test(expected = ClassCastException.class)
    public void testSetFileValue() {
        fileValue.setValue(GridValue.class);

    }

    @Test(expected = ClassCastException.class)
    public void testRangeValue() {
        rangeValue.setValue(GridValue.class);
    }

    @Test(expected = ClassCastException.class)
    public void testListTextValue() {
        listTextValue.setValue(GridValue.class);
    }

    @Test(expected = ClassCastException.class)
    public void testTextValue() {
        textValue.setValue(GridValue.class);
    }

    @Test(expected = ClassCastException.class)
    public void testDateValue() {
        dateValue.setValue(ListTextValue.class);
    }


}
