package jpaProject.ebook.domain.file;

import jpaProject.ebook.domain.item.Item;
import jpaProject.ebook.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    @Value("${file.dir}")
    private String fileDir;

    private final FileRepository fileRepository;

    private final ItemService itemService;

    public Long saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }
        //원래 파일의 이름을 추출
        String origName = file.getOriginalFilename();
        //파일의 이름으로 쓸 UUID 생성
        String uuid = UUID.randomUUID().toString();
        //확장자 추출 (EX .png)
        String extension = origName.substring(origName.lastIndexOf("."));
        //uuid 와 확장자 결합
        String savedName = uuid + extension;

        //파일을 불러올 때 사용할 경로
        String savedPath = fileDir + savedName;
        //엔티티 생성
        FileEntity fileEntity = FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savePath(savedPath)
                .build();
        //실제로 로컬에 UUID를 파일명으로 저장
        file.transferTo(new File(savedPath));

        //데이터베이스에 파일 정보 저장
        FileEntity save = fileRepository.save(fileEntity);
        return save.getId();

    }

    public Long updateFile(FileEntity updateFile, MultipartFile multipartFile) throws IOException {
        /**
         * param2 : 저장된 이미지 파일아이디를 불러온다.
         * 파라미터 : 저장된 이미지를  form으로 들어온 이미지로 set 치환후
         * 저장
         */
        Long updateFileId = updateFile.getId();
        FileEntity findFile = fileRepository.findOne(updateFileId);

        //원래 파일의 이름을 추출
        String origName = multipartFile.getOriginalFilename();
        //파일의 이름으로 쓸 UUID 생성
        String uuid = UUID.randomUUID().toString();
        //확장자 추출 (EX .png)
        String extension = origName.substring(origName.lastIndexOf("."));
        //uuid 와 확장자 결합
        String savedName = uuid + extension;

        //파일을 불러올 때 사용할 경로
        String savedPath = fileDir + savedName;

        findFile.setSavePath(savedPath);
        findFile.setSavedNm(savedName);
        findFile.setOrgNm(origName);

        multipartFile.transferTo(new File(savedPath));

        return findFile.getId();
    }

}
