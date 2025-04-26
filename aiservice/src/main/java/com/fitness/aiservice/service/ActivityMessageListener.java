package com.fitness.aiservice.service;


import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityMessageListener {

   // @Value("${rabbitmq.queue.name}")
   // private String queueName;

    private final RecommendationRepository recommendationRepository;

    private final ActivityAIService activityAIService;


    @RabbitListener(queues = "activity.queue")
    public void processActivity(Activity activity){
         log.info("Received activity for processing: {}", activity.toString());
         //log.info("Generated Recommendations: {}", activityAIService.generateRecommendation(activity));
         recommendationRepository.save(activityAIService.generateRecommendation(activity));
    }

}
