package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.web.validation.constraint.MatchingFields;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@MatchingFields(first = "password", second = "matchingPassword", message = "passwords do not match")
public class RegistrationContext {

    @NotEmpty(message = "must not be empty")
    @NotNull(message = "must not be null")
    private String name;

    @Min(value = 8, message = "must be 8 or greater")
    @NotNull(message = "must not be null")
    private Integer grade;

    @NotEmpty(message = "must not be empty")
    @Email(message = "incorrect format")
    private String email;

    @NotEmpty(message = "must not be empty")
    @NotNull(message = "must not be null")
    private String password;

    @NotEmpty(message = "must not be empty")
    @NotNull(message = "must not be null")
    @JsonProperty("matching_password")
    private String matchingPassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }

}
