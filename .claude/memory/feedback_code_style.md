---
name: feedback-code-style
description: "Project-wide code style — flat code, no comments, uniform admin tables, thin controllers/services"
metadata: 
  node_type: memory
  type: feedback
  originSessionId: 0566fa33-6893-47f9-a85f-72dee5d4ea77
---

Codified in `CLAUDE.md` at repo root. Highlights:

- Admin index tables must share an identical structure: first column **S/N** (`w-10 px-3 text-center border-r border-zinc-100`, value `($paginator->currentPage() - 1) * $paginator->perPage() + $loop->iteration`), last column **Action** (`px-4 text-center border-l border-zinc-100`, header "Action", buttons `justify-center`). Edit/Delete use text labels — never SVG-only buttons. Wrap table in `<div class="overflow-x-auto">`. No Blade comments.
- Write simple, flat code a junior can follow. No comments anywhere — names must speak for themselves.
- Extract duplicated logic into shared base classes/traits.
- Prefer `replaceFile`-style helpers over verbose if/else for optional updates.
- Services are thin wrappers around Eloquent queries.
- Controllers: validate → handle files → call service → redirect.

**Why:** User maintains both Laravel and Android codebases and wants every admin table to look identical so a new dev can predict structure.
**How to apply:** Before creating any new admin index page, copy structure from a sibling. Never add comments unless asked.
