package edu.hm.ba.classic.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BookControllerIntegrationTest {

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
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/books");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[{\"isbn\":\"9783866801929\",\"title\":\"Titel des Testbuchs\",\"author\":\"Test Autor\",\"category\":\"HISTORY\",\"lender\":null},{\"isbn\":\"9783866801234\",\"title\":\"Neuer Titel des Testbuchs\",\"author\":\"Test Autor2\",\"category\":\"SCIENCE\",\"lender\":null},{\"isbn\":\"9783866809876\",\"title\":\"Titel 2 des Testbuchs 2\",\"author\":\"Test Autor\",\"category\":\"FANTASY\",\"lender\":null}]",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testAddBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783257230888\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783257230888\",\"title\":\"TestBuch1\",\"author\":\"TestAutor\",\"category\":\"SCIENCE\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testUpdateBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/9783866809876")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783866809876\",\"title\":\"Titel 2\",\"author\":\"Test Autor 2\",\"category\":\"FANTASY\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783866809876\",\"title\":\"Titel 2\",\"author\":\"Test Autor 2\",\"category\":\"FANTASY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testDeleteBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/9783866801929");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783866801929\",\"title\":\"Titel des Testbuchs\",\"author\":\"Test Autor\",\"category\":\"HISTORY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void testLendBook() throws Exception {

    }

    @Test
    public void testReturnBook() throws Exception {

    }

    @Test
    public void testGetLoans() throws Exception {

    }

}
