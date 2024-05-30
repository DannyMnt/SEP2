package model;

/**
 * The Country class represents a country entity with its name, dialing code, and country code.
 */
public class Country {
    private String name;
    private String dialCode;
    private String code;

    /**
     * Constructs a Country object with the specified name, dialing code, and country code.
     * @param name The name of the country.
     * @param dialCode The dialing code of the country.
     * @param code The country code.
     */
    public Country(String name, String dialCode, String code) {
        this.name = name;
        this.dialCode = dialCode;
        this.code = code;

    }
    /**
     * Gets the name of the country.
     * @return The name of the country.
     */
    public String getName() {
        return name;
    }


    /**
     * Returns a string representation of the country (its name).
     * @return The name of the country.
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Gets the dialing code of the country.
     * @return The dialing code of the country.
     */
    public String getDialCode() {
        return dialCode;
    }


}
