package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Handles database queries related to users.
 *
 * @author James Carney
 */
public class UserDAO {

    /**
     * Creates a new user
     *
     * @param user The user to be created.
     * @return boolean representing success/failure of the create operation.
     * @throws SQLException An exception related to the database query.
     */
    public boolean createUser(User user) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "INSERT INTO users(userId, userName, password) VALUES(?, ?, ?)";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setInt(1, user.getUserId());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getPassword());

        if (ps.getUpdateCount() > 0) {
            System.out.println(ps.getUpdateCount() + " row(s) affected.");
            DBConnection.closeConnection();
            return true;
        }
        else {
            System.out.println("No change.");
            DBConnection.closeConnection();
            return false;
        }
    }

    /**
     * Gets a user from the database.
     *
     * @param userName Name of the requested user.
     * @return A User object representing the requested user.
     * @throws Exception A generic exception.
     */
    public static User getUser(String userName) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "SELECT * FROM users WHERE User_Name = ?";

        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setString(1, userName);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String usrName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User user = new User(userId, usrName, password);
            DBConnection.closeConnection();
            return user;
        }
        DBConnection.closeConnection();
        return null;
    }

    /**
     * Gets a list of all users in the database.
     *
     * @return A list of users.
     * @throws Exception a generic exception.
     */
    public static ObservableList<User> getAllUsers() throws Exception {

        Connection conn = DBConnection.getConnection();
        String sqlStatement = "SELECT * FROM users";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ResultSet rs = ps.getResultSet();
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User user = new User(userId, userName, password);
            allUsers.add(user);
        }
        DBConnection.closeConnection();
        return allUsers;
    }

    /**
     * Updates a user in the database.
     *
     * @param user The user to be updated.
     * @return boolean representing success/failure of the update operation.
     * @throws Exception A generic exception.
     */
    public static boolean updateUser(User user) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "UPDATE users SET User_ID = ?, User_Name = ?, Password = ? WHERE User_ID = ?";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setInt(1, user.getUserId());
        ps.setString(2, user.getUserName());
        ps.setString(3, user.getPassword());

        return ps.getUpdateCount() > 0;
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId The ID of the user to be deleted.
     * @return boolean representing success/failure of the delete operation.
     * @throws Exception A generic exception.
     */
    public static boolean deleteUser(int userId) throws Exception {
        Connection conn = DBConnection.getConnection();
        String sqlStatement = "DELETE * FROM users WHERE User_ID = ?";
        Query.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = Query.getPreparedStatement();

        ps.setInt(1, userId);

        return ps.getUpdateCount() >0;
    }
}

