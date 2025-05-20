package com.example;

import org.apache.pdfbox.pdmodel.documentinterchange.taggedpdf.PDExportFormatAttributeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExamplesController {

    private static final Logger log = LoggerFactory.getLogger(ExamplesController.class);

    private ChatClient chatClient;
    private VectorStore vectorStore;
    private ChatMemory chatMemory;

    @Value("classpath:article.pdf")
    private Resource articlePdf;

    ExamplesController(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory) {
        this.chatClient = builder.build();
        this.vectorStore = vectorStore;
        this.chatMemory = chatMemory;
    }

    @GetMapping("/system")
    String systemInstructions(@RequestParam(defaultValue = "who are you") String query) {
        log.info("Inside systemInstructions() - query: {}", query);
        return chatClient
                .prompt()
                .system("You are a useful assistant. Be polite, and always finish the sentence with 'and let the Force be with you'.")
                .user(query)
                .call()
                .content();
    }

    @GetMapping("/prompt-stuffing")
    String promptStuffing(@RequestParam(defaultValue = "Stockholm") String topic) {
        log.info("Inside promptStuffing() - topic: {}", topic);
        return chatClient
                .prompt()
                .system("You are a useful assistant. Be polite, and always finish the sentence with 'and let the Force be with you'.")
                .user(u-> u
                        .text("Please summarize the following topic in 20 words using plain text :\n {topic}")
                        .param("topic", topic))
                .call()
                .content();
    }

    @GetMapping("/load-pdf")
    String loadPdf() {
        log.info("Inside loadPdf() - loading: {}", articlePdf);
        try {
            vectorStore.add(new TokenTextSplitter().split(new PagePdfDocumentReader(articlePdf).read()));
        } catch (Exception e) {
          log.error("Exception caught in /load-pdf: {}", e.getMessage());
        }
        return "Loading document: " + articlePdf.getFilename();
    }

    @GetMapping("/ask-about-pdf")
    String askAboutPdf(@RequestParam(defaultValue = "what is this article about?") String query) {
        return chatClient
                .prompt()
                .system("You are a useful assistant. Be polite, and always finish the sentence with 'and let the Force be with you'.")
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .user(query)
                .call()
                .content();
    }

    @GetMapping("/rag")
    String rag(@RequestParam(defaultValue = "what is this article about?") String query) {
        return chatClient
                .prompt()
                .system("You are a useful assistant. Be polite, and always finish the sentence with 'and let the Force be with you'.")
                .advisors(
                        RetrievalAugmentationAdvisor
                                .builder()
                                .documentRetriever(
                                        VectorStoreDocumentRetriever.builder()
                                                .similarityThreshold(0.50)
                                                .vectorStore(vectorStore)
                                                .build()
                                )
                                .build()
                )
                .user(query)
                .call()
                .content();
    }

    @GetMapping("/memory")
    String memory(@RequestParam (defaultValue = "hello") String query) {
        return chatClient
                .prompt()
                .system("You are a useful assistant. Be polite, and always finish the sentence with 'and let the Force be with you'.")
                .advisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .user(query)
                .call()
                .content();
    }

    @GetMapping("/tool")
    String tool(@RequestParam (defaultValue = "What kind of clothes should I pack for Stockholm?") String query, MyTools tools) {
        return chatClient
                .prompt()
                .system("You are a useful assistant. Be polite, and always finish the sentence with 'and let the Force be with you'.")
                .tools(tools)
                .user(query)
                .call()
                .content();
    }

}
