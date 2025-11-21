package com.giho.king_of_table_tennis.event;

import java.time.LocalDateTime;

public record ReadMessageEvent(
  String roomId,
  String readerId,
  Long lastReadMessageId,
  LocalDateTime lastReadAt
) {
}
