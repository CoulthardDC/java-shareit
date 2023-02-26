package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, CommentMapper.class})
abstract public class BookingMapper {
    public Booking toBooking(BookingInputDto bookingInputDto,
                      User user, Item item) {
        return Booking.builder()
                .start(bookingInputDto.getStart())
                .end(bookingInputDto.getEnd())
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
    }

    public abstract BookingDto toBookingDto(Booking booking);

    @Mapping(target = "bookerId", source = "booking.booker.id")
    public abstract BookingShortDto toBookingShortDto (Booking booking);
}
