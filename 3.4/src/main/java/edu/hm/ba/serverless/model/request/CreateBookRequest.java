package edu.hm.ba.serverless.model.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import edu.hm.ba.serverless.model.Category;
import lombok.*;

/**
 * Object for the creation of a book.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonAutoDetect
public class CreateBookRequest {

    /**
     * ISBN number of the book.
     */
    private String isbn;

    /**
     * Title of the book.
     */
    private String title;

    /**
     * Author of the book.
     */
    private String author;

    /**
     * Category of the book.
     */
    private Category category;

    /**
     * The User who lends the book. "null" if the book is available.
     */
    private String lender;

}
