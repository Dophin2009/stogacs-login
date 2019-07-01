package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

import static net.edt.web.validation.constraint.PatternConstants.UUID_REGEXPR_CI;

public class UserDto {

    @NotEmpty(message = "must not be empty")
    @NotNull(message = "must not be null")
    private String id;

    @NotEmpty(message = "must not be empty")
    @NotNull(message = "must not be null")
    private String name;

    @Min(value = 8, message = "must be 8 or greater")
    @NotNull(message = "must not be null")
    private Integer grade;

    @NotEmpty(message = "must not be empty")
    @Email(message = "incorrect format")
    private String email;

    @JsonProperty(value = "signin_request_ids")
    private Set<@Pattern(regexp = UUID_REGEXPR_CI,
                         flags = {Pattern.Flag.CASE_INSENSITIVE}) String>
            signInRequestIds = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Set<String> getSignInRequestIds() {
        return signInRequestIds;
    }

    public void setSignInRequestIds(Set<String> signInRequestIds) {
        this.signInRequestIds = signInRequestIds;
    }

}
