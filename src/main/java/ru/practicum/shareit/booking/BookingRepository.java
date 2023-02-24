package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    public List<Booking> findByBookerId(Long bookerId, Sort sort);
    public List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfter(Long bookerId,
                                                                     LocalDateTime start,
                                                                     LocalDateTime end,
                                                                     Sort sort);

    public List<Booking> findByBookerIdAndStartIsBeforeAndEndIsBefore(Long bookerId,
                                                                      LocalDateTime start,
                                                                      LocalDateTime end,
                                                                      Sort sort);

    public List<Booking> findByBookerIdAndStartIsAfter(Long bookerId,
                                                       LocalDateTime start,
                                                       Sort sort);

    public List<Booking> findByBookerIdAndStatus(Long bookerId,
                                                 Status status,
                                                 Sort sort);

    public List<Booking> findByItem_Owner_id(Long ownerId,
                                             Sort sort);

    public List<Booking> findByItem_Owner_IdAndStartIsBeforeAndEndIsAfter(
            Long ownerId,
            LocalDateTime start,
            LocalDateTime end,
            Sort sort
    );

    public List<Booking> findByItem_Owner_IdAndEndIsBefore(Long ownerId,
                                                           LocalDateTime end,
                                                           Sort sort);

    public List<Booking> findByItem_Owner_IdAndStartIsAfter(Long ownerId,
                                                            LocalDateTime start,
                                                            Sort sort);

    public List<Booking> findByItem_Owner_IdAndStatus(Long ownerId,
                                                      Status status,
                                                      Sort sort);

    public Booking findFirstByItem_IdAndEndBeforeOrderByEndDesc(Long itemId,
                                                            LocalDateTime end);

    public Booking findFirstByItem_IdAndStartAfterOrderByStartAsc(Long itemId,
                                                               LocalDateTime start);
}
