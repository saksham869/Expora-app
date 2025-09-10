
package com.example.demo.services.impl;

import com.example.demo.entity.Comment;
import com.example.demo.entity.Post;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.payloads.CommentDto;
import com.example.demo.repositries.CommentRepo;
import com.example.demo.repositries.PostRepo;
import com.example.demo.services.CommentService;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId) {
        return createComment(commentDto, postId, null);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto, Integer postId, Integer parentCommentId) {
        Post post = this.postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "post id", postId));

        Comment comment = this.modelMapper.map(commentDto, Comment.class);
        comment.setPost(post);

        if (parentCommentId != null) {
            Comment parentComment = this.commentRepo.findById(parentCommentId)
                    .orElseThrow(() -> new ResourceNotFoundException("Comment", "parent comment id", parentCommentId));
            comment.setParent(parentComment);
        }

        Comment savedComment = this.commentRepo.save(comment);
        return this.modelMapper.map(savedComment, CommentDto.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "comment id", commentId));
        this.commentRepo.delete(comment);
    }

	@Override
	public List<CommentDto> getCommentsByPostId(Integer postId) {
		// TODO Auto-generated method stub
		return null;
	}
}
