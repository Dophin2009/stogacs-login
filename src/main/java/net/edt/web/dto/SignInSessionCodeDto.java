package net.edt.web.dto;

import net.edt.persistence.domain.SignInSessionCode;
import net.edt.web.validation.constraint.EmptyOrSize;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class SignInSessionCodeDto {

    @EmptyOrSize(min = SignInSessionCode.ID_LENGTH, max = SignInSessionCode.ID_LENGTH)
    private String code;

//    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    private Long startTime;

//    @DateTimeFormat(format = "yyyy-MM-dd'T'HH:mm:ss", type = DateTimeType.DATE_TIME)
    private Long endTime;

    public long getEndsIn() {
        long now = LocalDateTime.now(ZoneId.ofOffset("UTC", ZoneOffset.UTC)).toEpochSecond(ZoneOffset.UTC);
        return endTime - now;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

}
