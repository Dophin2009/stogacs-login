package net.edt.persistence.domain;

import net.edt.persistence.generator.AlphanumericGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class SignInRequest {

    public static final int ID_LENGTH = 35;

    @Id
    @GeneratedValue(generator = "alphanumeric")
    @GenericGenerator(name = "alphanumeric", strategy = "net.edt.persistence.generator.AlphanumericGenerator",
                      parameters = {@Parameter(name = "length", value = "" + ID_LENGTH),
                                    @Parameter(name = "symbols", value = AlphanumericGenerator.ALPHANUM)})
    @Column(name = "id", length = ID_LENGTH, updatable = false, nullable = false)
    private String id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "device_info")
    private String deviceInfo;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private SignInSession session;

    @Column(name = "success")
    private boolean success;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public SignInSession getSession() {
        return session;
    }

    public void setSession(SignInSession session) {
        this.session = session;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
