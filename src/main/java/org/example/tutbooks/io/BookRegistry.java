package org.example.tutbooks.io;

import org.example.tutbooks.model.Book;
import org.example.tutbooks.model.BookIndex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BookRegistry {
    private final Map<String, Book> books = new HashMap<>();
    private final Map<String, List<String>> errors = new HashMap<>();

    public void loadFromResources(ClassLoader loader, String indexPath) throws IOException {
        BookLoader bookLoader = new BookLoader();
        String indexJson = ResourceUtils.readResource(loader, indexPath);
        BookIndex index = bookLoader.loadIndex(indexJson);
        if (index == null || index.books == null) {
            errors.put("_index", List.of("index.json is missing or invalid."));
            return;
        }

        for (BookIndex.Entry entry : index.books) {
            if (entry == null || isBlank(entry.id) || isBlank(entry.path)) {
                errors.put("_index", List.of("index entry is missing id or path."));
                continue;
            }
            try {
                String bookJson = ResourceUtils.readResource(loader, entry.path);
                Book book = bookLoader.loadBook(bookJson);
                if (book == null) {
                    errors.put(entry.id, List.of("book JSON failed to parse."));
                    continue;
                }
                books.put(entry.id, book);
                List<String> validation = new ArrayList<>(BookValidator.validate(book));
                if (!entry.id.equals(book.id)) {
                    validation.add("index id does not match book.id: " + book.id);
                }
                if (!validation.isEmpty()) {
                    errors.put(entry.id, validation);
                }
            } catch (IOException ex) {
                errors.put(entry.id, List.of("missing book file: " + entry.path));
            }
        }
    }

    public Book get(String id) {
        return books.get(id);
    }

    public int size() {
        return books.size();
    }

    public List<String> getIdsSorted() {
        List<String> ids = new ArrayList<>(books.keySet());
        Collections.sort(ids);
        return ids;
    }

    public Map<String, List<String>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
