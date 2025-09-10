package com.example.demo.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.config.AppConstants;
import com.example.demo.payloads.PostDto;
import com.example.demo.payloads.PostResponse;
import com.example.demo.services.FileService;
import com.example.demo.services.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    // CREATE a new post
    @PostMapping("/user/{userId}/category/{categoryId}/post")
    public ResponseEntity<PostDto> createPost(
            @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) {
        PostDto createdPost = this.postService.createPosts(postDto, userId, categoryId);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    // GET all posts created by a user
    @GetMapping("/user/{userId}/post")
    public ResponseEntity<List<PostDto>> getPostsByUser(@PathVariable Integer userId) {
        List<PostDto> posts = this.postService.getPostsByUser(userId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // GET all posts under a category
    @GetMapping("/category/{categoryId}/post")
    public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Integer categoryId) {
        List<PostDto> posts = this.postService.getPostsByCategory(categoryId);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // GET all posts with pagination and sorting
    @GetMapping
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.SORT_DIR, required = false) String sortDir) {

        PostResponse postResponse = this.postService.getAllPosts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    // GET a single post by ID
    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        PostDto post = this.postService.getPostDtoById(postId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    // DELETE a post
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Integer postId) {
        this.postService.deletePosts(postId);
        return new ResponseEntity<>("Post deleted successfully", HttpStatus.OK);
    }

    // UPDATE a post
    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
        PostDto updatedPost = this.postService.updatePosts(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // SEARCH posts by title
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<PostDto>> searchPosts(@PathVariable String keyword) {
        List<PostDto> posts = this.postService.searchPosts(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    // POST image upload
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image") MultipartFile image,
                                                   @PathVariable Integer postId) throws IOException {
        PostDto postDto = this.postService.getPostDtoById(postId);
        String fileName = this.fileService.uploadFile(path, image);

        postDto.setImageName(fileName);
        PostDto updatedPost = this.postService.updatePosts(postDto, postId);
        return new ResponseEntity<>(updatedPost, HttpStatus.OK);
    }

    // GET image by image name
    // GET image by image name
    @GetMapping("/post/image/{imageName}")
    public ResponseEntity<Resource> serveImage(@PathVariable String imageName) throws IOException {
        String fullPath = path + File.separator + imageName;
        File imgFile = new File(fullPath);

        // Logging for debug
        System.out.println("🔍 Looking for image: " + fullPath);

        if (!imgFile.exists() || !imgFile.isFile()) {
            System.out.println("❌ File not found: " + fullPath);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        InputStreamResource resource = new InputStreamResource(fileService.getResource(path, imageName));

        String contentType = Files.probeContentType(imgFile.toPath());
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    // GET image by postId (optional endpoint for cleaner URL)
    @GetMapping("/upload/{postId}/image")
    public ResponseEntity<Resource> getPostImageByPostId(@PathVariable Integer postId) throws IOException {
        PostDto post = this.postService.getPostDtoById(postId);
        String imageName = post.getImageName();

        if (imageName == null || imageName.isEmpty()) {
            System.out.println("⚠️ No image name found for post ID: " + postId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String fullPath = path + File.separator + imageName;
        File imgFile = new File(fullPath);

        // Logging
        System.out.println("🔍 Fetching image for post ID " + postId + ": " + fullPath);

        if (!imgFile.exists() || !imgFile.isFile()) {
            System.out.println("❌ Image not found for post ID " + postId + ": " + fullPath);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        InputStreamResource resource = new InputStreamResource(fileService.getResource(path, imageName));

        String contentType = Files.probeContentType(imgFile.toPath());
        if (contentType == null) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }
}
