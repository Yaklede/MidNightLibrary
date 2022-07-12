package jpaProject.ebook.domain.item;

import jpaProject.ebook.domain.file.FileEntity;
import jpaProject.ebook.exception.NotEnoughStockException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item {
    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
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


    public Item(String title, int price, String author, String isbn, String content) {
        this.title = title;
        this.price = price;
        this.author = author;
        this.isbn = isbn;
        this.content = content;
    }

}
