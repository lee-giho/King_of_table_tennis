package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.RegisterCommentRequestDTO;
import com.giho.king_of_table_tennis.entity.CommentEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.CommentRepository;
import com.giho.king_of_table_tennis.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;

  private final PostRepository postRepository;

  @Transactional
  public void registerComment(String postId, RegisterCommentRequestDTO registerCommentRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    if (!postRepository.existsById(postId)) {
      throw new CustomException(ErrorCode.POST_NOT_FOUND);
    }

    CommentEntity commentEntity = new CommentEntity();

    commentEntity.setId(UUID.randomUUID().toString());
    commentEntity.setPostId(postId);
    commentEntity.setWriterId(userId);
    commentEntity.setContent(registerCommentRequestDTO.getContent());

    commentRepository.save(commentEntity);
  }
}
