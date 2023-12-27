package com.example.Ecommerce.interesthistory.controller;

import com.example.Ecommerce.interesthistory.dto.InterestHistoryCreateDto;
import com.example.Ecommerce.interesthistory.dto.InterestHistoryResponseDto;
import com.example.Ecommerce.interesthistory.service.InterestHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interests")
public class InterestHistoryController {

    private final InterestHistoryService interestHistoryService;

    @Autowired
    public InterestHistoryController(InterestHistoryService interestHistoryService) {
        this.interestHistoryService = interestHistoryService;
    }



    @PostMapping
    public ResponseEntity<InterestHistoryCreateDto.Response> addInterest(
            @RequestBody InterestHistoryCreateDto.Request request) {
        try {
            InterestHistoryCreateDto.Response response = interestHistoryService.addInterest(request.getUserId(), request.getProductId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/{interestHistoryId}")
    public ResponseEntity<Void> deleteInterest(@PathVariable Long interestHistoryId) {
        try {
            interestHistoryService.deleteInterest(interestHistoryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<InterestHistoryResponseDto>> getInterestHistories(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Page<InterestHistoryResponseDto> interestHistories = interestHistoryService.getInterestHistories(userId, page, size);
            return new ResponseEntity<>(interestHistories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}