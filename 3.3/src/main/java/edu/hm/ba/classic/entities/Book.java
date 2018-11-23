package edu.hm.ba.classic.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Book {

    @Id
    private String isbn;

    @NotNull
    private String title;

    @NotNull
    private String author;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    @ManyToOne
    private User lender;

    public Book() {}

}
