package com.travel.global.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@AllArgsConstructor
@Getter
public enum ErrorType {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    AUTH_NOT_EXIST(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "조회 결과가 없습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),
    ALREADY_RESERVATION(HttpStatus.CONFLICT, "이미 예약된 숙박 정보가 존재합니다."),
    INVALID_NUMBER_OF_PEOPLE(HttpStatus.BAD_REQUEST, "잘못된 인원"),
    INCLUDES_FULLY_BOOKED_PRODUCT(HttpStatus.BAD_REQUEST, "예약 마감된 객실이 포함되어 있습니다."),
    INVALID_CHECK_IN(HttpStatus.BAD_REQUEST, "체크인 날짜는 오늘 또는 이후 날짜여야 합니다."),
    INVALID_CHECK_OUT(HttpStatus.BAD_REQUEST, "체크아웃 날짜는 체크인 날짜 이후여야 합니다."),
    EMAIL_SEND_FAILURE(HttpStatus.SERVICE_UNAVAILABLE, "이메일 전송에 실패했습니다."),
    TEMPLATE_LOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "템플릿 로드에 실패했습니다."),
    INVALID_EMAIL_AND_PASSWORD(HttpStatus.BAD_REQUEST, "이메일 또는 비밀번호가 일치하지 않습니다."),
    DUPLICATED_MEMBER(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    DUPLICATED_REVIEW(HttpStatus.BAD_REQUEST, "이미 작성하셨습니다. "),
    FAILED_TO_CREATE_REVIEW(HttpStatus.BAD_REQUEST, "체크아웃 후에 리뷰를 작성해주세요"),
    ADDRESS_IS_EMPTY(HttpStatus.BAD_REQUEST, "주소가 없습니다"),
    ALREADY_IN_CART(HttpStatus.BAD_REQUEST, "해당 품목이 장바구니에 이미 존재합니다."),

    // auth
    TOKEN_AUTHORIZATION_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않음"),
    INVALID_TOKEN(HttpStatus.FORBIDDEN, "토큰이 유효하지않습니다."),
    TOKEN_AUTHORIZATION_FAIL(HttpStatus.FORBIDDEN, "토큰 인증 실패"),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다.");

    private final HttpStatusCode statusCode;
    private final String message;
}