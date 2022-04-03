package model;

/**
 * A class representing a first-level division.
 *
 * @author James Carney
 */
public class FirstLevelDivision {
    private int countryId;
    private String divisionName;
    private int divisionId;

    /**
     * Class constructor.
     *
     * @param divisionId The ID of the FLD.
     * @param divisionName The name of the FLD.
     * @param countryId The country in which the FLD exists.
     */
    public FirstLevelDivision(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }

    /**
     * Gets the country ID of the FLD.
     *
     * @return The country ID number.
     */
    public int getCountryId() {
        return countryId;
    }

    /**
     * Sets the country of the FLD by ID.
     *
     * @param countryId The country ID number.
     */
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    /**
     * Gets the FLD name.
     *
     * @return The name.
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets the FLD name.
     *
     * @param divisionName The name.
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    /**
     * Gets the ID number of the FLD.
     *
     * @return The ID number.
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * Sets the ID number of the FLD.
     *
     * @param divisionId The ID number.
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * A string representation of the FLD containing the FLD ID number and FLD name.
     *
     * @return String of the format "divisionID - divisionName".
     */
    @Override
    public String toString() {
        return (divisionId + " - " + divisionName);
    }

}
