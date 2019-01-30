package edu.hm.ba.serverless.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private String isbn;
    private String title;
    private String author;

}
