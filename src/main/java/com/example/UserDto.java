package com.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.SecondaryTable;

@Getter
@Setter
@ToString
public class UserDto {
    private String name;
    private Integer age;
}
