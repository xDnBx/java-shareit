package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoInput;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.List;

@UtilityClass
public class ItemRequestMapper {
    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, Long userId, List<Item> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(userId)
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestDtoInput dto, User requestor) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .requestor(requestor)
                .build();
    }

    public ItemDto toItemDtoBooking(Item item, BookingDto lastBooking, BookingDto nextBooking,
                                    Collection<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }
}