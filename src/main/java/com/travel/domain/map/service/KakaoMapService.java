package com.travel.domain.map.service;

import com.travel.domain.map.dto.response.MapResponse;
import com.travel.global.exception.MapException;
import com.travel.global.exception.type.ErrorType;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoMapService {

    private final RestTemplate restTemplate;

    private static final String KAKAO_MAP_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    @Value("${kakao.rest.api.key}")
    private String apiKey;

    public MapResponse getAddress(String address) {

        if (ObjectUtils.isEmpty(address)) {
            throw new MapException(ErrorType.ADDRESS_IS_EMPTY);
        }

        URI uri = builderUriByAddress(address);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, MapResponse.class).getBody();

    }

    public URI builderUriByAddress(String address) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(KAKAO_MAP_URL);
        uriBuilder.queryParam("query", address);

        URI uri = uriBuilder.build().encode().toUri(); //encode default utf-8
        log.info("[Build by Uri Address Search] address : {}, uri : {}", address, uri);

        return uri;
    }
}
