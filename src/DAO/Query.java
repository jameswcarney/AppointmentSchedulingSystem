package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Manages queries to the database.
 *
 * @author James Carney
 */
public class Query {
    private static String query;
    private static PreparedStatement preparedStatement;

    /**
     * Creates a PreparedStatement for use in a database query.
     *
     * @param connection The connection to be used.
     * @param sqlStatement The SQL statement to be executed.
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) {

        try {
            preparedStatement = connection.prepareStatement(sqlStatement);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets a previously created PreparedStatement for use in a database query.
     *
     * @return a PreparedStatement to be used for a query.
     */
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }
}
