package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    public List<Booking> findByBookerId(Long bookerId, Pageable pageable);

    public List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId,
                                                                     LocalDateTime start,
                                                                     LocalDateTime end,
                                                                     Pageable pageable);

    public List<Booking> findByBookerIdAndStartIsBeforeAndEndIsBefore(Long bookerId,
                                                                      LocalDateTime start,
                                                                      LocalDateTime end,
                                                                      Pageable pageable);

    public List<Booking> findByBookerIdAndStartIsAfter(Long bookerId,
                                                       LocalDateTime start,
                                                       Pageable pageable);

    public List<Booking> findByBookerIdAndStatus(Long bookerId,
                                                 Status status,
                                                 Pageable pageable);

    public List<Booking> findByItem_Owner_id(Long ownerId,
                                             Pageable pageable);

    public List<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
            Long ownerId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    public List<Booking> findByItem_Owner_IdAndEndIsBefore(Long ownerId,
                                                           LocalDateTime end,
                                                           Pageable pageable);

    public List<Booking> findByItem_Owner_IdAndStartIsAfter(Long ownerId,
                                                            LocalDateTime start,
                                                            Pageable pageable);

    public List<Booking> findByItem_Owner_IdAndStatus(Long ownerId,
                                                      Status status,
                                                      Pageable pageable);

    public Booking findFirstByItem_IdAndEndBeforeOrderByEndDesc(Long itemId,
                                                            LocalDateTime end);

    public Booking findFirstByItem_IdAndStartAfterAndStatusNotOrderByStartAsc(Long itemId,
                                                                              LocalDateTime start,
                                                                              Status status);

    public Booking findFirstByItem_idAndBooker_IdAndEndBefore(Long itemId,
                                                           Long userId,
                                                           LocalDateTime end);
}
