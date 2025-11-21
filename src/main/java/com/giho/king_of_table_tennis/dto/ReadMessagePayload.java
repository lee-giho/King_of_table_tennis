package com.giho.king_of_table_tennis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadMessagePayload {
  private String roomId;
  private Long lastReadMessageId;
}
