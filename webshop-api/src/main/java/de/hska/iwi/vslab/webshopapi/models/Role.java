package de.hska.iwi.vslab.webshopapi.models;

/**
 * This class contains details about roles.
 */

public class Role {
    private Long id;
    private String typ;
    private int level;

    public Role() {
    }

    public Role(String typ, int level) {
        this.typ = typ;
        this.level = level;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTyp() {
        return this.typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
