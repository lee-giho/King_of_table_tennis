package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tableTennisCourts")
public class TableTennisCourt {
  @Id
  private String id;
  private String name;
  private String address;
  private Location location;
  private String phoneNumber;
  private Map<String, String> businessHours;
}
