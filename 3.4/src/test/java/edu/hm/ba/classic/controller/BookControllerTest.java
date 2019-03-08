
package edu.hm.ba.classic.controller;

import edu.hm.ba.classic.entities.Book;
import edu.hm.ba.classic.entities.Category;
import edu.hm.ba.classic.entities.Role;
import edu.hm.ba.classic.entities.User;
import edu.hm.ba.classic.services.BookService;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
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

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookControllerTest {

    private static final Book BOOK = new Book("9783764504458", "Blackout", "Marc Elsberg", Category.FANTASY);

    private static final Collection<Book> BOOKS = new ArrayList<>(Arrays.asList(
            new Book("9783442151479", "Bildung - Alles, was man wissen muss", "Dietrich Schwanitz", Category.SCIENCE),
            new Book("9783806234770", "Schwarze Flaggen", "Joby Warrick", Category.HISTORY),
            BOOK
    ));

    private static final User LENDER = new User(1, "Admin", "admin1", Role.ADMIN);

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
        assertEquals("[{\"isbn\":\"9783442151479\",\"title\":\"Bildung - Alles, was man wissen muss\",\"author\":\"Dietrich Schwanitz\",\"category\":\"SCIENCE\",\"lender\":null},{\"isbn\":\"9783806234770\",\"title\":\"Schwarze Flaggen\",\"author\":\"Joby Warrick\",\"category\":\"HISTORY\",\"lender\":null},{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}]",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testAddBook() throws Exception {
        when(bookService.addBook(BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testAddBookAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(bookService.addBook(BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testUpdateBook() throws Exception {
        when(bookService.updateBook(BOOK.getIsbn(), BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/9783764504458")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testUpdateBookAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(bookService.updateBook(BOOK.getIsbn(), BOOK)).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/9783764504458")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testDeleteBook() throws Exception {
        when(bookService.deleteBook(BOOK.getIsbn())).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/9783764504458");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "CUSTOMER")
    public void testDeleteBookAccessDenied() throws Exception {
        exception.expectCause(IsInstanceOf.<Throwable>instanceOf(AccessDeniedException.class));
        when(bookService.deleteBook(BOOK.getIsbn())).thenReturn(BOOK);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/9783764504458");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
    }

    @Test
    public void testLendBook() throws Exception {
        when(bookService.lendBook(BOOK.getIsbn(), null)).thenReturn(LENDER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/lend/9783764504458");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":1,\"username\":\"Admin\",\"password\":\"admin1\",\"role\":\"ADMIN\"}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void testReturnBook() throws Exception {
        when(bookService.returnBook(BOOK.getIsbn(), null)).thenReturn(LENDER);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/return/9783764504458");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":1,\"username\":\"Admin\",\"password\":\"admin1\",\"role\":\"ADMIN\"}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void testGetLendings() throws Exception {
        when(bookService.getLendings(null)).thenReturn(BOOKS);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/lend");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[{\"isbn\":\"9783442151479\",\"title\":\"Bildung - Alles, was man wissen muss\",\"author\":\"Dietrich Schwanitz\",\"category\":\"SCIENCE\",\"lender\":null},{\"isbn\":\"9783806234770\",\"title\":\"Schwarze Flaggen\",\"author\":\"Joby Warrick\",\"category\":\"HISTORY\",\"lender\":null},{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}]",
                result.getResponse().getContentAsString());
    }

}