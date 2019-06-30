package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.web.validation.constraint.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class SignInSessionDto {

    @NotNull(message = "must not be null")
    private String id;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(value = "start_time")
    private String startTime;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonProperty(value = "end_time")
    private String endTime;

    @JsonIgnoreProperties(value = {"users"})
    @NotNull(message = "must not be null")
    private MeetingDto meeting;

    @JsonProperty(value = "signin_requests")
    private Set<SignInRequestDto> signInRequests = new HashSet<>();

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

    public MeetingDto getMeeting() {
        return meeting;
    }

    public void setMeeting(MeetingDto meeting) {
        this.meeting = meeting;
    }

    public Set<SignInRequestDto> getSignInRequests() {
        return signInRequests;
    }

    public void setSignInRequests(Set<SignInRequestDto> signInRequests) {
        this.signInRequests = signInRequests;
    }

}
