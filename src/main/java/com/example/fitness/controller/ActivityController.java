package com.example.fitness.controller;

import com.example.fitness.dto.ActivityRequest;
import com.example.fitness.dto.ActivityResponse;
import com.example.fitness.service.AcitivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final AcitivityService acitivityService;
    @PostMapping
    public ResponseEntity<ActivityResponse> trackActivity(@RequestBody  ActivityRequest activityRequest){

        return ResponseEntity.ok(acitivityService.trackActivity(activityRequest));
    }
   @GetMapping
   public ResponseEntity<List<ActivityResponse>> getUserActivities(
          @RequestHeader(value = "X-User-ID") String userId
   ){
       return ResponseEntity.ok(acitivityService.getUserActivities(userId));
  }
}
