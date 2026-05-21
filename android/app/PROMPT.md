# Prompt for Claude — Build "Hajime" Android app (Java + XML)

Paste the entire block below into Claude (or Claude Code) along with the **`exports/android/`** folder attached. The folder already contains `colors.xml`, `bg_*.png` drawables at every density, and the screen reference PNGs.

---

You are building an Android app called **"Hajime · Fun Language Learning"** — a children's Japanese vocabulary learning app inspired by Studio Ghibli's warm, magical aesthetic. The app teaches JLPT N5 vocabulary organized as **Categories → Subcategories → Words**.

**Stack:** Java + XML layouts (no Compose, no Kotlin). Use **AndroidX**, **Material Components for Android**, **ViewBinding**, and **Navigation Component** (jetpack navigation). minSdk 24, targetSdk 34.

Match the attached reference screenshots **pixel-for-pixel as closely as XML allows**. Do not invent new screens, colors, or layouts. Do not use Material 3 dynamic color. Do not use system fonts — use Nunito + Noto Sans JP.

---

## 0. Project setup

1. Create package `app.hajime`.
2. Copy `colors.xml` from the attached `android/` folder into `res/values/colors.xml`.
3. Copy every `bg_*.png` from `android/res/drawable-*/` into the matching `res/drawable-*/` density folders.
4. Download these fonts from Google Fonts and place TTFs in `res/font/`:
   - **Nunito**: regular, semibold (600), bold (700), extrabold (800), black (900) → `nunito_regular.ttf`, `nunito_semibold.ttf`, `nunito_bold.ttf`, `nunito_extrabold.ttf`, `nunito_black.ttf`
   - **Noto Sans JP**: regular, bold, black → `noto_sans_jp_regular.ttf`, `noto_sans_jp_bold.ttf`, `noto_sans_jp_black.ttf`
   - Create font families in `res/font/nunito.xml` and `res/font/noto_sans_jp.xml`.
5. In `res/values/themes.xml`, base on `Theme.MaterialComponents.Light.NoActionBar`. Set:
   - `colorPrimary` → `@color/hajime_sage_deep`
   - `colorPrimaryVariant` → `@color/hajime_sage_dark`
   - `colorSecondary` → `@color/hajime_terra`
   - `android:windowBackground` → `@color/hajime_paper`
   - `android:statusBarColor` → `@android:color/transparent`, light icons false
   - `android:navigationBarColor` → `@color/hajime_paper`
   - `android:fontFamily` → `@font/nunito`
6. Make status bar transparent and draw behind it via `WindowCompat.setDecorFitsSystemWindows(window, false)` in `BaseActivity` — backgrounds must extend full-bleed.

---

## 1. Color tokens (already in colors.xml — reference these names, never raw hex)

| Token | Hex | Purpose |
|---|---|---|
| `hajime_paper` | `#F6EBD7` | Default screen bg |
| `hajime_paper_alt` | `#FBF4E2` | Home/profile bg |
| `hajime_surface` | `#FFFCF4` | Cards, sheets |
| `hajime_ink` | `#2E3A33` | Primary text |
| `hajime_ink_soft` | `#5E6B5F` | Secondary text |
| `hajime_ink_mute` | `#8C9389` | Hints, captions |
| `hajime_sage` | `#9CBFA1` | Soft fills, gradient start |
| `hajime_sage_deep` | `#6E9579` | Primary buttons, gradient end |
| `hajime_sage_dark` | `#4F6F5A` | Active states |
| `hajime_terra` | `#D78867` | Secondary CTA |
| `hajime_terra_deep` | `#B86C4D` | English word color, active accent |
| `hajime_butter` | `#F2CE6A` | XP, stars |
| `hajime_rose` | `#EFB6B0` | Pet category |
| `hajime_petal` | `#F2D0CB` | Word hero wash |
| `hajime_lavender` | `#C9B7DC` | Family category |
| `hajime_sky` | `#A8C9D6` | Travel category, info |
| `hajime_sky_light` | `#CFE0E7` | Cool tints |

---

## 2. Typography (text appearance styles in `styles.xml`)

Create these `TextAppearance` styles and use them as `android:textAppearance`:

| Style | Family | Size | Weight | Letter spacing |
|---|---|---|---|---|
| `Hajime.Display.Large` | Nunito | 56sp | 900 | -0.04 |
| `Hajime.Display.Medium` | Nunito | 38sp | 900 | -0.02 |
| `Hajime.Display.Small` | Nunito | 32sp | 800 | -0.01 |
| `Hajime.Headline.Large` | Nunito | 26sp | 800 | 0 |
| `Hajime.Headline.Medium` | Nunito | 22sp | 800 | 0 |
| `Hajime.Title.Large` | Nunito | 18sp | 800 | 0 |
| `Hajime.Title.Medium` | Nunito | 16sp | 700 | 0 |
| `Hajime.Body.Large` | Nunito | 15sp | 600 | 0 |
| `Hajime.Body.Medium` | Nunito | 14sp | 400 | 0 |
| `Hajime.Label.Large` | Nunito | 12sp | 800 | 0.1 (ALL CAPS) |
| `Hajime.Label.Medium` | Nunito | 11sp | 700 | 0.12 |
| `Hajime.Jp.Display` | Noto Sans JP | 48sp | 700 | 0 |
| `Hajime.Jp.Body` | Noto Sans JP | 18sp | 700 | 0 |

---

## 3. Reusable drawables (create in `res/drawable/`)

- `btn_primary.xml` — rounded 999dp, gradient `#9CBFA1 → #6E9579`, ripple `hajime_sage_dark`. Height 56dp, paddingHorizontal 24dp.
- `btn_secondary.xml` — gradient `#D78867 → #B86C4D`.
- `btn_outline.xml` — stroke 1.5dp `hajime_ink`, fill `hajime_surface`, radius 999dp.
- `card_surface.xml` — radius 20dp, fill `hajime_surface`, elevation handled by `CardView` with `app:cardElevation="2dp"` and `app:cardCornerRadius="20dp"`.
- `chip_xp.xml` — radius 14dp, gradient `#F2CE6A → #D78867`.
- `chip_pro_locked.xml` — radius 999dp, fill `#D92E3A33` (ink at 85%).
- `ring_progress_track.xml` — circular ring, stroke `#142E3A33`, width 8dp.
- `divider_soft.xml` — height 1dp, fill `#0F2E3A33`.

---

## 4. Screen list & navigation

Bottom nav (only on Home / Categories / Profile screens) with 3 destinations:
- Home (icon: house)
- Profile (icon: person)

Other screens push as a full-screen fragment with a back arrow in the top-left.

| # | Activity / Fragment | Layout | Background drawable |
|---|---|---|---|
| 1 | `SplashActivity` | `activity_splash.xml` | `@drawable/bg_splash` |
| 2 | `LoginFragment` | `fragment_login.xml` | `@drawable/bg_login` |
| 3 | `HomeFragment` | `fragment_home.xml` | `@drawable/bg_home` |
| 4 | `SubcategoryFragment` | `fragment_subcategory.xml` | `@drawable/bg_subcategory` |
| 5 | `WordDetailFragment` | `fragment_word_detail.xml` | `@drawable/bg_word` |
| 6 | `ScoreFragment` | `fragment_score.xml` | `@drawable/bg_score` |
| 7 | `ProfileFragment` | `fragment_profile.xml` | `@drawable/bg_profile` |

Every screen has the same structure:
```xml
<FrameLayout android:fitsSystemWindows="false">
  <ImageView android:src="@drawable/bg_xxx" android:scaleType="centerCrop"
             android:layout_width="match_parent" android:layout_height="match_parent" />
  <androidx.core.widget.NestedScrollView android:fillViewport="true">
    <!-- content -->
  </androidx.core.widget.NestedScrollView>
</FrameLayout>
```

---

## 5. Screen specs

### 5.1 Splash (`activity_splash.xml`)
- Full-bleed `bg_splash`.
- Centered owl mascot illustration (place as `ic_owl_mascot.png` in drawable — for now use a placeholder circle with text "🦉" if not available).
- Below mascot: app name **"Hajime"** in `Hajime.Display.Large`, `hajime_ink`, with subtitle **"Fun Language Learning"** in `Hajime.Title.Medium`, `hajime_ink_soft`.
- Bottom 48dp: 3-dot loading indicator (sage_deep).
- Auto-advance to Login after 2 seconds.

### 5.2 Login (`fragment_login.xml`)
- Top 80dp safe inset.
- Title **"Welcome back!"** `Hajime.Headline.Large`, ink. Subtitle **"Continue your adventure"** `Hajime.Body.Large`, ink_soft.
- 2 `TextInputLayout` (outlined style, corner radius 16dp): Email, Password. Stroke color `hajime_ink_mute`, focused `hajime_sage_deep`.
- Primary button **"Log in"** (full width, `btn_primary`).
- Divider with text "or continue with" → 3 round icon buttons (Google, Apple, Facebook) — 56dp circles, surface bg, ink icon.
- Bottom: "New here? **Create account**" — `Hajime.Body.Medium`, with "Create account" in `hajime_terra_deep`, bold.

### 5.3 Home (`fragment_home.xml`)

Top app bar (transparent, 64dp):
- Left: small circular avatar (40dp).
- Right: streak chip (terra gradient pill, "🔥 7" in surface color, `Hajime.Label.Large`).

Greeting block (paddingHorizontal 24dp):
- "こんにちは, Hana!" — `Hajime.Display.Medium`, ink.
- "Ready to learn today?" — `Hajime.Body.Large`, ink_soft.

Daily stats card (CardView, surface, 20dp radius, full-width minus 24dp margin):
- 3 columns separated by 1dp vertical dividers: **Streak / Words / XP**.
- Each column: big number (Display.Small, sage_deep) + label (Label.Medium, ink_soft, ALL CAPS).

Daily quest card (CardView, gradient bg sage→sage_deep, surface text):
- Top: "DAILY QUEST" Label.Large.
- Title: "Learn 5 animal words" Headline.Medium.
- Progress bar (rounded, butter fill on rgba(white,0.25) track) 3/5.
- Right side: small XP chip "+50 XP".

Categories grid (heading **"Categories"** Headline.Large ink, then 2-column GridLayout, 16dp gap):
- 6 cards, 1:1.1 aspect, surface bg, 20dp radius, padding 16dp.
- Each card: large emoji/icon at top, category name in JP (Jp.Display 32sp), romaji underneath (Title.Medium, ink_soft), small "n words" caption (Label.Medium, ink_mute).
- One card has a PRO lock badge (chip_pro_locked) in top-right.
- Categories: Animals (sage), Food (terra), Family (lavender), Travel (sky), Numbers (butter), Body (rose) — each with a soft tinted background patch behind the icon.

Bottom: bottom navigation (surface bg, 72dp tall, top corners 24dp radius, elevation 8dp).

### 5.4 Subcategory (`fragment_subcategory.xml`)

Top bar (64dp): back arrow (ink, 24dp) left, title "Animals" Headline.Medium center, more icon right.

Hero block (32dp top padding):
- Big JP word "動物" (Jp.Display 56sp, ink) with romaji "dōbutsu" below (Title.Medium, terra_deep).
- Caption "36 words · across 4 worlds" Body.Medium, ink_soft.

Subcategory list (vertical, 14dp gap):
- Each row is a CardView, surface bg, 20dp radius, padding 16dp, height ~96dp.
- Layout: [circular icon 56dp, tinted bg] [title + subtitle stack, flex] [mastery ring 44dp with % inside]
- Title: "Pets" Title.Large ink. Subtitle: "12 words · 8 mastered" Body.Medium ink_soft.
- Mastery ring: circular progress, sage_deep fill on `#142E3A33` track, % in center (Label.Large, sage_dark).
- Subcategories: Pets, Wild Animals, Birds, Sea Creatures. Last one shows lock overlay (chip_pro_locked across the ring area).

Bottom CTA pinned (24dp margin): **"Continue lesson"** secondary button (terra gradient), full width.

### 5.5 Word detail (`fragment_word_detail.xml`)

Top bar: back arrow, star icon (toggle favorite, terra_deep when active), share icon.

Hero card (CardView, 24dp top margin, surface bg, 28dp radius, padding 32dp, centered content):
- Tag chip "ANIMALS · PETS · N5" Label.Large, ink_mute.
- Huge JP word "猫" Jp.Display 96sp, ink, centered.
- Hiragana "ねこ" Jp.Body 22sp, ink_soft.
- Romaji "neko" Title.Large, terra_deep.
- English "Cat" Headline.Large, ink_soft.
- Below, a row of 2 circular icon buttons (64dp, surface, soft shadow): play audio (sage_deep speaker icon), slow audio (sage_deep slow icon).

Example sentence card (CardView, paper_alt bg, 20dp radius, 24dp top margin):
- Label "EXAMPLE" Label.Large terra_deep.
- JP sentence "私の猫はとても可愛いです。" Jp.Body 20sp, ink, line height 32sp.
- English "My cat is very cute." Body.Large, ink_soft, italic.
- Small "▶" play button bottom-right.

Word details list (3 rows, 1dp soft dividers): Type · Noun · Level · N5 · Frequency · Common. Use Title.Medium for labels, Body.Large for values aligned right.

Bottom: **"Next word →"** primary button (sage gradient), full width with 24dp margin.

### 5.6 Lesson score (`fragment_score.xml`)

Confetti/petal SVG background already baked into `bg_score`. Content centered vertically.

- Top: "Lesson complete!" Headline.Large, ink.
- Subtitle: "You're amazing!" Body.Large, terra_deep.
- Big score ring (220dp): track `#142E3A33` 12dp, fill sage_deep, ink % in center as Display.Large (e.g. "92%"). Below ring: "Mastery" Label.Large ink_mute.
- Star row: 3 large stars (butter when earned, ink_mute outline when not), 48dp each, 16dp gap.
- Rewards row: 2 chips horizontally:
  - "+85 XP" gradient butter→terra chip, surface text Title.Large.
  - "🔥 Streak +1" surface chip with terra_deep text.
- Word breakdown card (CardView, surface, 20dp radius): "Words learned" Title.Large header, then list of 5 word rows. Each row: JP word (Jp.Body, ink) on left, romaji (Body.Medium ink_soft) below, ✓ in sage_deep circle 28dp on right.
- Bottom: 2 buttons stacked — primary "Continue" (sage gradient) and outlined "Review words" (btn_outline).

### 5.7 Profile (`fragment_profile.xml`)

Top bar: title "Profile" Headline.Medium, settings cog icon right.

Profile header (centered, 32dp top margin):
- Avatar 120dp circle, butter fill, ink emoji/initials. 4dp white border, soft shadow.
- Name "Hana Watanabe" Display.Small, ink.
- "Level 4 · 1240 XP" Body.Large, ink_soft.
- XP progress bar (rounded, 8dp tall, sage_deep on rgba(ink,0.08)) with caption "260 XP to level 5" Label.Medium, ink_mute.

Stats grid (2×2, surface cards, 16dp gap):
- Days streak (number Display.Small terra_deep, label Label.Medium)
- Words learned
- Lessons done
- Hours studied

Badges section ("Badges earned" Headline.Medium, ink):
- Horizontal RecyclerView, badges as 80dp circles with emoji + name underneath (Label.Medium). 5 badges, last one greyed out.

Settings list (CardView, surface, 20dp radius, contains LinearLayout rows divided by `divider_soft`):
- Each row: 24dp icon (ink_soft), label Title.Medium, chevron right.
- Rows: Account, Notifications, Audio settings, Parental controls, Language, Help, Log out (terra_deep text).

Toggle switches use `SwitchCompat` with `thumbTint=hajime_surface`, `trackTint` checked `hajime_sage_deep` / unchecked `hajime_ink_mute`.

---

## 6. Data layer (simple, single source for now)

Create `assets/vocab.json` with this shape:
```json
{
  "categories": [
    { "id":"animals","name":"Animals","jp":"動物","romaji":"dōbutsu","color":"hajime_sage","icon":"🐾",
      "subcategories":[
        { "id":"pets","name":"Pets","words":[
          { "jp":"猫","kana":"ねこ","romaji":"neko","en":"Cat","example_jp":"私の猫はとても可愛いです。","example_en":"My cat is very cute." }
        ]}
      ]
    }
  ]
}
```

Load with Gson on app start into a singleton `VocabRepository`. Track progress (mastered word ids, XP, streak) in `SharedPreferences` (`hajime_prefs`).

---

## 7. Animations & polish

- All buttons: `StateListAnimator` scaling to 0.97 on pressed (150ms).
- Cards in lists: stagger entrance with 60ms delay each, `translationY` 24→0, alpha 0→1, 280ms.
- Score ring: animate sweep from 0 to value over 800ms, decelerate interpolator.
- Petal/leaf overlays on splash + word_detail: 8-second `ObjectAnimator` looping translateY (-20 → 20) + rotation (-5° → 5°).
- Page transitions: use `MaterialContainerTransform` for category card → subcategory and subcategory row → word detail (shared element transitions).

---

## 8. Acceptance criteria

For each of the 7 screens, your XML output must match the reference screenshot for:
1. Background image is full-bleed, extends behind status bar.
2. Type sizes, weights, and colors match the table above exactly.
3. All cards use 20dp corner radius, 2dp elevation, `hajime_surface` fill — never grey.
4. Spacing: outer padding 24dp horizontal on all main content blocks; vertical rhythm uses 16/24/32dp steps only.
5. Buttons are 56dp tall, fully rounded (999dp), use the gradient drawables, and have ripple feedback.
6. No raw hex codes in layout XML — only `@color/hajime_*` references.
7. No system default fonts — every TextView uses `android:fontFamily="@font/nunito"` or `@font/noto_sans_jp`.

Build one screen at a time. After finishing a screen, output its full XML and a screenshot of how you've structured the layout hierarchy. Move on once it matches the reference.
