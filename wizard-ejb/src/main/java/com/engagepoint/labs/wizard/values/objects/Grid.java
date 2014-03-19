package com.engagepoint.labs.wizard.values.objects;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by igor.guzenko on 2/17/14.
 */
public class Grid {
    public static String CELL_PREFIX = "gridcell_";
    private Map<String, Boolean> values;

    public Grid(String gridID, List<String> values, int size) {
        this.values = new LinkedHashMap<String, Boolean>();
        parseStrings(gridID, values, size);
    }

    private void parseStrings(String gridID, List<String> values, int size) {
        int checkBoxNumber = 0;
        for (String stringRow : values) {
            String[] cellIDs = stringRow.split(",");
            for (int i = 0; i < cellIDs.length && checkBoxNumber < size; i++) {
                this.values.put(createCheckBoxID(gridID, checkBoxNumber),
                        Boolean.parseBoolean(cellIDs[i]));
                checkBoxNumber++;
            }
        }
    }

    public static String createCheckBoxID(String gridID, int cellNumber) {
        String id = CELL_PREFIX + gridID + "_"
                + Integer.toString(cellNumber, 10);
        return id;
    }

    public static int getCheckBoxNumberFromID(String id) {
        int checkBoxIDNumberStartPosition = id.lastIndexOf('_') + 1;
        String checkBoxNumberString = id
                .substring(checkBoxIDNumberStartPosition);

        return Integer.parseInt(checkBoxNumberString);
    }

    public Map<String, Boolean> getValues() {
        return values;
    }

    public void setValues(Map<String, Boolean> values) {
        this.values = values;
    }
}
