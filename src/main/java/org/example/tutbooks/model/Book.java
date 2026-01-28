package org.example.tutbooks.model;

import java.util.List;

public final class Book {
    public String schema;
    public String id;
    public String title;
    public String subtitle;
    public String author;
    public String version;
    public String mod;
    public String lang;
    public Theme theme;
    public List<TocEntry> toc;
    public List<Page> pages;

    public static final class Theme {
        public Double pageWidth;
        public Double pageHeight;
        public String font;
        public String color;
        public String accent;
        public String headerAlign;
        public String footerAlign;
    }

    public static final class TocEntry {
        public String label;
        public Target target;
    }

    public static final class Target {
        public String page;
        public String anchor;
    }

    public static final class Page {
        public String id;
        public HeaderFooter header;
        public HeaderFooter footer;
        public List<Block> blocks;
    }

    public static final class HeaderFooter {
        public String text;
        public String align;
    }

    public static final class Block {
        public String type;
        public String id;
        public String align;
        public String style;
        public String text;
        public Boolean ordered;
        public List<String> items;
        public String src;
        public String alt;
        public Double width;
        public Double height;
        public String url;
        public Action action;
        public String cite;
    }

    public static final class Action {
        public String type;
        public String page;
        public String anchor;
        public String url;
    }
}
