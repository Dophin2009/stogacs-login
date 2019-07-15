package net.edt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.persistence.domain.SignInRequest;
import net.edt.persistence.domain.SignInSession;
import net.edt.web.validation.constraint.DateTimeFormat;
import net.edt.web.validation.constraint.DateTimeType;
import net.edt.web.validation.constraint.EmptyOrSize;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class SignInSessionDto {

    @EmptyOrSize(min = SignInSession.ID_LENGTH, max = SignInSession.ID_LENGTH)
    @NotNull(message = "must not be null")
    private String id;

    @NotNull(message = "must not be null")
    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    @JsonProperty(value = "start_time")
    private String startTime;

    @NotNull(message = "must not be null")
    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    @JsonProperty(value = "end_time")
    private String endTime;

    @NotNull(message = "must not be null")
    @JsonProperty(value = "meeting_id")
    private Long meetingId;

    @JsonProperty(value = "session_codes")
    private Set<SignInSessionCodeDto> sessionCodes;

    @JsonProperty(value = "code_refresh")
    private int codeRefresh = 60;

    @JsonProperty(value = "signin_requests")
    private Set<@EmptyOrSize(min = SignInRequest.ID_LENGTH, max = SignInRequest.ID_LENGTH) String>
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

    public Set<SignInSessionCodeDto> getSessionCodes() {
        return sessionCodes;
    }

    public void setSessionCodes(Set<SignInSessionCodeDto> sessionCodes) {
        this.sessionCodes = sessionCodes;
    }

    public int getCodeRefresh() {
        return codeRefresh;
    }

    public void setCodeRefresh(int codeRefresh) {
        this.codeRefresh = codeRefresh;
    }

    public Set<String> getSignInRequestIds() {
        return signInRequestIds;
    }

    public void setSignInRequestIds(Set<String> signInRequestIds) {
        this.signInRequestIds = signInRequestIds;
    }

}
