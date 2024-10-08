package br.com.marcotulio.rag_poc.controller;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class AIController {

    private final OpenAiChatModel chatClient;
    private final SimpleVectorStore vectorStore;
    @Value("classpath:/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    public AIController(OpenAiChatModel chatClient, SimpleVectorStore vectorStore) {
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
    }

    @GetMapping("/chat")
    public String sendMessage(@RequestParam(defaultValue = "Ola Mundo") String message) {
        return chatClient.call(message);
    }

    @GetMapping("/chat/metadata")
    public ChatResponse sendMessageAndReturnMetaData(@RequestParam(defaultValue = "Ola Mundo") String message) {
        return chatClient.call(new Prompt(message));
    }

    @GetMapping("/chat-template")
    public String sendMessageWithPromptTemplate(@RequestParam(defaultValue = "Ola Mundo") String message) {
        final SystemMessage systemMessage = new SystemMessage("""
                    Seu nome é TulioAI, assistente de IA do Marco Túlio.
                    Você é um assistente para responder perguntas;
                    Voce não deve responder outras frases que não se caracterizem como perguntas;
                """);
        final UserMessage userMessage = new UserMessage(message);
        return chatClient.call(systemMessage, userMessage);
    }

    @GetMapping("/chat-template-rag")
    public String sendMessageWithPromptTemplateAndRag(@RequestParam(defaultValue = "Ola Mundo") String message) {
        final SystemPromptTemplate systemMessageTemplate = new SystemPromptTemplate(ragPromptTemplate);
        final List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        final List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        final Message systemMessage = systemMessageTemplate.createMessage(Map.of("input", message, "documents", String.join("\n", contentList)));
        return chatClient.call(systemMessage);
    }
}
