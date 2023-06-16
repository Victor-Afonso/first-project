package org.study.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.study.entity.Book;
import org.study.exceptions.DuplicateEntryException;
import org.study.service.BookService;
import java.time.LocalDate;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class BookResourceTest {

    @InjectMock
    BookService mockBookService;

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
        book.setAuthor("AuthorTest");
        book.setCreatedAt(LocalDate.of(2015, 2, 8));


        Mockito.when(mockBookService.findBook("test_1f8b720a67d8")).thenReturn(book);

        given()
                .when()
                .get("/books/test_1f8b720a67d8")
                .then()
                .statusCode(200)
                .body("name", Matchers.is("test_1f8b720a67d8"))
                .body("pages", Matchers.is(34))
                .body("author", Matchers.is("AuthorTest"))
                .body("created_at", Matchers.is("2015-02-08"));
    }

    @Test
    public void testCreateBookEndpointShouldCreateWithSuccess() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                        {"name":"test_1f8b720a67d8","pages": 34, "author":  "AuthorTest","created_at": "2015-02-08"}
                        """)
                .when()
                .post("/books")
                .then()
                .statusCode(201)
                .body("name", Matchers.is("test_1f8b720a67d8"))
                .body("pages", Matchers.is(34))
                .body("author", Matchers.is("AuthorTest"))
                .body("created_at", Matchers.is("2015-02-08"));
    }

    @Test
    public void testCreateBookEndpointShouldFailWhenNameIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {"name":"","pages": 34, "author": "AuthorTest","created_at": "2015-02-08"}
                    """)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("violations.message", Matchers.hasItem("Name cannot be blank"));
    }

    @Test
    public void testCreateBookEndpointShouldFailWhenPagesIsNull() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {"name":"TestBook","pages": null, "author": "AuthorTest","created_at": "2015-02-08"}
                    """)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("violations.message", Matchers.hasItem("Pages cannot be null"));
    }

    @Test
    public void testCreateBookEndpointShouldFailWhenPagesIsNegative() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {"name":"TestBook","pages": -1, "author": "AuthorTest","created_at": "2015-02-08"}
                    """)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("violations.message", Matchers.hasItem("Pages has to be higher than 0"));
    }

    @Test
    public void testCreateBookEndpointShouldFailWhenAuthorIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {"name":"TestBook","pages": 34, "author": "","created_at": "2015-02-08"}
                    """)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("violations.message", Matchers.hasItem("Author cannot be blank"));
    }

    @Test
    public void testCreateBookEndpointShouldFailWhenCreatedAtIsInFuture() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {"name":"TestBook","pages": 34, "author": "AuthorTest","created_at": "2999-02-08"}
                    """)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("violations.message", Matchers.hasItem("Created at cannot be in the future"));
    }

    @Test
    public void testCreateBookEndpointShouldFailWhenCreatedAtIsNull() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("""
                    {"name":"TestBook","pages": 34, "author": "AuthorTest","created_at": null}
                    """)
                .when()
                .post("/books")
                .then()
                .statusCode(400)
                .body("violations.message", Matchers.hasItem("Created at cannot be null"));
    }

    @Test
    public void testCreateBookEndpointShouldThrowDuplicateEntryException() throws DuplicateEntryException {
        Book book = new Book();
        book.setName("duplicate_entry");
        book.setPages(45);
        book.setAuthor("AuthorTest");
        book.setCreatedAt(LocalDate.of(2015, 2, 8));

        Mockito.doThrow(DuplicateEntryException.class).when(mockBookService).addBook(book);

        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(book)
                .when()
                .post("/books")
                .then()
                .statusCode(409)
                .body(Matchers.containsString("Error duplicate entry found for book duplicate_entry"));
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
        book.setAuthor("AuthorTest");
        book.setCreatedAt(LocalDate.of(2015, 2, 8));


        Mockito.when(mockBookService.removeBook("test_1f8b720a67d8")).thenReturn(book);

        given()
                .when()
                .delete("/books/test_1f8b720a67d8")
                .then()
                .statusCode(200);
    }

}