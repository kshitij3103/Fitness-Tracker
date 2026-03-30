package com.example.fitness.controller;

import com.example.fitness.dto.RecommendationRequest;
import com.example.fitness.model.Recommendation;
import com.example.fitness.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation")
@RequiredArgsConstructor

public class RecommendationController {
    private final RecommendationService recommendationService;
    @PostMapping("/generate")
        public ResponseEntity<Recommendation>generateRecommendation(
                @RequestBody RecommendationRequest recommendationRequest

                ){
            Recommendation recommendation = recommendationService.generateRecommendation(recommendationRequest);
            return ResponseEntity.ok(recommendation);

        }
    @GetMapping ("/user/{userId}")
    public ResponseEntity<List<Recommendation>>getUserRecommendation(
            @PathVariable String userId

    ){
        List<Recommendation> recommendationList = recommendationService.getUserRecommendation(userId);
        return ResponseEntity.ok(recommendationList);

    }
    @GetMapping ("/activity/{activityId}")
    public ResponseEntity<List<Recommendation>>getActivityRecommendation(
            @PathVariable String activityId

    ){
        List<Recommendation> recommendationList = recommendationService.getActivityRecommendation(activityId);
        return ResponseEntity.ok(recommendationList);

    }



}
