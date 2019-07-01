package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static net.edt.web.validation.constraint.PatternConstants.UUID_REGEXPR_CI;

public class UserDto {

    @NotNull(message = "must not be null")
    private String id;

    @NotNull(message = "must not be null")
    private String name;

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

    public Set<String> getSignInRequestIds() {
        return signInRequestIds;
    }

    public void setSignInRequestIds(Set<String> signInRequestIds) {
        this.signInRequestIds = signInRequestIds;
    }

}
