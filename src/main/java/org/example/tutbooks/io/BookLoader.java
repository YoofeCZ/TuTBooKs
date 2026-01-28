package org.example.tutbooks.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.tutbooks.model.Book;
import org.example.tutbooks.model.BookIndex;

public final class BookLoader {
    private final Gson gson;

    public BookLoader() {
        this.gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public BookIndex loadIndex(String json) {
        return gson.fromJson(json, BookIndex.class);
    }

    public Book loadBook(String json) {
        return gson.fromJson(json, Book.class);
    }
}
