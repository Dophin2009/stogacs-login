package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.web.validation.constraint.DateTimeFormat;
import net.edt.web.validation.constraint.DateTimeType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static net.edt.web.validation.constraint.PatternConstants.UUID_REGEXPR_CI;

public class SignInSessionDto {

    @NotNull(message = "must not be null")
    private String id;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    @JsonProperty(value = "start_time")
    private String startTime;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    @JsonProperty(value = "end_time")
    private String endTime;

    @NotNull(message = "must not be null")
    @JsonProperty(value = "meeting_id")
    private Long meetingId;

    @JsonProperty(value = "signin_requests")
    private Set<@Pattern(regexp = UUID_REGEXPR_CI,
                         flags = {Pattern.Flag.CASE_INSENSITIVE}) String>
            signInRequestIds = new HashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public Set<String> getSignInRequestIds() {
        return signInRequestIds;
    }

    public void setSignInRequestIds(Set<String> signInRequestIds) {
        this.signInRequestIds = signInRequestIds;
    }

}
