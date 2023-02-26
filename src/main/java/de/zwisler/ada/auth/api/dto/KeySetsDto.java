package de.zwisler.ada.auth.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class KeySetsDto {

  List<KeySetDto> keys;

  public static KeySetsDto from(List<KeySetDto> newArrayList) {
    KeySetsDto keySetsDto = new KeySetsDto();
    keySetsDto.keys = newArrayList;
    return keySetsDto;
  }
}
