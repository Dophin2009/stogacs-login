package net.edt.persistence.domain;

import net.edt.persistence.generator.AlphanumericGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SignInSession {

    public static final int ID_LENGTH = 35;

    @Id
    @GeneratedValue(generator = "alphanumeric")
    @GenericGenerator(name = "alphanumeric", strategy = "net.edt.persistence.generator.AlphanumericGenerator",
                      parameters = {@Parameter(name = "length", value = "" + ID_LENGTH),
                                    @Parameter(name = "symbols", value = AlphanumericGenerator.ALPHANUM)})
    @Column(name = "id", length = ID_LENGTH, updatable = false, nullable = false)
    private String id;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @OneToMany(mappedBy = "session")
    private Set<SignInRequest> signInRequests = new HashSet<>();

    public SignInSession() { }

    public SignInSession(Meeting meeting, LocalDateTime startTime, LocalDateTime endTime) {
        this.meeting = meeting;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Set<SignInRequest> getSignInRequests() {
        return signInRequests;
    }

    public void setSignInRequests(Set<SignInRequest> signInRequests) {
        this.signInRequests = signInRequests;
    }

}
