package com.giho.king_of_table_tennis.event;

public record PreChatRoomUpdatedEvent(
  String roomId,
  String creatorId,
  String participantId
) {
}
