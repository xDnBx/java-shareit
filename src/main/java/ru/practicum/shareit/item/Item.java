package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    Long id;
    String name;
    String description;
    Boolean available;
    Long ownerId;
    Long requestId;
}