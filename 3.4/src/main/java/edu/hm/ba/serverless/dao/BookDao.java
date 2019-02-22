package edu.hm.ba.serverless.dao;

import edu.hm.ba.serverless.exception.*;
import edu.hm.ba.serverless.model.Book;
import edu.hm.ba.serverless.model.Category;
import edu.hm.ba.serverless.model.request.CreateBookRequest;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents all functionalities with database access to the book table.
 */
public class BookDao {

    /**
     * Name of the key in the book table.
     */
    private static final String BOOK_ID = "isbn";

    /**
     * Update expressions for updating a book.
     */
    private static final String UPDATE_EXPRESSION = "SET title = :t, author = :a, category = :c, lender = :l";

    /**
     * Length of an isbn code
     */
    private static final int ISBN_LENGTH = 13;

    /**
     * Table name for the database access.
     */
    private final String tableName;

    /**
     * Database client to manage the access.
     */
    private final DynamoDbClient dynamoDb;

    /**
     * Constructs a BookDao with database client and table name.
     * @param dynamoDb the DynamoDbClient
     * @param tableName the table name
     */
    public BookDao (final DynamoDbClient dynamoDb, final String tableName) {
        this.dynamoDb = dynamoDb;
        this.tableName = tableName;
    }

    /**
     * Returns the book for the specified isbn.
     * @param isbn the isbn for which the bock will be returned
     * @return the book for the specified isbn
     */
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

    /**
     * Returns all books.
     * @return list with all books
     */
    public List<Book> getBooks() {
        final ScanResponse result;
        try {
            ScanRequest.Builder scanBuilder = ScanRequest.builder()
                    .tableName(tableName);
            result = dynamoDb.scan(scanBuilder.build());
        } catch (ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Book table " + tableName + " does not exist.");
        }
        final List<Book> books = result.items().stream()
                .map(this::convert)
                .collect(Collectors.toList());
        return books;
    }

    /**
     * Creates a book in the table.
     * @param createBookRequest the book to create
     * @return the created book
     */
    public Book createBook(final CreateBookRequest createBookRequest) {
        if (!checkIsbn(createBookRequest.getIsbn()) || createBookRequest.getTitle().isEmpty() || createBookRequest.getAuthor().isEmpty()) {
            throw new CouldNotCreateBookException("Unable to add book with isbn " + createBookRequest.getIsbn() +
                    " title " + createBookRequest.getTitle() + " and author " + createBookRequest.getAuthor());
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
                        .category(Category.valueOf(item.get("category").s()))
                        .lender(item.get("lender").s())
                        .build();
            } catch (ConditionalCheckFailedException e) {
                tries++;
            } catch (ResourceNotFoundException e) {
                throw new TableDoesNotExistException("Book table " + tableName + " does not exist.");
            }
        }
        throw new CouldNotCreateBookException("Unable to generate unique book id after 10 tries");
    }

    /**
     * Deletes a book from the table.
     * @param isbn the isbn of the book to delete
     * @return the deleted book
     */
    public Book deleteBook(final String isbn) {
        try {
            return Optional.ofNullable(dynamoDb.deleteItem(DeleteItemRequest.builder()
                        .tableName(tableName)
                        .key(Collections.singletonMap(BOOK_ID,
                                AttributeValue.builder().s(isbn).build()))
                        .conditionExpression("attribute_exists(isbn)")
                        .returnValues(ReturnValue.ALL_OLD)
                        .build()))
                    .map(DeleteItemResponse::attributes)
                    .map(this::convert)
                    .orElseThrow(() -> new IllegalStateException("Condition passed but deleted item was null"));
        } catch (ConditionalCheckFailedException e) {
            throw new UnableToDeleteException(
                    "A competing request changed the book while processing this request");
        } catch (ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Book table " + tableName
                    + " does not exist and was deleted after reading the book");
        }
    }

    /**
     * Updates a book in the table.
     * @param isbn the isbn of the book to update
     * @param book the book object with the new book informations
     * @return the updated book
     */
    public Book updateBook(final String isbn, final Book book) {
        if (book.getTitle().isEmpty() || book.getAuthor().isEmpty() || book.getIsbn().isEmpty()) {
            throw new CouldNotUpdateBookException("Unable to update book with isbn " + isbn +
                    " and specified title " + book.getTitle() + " and author " + book.getAuthor());
        }
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":t", AttributeValue.builder().s(book.getTitle()).build());
        expressionAttributeValues.put(":a", AttributeValue.builder().s(book.getAuthor()).build());
        expressionAttributeValues.put(":c", AttributeValue.builder().s(book.getCategory().toString()).build());
        expressionAttributeValues.put(":l", AttributeValue.builder().s(book.getLender()).build());
        final UpdateItemResponse result;
        try {
            result = dynamoDb.updateItem(UpdateItemRequest.builder()
                    .tableName(tableName)
                    .key(Collections.singletonMap(BOOK_ID,
                            AttributeValue.builder().s(book.getIsbn()).build()))
                    .returnValues(ReturnValue.ALL_NEW)
                    .updateExpression(UPDATE_EXPRESSION)
                    .conditionExpression("attribute_exists(isbn)")
                    .expressionAttributeValues(expressionAttributeValues)
                    .build());
        } catch (ConditionalCheckFailedException e) {
            throw new UnableToUpdateException(
                    "the book does not exist");
        } catch (ResourceNotFoundException e) {
            throw new TableDoesNotExistException("Book table " + tableName
                    + " does not exist and was deleted after reading the book");
        }
        return convert(result.attributes());
    }

    /**
     * Marks a book as lent. After this the lender of the book is set.
     * @param isbn the isbn of the lent book
     * @return the username of the lender
     */
    public String lendBook(String isbn) {
        Book toLend = getBook(isbn);
        if (toLend != null) {
            String lender = "TestUser";
            toLend.setLender(lender);
            updateBook(isbn, toLend);
            return lender;
        } else {
            throw new CouldNotLendBookException("Book " + isbn + " could not be lent.");
        }
    }

    /**
     * Unmarks a lent book. After this the book is available and the lender of the book is "null".
     * @param isbn the isbn of the returned book
     * @return the username of the returner
     */
    public String returnBook(String isbn) {
        Book toReturn = getBook(isbn);
        if (toReturn != null) {
            //check if lender is active User
            String returner = toReturn.getLender();
            toReturn.setLender("null");
            updateBook(isbn, toReturn);
            return returner;
        } else {
            throw new CouldNotLendBookException("Book " + isbn + " could not be returned.");
        }
    }

    /**
     * Returns all lent books for a specific user.
     * @return list with all lent books
     */
    public List<Book> getLendings() {
        List<Book> books = getBooks();
        List<Book> lendings = new ArrayList<>();
        for (Book book : books) {
            //prüfen, ob aktiver User der Ausleiher ist
            if (book.getLender().equals("TestUser")) {
                lendings.add(book);
            }
        }
        return lendings;
    }

    /**
     * Converts a database response to a book object.
     * @param item map as structure of a GetItemResponse from the DynamoDB
     * @return the equivalent book object
     */
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
        try {
            builder.category(Category.valueOf(item.get("category").s()));
        } catch (NullPointerException e) {
            throw new IllegalStateException("item did not have a category attribute or it was not a String");
        }
        try {
            builder.lender(item.get("lender").s());
        } catch (NullPointerException e) {
            builder.lender(""); //Buch ist noch verfügbar
            //throw new IllegalStateException("item did not have a lender attribute or it was not a String");
        }
        return builder.build();
    }

    /**
     * Creates a item for insertion to the table.
     * @param book the book with the information for the item
     * @return the item for insertion
     */
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
        try {
            item.put("category", AttributeValue.builder().s(book.getCategory().toString()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("category was null");
        }
        try {
            item.put("lender", AttributeValue.builder().s(book.getLender()).build());
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("lender was null");
        }
        return item;
    }

    /**
     * Checks if there is a valid isbn number.
     * @param isbn isbn to check
     * @return true if the isbn is valid
     */
    private boolean checkIsbn(String isbn) {
        if (isbn.isEmpty()) {
            return false;
        } else {
            isbn = isbn.replace("-", ""); //mögliche Trennzeichen entfernen
        }
        if (isbn.length() != ISBN_LENGTH) {
            return false;
        }
        ArrayList<Integer> ints = convertToList(isbn);
        int checksum = (ints.get(0) + ints.get(2) + ints.get(4) + ints.get(6) + ints.get(8) + ints.get(10) + ints.get(12) + 3 * (ints.get(1) + ints.get(3) + ints.get(5) + ints.get(7) + ints.get(9) + ints.get(11))) % 10;
        if (checksum == 0) {
            return true;
        }
        return false;
    }

    /**
     * Converts a String of numbers to a list of integers.
     * @param isbn String to convert
     * @return list of integers
     */
    private ArrayList<Integer> convertToList(String isbn) {
        ArrayList<Integer> result = new ArrayList<>();
        for (char c: isbn.toCharArray()) {
            result.add(Character.getNumericValue(c));
        }
        return result;
    }

}
