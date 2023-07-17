package com.example.LibraryProject.API;
//     API KEY  AIzaSyDHWnqOi0ULoK7KKWsfiP0Jx0BJ5TbX5ik
import com.example.LibraryProject.pojo.dto.GoogleBookItem;
import com.example.LibraryProject.pojo.dto.GoogleBooksResponse;
import org.springframework.web.client.RestTemplate;
import com.example.LibraryProject.pojo.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class GoogleBookService {


                         // RestTemplate is used to invoke an external REST endpoint by another service
    private RestTemplate restTemplate; // Object that enables http requests from my code to different APIs;
    private GoogleBookService googleBookService;
    public GoogleBookService(RestTemplateBuilder templateBuilder){
        this.restTemplate = templateBuilder.build();
    }
    private static final Logger serviceLogger = LoggerFactory.getLogger(GoogleBookService.class);

    /**
     *  In this method we return all the books from the API.
     * @return GoogleBookItem
     */
    @Async
    public CompletableFuture<List<GoogleBookItem>> getExternalLibraryBooks(Double maxprice, Double minprice) {
        String url = "https://www.googleapis.com/books/v1/volumes?q=Harry+Potter";
        ResponseEntity<GoogleBooksResponse> responseEntity = restTemplate.getForEntity(url, GoogleBooksResponse.class);
        GoogleBooksResponse googleBooksResponse = responseEntity.getBody();
        List<GoogleBookItem> items = googleBooksResponse != null ? googleBooksResponse.getItems() : Collections.emptyList();

        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        List<GoogleBookItem> booksInRange = new ArrayList<>();
        for (GoogleBookItem item : items) {
            double titleLength = item.getVolumeInfo().getTitle().length();
            double price = titleLength + 0.01*(item.getVolumeInfo().getPageCount()) ;
                                 // Sets the price based on the length of the book title and the amount of pages.

            item.getVolumeInfo().setPrice(Double.valueOf(decimalFormat.format(price)));

            if (minprice == null || maxprice == null || (minprice <= item.getVolumeInfo().getPrice() && item.getVolumeInfo().getPrice() <= maxprice)) {
                booksInRange.add(item);
            }
        }

        if (minprice == null || maxprice == null) {
            return CompletableFuture.completedFuture(items);
        } else {
            return CompletableFuture.completedFuture(booksInRange);
        }
    }


    /**
     * In this method we return a single book from the API by its ID.
     * @param bookId -> ID of the book.
     * @return single GoogleBookItem.
     */
    @Async
    public CompletableFuture<GoogleBookItem> getExternalBookById(String bookId) {
        String url = "https://www.googleapis.com/books/v1/volumes/{bookId}?key={apiKey}";

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("bookId", bookId);
        uriVariables.put("apiKey", "AIzaSyDHWnqOi0ULoK7KKWsfiP0Jx0BJ5TbX5ik");


        return CompletableFuture.supplyAsync(() -> {
            ResponseEntity<GoogleBookItem> responseEntity = restTemplate.getForEntity(url, GoogleBookItem.class, uriVariables);
            return responseEntity.getBody();
        });
    }





            //retrieves books without any specific filters.
    @Async
    public CompletableFuture<List<Book>> getAPIBooks() {
        String template = "https://www.googleapis.com/books/v1/volumes?key={apiKey}";

        Map<String, String> uriVariables = new HashMap<>();

        uriVariables.put("apiKey", "AIzaSyDHWnqOi0ULoK7KKWsfiP0Jx0BJ5TbX5ik");

        ResponseEntity<GoogleBooksResponse> responseEntity = this.restTemplate.getForEntity(template, GoogleBooksResponse.class, uriVariables);
        GoogleBooksResponse googleBooksResponse = responseEntity.getBody();

        List<Book> books = googleBooksResponse != null ? googleBooksResponse.getItems().stream()
                                                                            .map(GoogleBookItem::toBook)
                                                                            .collect(Collectors.toList()) : Collections.emptyList();

        return CompletableFuture.completedFuture(books);
    }
}
