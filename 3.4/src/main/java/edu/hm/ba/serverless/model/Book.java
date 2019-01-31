package edu.hm.ba.serverless.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private String isbn;
    private String title;
    private String author;

}
