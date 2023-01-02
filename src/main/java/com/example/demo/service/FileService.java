package com.example.demo.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // 1. UUID : 객체 구별을 위해 유일한 이름 부여
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; // 2. UUID와 조합해서 저장할 파일 이름 생성
        String fileUploadFullUrl = uploadPath + "/" +savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // 3. FileOutputStream : 바이트 단위의 출력을 내보냄. 생성자에 저장될 위치와 파일 이름 넘겨서 출력 스트림 만듬
        fos.write(fileData); // 4. fuleData를 파일 출력 스트림에 입력
        fos.close();
        return savedFileName; // 5. 업로드된 파일의 이름을 반환
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); // 6. 파일이 저장된 경로를 이용해서 파일 객체 생성

        if(deleteFile.exists()){ // 7. 해당 파일이 존재하면 파일을 삭제
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }
        else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
