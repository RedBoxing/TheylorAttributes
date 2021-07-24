package fr.redboxing.theylor.attributes.sql;

import fr.redboxing.theylor.attributes.TheylorAttributes;

import java.sql.*;

public class SQLHandler {
    private static final String databaseName = "s15_theylor";
    private static final String databaseHost = "193.70.42.207";
    private static final int databasePort = 3306;
    private static final String databaseUser = "u15_qdykAvIxFg";
    private static final String databasePass = "ff.23nLJ8sotpJX61TDnI!Lj";
    public static Connection connection;

    public static void connect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mariadb://" + databaseHost + ":" + databasePort + "/" + databaseName, databaseUser, databasePass);
        } catch (SQLException | ClassNotFoundException ex) {
            TheylorAttributes.getLogger().error("Failed to connect to database!", ex);
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException ignore) {}
    }

    public static boolean executeStatement(String sql) {
        boolean executed = false;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            executed = true;

        } catch (SQLException ignore) {}
        return executed;
    }

    public static void executeStatement(String sql, String value) {
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, value);
            stmt.execute();
        } catch (SQLException ignore) {}
    }

    public static ResultSet executeStatementAndReturn(String sql) {
        ResultSet resultSet = null;
        try {
            Statement stmt = connection.createStatement();
            resultSet = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    public static boolean columnExists (String tableName, String column) {
        return executeStatement("SELECT " + column + " FROM " + tableName);
    }

    public static void createTable(String tableName) {
        executeStatement("CREATE TABLE IF NOT EXISTS " + tableName + " (id VARCHAR(255) PRIMARY KEY)");
    }

    public static void dropTable(String tableName) {
        executeStatement("DROP TABLE IF EXISTS " + tableName);
    }

    public static void createRow(String tableName, String id, String value) {
        executeStatement("REPLACE INTO " + tableName + "(" + id + ") VALUES('" + value + "');");
    }

    public static void saveString(String tableName, String where, String name, String str) {
        executeStatement("UPDATE " + tableName + " SET " + name + " = ? WHERE id = '" + where + "'", str);
    }

    public static void saveInt(String tableName, String where, String name, int amount) {
        executeStatement("UPDATE " + tableName + " SET " + name + " = " + amount + " WHERE id = '" + where + "'");
    }

    public static void saveFloat(String tableName, String where, String name, float amount) {
        executeStatement("UPDATE " + tableName + " SET " + name + " = " + amount + " WHERE id = '" + where + "'");
    }

    public static String loadString(String tableName, String where, String name, String defaultValue) {
        ResultSet resultSet = executeStatementAndReturn("SELECT " + name + " FROM " + tableName + " WHERE id = '" + where + "'");
        String data = defaultValue;
        if (resultSet != null) {
            try {
                if(resultSet.next())
                    data = resultSet.getString(name);
            } catch (SQLException ignore) {
                ignore.printStackTrace();
            }
        }
        return data;
    }

    public static int loadInt(String tableName, String where, String name, int defaultValue) {
        ResultSet resultSet = executeStatementAndReturn("SELECT " + name + " FROM " + tableName + " WHERE id = '" + where + "'");
        int data = defaultValue;
        if (resultSet != null) {
            try {
                data = Integer.parseInt(resultSet.getString(name));
            } catch (SQLException ignore) {}
        }
        return data;
    }

    public static float loadFloat(String tableName, String where, String name, float defaultValue) {
        ResultSet resultSet = executeStatementAndReturn("SELECT " + name + " FROM " + tableName + " WHERE id = '" + where + "'");
        float data = defaultValue;
        if (resultSet != null) {
            try {
                data = Float.parseFloat(resultSet.getString(name));
            } catch (SQLException ignore) {}
        }
        return data;
    }
}
