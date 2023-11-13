package com.java08.quanlituyendung;

import com.java08.quanlituyendung.utils.Constant;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@Transactional
class ProfileTests {
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    private String login(MockMvc mockMvc, String email, String password) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("client_id", "fooClientIdPassword");
        String requestBody = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        ResultActions result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("fooClientIdPassword", "secret"))
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));
        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

    @Test
    void getProfile() throws Exception {
        String token = login(mockMvc,"tuanbmt123123@gmail.com","1234");
        MvcResult result = mockMvc.perform(get("/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value(Constant.SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();
    }

    @Test
    void update() throws Exception {
        String requestBodyCreate = "{\r\n" + //
                "    \"fullName\": \"Nguyen Thanh Da\",\r\n" + //
                "    \"phone\": \"0369479785\",\r\n" + //
                "    \"gender\": \"MALE\",\r\n" + //
                "    \"address\": \"HCM City\",\r\n" + //
                "    \"dob\": \"24/10/2002\",\r\n" + //
                "    \"avatar\": \"https://images2.thanhnien.vn/528068263637045248/2023/6/26/blackpinksanghanoivietnambieudien11-1687757871275987370049.jpg\",\r\n" + //
                "    \"language\": \"Java\"\r\n" + //
                "}";
        String token = login(mockMvc,"tuanbmt123123@gmail.com","1234");
        MvcResult result = mockMvc.perform(put("/profile")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyCreate)
                        .accept("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.toString()))
                .andExpect(jsonPath("$.message").value(Constant.UPDATE_PROFILE_SUCCESS))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andReturn();
    }

}