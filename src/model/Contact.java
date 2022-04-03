package model;

/**
 * A class representing a contact.
 *
 * @author James Carney
 */
public class Contact {
    private int contactId;
    private String contactName;
    private String email;

    /**
     * Class constructor.
     *
     * @param contactId The ID of the contact.
     * @param contactName The name of the contact.
     * @param email The e-mail address of the contact.
     */
    public Contact(int contactId, String contactName, String email) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.email = email;
    }

    /**
     * Gets the contact's ID.
     *
     * @return The ID.
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * Sets the contact's ID.
     *
     * @param contactId The ID.
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * Gets the contact's name.
     *
     * @return The name.
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets the contact's name.
     *
     * @param contactName The name.
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets the contact's e-mail address.
     *
     * @return The e-mail address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the contact's e-mail address.
     *
     * @param email The e-mail address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * A representation of the contact as a string containing the contact's name.
     *
     * @return The contact's name.
     */
    @Override
    public String toString() {
        return (contactName);
    }
}
