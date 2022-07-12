package jpaProject.ebook.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileRepository {
    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public FileEntity save(FileEntity fileEntity) {
        em.persist(fileEntity);
        return fileEntity;
    }

    public FileEntity findOne(Long id) {
        return em.find(FileEntity.class, id);
    }

    public List<FileEntity> findAll() {
        return em.createQuery("select f from FileEntity f ")
                .getResultList();
    }
    
}
