package com.example.demo.repository;

import com.example.demo.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
