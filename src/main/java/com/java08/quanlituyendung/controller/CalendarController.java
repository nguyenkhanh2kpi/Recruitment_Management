package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.calendar.CalendarGoogleService;
import com.java08.quanlituyendung.dto.CalendarAddRequestDTO;
import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.ILocalCalendarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.security.GeneralSecurityException;


@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
@Tag(name = "Calendar")
public class CalendarController {

    @Autowired
    private ILocalCalendarService localCalendarService;

    @Autowired
    private CalendarGoogleService calendarGoogleService;

    @Operation(summary = "Lấy danh sách calendar của mình bao gồm các event và interviewRoom")
    @GetMapping("/my-calendar")
    public ResponseEntity<ResponseObject> getMyCalendar(Authentication authentication) {
        return localCalendarService.getMyCalendar(authentication);
    }

    @Operation(summary = "Tạo room phỏng vấn trên google calendar để lấy linkmeet, danh sách attendee là email của những tài khoản sẽ tham gia meet bao gồm candidate và interivewer")
    @PostMapping("/google-send-invitation")
    public ResponseEntity<ResponseObject> calendarGoogle(@RequestBody CalendarAddRequestDTO requestDTO) throws GeneralSecurityException, IOException {
//        try {
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .message("SUCCESS!!")
                    .data(calendarGoogleService.createEvent(requestDTO))
                    .build());

//        }catch (Exception e) {
//            return ResponseEntity.ok(ResponseObject.builder()
//                    .status(HttpStatus.OK.toString())
//                    .message("FAIL")
//                    .data("")
//                    .build());
//        }

    }

}