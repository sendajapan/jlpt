---
name: feedback-pint
description: "Always run `vendor/bin/pint --dirty --format agent` after editing PHP files"
metadata: 
  node_type: memory
  type: feedback
  originSessionId: 0566fa33-6893-47f9-a85f-72dee5d4ea77
---

After modifying any PHP file in this repo, run `vendor/bin/pint --dirty --format agent` (fix mode, not `--test`) before finalizing.

**Why:** Project enforces Pint style; CI/reviewers expect formatted code. Stated in CLAUDE.md laravel-boost guidelines.
**How to apply:** Run once at the end of a PHP-touching task, not after every edit. Skip for Android/Kotlin-only changes.
