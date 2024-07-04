package com.java08.quanlituyendung.controller;

import com.java08.quanlituyendung.dto.ResponseObject;
import com.java08.quanlituyendung.service.impl.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;
    @Operation(summary = "Phương thức này vừa thêm vừa xóa")
    @PostMapping("/add")
    public ResponseEntity<ResponseObject> addFavorite(@RequestParam Long jobPostingId, Authentication authentication) {
        try {
            var favorite = favoriteService.addFavorite(authentication, jobPostingId);
            return ResponseEntity.ok(ResponseObject.builder()
                    .status(HttpStatus.OK.toString())
                    .message("Favorite")
                    .data(favorite)
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseObject.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                            .message("Failed to add favorite: " + e.getMessage())
                            .build());
        }
    }

    @GetMapping("/my-wishlist")
    public ResponseEntity<ResponseObject> getMyWishlist(Authentication authentication) {
        var favorites = favoriteService.getUserFavorites(authentication);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK.toString())
                .message("Wishlist retrieved successfully")
                .data(favorites)
                .build());
    }
}