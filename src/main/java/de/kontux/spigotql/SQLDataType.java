package de.kontux.spigotql;

public class SQLDataType {

    public static String VARCHAR(int length) {
        return "varchar(" + length + ")";
    }

    public static final String BIG_INT = "BIGINT";

    public static final String DATE = "DATE";

    public static final String INT = "int";
}
