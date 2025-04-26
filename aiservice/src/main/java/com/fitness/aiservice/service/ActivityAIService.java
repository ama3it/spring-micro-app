package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityAIService {
    private final GeminiService geminiService;

    public  Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse= geminiService.getAnswer(prompt);
        return processAiResponse(activity,aiResponse);

    }


    private Recommendation processAiResponse(Activity activity, String aiResponse){
          try{
               ObjectMapper objectMapper = new ObjectMapper();
               JsonNode jsonNode = objectMapper.readTree(aiResponse);

               JsonNode textNode=jsonNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");
               String jsonContent=textNode.asText().replaceAll("```json\\n","").replaceAll("\\n```", "").trim();
               //log.info("AI response from activity: {}", jsonContent);

               JsonNode analysisJson= objectMapper.readTree(jsonContent);
               JsonNode analysisNode=analysisJson.path("analysis");
               StringBuilder fullAnalysis =new StringBuilder();

               addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall:");
               addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace:");
               addAnalysisSection(fullAnalysis,analysisNode,"heartRate","Heart Rate:");
               addAnalysisSection(fullAnalysis,analysisNode,"caloriesBurned","Calories:");

               List<String> improvements=extractImprovements(analysisJson.path("improvements"));
               List<String> suggestions=extractSuggestions(analysisJson.path("suggestions "));
               List <String> safety= extractSafetyGuidelines(analysisJson.path("safety"));
               return Recommendation.builder()
                       .activityId(activity.getId())
                       .userId(activity.getUserId())
                       .activity(activity.getType())
                       .recommendation(fullAnalysis.toString().trim())
                       .improvements(improvements)
                       .suggestions(suggestions)
                       .safety(safety)
                       .createdAt(LocalDateTime.now())
                       .build();



          }
          catch (Exception e){
             // e.printStackTrace();
              return CreateDeafaultRecommendation(activity);
          }
    }

    private Recommendation CreateDeafaultRecommendation(Activity activity) {
         return Recommendation.builder()
                 .activityId(activity.getId())
                 .userId(activity.getUserId())
                 .activity(activity.getType())
                 .recommendation("Unable to generate detailed analysis.")
                 .improvements(Collections.singletonList("Continue with your current goal."))
                 .suggestions(Collections.singletonList("No suggestions right now."))
                 .safety(Arrays.asList("Always warm up before exercise", "stay Hydrated", "Listen to your body"))
                 .createdAt(LocalDateTime.now())
                 .build();

    }

    private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
        List<String> safetyList = new ArrayList<>();

        if(safetyNode.isArray()){
            safetyNode.forEach(safety -> {

                safetyList.add(safety.asText());
            });
        }
        return safetyList.isEmpty()? Collections.singletonList("Follow general safety guidelines .") : safetyList;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {

        List<String> suggestions = new ArrayList<>();

        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                String desc = suggestion.path("description").asText();
                suggestions.add(String.format("%s: %s", workout, desc));
            });
        }
        return suggestions.isEmpty()? Collections.singletonList("No specific suggestions.") : suggestions;
    }

    private List<String> extractImprovements(JsonNode improvements) {
       List<String> improvementsList=new ArrayList<>();
       if(improvements.isArray()){
           improvements.forEach(improvement->{
               String area = improvement.path("area").asText();
               String detail = improvement.path("recommendation").asText();
               improvementsList.add(String.format("%s: %s", area, detail));
           });
       }
       return improvementsList.isEmpty()? Collections.singletonList("No specific improvements") : improvementsList;
    }



    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(key).append(" ").append(analysisNode.path(key).asText()).append("\n\n");
        }

    }

    private String createPromptForActivity(Activity activity){
        return String.format("""
                
                Analyze this fitness activity and provide detailed recommendations in the following Exact JSON formate:
                {
                  "analysis":{
                    "overall":"Overall analysis here",
                    "pace":"pace analysis here",
                    "heartRate": "Heart rate analysis here",
                    "caloriesBurned":"Calories burned analysis here",
                  },
                  "improvements":[{
                    "area":"Area name",
                    "recommendation":"Recommendations for improve",
                  }],
                  "suggestions":[{
                    "workout":"Workout name",
                    "description": "Detailed Workout description"
                  }],
                  "safety":[
                    "Safety Point 1",
                    "Safety Point 2"
                  ]
                }
                
                Analyze this activity:
                Activity type: %s
                Duration: %d minutes
                Calories Burned: %d
                Additional Metrics: %s
                
                Provide Detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
                Ensure the response follows the EXACT JSON  formate shown above.  
                """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionalMetrics()

                );


    }
}
