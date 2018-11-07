package edu.hm.ba.klassisch.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class User implements Serializable{

    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String username;
    private String password;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH})
    private List<Lending> lendings;

    public User() {
    }

    public User(String username, String password, List<Lending> lendings) {

        this.username = username;
        this.password = password;
        this.lendings = lendings;
    }

}
