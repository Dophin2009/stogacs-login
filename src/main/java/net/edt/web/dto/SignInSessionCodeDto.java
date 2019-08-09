package net.edt.web.dto;

import net.edt.persistence.domain.SignInSessionCode;
import net.edt.web.validation.constraint.DateTimeFormat;
import net.edt.web.validation.constraint.DateTimeType;
import net.edt.web.validation.constraint.EmptyOrSize;

import java.time.LocalDateTime;

public class SignInSessionCodeDto {

    @EmptyOrSize(min = SignInSessionCode.ID_LENGTH, max = SignInSessionCode.ID_LENGTH)
    private String code;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    private LocalDateTime startTime;

    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    private LocalDateTime endTime;

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
