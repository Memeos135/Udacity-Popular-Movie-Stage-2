package com.example.popularmoviesstage1;

public class ReviewInfo {
    private String author;
    private String content;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReviewInfo() {
    }

    public ReviewInfo(String author, String content) {
        this.author = author;
        this.content = content;
    }
}
