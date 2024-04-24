package com.github.kmpk.democrud.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonAutoDetect(isGetterVisibility = JsonAutoDetect.Visibility.NONE)
public class Book implements HasId {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;
}
