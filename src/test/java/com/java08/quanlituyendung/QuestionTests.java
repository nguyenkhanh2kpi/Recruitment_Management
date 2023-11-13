package com.java08.quanlituyendung;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java08.quanlituyendung.dto.QuestionPayload.QuestionRequestDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.IQuestionService;
import com.java08.quanlituyendung.utils.Constant;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
 public class QuestionTests {

    @MockBean
    private IQuestionService questionService;
    @Autowired
    private MockMvc mockMvc;


    private String login(MockMvc mockMvc) throws Exception {
        String email="admin@gmail.com";
        String password ="1234";
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
    public void testCreateQuestion() throws Exception {
        String tk = login(mockMvc);
        QuestionRequestDTO question = new QuestionRequestDTO();
        question.setQuestion("What is your name?");
        ResponseObject responseObject = new ResponseObject("OK", Constant.SUCCESS,null);
        mockMvc.perform(post("/question")
                        .header("authorization","Bearer "+ tk)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(question)))
                .andExpect(status().isOk());
    }
    @Test
    public void testDelete() throws Exception {
        String tk = login(mockMvc);
        Long id = 1L;

        mockMvc.perform(delete("/question/{id}", id)
                        .header("authorization","Bearer "+ tk)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(id)))
                .andExpect(status().isOk());
    }
    @Test
    public void testUpdateQuestion() throws Exception {
        String tk = login(mockMvc);
        long id = 1L;
        QuestionRequestDTO question = new QuestionRequestDTO();
        question.setId(id);
        question.setQuestion("What is your name?");

        mockMvc.perform(put("/question")
                        .header("authorization","Bearer "+ tk)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(question)))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetAllQuestion() throws Exception {
        String tk = login(mockMvc);
        mockMvc.perform(get("/question")
                        .header("authorization","Bearer "+ tk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetOneById() throws Exception {
        Long id = 1L;
        String tk = login(mockMvc);
        mockMvc.perform(get("/question/{id}", id)
                        .header("authorization","Bearer "+ tk)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    public void testGetAllBySkill() throws Exception {
        Long id = 1L;
        String tk = login(mockMvc);
        mockMvc.perform(get("/skill")
                        .header("authorization","Bearer "+ tk)
                        .param("skillIds", "1", "2", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


