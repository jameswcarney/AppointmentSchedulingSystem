package model;

import javafx.beans.value.ObservableValue;

/**
 * A class representing a customer.
 *
 * @author James Carney
 */
public class Customer {
    private ObservableValue<Integer> customerId;
    private ObservableValue<String> customerName;
    private ObservableValue<String> address;
    private ObservableValue<String> postalCode;
    private ObservableValue<String> phone;
    private ObservableValue<Integer> divisionId;
    private ObservableValue<Integer> countryId;

    /**
     * Customer constructor with customerId. Used when editing a customer that already exists in the database.
     *
     * @param customerId The customer's ID number.
     * @param customerName The customer's name.
     * @param address The customer's address.
     * @param postalCode The customer's postal code.
     * @param phone The customer's phone number.
     * @param divisionId The ID of the customer's first-level division.
     */
    public Customer(ObservableValue<Integer> customerId, ObservableValue<String> customerName, ObservableValue<String> address,
                    ObservableValue<String> postalCode, ObservableValue<String> phone, ObservableValue<Integer> divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * Customer constructor without customerId. Used when adding a new customer to the database.
     *
     * @param customerName The customer's name.
     * @param address The customer's address.
     * @param postalCode The customer's postal code.
     * @param phone The customer's phone number.
     * @param divisionId The ID of the customer's first-level division.
     */
    public Customer(ObservableValue<String> customerName, ObservableValue<String> address, ObservableValue<String> postalCode,
                    ObservableValue<String> phone, ObservableValue<Integer> divisionId) {
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * Gets the customer's ID number.
     *
     * @return The ID number.
     */
    public ObservableValue<Integer> getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer's ID number.
     *
     * @param customerId The ID number.
     */
    public void setCustomerId(ObservableValue<Integer> customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the customer's name.
     *
     * @return The name.
     */
    public ObservableValue<String> getCustomerName() {
        return customerName;
    }

    /**
     * Sets the customer's name.
     *
     * @param customerName The name.
     */
    public void setCustomerName(ObservableValue<String> customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets the customer's address.
     *
     * @return The address.
     */
    public ObservableValue<String> getAddress() {
        return address;
    }

    /**
     * Sets the customer's address.
     *
     * @param address The address.
     */
    public void setAddress(ObservableValue<String> address) {
        this.address = address;
    }

    /**
     * Gets the customer's postal code.
     *
     * @return The postal code.
     */
    public ObservableValue<String> getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the customer's postal code.
     *
     * @param postalCode The postal code.
     */
    public void setPostalCode(ObservableValue<String> postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets the customer's phone number.
     *
     * @return The phone number.
     */
    public ObservableValue<String> getPhone() {
        return phone;
    }

    /**
     * Sets the customer's phone number.
     *
     * @param phone The phone number.
     */
    public void setPhone(ObservableValue<String> phone) {
        this.phone = phone;
    }

    /**
     * Gets the customer's first-level division ID.
     *
     * @return The FLD ID.
     */
    public ObservableValue<Integer> getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the customer's first-level division ID.
     *
     * @param divisionId The FLD ID.
     */
    public void setDivisionId(ObservableValue<Integer> divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * Gets the ID number of the customer's country.
     *
     * @return The country ID number.
     */
    public ObservableValue<Integer> getCountryId() {
        return countryId;
    }

    /**
     * Sets the customer's country by ID number.
     *
     * @param countryId The country ID number.
     */
    public void setCountryId(ObservableValue<Integer> countryId) {
        this.countryId = countryId;
    }

    /**
     * A representation of the customer as a string containing the customer's name.
     *
     * @return The customer's name.
     */
    @Override
    public String toString() {
        return (String.valueOf(customerName.getValue()));
    }
}
