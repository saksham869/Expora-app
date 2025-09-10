package com.example.demo.payloads;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class LikeDto {
    private Integer id;
    private Integer postId;
    private Integer userId;
}
