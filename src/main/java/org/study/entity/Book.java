package org.study.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Generated;

import java.time.LocalDate;

@Generated
@Data
public class Book {
    @NotBlank(message="Name cannot be blank")
    private String name;

    @NotNull(message = "Pages cannot be null")
    @Min(value = 0, message ="Pages has to be higher than 0")
    private Integer pages;

    @NotBlank(message="Author cannot be blank")
    private String author;

    @NotNull(message = "Created at cannot be null")
    @PastOrPresent(message = "Created at cannot be in the future")
    @JsonProperty("created_at")
    private LocalDate createdAt;
}