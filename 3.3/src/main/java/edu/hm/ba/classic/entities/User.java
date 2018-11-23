package edu.hm.ba.classic.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class User {

    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(unique=true)
    private String username;

    @NotNull
    private String password;

    @OneToMany(mappedBy = "lender")
    @JsonBackReference
    private List<Book> lendings;

    public User() {}

}
