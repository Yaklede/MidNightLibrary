package jpaProject.ebook.domain.file;

import jpaProject.ebook.domain.item.Item;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@Table(name = "FILE")
@Entity
public class FileEntity {
    @Id
    @GeneratedValue
    @Column(name = "FILE_ID")
    private Long id;

    private String orgNm;

    private String savedNm;

    private String savePath;


    @Builder
    public FileEntity(Long id, String orgNm, String savedNm, String savePath) {
        this.id = id;
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savePath = savePath;
    }
}
