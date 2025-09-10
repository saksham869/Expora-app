package com.example.demo.payloads;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.xml.stream.events.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter
@NoArgsConstructor
public class PostDto {    
    private Integer id;
    private String title;
    private String content;
    private String imageName = "default.png";
    private Date addedDate;
    private Date updatedDate;           // New
    private String slug;                // New
    private String summary;             // New

    private String fileType;            // New
    private Long fileSize;              // New
    private String fileDownloadUri;     // New

    private int pageViews;              // New
    private boolean isFeatured;         // New

    
    private CategoryDto category;
    private UserDto user;
    private Set<Comment> comment=new HashSet<>();
	public void setAudioName(String fileName) {
		// TODO Auto-generated method stub
		
	}
	public void setImageName(String fileName) {
		// TODO Auto-generated method stub
		
	}
	
}
