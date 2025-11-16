package com.giho.king_of_table_tennis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMessagePayload {
  private String roomId;
  private String content;
}
