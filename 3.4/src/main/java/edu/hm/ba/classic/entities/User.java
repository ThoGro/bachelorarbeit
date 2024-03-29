package edu.hm.ba.classic.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a user in the library service.
 * @author Thoams Großbeck
 */
@Data
@ToString(exclude = "lendings")
@Entity
public class User {

    /**
     * ID of the user.
     */
    @Id
    @Column(name = "UserID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Username of the user.
     */
    @NotNull
    @Column(unique=true)
    private String username;

    /**
     * Password of the user.
     */
    @NotNull
    private String password;

    /**
     * Role of the user.
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    /**
     * List with all lent books from the user.
     */
    @OneToMany(mappedBy = "lender", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<Book> lendings;

    /**
     * Default constructor.
     */
    public User() {}

    /**
     * Constructs a user with id, username, password and role.
     * @param id the id of the user
     * @param username the username of the user
     * @param password the password of the user
     * @param role the role of the user
     */
    public User(int id, String username, String password, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
