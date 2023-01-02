package com.example.demo.service;

import com.example.demo.entity.ItemImg;
import com.example.demo.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}") // 1. 프로퍼티에 등록한 itemImgLocation을 String 객체에 저장
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            // 2. 상품의 이미지를 등록했다면, uploadFile을 통해 로컬에 저장된 파일의 이름을 받아옴
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 3. 저장한 상품 이미지를 불러올 경로 / 프로퍼티의 uploadPath(C:/shop) 아래의 /images/item/imgName을 불러옴
            imgUrl = "/images/item/"+imgName;
        }

        // 상품 이미지 정보 저장
        // 4.5.
        // imgName : 실제 로컬에 저자오딘 이미지 파일 이름
        // oriImgName : 업로드했던 이미지 파일의 원래 이름
        // imgUrl : 업로드 결과 로컬에 저장된 이미지 파일을 불러오는 경로
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){ // 1.
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId) // 2.
                    .orElseThrow(EntityNotFoundException::new);

            // 기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){ // 3. 기존 등록된 상품 이미지 있으면 해당 파일 삭제
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); // 4. 업데이트한 상품 이미지 파일 업로드
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl); // 변경된 상품 이미지 정보를 세팅함. savedItemImg 얘는 영속상태기 때문에 save 안해도 커밋시 쿼리 실행됨
        }

    }
}
