package com.travel.domain.cart.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.travel.domain.accommodation.controller.AccommodationController;
import com.travel.domain.cart.dto.request.CartRequest;
import com.travel.domain.cart.dto.response.CartResponse;
import com.travel.domain.cart.service.CartService;
import com.travel.domain.config.TestConfig;
import com.travel.domain.member.entity.Member;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @InjectMocks
    private AccommodationController controller;

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
    @DisplayName("장바구니 추가 api 테스트")
    void addToCart_Success() throws Exception {
        CartResponse cartResponse = CartResponse.builder()
            .accommodationName("Test Hotel")
            .productName("Deluxe Room")
            .standardNumber(2)
            .maximumNumber(4)
            .checkInDate(LocalDate.of(2024, 7, 1))
            .checkOutDate(LocalDate.of(2024, 7, 5))
            .personNumber(2)
            .price(500)
            .nights(4)
            .totalPrice(2000)
            .build();

        Mockito.when(cartService.addToCart(anyLong(), Mockito.any(CartRequest.class)))
            .thenReturn(cartResponse);

        mockMvc.perform(post("/api/auth/cart")
                .header("mock-token", member.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accommodationId\":1,"
                    + "\"productId\":1,"
                    + "\"checkInDate\":\"2024-07-01\","
                    + "\"checkOutDate\":\"2024-07-05\","
                    + "\"personNumber\":2}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accommodation_name", is("Test Hotel")))
            .andExpect(jsonPath("$.product_name", is("Deluxe Room")))
            .andExpect(jsonPath("$.nights", is(4)))
            .andExpect(jsonPath("$.total_price", is(2000)));
    }

    @Test
    @WithMockUser
    @DisplayName("장바구니 조회 api 테스트")
    void getCartByMemberId_Success() throws Exception {
        CartResponse cartResponse = CartResponse.builder()
            .accommodationName("Test Hotel")
            .productName("Deluxe Room")
            .standardNumber(2)
            .maximumNumber(4)
            .checkInDate(LocalDate.of(2024, 7, 1))
            .checkOutDate(LocalDate.of(2024, 7, 5))
            .personNumber(2)
            .price(500)
            .nights(4)
            .totalPrice(2000)
            .build();

        List<CartResponse> mockResponse = List.of(cartResponse);

        Mockito.when(cartService.getCartByMemberId(anyLong())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/auth/cart")
                .header("mock-token", member.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].accommodation_name", is("Test Hotel")))
            .andExpect(jsonPath("$[0].product_name", is("Deluxe Room")))
            .andExpect(jsonPath("$[0].nights", is(4)))
            .andExpect(jsonPath("$[0].total_price", is(2000)));
    }

    @Test
    @WithMockUser
    @DisplayName("장바구니 삭제 api 테스트")
    void removeFromCart_Success() throws Exception {
        Mockito.doNothing().when(cartService).removeFromCart(anyLong());

        mockMvc.perform(delete("/api/auth/cart/{cartId}", 1L)
                .header("mock-token", member.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
