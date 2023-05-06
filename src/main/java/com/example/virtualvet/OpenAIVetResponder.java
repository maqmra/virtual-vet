package com.example.virtualvet;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
public class OpenAIVetResponder implements VetResponder {

    private static OpenAiService openAiService;

    @Value("${openai.key}")
    private String apiKey;

    @Value("${openai.timeout}")
    private String apiTimeout;

    private static final String GPT_MODEL = "gpt-3.5-turbo";

    @PostConstruct
    public void initGptService() {
        openAiService = new OpenAiService(apiKey, Duration.parse("pt" + apiTimeout +"s"));
        System.out.println("Connected to OpenAI!");
    }

    @Override
    public Message answer(Chat chat, Message question) {
        String systemTaskMessage = systemTaskMessage(chat);
        String prompt = String.format(question.getMessage());

        ChatCompletionRequest request = new ChatCompletionRequest().builder()
                .model(GPT_MODEL)
                .temperature(0.8)
                .messages(List.of(
                        new ChatMessage("system", systemTaskMessage),
                        new ChatMessage("user", prompt)))
                .build();

        StringBuilder builder = new StringBuilder();
        openAiService.createChatCompletion(request).getChoices().forEach(choice -> {
            builder.append((choice.getMessage().getContent()));
        });
        String jsonResponse = builder.toString();
        return new Message(jsonResponse, Type.RESPONSE);
    }

    private String systemTaskMessage(Chat chat) {
        User user = chat.getUser();
        Pet pet = chat.getPet();
        return String.format("""
                You are a virtual vet and you are talking to a pet owner who is asking you about their pet.
                Give professional and friendly answers. Adjust the language of the conversation to the language in which the question was asked.
                Remind the pet owner to contact a real veterinarian and not just rely on your answers.
                The pet's owner name is %s. The pet's name is %s.
                The pet is a %s. The breed of the pet is a %s. It is a %s. The pet was born at %s
                """, user.getFirstName(), pet.getName(), pet.getSpecies(), pet.getBreed(), pet.getSex(), pet.getDateOfBirth());
    }


}
