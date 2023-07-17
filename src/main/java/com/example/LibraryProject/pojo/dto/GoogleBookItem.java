package com.example.LibraryProject.pojo.dto;
import com.example.LibraryProject.pojo.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.api.services.books.v1.model.Volume;
import lombok.Data;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleBookItem {
    @JsonIgnore
    private Book book;
    @JsonProperty("volumeInfo")
    private VolumeInfo volumeInfo;
    private String id;
    @JsonIgnore
    private Double price;
    public Book toBook() {
        Book book = new Book();
        if (volumeInfo != null) {
            book.setBookTitle(volumeInfo.getTitle());
            book.setBookTitle(volumeInfo.getTitle());
            book.setPrice(volumeInfo.getPrice());
        }

        return book;
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VolumeInfo { // All the volume's fields that we required;
        private String title;
        private String subtitle;
        private Double price;
        private String[] authors;
        private String publisher;
        private String publishedDate;
        private String description;
        private IndustryIdentifier[] industryIdentifiers;
        private Volume.VolumeInfo.ReadingModes readingModes;
        private int pageCount;
        private String printType;
        private String[] categories;
        private double averageRating;
        private int ratingsCount;
        private String maturityRating;
        private boolean allowAnonLogging;
        private String contentVersion;
        private Volume.VolumeInfo.PanelizationSummary panelizationSummary;
        private Volume.VolumeInfo.ImageLinks imageLinks;
        private String language;
        private String previewLink;
        private String infoLink;
        private String canonicalVolumeLink;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IndustryIdentifier {
        private String type;
        private String identifier;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ReadingModes {
        private boolean text;
        private boolean image;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PanelizationSummary {
        private boolean containsEpubBubbles;
        private boolean containsImageBubbles;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageLinks {
        private String smallThumbnail;
        private String thumbnail;
    }



}
