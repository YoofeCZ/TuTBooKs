package org.example.tutbooks.model;

import java.util.List;

public final class BookIndex {
    public String schema;
    public List<Entry> books;

    public static final class Entry {
        public String id;
        public String path;
    }
}
