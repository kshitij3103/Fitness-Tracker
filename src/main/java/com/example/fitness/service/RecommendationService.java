package com.example.fitness.service;

import com.example.fitness.dto.RecommendationRequest;
import com.example.fitness.model.Activity;
import com.example.fitness.model.Recommendation;
import com.example.fitness.model.User;
import com.example.fitness.repository.AcitivityRepository;
import com.example.fitness.repository.RecommendationRepository;
import com.example.fitness.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final AcitivityRepository acitivityRepository;
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(UserRepository userRepository, AcitivityRepository acitivityRepository, RecommendationRepository recommendationRepository) {
        this.userRepository = userRepository;
        this.acitivityRepository = acitivityRepository;
        this.recommendationRepository = recommendationRepository;
    }

    public Recommendation generateRecommendation(RecommendationRequest recommendationRequest) {
        User user = userRepository.findById(recommendationRequest.getUserId())
                .orElseThrow(() ->new RuntimeException("user not found"+ recommendationRequest.getUserId()));
        Activity activity= acitivityRepository.findById(recommendationRequest.getActivityId())
                .orElseThrow(() ->new RuntimeException("activity not found"+ recommendationRequest.getActivityId()));
        Recommendation recommendation= Recommendation.builder()
                .user(user)
                .activity(activity)
                .improvements(recommendationRequest.getImprovements())
                .suggestions(recommendationRequest.getSuggestions())
                .safety(recommendationRequest.getSafety())
                .build();
        return recommendationRepository.save(recommendation);


    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
