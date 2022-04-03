package model;

/**
 * A class representing a country.
 *
 * @author James Carney
 */
public class Country {
    private int countryId;
    private String countryName;

    /**
     * The class constructor.
     *
     * @param countryId The ID number of the country.
     * @param countryName The name of the country.
     */
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /**
     * Gets the country's ID.
     *
     * @return The ID.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country's ID.
     *
     * @param countryId The ID.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the name of the country.
     *
     * @return The name.
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Sets the name of the country.
     *
     * @param countryName The name.
     */
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /**
     * A representation of the country as a string containing the country's name.
     *
     * @return The country's name.
     */
    @Override
    public String toString() {
        return (countryId + " - " + countryName);
    }
}
