package core.zsheshire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.*;

@Data
@Component
@Entity
@NodeEntity
@Table(name = "USER")
@JsonIgnoreProperties(value = {"enabled", "user"})
public class User {

    @Id
    @GeneratedValue
    @GraphId
    private Long id;
    @Column(name = "MOBILE_PHONE")
    @Property(name = "MOBILE_PHONE")
    private String mobilePhone;
    @Column(name = "PASSWORD")
    @Property(name = "PASSWORD")
    private String password;
    @Property(name = "NAME")
    @Column(name = "NAME")
    private String name;
    @Column(name = "SURNAME")
    private String surName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "CONTACT")
    private String contacts;
    @Column(name = "BIRTH")
    private Date dateBirth;
    @Column(name = "DEATH")
    private Date dateDeath;
    @Column(name = "REAL_USER")
    private boolean real_user;
    @Column(name = "ENABLED")
    private Boolean enabled = true;
    @Column(name = "PUBLIC_UUID")
    private String publicUUID;

    @Transient
    @Relationship
    private User user;

    @OneToMany
    @org.neo4j.ogm.annotation.Transient
    private List<MediaFile> mediaFiles;

    public User() {
    }

    public User(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String mobilePhone, String name, String surName, String address, String contacts, Date dateBirth, Date dateDeath) {
        this.id = id;
        this.mobilePhone = mobilePhone;
        this.name = name;
        this.surName = surName;
        this.address = address;
        this.contacts = contacts;
        this.dateBirth = dateBirth;
        this.dateDeath = dateDeath;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", address='" + address + '\'' +
                ", contacts='" + contacts + '\'' +
                ", dateBirth=" + dateBirth +
                ", dateDeath=" + dateDeath +
                ", user=" + user +
                '}';
    }
}
