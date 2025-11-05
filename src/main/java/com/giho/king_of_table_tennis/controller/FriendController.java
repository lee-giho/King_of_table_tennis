package com.giho.king_of_table_tennis.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Friend-Controller", description = "친구 관련 API")
@RequestMapping("/api/friend")
public class FriendController {
}
