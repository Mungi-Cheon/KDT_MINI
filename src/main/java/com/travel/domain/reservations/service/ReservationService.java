package com.travel.domain.reservations.service;

import com.travel.domain.accommodation.entity.Accommodation;
import com.travel.domain.accommodation.repository.AccommodationRepository;
import com.travel.domain.email.service.EmailService;
import com.travel.domain.member.entity.Member;
import com.travel.domain.member.repository.MemberRepository;
import com.travel.domain.product.entity.Product;
import com.travel.domain.product.entity.ProductInfoPerNight;
import com.travel.domain.product.repository.ProductInfoPerNightRepository;
import com.travel.domain.product.repository.ProductRepository;
import com.travel.domain.reservations.dto.request.ReservationCancelRequest;
import com.travel.domain.reservations.dto.request.ReservationRequest;
import com.travel.domain.reservations.dto.response.ReservationCancelResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryListResponse;
import com.travel.domain.reservations.dto.response.ReservationHistoryResponse;
import com.travel.domain.reservations.dto.response.ReservationResponse;
import com.travel.domain.reservations.entity.Reservation;
import com.travel.domain.reservations.repository.ReservationRepository;
import com.travel.global.exception.ReservationsException;
import com.travel.global.exception.type.ErrorType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final AccommodationRepository accommodationRepository;

    private final ReservationRepository reservationRepository;

    private final ProductRepository productRepository;

    private final ProductInfoPerNightRepository productInfoPerNightRepository;

    private final MemberRepository memberRepository;

    private final EmailService emailService;

    private static final String MAIL_SUBJECT_CONFIRMATION = "예약 확인서";
    private static final String MAIL_SUBJECT_CANCEL = "예약 취소 확인서";
    private static final String MAIL_TEMPLATE_CONFIRMATION = "templates/reservation-confirmation.html";
    private static final String MAIL_TEMPLATE_CANCEL = "templates/reservation-cancel.html";

    @Transactional(readOnly = true)
    public ReservationHistoryListResponse getReservationHistories(Long memberId) {

        List<Reservation> reservations = reservationRepository.findByMemberId(memberId);
        if (reservations.isEmpty()) {
            return ReservationHistoryListResponse.builder()
                .reservationHistoryList(new ArrayList<>()).build();
        }

        List<ReservationHistoryResponse> rhList = createReservationHistoryList(reservations);
        return ReservationHistoryListResponse.builder()
            .reservationHistoryList(rhList)
            .build();
    }

    @CacheEvict(value = "accommodations", allEntries = true)
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ReservationResponse createReservation(ReservationRequest request,
        Long memberId) {
        Member member = memberRepository.getMember(memberId);

        LocalDate checkInDate = request.getCheckInDate();
        LocalDate checkOutDate = request.getCheckOutDate();
        int nights = calcNight(checkInDate, checkOutDate);
        long productId = request.getProductId();

        Accommodation accommodation = accommodationRepository
            .getByIdJoinImagesAndOptions(request.getAccommodationId());

        reservationRepository.checkExistReservation(memberId, productId, checkInDate,
            checkOutDate);

        Product product = productRepository.
            getByAccommodationIdAndProductIdJoinImagesAndOption(
                request.getAccommodationId(), productId);
        List<ProductInfoPerNight> productInfoList = findProductInfoPerNightList(productId,
            checkInDate, checkOutDate.minusDays(1));
        decreaseCountByOne(productInfoList);

        Reservation reservation = createReservation(
            member, accommodation,
            product, request.getPersonNumber(),
            productInfoList.get(0).getPrice(), nights,
            checkInDate, checkOutDate
        );

        Reservation saved = reservationRepository.save(reservation);

        emailService.sendReservationConfirmation(
            member.getEmail(),
            saved,
            MAIL_TEMPLATE_CONFIRMATION,
            MAIL_SUBJECT_CONFIRMATION
        );
        return ReservationResponse.from(saved);
    }

    @Transactional()
    public ReservationCancelResponse cancelReservation(
        Long memberId, ReservationCancelRequest request) {
        Reservation target = reservationRepository
            .getReservationByIdAndMemberId(request.getId(), memberId);

        reservationRepository.delete(target);
        reservationRepository.flush();

        Member member = memberRepository.getMember(memberId);
        Product product = target.getProduct();

        emailService.sendReservationConfirmation(member.getEmail(), target,
            MAIL_TEMPLATE_CANCEL, MAIL_SUBJECT_CANCEL);

        return ReservationCancelResponse.from(
            target,
            target.getAccommodation().getName(),
            product.getType(),
            product.getStandardNumber(),
            product.getMaximumNumber(),
            LocalDateTime.now().withNano(0)
        );
    }

    private int calcNight(LocalDate checkInDate, LocalDate checkOutDate) {
        return (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    private List<ReservationHistoryResponse> createReservationHistoryList(
        List<Reservation> reservations) {
        List<ReservationHistoryResponse> rhList = new ArrayList<>();

        reservations.forEach(reservation -> {
            Product product = reservation.getProduct();
            rhList.add(
                ReservationHistoryResponse.from(
                    reservation,
                    reservation.getAccommodation().getId(),
                    reservation.getAccommodation().getName(),
                    product.getType(),
                    product.getStandardNumber(),
                    product.getMaximumNumber(),
                    product.getProductImage().getImageUrl1()
                )
            );
        });
        return rhList;
    }

    private void decreaseCountByOne(
        List<ProductInfoPerNight> productInfoPerNightsList) {
        for (ProductInfoPerNight pi : productInfoPerNightsList) {
            if (pi.getCount() <= 0) {
                throw new ReservationsException(ErrorType.INCLUDES_FULLY_BOOKED_PRODUCT);
            }
            pi.decreaseCountByOne();
        }
    }

    private List<ProductInfoPerNight> findProductInfoPerNightList(long productId,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return productInfoPerNightRepository
            .findByProductIdAndDateRangeWithPessimisticLock(productId, checkInDate,
                checkOutDate);
    }

    private Reservation createReservation(
        Member member, Accommodation accommodation,
        Product product, Integer personNumber,
        int price, int night,
        LocalDate checkInDate, LocalDate checkOutDate) {
        return Reservation.builder()
            .member(member)
            .accommodation(accommodation)
            .product(product)
            .personNumber(personNumber)
            .price(price)
            .night(night)
            .checkInDate(checkInDate)
            .checkOutDate(checkOutDate)
            .build();
    }
}