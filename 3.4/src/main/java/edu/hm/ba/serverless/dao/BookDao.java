package edu.hm.ba.serverless.dao;

import edu.hm.ba.serverless.exception.CouldNotCreateBookException;
import edu.hm.ba.serverless.exception.TableDoesNotExistException;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BookDao {

    private static final String BOOK_ID = "isbn";

    private final String tableName;
    private final DynamoDbClient dynamoDb;

    public BookDao (final DynamoDbClient dynamoDb, final String tableName) {
        this.dynamoDb = dynamoDb;
        this.tableName = tableName;
    }

    public Book getBook(final String isbn) {
        try {
            return Optional.ofNullable(
                    dynamoDb.getItem(GetItemRequest.builder()
                            .tableName(tableName)
                            .key(Collections.singletonMap(BOOK_ID,
                                    AttributeValue.builder().s(isbn).build()))
                            .build()))
                    .map(GetItemResponse::item)
                    .map(this::convert)
                    .orElse(null);
        } catch (ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Book table " + tableName + " does not exist.");
        }
    }

    private Book convert(final Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) {
            return null;
        }
        Book.BookBuilder builder = Book.builder();

        try {
            builder.isbn(item.get(BOOK_ID).s());
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have an isbn attribute or it was not a String");
        }
        try {
            builder.title(item.get("title").s());
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have a title attribute or it was not a String");
        }
        try {
            builder.author(item.get("author").s());
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have an author attribute or it was not a String");
        }
        return builder.build();
    }

    private Map<String, AttributeValue> createBookItem(final CreateBookRequest book) {
        Map<String, AttributeValue> item = new HashMap<>();
        try {
            item.put(BOOK_ID, AttributeValue.builder().s(book.getIsbn()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("isbn was null");
        }
        try {
            item.put("title", AttributeValue.builder().s(book.getTitle()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("title was null");
        }
        try {
            item.put("author", AttributeValue.builder().s(book.getAuthor()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("author was null");
        }
        return item;
    }

    public Book createBook(final CreateBookRequest createBookRequest) {
        if (createBookRequest == null) {
            throw new IllegalArgumentException("CreateBookRequest was null");
        }
        int tries = 0;
        while (tries < 10) {
            try {
                Map<String, AttributeValue> item = createBookItem(createBookRequest);
                dynamoDb.putItem(PutItemRequest.builder()
                        .tableName(tableName)
                        .item(item)
                        .conditionExpression("attribute_not_exists(isbn)")
                        .build());
                return Book.builder()
                        .isbn(item.get(BOOK_ID).s())
                        .title(item.get("title").s())
                        .author(item.get("author").s())
                        .build();
            } catch (ConditionalCheckFailedException e) {
                tries++;
            } catch (ResourceNotFoundException e) {
                throw new TableDoesNotExistException("Book table " + tableName + " does not exist.");
            }
        }
        throw new CouldNotCreateBookException("Unable to generate unique book id after 10 tries");
    }

}