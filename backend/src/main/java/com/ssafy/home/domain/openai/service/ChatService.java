package com.ssafy.home.domain.openai.service;

import com.ssafy.home.domain.openai.dto.OpenAIParam;
import com.ssafy.home.domain.openai.dto.OpenAIResponseDto;
import com.ssafy.home.external.openai.dto.request.ChatRequest;
import com.ssafy.home.external.openai.dto.response.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ChatService {
    @Qualifier("openaiRestTemplate")
    @Autowired
    private RestTemplate restTemplate;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiUrl;

    public OpenAIResponseDto chat(OpenAIParam openAIParam) {
        StringBuilder sb = new StringBuilder();
        sb.append(openAIParam.getCity()).append(" ").append(openAIParam.getDong()).append("의 ").append("근처 인기있는 편의시설을 주소, 특징들을 5가지 알려줄래?");
        ChatRequest request = new ChatRequest(model, sb.toString());
        ChatResponse response = restTemplate.postForObject(apiUrl, request, ChatResponse.class);

        if(response==null || response.getChoices() == null || response.getChoices().isEmpty()){
            return new OpenAIResponseDto("No Response");
        }
        return new OpenAIResponseDto(response.getChoices().get(0).getMessage().getContent());
    }
}
