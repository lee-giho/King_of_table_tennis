package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.dto.UserRankingInfo;
import com.giho.king_of_table_tennis.dto.enums.RankingSortOption;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.UserRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class UserRankingService {

  private final UserRankingRepository userRankingRepository;

  @Transactional(readOnly = true)
  public PageResponse<UserRankingInfo> getUserRankings(int page, int size, RankingSortOption sortOption) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserId = authentication.getName();

    Sort sort = switch (sortOption) {
      case WIN_RATE -> Sort.by(Sort.Direction.DESC, "winRate")
        .and(Sort.by(Sort.Direction.DESC, "winCount"));
      case WIN_COUNT -> Sort.by(Sort.Direction.DESC, "winCount")
        .and(Sort.by(Sort.Direction.DESC, "winRate"));
    };

    Pageable pageable = PageRequest.of(page, size, sort);

    Page<UserRankingInfo> userRankingInfoPage = userRankingRepository.findUserRankingInfos(currentUserId, pageable);

    int startRank = page * size + 1;
    AtomicInteger offset = new AtomicInteger(0);

    List<UserRankingInfo> content = userRankingInfoPage.getContent()
      .stream()
      .peek(r -> r.setRanking(startRank + offset.getAndIncrement()))
      .toList();

    return new PageResponse<>(
      content,
      userRankingInfoPage.getTotalPages(),
      userRankingInfoPage.getTotalElements(),
      userRankingInfoPage.getNumber(),
      userRankingInfoPage.getSize()
    );
  }

  @Transactional(readOnly = true)
  public UserRankingInfo getUserRanking(String userId, RankingSortOption sortOption) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String currentUserId = authentication.getName();

    UserRankingInfo userRankingInfo = userRankingRepository.findUserRankingInfoByUserId(currentUserId, userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    int rank = userRankingRepository.findUserRankBySort(userId, sortOption.name());

    userRankingInfo.setRanking(rank);
    return userRankingInfo;
  }
}
