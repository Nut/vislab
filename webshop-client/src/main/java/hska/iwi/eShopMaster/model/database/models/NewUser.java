package hska.iwi.eShopMaster.model.database.models;

public class NewUser {

    private String username;
    private String firstname;
    private String lastname;
    private String password;
    private int rolelevel;

    public NewUser(String username, String firstname, String lastname, String password, int rolelevel) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.rolelevel = rolelevel;
    }

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
