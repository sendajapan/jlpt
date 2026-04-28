# Code Style

## Admin Index Tables

All admin index tables must be identical in structure:

- First column **S/N**: `w-10 px-3 text-center border-r border-zinc-100` on `<th>` and `<td>`. Value: `($paginator->currentPage() - 1) * $paginator->perPage() + $loop->iteration`
- Last column **Action**: `px-4 text-center border-l border-zinc-100` on `<th>` and `<td>`, header text is "Action", buttons use `justify-center`
- Edit/Delete use text labels only — never SVG-icon-only buttons
- Table wrapped in `<div class="overflow-x-auto">`
- No Blade comments (`{{-- --}}`)

##

- Write simple, flat code that a junior developer can follow immediately.
- No comments anywhere — names must speak for themselves.
- Extract duplicated logic into shared base classes or traits rather than repeating it.
- Prefer `replaceFile`-style helpers over verbose if/else blocks when handling optional updates.
- Keep methods short and linear — avoid nesting where a flat alternative exists.
- Services stay thin: they wrap Eloquent queries and nothing else.
- Controllers stay thin: validate, handle files, call service, redirect.
