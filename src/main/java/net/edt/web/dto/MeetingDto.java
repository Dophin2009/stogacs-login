package net.edt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.persistence.domain.SignInSession;
import net.edt.web.validation.constraint.DateTimeFormat;
import net.edt.web.validation.constraint.DateTimeType;
import net.edt.web.validation.constraint.EmptyOrSize;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class MeetingDto {

    @NotNull(message = "must not be null")
    private Long id;

    @NotNull(message = "must not be null")
    @DateTimeFormat(type = DateTimeType.DATE, format = "yyyy-MM-dd")
    private String date;

    @NotNull(message = "must not be null")
    private Integer val = 1;

    @JsonProperty(value = "signin_session_ids")
    private Set<@EmptyOrSize(min = SignInSession.ID_LENGTH, max = SignInSession.ID_LENGTH) String>
            signInSessionIds = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public Set<String> getSignInSessionIds() {
        return signInSessionIds;
    }

    public void setSignInSessionIds(Set<String> signInSessionIds) {
        this.signInSessionIds = signInSessionIds;
    }

}
