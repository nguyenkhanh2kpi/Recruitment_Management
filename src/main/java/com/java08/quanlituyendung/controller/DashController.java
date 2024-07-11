package com.java08.quanlituyendung.controller;


import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.ICvService;
import com.java08.quanlituyendung.service.impl.DashService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/dash")
@Tag(name = "Dash")
public class DashController {
    @Autowired
    private DashService dashService;

    @Operation(summary = "reccer")
    @GetMapping("/reccer")
    public ResponseEntity<ResponseObject> getAllCv(Authentication authentication) {
        return dashService.getReccer(authentication);
    }
}
