package com.example.demo.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {
	
		private Integer categoryId;
	@NotBlank(message = "Title should not be blank")
	private String categoryTitle;
	@NotBlank(message = "Description should not be blank")
	@Size(min = 10, message = "Description must be at least 10 characters long")
	private String categoryDescription;
	
	
	

}
