package com.example.fitness.service;

import com.example.fitness.dto.RecommendationRequest;
import com.example.fitness.model.Activity;
import com.example.fitness.model.Recommendation;
import com.example.fitness.model.User;
import com.example.fitness.repository.AcitivityRepository;
import com.example.fitness.repository.RecommendationRepository;
import com.example.fitness.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private final UserRepository userRepository;
    private final AcitivityRepository acitivityRepository;
    private final RecommendationRepository recommendationRepository;
    private final AiService aiService;

    public RecommendationService(UserRepository userRepository, AcitivityRepository acitivityRepository, RecommendationRepository recommendationRepository,  AiService aiService) {
        this.userRepository = userRepository;
        this.acitivityRepository = acitivityRepository;
        this.recommendationRepository = recommendationRepository;
        this.aiService=aiService;
    }

    public Recommendation generateRecommendation(RecommendationRequest recommendationRequest) {
        User user = userRepository.findById(recommendationRequest.getUserId())
                .orElseThrow(() ->new RuntimeException("user not found"+ recommendationRequest.getUserId()));
        Activity activity= acitivityRepository.findById(recommendationRequest.getActivityId())
                .orElseThrow(() ->new RuntimeException("activity not found"+ recommendationRequest.getActivityId()));
        Map<String, Object> metrics = activity.getAdditionalMetrics();

        Object sets = (metrics != null) ? metrics.get("sets") : "N/A";
        Object reps = (metrics != null) ? metrics.get("reps") : "N/A";
        List<String> existingExercises = new ArrayList<>();
        if (metrics != null && metrics.get("exercises") != null) {
            existingExercises = (List<String>) metrics.get("exercises");
        }
        String prompt = "Give fitness recommendations for this activity:\n" +
                "Type: " + activity.getType() + "\n" +
                "Duration: " + activity.getDuration() + " minutes\n" +
                "Calories Burned: " + activity.getCaloriesBurned() + "\n" +
                "Sets: " + sets + "\n" +
                "Reps: " + reps + "\n" +
                "Existing Exercises: " + existingExercises + "\n\n" +

                "Also suggest top 3 NEW exercises excluding the above ones.\n\n" +

                "Return strictly in JSON format like:\n" +
                "{\n" +
                "  \"improvements\": [\"\", \"\"],\n" +
                "  \"suggestions\": [\"\", \"\"],\n" +
                "  \"safety\": [\"\", \"\"],\n" +
                "  \"newExercises\": [\"\", \"\", \"\"]\n" +
                "}";

        String aiResponse = aiService.generateRecommendation(prompt);

        aiResponse = aiResponse.replace("```json", "")
                .replace("```", "")
                .trim();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            aiResponse = aiResponse.substring(
                    aiResponse.indexOf("{"),
                    aiResponse.lastIndexOf("}") + 1
            );


            Map<String, List<String>> parsed = objectMapper.readValue(aiResponse, Map.class);

            List<String> improvements = parsed.getOrDefault("improvements", List.of("No data"));
            List<String> suggestions = parsed.getOrDefault("suggestions", List.of("No data"));
            List<String> safety = parsed.getOrDefault("safety", List.of("No data"));
            List<String> newExercises = parsed.getOrDefault("newExercises", List.of());

            // ✅ Remove duplicates
            List<String> finalExistingExercises = existingExercises;
            newExercises = newExercises.stream()
                    .filter(ex -> !finalExistingExercises.contains(ex))
                    .limit(3)
                    .toList();
            String recommendationText = "Top 3 exercises you can try: " +
                    String.join(", ", newExercises);


            Recommendation recommendation = Recommendation.builder()
                    .user(user)
                    .activity(activity)
                    .type(String.valueOf(activity.getType()))
                    .recommendation(recommendationText)
                    .improvements(improvements)
                    .suggestions(suggestions)
                    .safety(safety)
                    .build();

            return recommendationRepository.save(recommendation);

        } catch (Exception e) {
            throw new RuntimeException("Error parsing AI response: " + e.getMessage());
        }


    }

    public List<Recommendation> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}
