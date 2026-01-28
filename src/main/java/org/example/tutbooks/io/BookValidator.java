package org.example.tutbooks.io;

import org.example.tutbooks.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BookValidator {
    private BookValidator() {
    }

    public static List<String> validate(Book book) {
        List<String> errors = new ArrayList<>();
        if (book == null) {
            errors.add("Book is null.");
            return errors;
        }

        if (!"tutbooks.book.v1".equals(book.schema)) {
            errors.add("schema must be tutbooks.book.v1.");
        }
        if (isBlank(book.id)) {
            errors.add("id is required.");
        }
        if (isBlank(book.title)) {
            errors.add("title is required.");
        }
        if (isBlank(book.version)) {
            errors.add("version is required.");
        }
        if (book.pages == null || book.pages.isEmpty()) {
            errors.add("pages is required and must contain at least one page.");
        }

        Map<String, Set<String>> anchorsByPage = new HashMap<>();
        Set<String> pageIds = new HashSet<>();
        if (book.pages != null) {
            for (Book.Page page : book.pages) {
                if (page == null) {
                    errors.add("page is null.");
                    continue;
                }
                if (isBlank(page.id)) {
                    errors.add("page id is required.");
                    continue;
                }
                if (!pageIds.add(page.id)) {
                    errors.add("duplicate page id: " + page.id);
                }
                Set<String> anchors = anchorsByPage.computeIfAbsent(page.id, k -> new HashSet<>());
                if (page.blocks != null) {
                    for (Book.Block block : page.blocks) {
                        if (block != null && !isBlank(block.id)) {
                            anchors.add(block.id);
                        }
                    }
                }
            }
        }

        if (book.pages != null) {
            for (Book.Page page : book.pages) {
                if (page == null) {
                    continue;
                }
                String pageId = isBlank(page.id) ? "<missing>" : page.id;
                if (page.blocks == null || page.blocks.isEmpty()) {
                    errors.add("page " + pageId + " has no blocks.");
                    continue;
                }
                for (Book.Block block : page.blocks) {
                    if (block == null) {
                        errors.add("page " + pageId + " has a null block.");
                        continue;
                    }
                    if (isBlank(block.type)) {
                        errors.add("page " + pageId + " has a block without type.");
                        continue;
                    }
                    switch (block.type) {
                        case "title":
                        case "subtitle":
                        case "header":
                        case "paragraph":
                        case "quote":
                            if (isBlank(block.text)) {
                                errors.add("block " + block.type + " on page " + pageId + " requires text.");
                            }
                            break;
                        case "list":
                            if (block.items == null || block.items.isEmpty()) {
                                errors.add("list block on page " + pageId + " requires items.");
                            }
                            break;
                        case "image":
                            if (isBlank(block.src)) {
                                errors.add("image block on page " + pageId + " requires src.");
                            }
                            break;
                        case "link":
                            if (isBlank(block.text) || isBlank(block.url)) {
                                errors.add("link block on page " + pageId + " requires text and url.");
                            }
                            break;
                        case "button":
                            if (isBlank(block.text)) {
                                errors.add("button block on page " + pageId + " requires text.");
                            }
                            validateAction(errors, pageId, block.action, anchorsByPage, pageIds);
                            break;
                        case "divider":
                            break;
                        default:
                            errors.add("unknown block type '" + block.type + "' on page " + pageId + ".");
                            break;
                    }
                }
            }
        }

        if (book.toc != null) {
            for (Book.TocEntry entry : book.toc) {
                if (entry == null) {
                    errors.add("toc entry is null.");
                    continue;
                }
                if (isBlank(entry.label)) {
                    errors.add("toc entry label is required.");
                }
                if (entry.target == null) {
                    errors.add("toc entry target is required.");
                    continue;
                }
                if (isBlank(entry.target.page) || isBlank(entry.target.anchor)) {
                    errors.add("toc entry requires page and anchor.");
                    continue;
                }
                if (!pageIds.contains(entry.target.page)) {
                    errors.add("toc entry points to missing page: " + entry.target.page);
                    continue;
                }
                Set<String> anchors = anchorsByPage.get(entry.target.page);
                if (anchors != null && !anchors.contains(entry.target.anchor)) {
                    errors.add("toc entry points to missing anchor: " + entry.target.page + "#" + entry.target.anchor);
                }
            }
        }

        return errors;
    }

    private static void validateAction(
            List<String> errors,
            String pageId,
            Book.Action action,
            Map<String, Set<String>> anchorsByPage,
            Set<String> pageIds
    ) {
        if (action == null) {
            errors.add("button block on page " + pageId + " requires action.");
            return;
        }
        if (isBlank(action.type)) {
            errors.add("action on page " + pageId + " requires type.");
            return;
        }
        switch (action.type) {
            case "goto":
                if (isBlank(action.page) || isBlank(action.anchor)) {
                    errors.add("goto action on page " + pageId + " requires page and anchor.");
                    return;
                }
                if (!pageIds.contains(action.page)) {
                    errors.add("goto action on page " + pageId + " points to missing page: " + action.page);
                    return;
                }
                Set<String> anchors = anchorsByPage.get(action.page);
                if (anchors != null && !anchors.contains(action.anchor)) {
                    errors.add("goto action points to missing anchor: " + action.page + "#" + action.anchor);
                }
                break;
            case "open_url":
                if (isBlank(action.url)) {
                    errors.add("open_url action on page " + pageId + " requires url.");
                }
                break;
            case "next_page":
            case "prev_page":
                break;
            default:
                errors.add("unknown action type '" + action.type + "' on page " + pageId + ".");
                break;
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
