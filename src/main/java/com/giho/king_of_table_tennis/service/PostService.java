package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.PostCategory;
import com.giho.king_of_table_tennis.entity.PostEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.PostRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  private final UserRepository userRepository;

  @Transactional
  public void registerPost(RegisterPostRequestDTO registerPostRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    PostEntity postEntity = new PostEntity();

    postEntity.setId(UUID.randomUUID().toString());
    postEntity.setWriterId(userId);

    postEntity.setTitle(registerPostRequestDTO.getTitle());

    postEntity.setContent(registerPostRequestDTO.getContent());

    try {
      postEntity.setCategory(PostCategory.valueOf(registerPostRequestDTO.getCategory().toUpperCase()));
    } catch (IllegalArgumentException e) {
      throw new CustomException(ErrorCode.INVALID_CATEGORY);
    }


    postRepository.save(postEntity);
  }

  public PageResponse<PostDTO> getPost(int page, int size, String userId) {
    if (userId == null) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      userId = authentication.getName();
    }

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));

    Page<PostDTO> myPost = postRepository.findAllByWriterId(userId, pageable);

    return new PageResponse<>(
      myPost.getContent(),
      myPost.getTotalPages(),
      myPost.getTotalElements(),
      myPost.getNumber(),
      myPost.getSize()
    );
  }

  public PostDTO getPostByPostId(String postId) {
    PostEntity postEntity = postRepository.findById(postId)
      .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    return toPostDTO(postEntity);
  }

  @Transactional
  public void deletePost(String postId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    PostEntity postEntity = postRepository.findById(postId)
      .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    // 자신이 작성한 게시물이 아닐 경우
    if (!postEntity.getWriterId().equals(userId)) {
      throw new CustomException(ErrorCode.POST_DELETE_FORBIDDEN);
    }

    postRepository.delete(postEntity);
  }

  @Transactional
  public void updatePost(String postId, UpdatePostRequestDTO updatePostRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    PostEntity postEntity = postRepository.findById(postId)
      .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

    if (!postEntity.getWriterId().equals(userId)) {
      throw new CustomException(ErrorCode.POST_UPDATE_FORBIDDEN);
    }

    if (updatePostRequestDTO.getTitle() != null) postEntity.setTitle(updatePostRequestDTO.getTitle());
    if (updatePostRequestDTO.getCategory() != null) postEntity.setCategory(updatePostRequestDTO.getCategory());
    if (updatePostRequestDTO.getContent() != null) postEntity.setContent(updatePostRequestDTO.getContent());

    postRepository.save(postEntity);
  }

  private PostDTO toPostDTO(PostEntity postEntity) {
    UserInfo writer = userRepository.findUserInfoById(postEntity.getWriterId())
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return new PostDTO(
      postEntity.getId(),
      writer,
      postEntity.getTitle(),
      postEntity.getCategory(),
      postEntity.getContent(),
      postEntity.getUpdatedAt() == null
        ? postEntity.getCreatedAt()
        : postEntity.getUpdatedAt()
    );
  }
}
