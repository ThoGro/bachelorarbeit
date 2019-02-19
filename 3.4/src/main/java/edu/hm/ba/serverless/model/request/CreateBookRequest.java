package edu.hm.ba.serverless.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import edu.hm.ba.serverless.model.Category;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonAutoDetect
public class CreateBookRequest {

    private String isbn;
    private String title;
    private String author;
    private Category category;
    private String lender;

}
