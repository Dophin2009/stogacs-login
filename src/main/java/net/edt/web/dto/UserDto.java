package net.edt.web.dto;

import net.edt.persistence.domain.SignInRequest;
import net.edt.web.validation.constraint.EmptyOrSize;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

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

    private Set<@EmptyOrSize(min = SignInRequest.ID_LENGTH, max = SignInRequest.ID_LENGTH) String>
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
