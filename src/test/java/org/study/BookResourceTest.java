package org.study;

import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.study.entity.Book;
import org.study.service.BookService;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BookResourceTest {

    @Inject
    BookService mockBookService;

    @BeforeEach
    public void setup() {
        BookService mock = Mockito.mock(BookService.class);
        QuarkusMock.installMockForType(mock, BookService.class);
    }

    @Test
    public void testGetBookEndpointShouldNotFoundTheBook() {
        given()
                .when()
                .get("/books/test_1f8b720a67d8")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetBookEndpointShouldGetWithSuccess() {

        Book book = new Book();
        book.setName("test_1f8b720a67d8");
        book.setPages(34);
        book.setCreatedAt(LocalDate.of(2015, 2, 8));


        Mockito.when(mockBookService.findBook("test_1f8b720a67d8")).thenReturn(book);

        given()
                .when()
                .get("/books/test_1f8b720a67d8")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("test_1f8b720a67d8"))
                .body("pages", Matchers.is(34))
                .body("created_at", Matchers.is("2015-02-08"));
    }

    @Test
    public void testCreateBookEndpointShouldCreateWithSuccess() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {"name":"test_1f8b720a67d8","pages": 34, "created_at": "2015-02-08"}""")
                .when()
                .post("/books")
                .then()
                .statusCode(201)
                .body("name", Matchers.is("test_1f8b720a67d8"))
                .body("pages", Matchers.is(34))
                .body("created_at", Matchers.is("2015-02-08"));
    }

    @Test
    public void testDeleteBookEndpointShouldNotFoundTheBook() {
        given()
                .when()
                .delete("/books/test_1f8b720a67d8")
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeleteBookEndpointShouldDeleteWithSuccess() {
        Book book = new Book();
        book.setName("test_1f8b720a67d8");
        book.setPages(34);
        book.setCreatedAt(LocalDate.of(2015, 2, 8));


        Mockito.when(mockBookService.removeBook("test_1f8b720a67d8")).thenReturn(book);

        given()
                .when()
                .delete("/books/test_1f8b720a67d8")
                .then()
                .statusCode(200);
    }

}