package net.edt.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.web.validation.constraint.DateTimeFormat;
import net.edt.web.validation.constraint.DateTimeType;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import static net.edt.web.validation.constraint.PatternConstants.UUID_REGEXPR_CI;

public class SignInRequestDto {

    @NotNull(message = "must not be null")
    private String id;

    @Pattern(regexp = UUID_REGEXPR_CI,
             flags = {Pattern.Flag.CASE_INSENSITIVE})
    @NotNull(message = "must not be null")
    @JsonProperty(value = "user_id")
    private String userId;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    @NotNull(message = "must not be null")
    private String time;

    @NotNull(message = "must not be null")
    @JsonProperty(value = "device_info")
    private String deviceInfo;

    @Pattern(regexp = UUID_REGEXPR_CI,
             flags = {Pattern.Flag.CASE_INSENSITIVE})
    @NotNull(message = "must not be null")
    @JsonProperty(value = "session_id")
    private String sessionId;

    private boolean success;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
