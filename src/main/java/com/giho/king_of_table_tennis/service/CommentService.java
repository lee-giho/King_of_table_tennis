package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.CommentDTO;
import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.dto.RegisterCommentRequestDTO;
import com.giho.king_of_table_tennis.dto.enums.CommentSortOption;
import com.giho.king_of_table_tennis.entity.CommentEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.CommentRepository;
import com.giho.king_of_table_tennis.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
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

  public PageResponse<CommentDTO> getCommentList(String postId, int page, int size, CommentSortOption sortOption, boolean showMyComment) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Sort sort = switch (sortOption) {
      case CREATED_ASC -> Sort.by(Sort.Direction.ASC, "createdAt");
      case CREATED_DESC -> Sort.by(Sort.Direction.DESC, "createdAt");
    };

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<CommentDTO> pageResponse;
    if (showMyComment) {
      pageResponse = commentRepository.findAllCommentDTOByUserId(userId, pageable);
    } else {
      pageResponse = commentRepository.findAllCommentDTOByPostId(postId, userId, pageable);
    }

    return new PageResponse<>(
      pageResponse.getContent(),
      pageResponse.getTotalPages(),
      pageResponse.getTotalElements(),
      pageResponse.getNumber(),
      pageResponse.getSize()
    );
  }

  @Transactional
  public void deleteComment(String commentId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    CommentEntity commentEntity = commentRepository.findById(commentId)
      .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

    if (!commentEntity.getWriterId().equals(userId)) {
      throw new CustomException(ErrorCode.COMMENT_DELETE_FORBIDDEN);
    }
    commentRepository.delete(commentEntity);
  }
}
