package com.example.demo.controller;

import com.example.demo.dto.ItemFormDto;
import com.example.demo.dto.ItemSearchDto;
import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "/item/itemForm";
    }

    @PostMapping(value="/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList){
        if(bindingResult.hasErrors()){ // 1. 상품 등록 시 필수 값이 없으면 다시 상품 등록 페이지 연결
            return "item.itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId()==null){ // 2. 첫번째 이미지 없으면 에러 메시지 출력 후, 재연결
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList); // 3. 상품 저장
        }catch(Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; // 4. 정상 등록 시, 메인페이지 이동
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId); // 1. 조회한 상품 데이터를 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);

        }catch (EntityNotFoundException e){ // 상품이 없으면 에러메시지
            model.addAttribute("errorMessage", "존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }


    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
        }catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    // 1. 상품 관리 진입 시 (페이지 번호 있는 경우), (페이지 번호 없는 경우)
    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto,
                             @PathVariable("page") Optional<Integer> page, Model model){
        // 2. Pagable 생성. (조회할 페이지 번호, 한 번에 가지고 올 데이터 수) / 페이지 번호 없으면 0
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        // 3. Page<Item> 생성
        Page<Item> items =
                itemService.getAdminItemPage(itemSearchDto, pageable);
        // 4. 조회한 상품 데이터 및 페이징 리뷰
        model.addAttribute("items", items);
        // 5. 페이지 전환 시 기존 검색 조건 유지하도록 데이터 다시 전달
        model.addAttribute("itemSearchDto", itemSearchDto);
        // 6. 하단에 보여줄 페이지 번호의 최대 개수
        model.addAttribute("maxPage", 5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }

}
