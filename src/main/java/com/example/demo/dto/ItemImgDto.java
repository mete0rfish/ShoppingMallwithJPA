package com.example.demo.dto;

import com.example.demo.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {
    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); // 멤버변수로 ModelMapper 객체를 추가

    public static ItemImgDto of(ItemImg itemImg){ // itemImg와 멤버변수 이름이 같으면 ItemImgDto로 값을 복사해서 반환
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
