package com.ssafy.home.domain.openai.controller;

import com.ssafy.home.domain.openai.dto.OpenAIParam;
import com.ssafy.home.domain.openai.dto.OpenAIResponseDto;
import com.ssafy.home.domain.openai.service.ChatService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "OpenAi", description = "openai 기반 응답 서비스")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("")
    public ResponseEntity<OpenAIResponseDto> chat(@ModelAttribute OpenAIParam openAIParam){
        return ResponseEntity.ok(chatService.chat(openAIParam));
    }
}
