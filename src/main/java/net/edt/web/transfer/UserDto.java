package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UserDto {

    private String id;

    @NotNull(message = "Name required")
    private String name;

    @JsonIgnoreProperties({"users"})
    private Set<MeetingDto> meetings = new HashSet<>();

    public UUID getId() {
        return id == null ? null : UUID.fromString(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MeetingDto> getMeetings() {
        return meetings;
    }

    public void setMeetings(Set<MeetingDto> meetings) {
        this.meetings = meetings;
    }

}
