package com.example.fitness.service;

import com.example.fitness.dto.ActivityRequest;
import com.example.fitness.dto.ActivityResponse;
import com.example.fitness.model.Activity;
import com.example.fitness.model.User;
import com.example.fitness.repository.AcitivityRepository;
import com.example.fitness.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class AcitivityService {
    private final AcitivityRepository acitivityRepository;
    private final UserRepository userRepository;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        User user = userRepository.findById(activityRequest.getUserId())
                .orElseThrow(()-> new RuntimeException("Invalid user"+ activityRequest.getUserId()));
        Activity activity =  Activity.builder()
                .user(user)
                .type(activityRequest.getType())

                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())




                .build();
        Activity savedActivity=acitivityRepository.save(activity);
        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(savedActivity.getId());
        activityResponse.setUserId(savedActivity.getUser().getId());
        activityResponse.setType(savedActivity.getType());
        activityResponse.setDuration(savedActivity.getDuration());
        activityResponse.setStartTime(savedActivity.getStartTime());
        activityResponse.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        activityResponse.setCaloriesBurned(savedActivity.getCaloriesBurned());
        activityResponse.setCreatedAt(savedActivity.getCreatedAt());
        activityResponse.setUpdatedAt(activityResponse.getUpdatedAt());
        return activityResponse;

    }

    public List<ActivityResponse> getUserActivities(String userId) {

        List<Activity> activityList=acitivityRepository.findByUserId(userId);
        return activityList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());


    }
}
