package net.edt.persistence.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "val", nullable = false)
    private Integer val;

    @OneToMany(mappedBy = "meeting")
    private Set<SignInSession> signInSessions = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }

    public Set<SignInSession> getSignInSessions() {
        return signInSessions;
    }

    public void setSignInSessions(Set<SignInSession> signInSessions) {
        this.signInSessions = signInSessions;
    }

}
