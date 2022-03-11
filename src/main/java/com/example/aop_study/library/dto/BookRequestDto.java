package com.example.aop_study.library.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class BookRequestDto {

    @NotBlank(message = "책 제목은 빈칸이 될 수 없습니다.")
    @Size(max = 255, message = "제목의 최대 길이는 255자입니다.")
    private String title;

    @NotBlank(message = "책 작가는 빈칸이 될 수 없습니다.")
    @Size(max = 255, message = "작가 이름은 최대 길이는 255자입니다.")
    private String author;

    @NotBlank(message = "책 출판사는 빈칸이 될 수 없습니다.")
    @Size(max = 255, message = "출판사는 최대 길이는 255자입니다.")
    private String publisher;

    @Nullable
    private String patron;

}
