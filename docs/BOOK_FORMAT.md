# TuTBooks book format v1

Each book is a JSON file. Books are listed in an index JSON file so the plugin
can discover them at startup.

Top-level fields
- schema (string, required)  "tutbooks.book.v1"
- id (string, required)      namespaced id: "modid:book_id"
- title (string, required)
- subtitle (string, optional)
- author (string, optional)
- version (string, required) content version
- mod (string, optional)
- lang (string, optional)    BCP47 tag, e.g. "en-US"
- theme (object, optional)
- toc (array, optional)
- pages (array, required)

Theme fields (optional)
- pageWidth, pageHeight (number)
- font (string)
- color (string, hex)
- accent (string, hex)
- headerAlign, footerAlign (left|center|right)

Page fields
- id (string, required)
- header (object, optional)  { text, align }
- footer (object, optional)  { text, align }
- blocks (array, required)

Block fields (base)
- type (string, required)
- id (string, optional)      anchor id for jumps
- align (string, optional)
- style (string, optional)

Block types
- title      { text }
- subtitle   { text }
- header     { text }
- paragraph  { text }
- list       { ordered, items[] }
- image      { src, alt, width, height }
- link       { text, url }
- button     { text, action }
- divider    { }
- quote      { text, cite }

Actions
- goto       { type: "goto", page, anchor }
- open_url   { type: "open_url", url }
- next_page  { type: "next_page" }
- prev_page  { type: "prev_page" }

Footer templating
- Use {page} and {pages} in footer text.

Anchors
- Any block with an id becomes an anchor. Use action.goto with page + anchor.

Resources
- Book index: src/main/resources/tutbooks/index.json
- Book files: src/main/resources/tutbooks/books/*.book.json
- Images:     src/main/resources/tutbooks/assets/
