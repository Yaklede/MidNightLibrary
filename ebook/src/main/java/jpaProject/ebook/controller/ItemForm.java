package jpaProject.ebook.controller;

import jpaProject.ebook.domain.file.FileEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class ItemForm {
    private Long id;
    @NotEmpty
    private String title;
    @Min(1)
    private int price;
    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String content;
}


