///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j-open-ai:0.31.0 
//DEPS dev.langchain4j:langchain4j:0.31.0 
//DEPS dev.langchain4j:langchain4j-web-search-engine-google-custom:0.31.0
//DEPS org.slf4j:slf4j-simple:2.0.7

import static java.lang.System.*;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class Langchain4JWebSearch {

    public static void main(String... args) {

        String apiKey = System.getenv("OPENAI_API_KEY");
        
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);
        
        WebSearchEngine webSearchEngine = GoogleCustomWebSearchEngine.builder()
            .apiKey(System.getenv("GOOGLE_CUSTOM_SEARCH_API_KEY"))
            .csi(System.getenv("GOOGLE_CUSTOM_SEARCH_CSI"))
            .logRequests(true)
            .logResponses(true)
            .build();

        ContentRetriever contentRetriever = WebSearchContentRetriever.builder()
            .webSearchEngine(webSearchEngine)
            .maxResults(10)
            .build();

        SearchWebsite website = AiServices.builder(SearchWebsite.class)
            .chatLanguageModel(model)
            .contentRetriever(contentRetriever)
            .build();

        String response = website.search("wie mache ich einen interlis web service mit java?");
        err.println("response = " + response);
    }

    // logging? brauchts ne slf4j implementierung?
    // Systemmessage.

    interface SearchWebsite {
        @SystemMessage("""
        Provide a paragraph-long answer. If possible show some code samples.

        Die Antwort soll in Deutsch erfolgen.
        """)
        String search(String query);
    }
    
}
