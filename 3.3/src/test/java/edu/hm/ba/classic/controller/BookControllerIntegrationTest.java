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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

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
        assertEquals("[{\"isbn\":\"9783442151479\",\"title\":\"Bildung - Alles, was man wissen muss\",\"author\":\"Dietrich Schwanitz\",\"category\":\"SCIENCE\",\"lender\":null},{\"isbn\":\"9783806234770\",\"title\":\"Schwarze Flaggen\",\"author\":\"Joby Warrick\",\"category\":\"HISTORY\",\"lender\":null},{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}]",
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
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/books/9783764504458")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\"}");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(authorities = "EMPLOYEE")
    public void testDeleteBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/books/9783764504458");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void testLendBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/lend/9783764504458")
                .with(user("Admin").password("admin1").roles("ADMIN"));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":\"1\",\"username\":\"Admin\",\"password\":\"admin1\"}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void testReturnBook() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/return/9783764504458");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("{\"id\":\"1\",\"username\":\"Admin\",\"password\":\"admin1\"}",
                result.getResponse().getContentAsString());
    }

    @Test
    public void testGetLendings() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/lend");
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[{\"isbn\":\"9783442151479\",\"title\":\"Bildung - Alles, was man wissen muss\",\"author\":\"Dietrich Schwanitz\",\"category\":\"SCIENCE\",\"lender\":null},{\"isbn\":\"9783806234770\",\"title\":\"Schwarze Flaggen\",\"author\":\"Joby Warrick\",\"category\":\"HISTORY\",\"lender\":null},{\"isbn\":\"9783764504458\",\"title\":\"Blackout\",\"author\":\"Marc Elsberg\",\"category\":\"FANTASY\",\"lender\":null}]",
                result.getResponse().getContentAsString());
    }

}
