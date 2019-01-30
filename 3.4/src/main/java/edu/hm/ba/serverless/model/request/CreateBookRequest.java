package edu.hm.ba.serverless.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonAutoDetect
public class CreateBookRequest {

    private String isbn;
    private String title;
    private String author;

}
