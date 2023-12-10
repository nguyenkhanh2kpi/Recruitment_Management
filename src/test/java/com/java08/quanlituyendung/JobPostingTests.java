package com.java08.quanlituyendung;

import com.java08.quanlituyendung.dto.JobPostingDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.IJobPostingService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class JobPostingTests {

    @Autowired private MockMvc mockMvc;
    @MockBean private IJobPostingService jobPostingService;

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

//    @Test
//    public void testGetAllJobPosting() throws Exception {
//        String tk = login(mockMvc, "reccer1@gmail.com", "1234");
//        JobPostingDTO jobPostingDTO1 = JobPostingDTO.builder()
//                .id(1L)
//                .name("Job 1")
//                .position("Position 1")
//                .language("English")
//                .location("City 1")
//                .salary("50000")
//                .number("3")
//                .workingForm("Full-time")
//                .sex("Male")
//                .experience("3 years")
//                .detailLocation("Street A, City 1")
//                .detailJob("Job details 1")
//                .requirements("Requirements for Job 1")
//                .interest("Interest for Job 1")
//                .image("image1.jpg")
//                .status(true)
//                .listCandidate(new ArrayList<>())
//                .build();
//
//        JobPostingDTO jobPostingDTO2 = JobPostingDTO.builder()
//                .id(2L)
//                .name("Job 2")
//                .position("Position 2")
//                .language("Spanish")
//                .location("City 2")
//                .salary("60000")
//                .number("2")
//                .workingForm("Part-time")
//                .sex("Female")
//                .experience("2 years")
//                .detailLocation("Street B, City 2")
//                .detailJob("Job details 2")
//                .requirements("Requirements for Job 2")
//                .interest("Interest for Job 2")
//                .image("image2.jpg")
//                .status(true)
//                .listCandidate(new ArrayList<>())
//                .build();
//
//
//        List<JobPostingDTO> jobPostingList = Arrays.asList(jobPostingDTO1, jobPostingDTO2);
//        ResponseObject responseObject = new ResponseObject("OK", "Success!", jobPostingList);
//        when(jobPostingService.getAllJobPosting()).thenReturn(ResponseEntity.ok(responseObject));
//        mockMvc.perform(get("/job-posting")
//                .header("authorization","Bearer "+ tk))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("Success!"))
//                .andExpect(jsonPath("$.data", hasSize(jobPostingList.size())))
//                .andExpect(jsonPath("$.data[0].id").value(jobPostingDTO1.getId()))
//                .andExpect(jsonPath("$.data[0].name").value(jobPostingDTO1.getName()))
//                .andExpect(jsonPath("$.data[0].position").value(jobPostingDTO1.getPosition()))
//                .andExpect(jsonPath("$.data[0].language").value(jobPostingDTO1.getLanguage()))
//                .andExpect(jsonPath("$.data[1].id").value(jobPostingDTO2.getId()))
//                .andExpect(jsonPath("$.data[1].name").value(jobPostingDTO2.getName()))
//                .andExpect(jsonPath("$.data[1].position").value(jobPostingDTO2.getPosition()))
//                .andExpect(jsonPath("$.data[1].language").value(jobPostingDTO2.getLanguage()))
//                .andDo(print());
//    }

    @Test
    public void testCreateJobPosting() throws Exception {
        String tk = login(mockMvc, "reccer1@gmail.com", "1234");
        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .id(1L)
                .name("Job 1")
                .position("Position 1")
                .language("English")
                .location("City 1")
                .salary("50000")
                .number("3")
                .workingForm("Full-time")
                .sex("Male")
                .experience("3 years")
                .detailLocation("Street A, City 1")
                .detailJob("Job details 1")
                .requirements("Requirements for Job 1")
                .interest("Interest for Job 1")
                .image("image1.jpg")
                .status(true)
                .listCandidate(new ArrayList<>())
                .build();


        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(jobPostingDTO);

        ResponseObject responseObject = new ResponseObject("OK", "Success!", jobPostingDTO);

//        doReturn(ResponseEntity.ok().body(responseObject))
//                .when(jobPostingService).save(Mockito.any(JobPostingDTO.class));
        doReturn(ResponseEntity.ok().body(responseObject))
                .when(jobPostingService)
                .save(Mockito.any(JobPostingDTO.class), Mockito.any(Authentication.class));
        mockMvc.perform(post("/job-posting")
                        .header("Authorization", "Bearer " + tk)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Success!"))
                .andExpect(jsonPath("$.data.id").value(jobPostingDTO.getId()))
                .andExpect(jsonPath("$.data.name").value(jobPostingDTO.getName()))
                .andExpect(jsonPath("$.data.position").value(jobPostingDTO.getPosition()))
                .andExpect(jsonPath("$.data.language").value(jobPostingDTO.getLanguage()))
                .andDo(print());
    }

//    @Test
//    public void testUpdateJobPosting() throws Exception {
//        String tk = login(mockMvc, "reccer1@gmail.com", "1234");
//        long JobPostingId = 1L;
//
//        JobPostingDTO existingJobPosting = JobPostingDTO.builder()
//                .id(JobPostingId)
//                .name("Job 1")
//                .position("Position 1")
//                .language("English")
//                .location("City 1")
//                .salary("50000")
//                .number("3")
//                .workingForm("Full-time")
//                .sex("Male")
//                .experience("3 years")
//                .detailLocation("Street A, City 1")
//                .detailJob("Job details 1")
//                .requirements("Requirements for Job 1")
//                .interest("Interest for Job 1")
//                .image("image1.jpg")
//                .status(true)
//                .listCandidate(new ArrayList<>())
//                .build();
//
//
//        JobPostingDTO updatedJobPosting = JobPostingDTO.builder()
//                .id(JobPostingId)
//                .name("Updated Job 1")
//                .position("Updated Position 1")
//                .language("English")
//                .location("City 1")
//                .salary("60000")
//                .number("5")
//                .workingForm("Full-time")
//                .sex("Male")
//                .experience("5 years")
//                .detailLocation("Street A, City 1")
//                .detailJob("Updated Job details 1")
//                .requirements("Updated Requirements for Job 1")
//                .interest("Updated Interest for Job 1")
//                .image("image1.jpg")
//                .status(true)
//                .listCandidate(new ArrayList<>())
//                .build();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        String requestBody = objectMapper.writeValueAsString(updatedJobPosting);
//
//        ResponseObject responseObject = new ResponseObject("OK", "Success!", updatedJobPosting);
//
//        doReturn(ResponseEntity.ok().body(responseObject))
//                .when(jobPostingService)
//                .save(Mockito.any(JobPostingDTO.class), Mockito.any(Authentication.class));
//
//
//        mockMvc.perform(put("/job-posting/{id}", JobPostingId)
//                        .header("Authorization", "Bearer " + tk)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("Success!"))
//                .andExpect(jsonPath("$.data.id").value(updatedJobPosting.getId()))
//                .andExpect(jsonPath("$.data.name").value(updatedJobPosting.getName()))
//                .andExpect(jsonPath("$.data.position").value(updatedJobPosting.getPosition()))
//                .andExpect(jsonPath("$.data.language").value(updatedJobPosting.getLanguage()))
//                .andDo(print());
//    }

//    @Test
//    public void testDeleteJobPosting() throws Exception {
//        String tk = login(mockMvc, "reccer1@gmail.com", "1234");
//        long jobPostingIdToDelete = 1L;
//
//        doReturn(ResponseEntity.ok().body(new ResponseObject("OK", "Job posting deleted successfully.", null)))
//                .when(jobPostingService).delete(jobPostingIdToDelete);
//
//        mockMvc.perform(delete("/job-posting/{id}", jobPostingIdToDelete)
//                        .header("Authorization", "Bearer " + tk))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("Job posting deleted successfully."))
//                .andExpect(jsonPath("$.data").isEmpty())
//                .andDo(print());
//    }

    @Test
    public void testGetDetailJobPosting() throws Exception {
        String tk = login(mockMvc, "reccer1@gmail.com", "1234");
        long jobPostingId = 1L;

        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .id(jobPostingId)
                .name("Job 1")
                .position("Position 1")
                .language("English")
                .location("City 1")
                .salary("50000")
                .number("3")
                .workingForm("Full-time")
                .sex("Male")
                .experience("3 years")
                .detailLocation("Street A, City 1")
                .detailJob("Job details 1")
                .requirements("Requirements for Job 1")
                .interest("Interest for Job 1")
                .image("image1.jpg")
                .status(true)
                .listCandidate(new ArrayList<>())
                .build();

        doReturn(ResponseEntity.ok().body(new ResponseObject("OK", "Job posting details retrieved successfully.", jobPostingDTO)))
                .when(jobPostingService).getDetailJobPosting(jobPostingId);

        mockMvc.perform(get("/job-posting/{id}", jobPostingId)
                        .header("Authorization", "Bearer " + tk))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("Job posting details retrieved successfully."))
                .andExpect(jsonPath("$.data.id").value(jobPostingDTO.getId()))
                .andExpect(jsonPath("$.data.name").value(jobPostingDTO.getName()))
                .andExpect(jsonPath("$.data.position").value(jobPostingDTO.getPosition()))
                .andExpect(jsonPath("$.data.language").value(jobPostingDTO.getLanguage()));
    }

}
