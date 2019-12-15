package de.hska.iwi.vslab.userservice.datamodels;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class contains details about roles.
 */

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    private String typ;

    @Column(name = "level")
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
