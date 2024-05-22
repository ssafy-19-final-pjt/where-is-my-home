package com.ssafy.home.external.openai.dto.response;

import com.ssafy.home.external.openai.dto.request.Message;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ChatResponse {
    private List<Choice> choices;

    @Getter
    public static class Choice{
        private int index;
        private Message message;
    }
}
