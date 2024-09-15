package com.travel.domain.reservations.repository;

import com.travel.domain.reservations.entity.Reservation;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.member.id =:memberId AND r.deletedAt IS NULL")
    List<Reservation> findByMemberId(@Param("memberId") Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r "
        + "FROM Reservation r "
        + "WHERE r.member.id = :memberId "
        + "AND r.product.id = :productId "
        + "AND :checkInDate <= r.checkInDate "
        + "AND :checkOutDate >= r.checkOutDate")
    List<Reservation> findAlreadyReservationWithPessimisticLock(
        @Param("memberId") Long memberId,
        @Param("productId") Long productId,
        @Param("checkInDate") LocalDate checkInDate,
        @Param("checkOutDate") LocalDate checkOutDate);

    Optional<Reservation> findByIdAndMemberId(Long id, Long memberId);


    default void checkExistReservation(
        Long memberId, Long productId,
        LocalDate checkInDate, LocalDate checkOutDate) {
        List<Reservation> reservationList = findAlreadyReservationWithPessimisticLock(
            memberId, productId, checkInDate, checkOutDate);

        if (!reservationList.isEmpty()) {
            throw new ReservationsException(ErrorType.ALREADY_RESERVATION);
        }
    }

    default Reservation getReservationByIdAndMemberId(Long id, Long memberId) {
        return findByIdAndMemberId(id, memberId).orElseThrow(
            () -> new ReservationsException(ErrorType.NOT_FOUND));
    }

    default Reservation getReservationById(Long id){
        return findById(id).orElseThrow(
            () -> new ReservationsException(ErrorType.NOT_FOUND));
    }
}
