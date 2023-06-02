package org.study.resource;

import io.micrometer.core.instrument.MeterRegistry;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.study.exceptions.DuplicateEntryException;
import org.study.service.BookService;
import org.study.entity.Book;

@Path("/books")
public class BookResource {

    @Inject
    BookService service;

    @Inject
    Logger log;

    @Inject
    MeterRegistry registry;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{name}")
    public Response get(@PathParam("name") String name) {
        log.info("Preparing to get the book %s".formatted(name));

        Book book = service.findBook(name);
        if (book != null) {
            log.info("Book %s found with success".formatted(book.getName()));
            registry.counter("books.name.get", "type", "success").increment();
            
            return Response.ok(book).build();
        } else {
            String errorMessage = "Error book %s not found".formatted(name);

            log.error(errorMessage);
            registry.counter("books.name.get", "type", "error").increment();

            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(@Valid Book book) {
        log.info("Preparing to create the book %s".formatted(book.getName()));

        try {
            service.addBook(book);

            log.info("Book %s created with success".formatted(book.getName()));
            registry.counter("books.create", "type", "success").increment();

            return Response.status(Response.Status.CREATED).entity(book).build();
        } catch (DuplicateEntryException e) {
            String errorMessage = "Error duplicate entry found for book %s".formatted(book.getName());

            log.error(errorMessage);
            registry.counter("books.create", "type", "error").increment();

            return Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
        }
    }

    @DELETE
    @Path("/{name}")
    public Response delete(@PathParam("name") String name) {
        log.info("Preparing to delete the book %s".formatted(name));

        Book book = service.removeBook(name);
        if (book != null) {
            log.info("Book deleted: " + book);
            registry.counter("books.name.delete", "type", "success").increment();

            return Response.ok(book).build();
        } else {
            String errorMessage = "Error book %s not found".formatted(name);

            log.error(errorMessage);
            registry.counter("books.name.delete", "type", "error").increment();

            return Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
        }
    }
}
