package com.example.demo.payloads;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediaDto {
    private Integer id;
    private String url;
    private String mediaType;
    private Integer displayOrder;
    private Integer postId; // optional but useful
}
