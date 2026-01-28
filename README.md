# TuTBooks (Hytale Tutorial Books)

TuTBooks is a starter plugin that loads tutorial books from JSON files.
It is designed for mod and modpack authors who want in-game guides with
headers, titles, footers, images, links, and buttons that jump to anchors.

What is included
- JSON book format with anchors and actions (goto, next/prev, open_url)
- Book index for registering multiple books
- Example book data and assets folder
- Lightweight validator with startup warnings

Project layout
- src/main/java/org/example/tutbooks/
- src/main/resources/tutbooks/index.json
- src/main/resources/tutbooks/books/*.book.json
- src/main/resources/tutbooks/assets/
- docs/BOOK_FORMAT.md
- schemas/book.schema.json
- schemas/index.schema.json

Quick start
1) Create a new book file in src/main/resources/tutbooks/books/
2) Register it in src/main/resources/tutbooks/index.json
3) Put images in src/main/resources/tutbooks/assets/
4) Run the server and use /tutbooks to verify the book list

Notes
- This plugin uses Gson to parse JSON. If Gson is not on the server classpath,
  add it or shade it into your plugin jar.
- The UI rendering is intentionally left open; wire your own UI and use the
  book data to render pages.
