package model;

/**
 * A class representing a user.
 *
 * @author James Carney
 */
public class User {

    private int userId;
    private String userName;
    private String password;

    /**
     * Class constructor with password. Used by the login system.
     *
     * @param userId The user's ID number.
     * @param userName The user's name.
     * @param password The user's password.
     */
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Class constructor without password. Used everywhere except during login.
     *
     * @param userId The user's ID number.
     * @param userName The user's name.
     */
    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    /**
     * Gets the user's ID number.
     *
     * @return The ID number.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the user's ID number.
     *
     * @param userId The ID number.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's name.
     *
     * @return The name.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's name.
     *
     * @param userName The name.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Gets the user's password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password.
     *
     * @param password The password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * A string representation of the user by user name.
     *
     * @return The user's name as a string.
     */
    @Override
    public String toString() {
        return (userName);
    }
}
