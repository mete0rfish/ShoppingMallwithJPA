package com.example.demo.service;

import com.example.demo.dto.ItemFormDto;
import com.example.demo.dto.ItemImgDto;
import com.example.demo.dto.ItemSearchDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.ItemImg;
import com.example.demo.repository.ItemImgRepository;
import com.example.demo.repository.ItemRepository;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        // 상품 등록
        Item item = itemFormDto.createItem();  // 1. 상품 등록 폼의 데이터로 item 생성
        itemRepository.save(item);  // 2. 상품 데이터 저장

        // 이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0){ // 3. 첫번째 이미지면 대표 이미지 설정
                itemImg.setRepImgYn("Y");
            }else{
                itemImg.setRepImgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); // 4. 상품의 이미지 정보 저장
        }
        return item.getId();
    }

    @Transactional(readOnly = true) // 읽기전용으로 하면 JPA가 더티체킹 안해서 성능향상
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품 이미지 조회 / 등록순으로 가져옴
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){ // 조회한 ItemImg 엔티티를 ItemImgDto객체로 만들어서 List에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId) // 상품 아이디로 상품 엔티티 조회 / 없으면 익센션
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()) // 멤버 폼으로 상품 불러옴
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto); // 상품을 상품 폼 데이터로 변경

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); // 폼에서 이미지정보들을 받아옴

        // 이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i)); // 이미지를 업데이트하기 위해 (이미지 id, 이미지 파일 정보)를 파라미터로 전달
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }
}
