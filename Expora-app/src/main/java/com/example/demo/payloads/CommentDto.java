package com.example.demo.payloads;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommentDto {
    private int id;
    private String title;
    private String content;
    private Integer parentId;
    private Integer postId;
    private List<CommentDto> replies; // For nested replies if needed
}
