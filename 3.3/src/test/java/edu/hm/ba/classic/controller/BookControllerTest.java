package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.services.BookService;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
@WebAppConfiguration
public class BookControllerTest {

    private static final Collection<Book> BOOKS = new ArrayList<>(Arrays.asList(
            new Book("1234567890000", "TestBuch1", "TestAutor", Category.SCIENCE),
            new Book("1234567890001", "TestBuch2", "TestAutor", Category.FANTASY),
            new Book("1234567890002", "TestBuch3", "Autor", Category.HISTORY)
    ));

    private static final Book BOOK = new Book("1234567890000", "TestBuch1", "TestAutor", Category.SCIENCE);

    @MockBean
    private BookService bookService;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Autowired
    private WebApplicationContext webAppContext;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @Test
    public void testGetBooks() throws Exception {
        when(bookService.getBooks()).thenReturn(BOOKS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\",\"lender\":null},{\"isbn\":\"1234567890001\",\"title\":\"TestBuch2\",\"author\":\"TestAutor\",\"category\":\"FANTASY\",\"lender\":null},{\"isbn\":\"1234567890002\",\"title\":\"TestBuch3\",\"author\":\"Autor\",\"category\":\"HISTORY\",\"lender\":null}]",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testAddBook() throws Exception {
        when(bookService.addBook(BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testAddBookAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(bookService.addBook(BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testUpdateBook() throws Exception {
        when(bookService.updateBook(BOOK.getIsbn(), BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/1234567890000")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testUpdateBookAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(bookService.updateBook(BOOK.getIsbn(), BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/1234567890000")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testDeleteBook() throws Exception {
        when(bookService.deleteBook(BOOK.getIsbn())).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/1234567890000");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"1234567890000\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testDeleteBookAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(bookService.deleteBook(BOOK.getIsbn())).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/1234567890000");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    public void testLendBook() {

    }

    @Test
    public void testReturnBook() {

    }

}