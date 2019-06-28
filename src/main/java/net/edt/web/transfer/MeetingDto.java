package net.edt.web.transfer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class MeetingDto {

    private Long id;

    @NotNull(message = "Date required")
    private String date;

    @JsonIgnoreProperties({"meetings"})
    private Set<UserDto> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

}
