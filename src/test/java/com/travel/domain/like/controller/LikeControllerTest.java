package com.travel.domain.like.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.domain.accommodation.dto.response.AccommodationResponse;
import com.travel.domain.config.TestConfig;
import com.travel.domain.like.dto.request.LikeRequest;
import com.travel.domain.like.dto.response.LikeResponse;
import com.travel.domain.like.service.LikeService;
import com.travel.domain.member.entity.Member;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    private LikeResponse likeResponse;

    private AccommodationResponse accommodationResponse;

    private Member member;

    @BeforeEach
    public void setUp() {
        member = Member.builder()
            .id(1L)
            .email("test@gmail.com")
            .name("testMember")
            .password("testMember1@#$")
            .build();
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요 클릭 api 테스트")
    void clickLike_Success() throws Exception {
        likeResponse = LikeResponse.builder()
            .liked(true)
            .likeCount(1)
            .build();

        LikeRequest likeRequest = new LikeRequest(1L);

        Mockito.when(likeService.clickLike(anyLong(), any(LikeRequest.class)))
            .thenReturn(likeResponse);

        mockMvc.perform(post("/api/auth/like")
                .header("mock-token", member.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(likeRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.liked", is(true)))
            .andExpect(jsonPath("$.like_count", is(1)));
    }

    @Test
    @WithMockUser
    @DisplayName("좋아요한 숙소 조회 api 테스트")
    void getMyLikedAccommodations_Success() throws Exception {
        accommodationResponse = AccommodationResponse.builder()
            .id(1L)
            .name("Test Hotel")
            .category("호텔")
            .build();

        List<AccommodationResponse> mockResponse = List.of(accommodationResponse);
        Mockito.when(likeService.getLikedAccommodations(anyLong()))
            .thenReturn(mockResponse);

        mockMvc.perform(get("/api/auth/like")
                .header("mock-token", member.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].name", is("Test Hotel")))
            .andExpect(jsonPath("$[0].category", is("호텔")));
    }
}
