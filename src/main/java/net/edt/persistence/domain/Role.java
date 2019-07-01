package net.edt.persistence.domain;

public enum Role {

    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
