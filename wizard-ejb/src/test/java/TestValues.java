import com.engagepoint.labs.wizard.values.*;
import com.engagepoint.labs.wizard.values.objects.Grid;
import com.engagepoint.labs.wizard.values.objects.Range;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public void testSetGridValueException() {
        gridValue.setValue(ListTextValue.class);
    }

    @Test
    public void setGridValue() {
        Grid grid = new Grid();
        gridValue.setValue(grid);
        assertNotNull(gridValue.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void testSetFileValueException() {
        fileValue.setValue(GridValue.class);
    }



    @Test(expected = ClassCastException.class)
    public void testRangeValueException() {
        rangeValue.setValue(GridValue.class);
    }

    @Test
    public void testRangeValue() {
        Range range = new Range();
        rangeValue.setValue(range);
        assertNotNull(rangeValue.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void testListTextValueException() {
        listTextValue.setValue(GridValue.class);
    }

    @Test
    public void testListTextValue() {
        List<String> textValueList = new ArrayList<>();
        textValueList.add("test1");
        textValueList.add("test2");
        textValueList.add("test3");
	textValueList.add("test4");
        listTextValue.setValue(textValueList);

        assertNotNull(listTextValue.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void testTextValueException() {
        textValue.setValue(GridValue.class);
    }

    @Test
    public void testTextValue() {
        String testString = "Test String";
        textValue.setValue(testString);
        assertNotNull(textValue.getValue());
    }

    @Test(expected = ClassCastException.class)
    public void testDateValueException() {
        dateValue.setValue(ListTextValue.class);
    }

    @Test
    public void testDateValue() {
        Date date = new Date();
        date.setDate(07 - 07 - 2007);
        dateValue.setValue(date);
        assertNotNull(dateValue.getValue());
    }

    @Test
    public void valueTest() {

    }

}
