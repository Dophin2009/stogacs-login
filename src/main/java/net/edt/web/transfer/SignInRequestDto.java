package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.edt.web.validation.constraint.DateTimeFormat;

import javax.validation.constraints.NotNull;

public class SignInRequestDto {

    @NotNull(message = "must not be null")
    private String id;

    @NotNull(message = "must not be null")
    @JsonIgnoreProperties(value = "meetings")
    private UserDto user;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss")
    @NotNull(message = "must not be null")
    private String time;

    @NotNull(message = "must not be null")
    @JsonProperty(value = "device_info")
    private String deviceInfo;

    @NotNull(message = "must not be null")
    @JsonIgnoreProperties(value = {"signin_requests"})
    private SignInSessionDto session;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
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

    public SignInSessionDto getSession() {
        return session;
    }

    public void setSession(SignInSessionDto session) {
        this.session = session;
    }

}
