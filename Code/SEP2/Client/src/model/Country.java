package model;

public class Country {
    private String name;
    private String dialCode;
    private String code;
    public Country(String name, String dialCode, String code) {
        this.name = name;
        this.dialCode = dialCode;
        this.code = code;

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getDialCode() {
        return dialCode;
    }


}
