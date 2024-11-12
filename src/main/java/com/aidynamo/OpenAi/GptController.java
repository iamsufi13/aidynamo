package com.aidynamo.OpenAi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequestMapping("/api/chatgpt")
@RestController
public class GptController {

    @Value("${openai.model}")
    private String model;

    @Autowired
    private RestTemplate template;

    @Value("${openai.api.url}")
    private String url;

    @GetMapping
    public String chatGPTResponse(@RequestParam("prompt") String prompt){
            ChatGPTRequest request = new ChatGPTRequest( model,prompt);
            ChatGPTResponse chatGPTResponse =template.postForObject(url,request,ChatGPTResponse.class);
            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
