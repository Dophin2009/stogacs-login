package net.edt.persistence.domain;

import net.edt.persistence.generator.AlphanumericGenerator;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class SignInSessionCode {

    public static final int ID_LENGTH = 10;

    @Id
    @GeneratedValue(generator = "alphanumeric")
    @GenericGenerator(name = "alphanumeric", strategy = "net.edt.persistence.generator.AlphanumericGenerator",
                      parameters = {@Parameter(name = "length", value = "" + ID_LENGTH),
                                    @Parameter(name = "symbols", value = AlphanumericGenerator.ALPHANUM)})
    private String code;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    public SignInSessionCode() {}

    public SignInSessionCode(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

}
