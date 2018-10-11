package edu.hm.ba.klassisch.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class User implements Serializable{

    @GeneratedValue
    @Id
    @Column(name = "UserID")
    private int id;
    private String username;
    private String password;

}
