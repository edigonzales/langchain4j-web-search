///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS dev.langchain4j:langchain4j-open-ai:0.31.0 
//DEPS dev.langchain4j:langchain4j:0.31.0 
//DEPS dev.langchain4j:langchain4j-web-search-engine-google-custom:0.31.0

import static java.lang.System.*;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.WebSearchContentRetriever;
import dev.langchain4j.web.search.WebSearchEngine;
import dev.langchain4j.web.search.google.customsearch.GoogleCustomWebSearchEngine;
import dev.langchain4j.service.AiServices;

public class Langchain4JWebSearch {

    public static void main(String... args) {
        out.println("Hello World");

        String apiKey = System.getenv("OPENAI_API_KEY");
        
        OpenAiChatModel model = OpenAiChatModel.withApiKey(apiKey);

        //String answer = model.generate("Tell me a dad joke");
        //err.println(answer); 
        
        
        WebSearchEngine webSearchEngine = GoogleCustomWebSearchEngine.builder()
            .apiKey(System.getenv("GOOGLE_CUSTOM_SEARCH_API_KEY"))
            .csi(System.getenv("GOOGLE_CUSTOM_SEARCH_CSI"))
            .logRequests(true)
            .logResponses(true)
            .build();

        ContentRetriever contentRetriever = WebSearchContentRetriever.builder()
            .webSearchEngine(webSearchEngine)
            .maxResults(3)
            .build();

        SearchWebsite website = AiServices.builder(SearchWebsite.class)
            .chatLanguageModel(model)
            .contentRetriever(contentRetriever)
            .build();

        String response = website.search("interlis webservice?");
        err.println("response = " + response);
    }

    // logging? brauchts ne slf4j implementierung?
    // Systemmessage.

    interface SearchWebsite {
        String search(String query);
    }
    
}
