package de.hska.iwi.vslab.userservice.datamodels;

import javax.validation.constraints.NotEmpty;

public class NewUser {

    @NotEmpty
    private String username;

    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotEmpty
    private String password;

    @NotEmpty
    private int rolelevel;

    public NewUser() {
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRolelevel() {
        return this.rolelevel;
    }

    public void setRolelevel(int rolelevel) {
        this.rolelevel = rolelevel;
    }
}
