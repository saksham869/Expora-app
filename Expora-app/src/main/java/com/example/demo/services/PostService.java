package com.example.demo.services;

import java.util.List;

import com.example.demo.entity.Post;
import com.example.demo.payloads.PostDto;
import com.example.demo.payloads.PostResponse;

public interface PostService {
PostDto createPosts(PostDto postDto ,Integer userId, Integer categoryId);

PostDto updatePosts(PostDto postDto ,Integer postId);
void deletePosts(Integer postId);
PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
Post getPostsById(Integer postId);
List<PostDto> getPostsByCategory(Integer categoryId);
List<PostDto> getPostsByUser(Integer userId);
List<PostDto> searchPosts(String keyword);

PostDto getPostDtoById(Integer postId);

}
