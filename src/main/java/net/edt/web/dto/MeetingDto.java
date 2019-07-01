package net.edt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.web.validation.constraint.DateTimeFormat;
import net.edt.web.validation.constraint.DateTimeType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static net.edt.web.validation.constraint.PatternConstants.UUID_REGEXPR_CI;

public class MeetingDto {

    @NotNull(message = "must not be null")
    private Long id;

    @NotNull(message = "must not be null")
    @DateTimeFormat(type = DateTimeType.DATE, format = "yyyy-MM-dd")
    private String date;

    @JsonProperty(value = "signin_session_ids")
    private Set<@Pattern(regexp = UUID_REGEXPR_CI,
                         flags = {Pattern.Flag.CASE_INSENSITIVE}) String>
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

    public Set<String> getSignInSessionIds() {
        return signInSessionIds;
    }

    public void setSignInSessionIds(Set<String> signInSessionIds) {
        this.signInSessionIds = signInSessionIds;
    }

}
