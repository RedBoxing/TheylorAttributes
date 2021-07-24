package fr.redboxing.theylor.attributes.sql.column;

import java.util.ArrayList;

public class PlayerDataColumns {
    private static final ArrayList<Column> columns = new ArrayList<>();

    public static ArrayList<Column> getColumns() {
        addColumn("class", "TEXT");

        return columns;
    }

    public static void addColumn(String name, String type) {
        columns.add(new Column(name, type));
    }
}