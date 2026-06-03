# PathLingo Design Architecture

## Fonts

| Token | XML | Weight | Usage |
|---|---|---|---|
| Regular | `@font/poppins_regular` | 400 | Body, subtitles, menu labels |
| SemiBold | `@font/poppins_semibold` | 600 | — |
| Bold | `@font/poppins_bold` | 700 | Buttons, labels, headings |
| Black | `@font/poppins_black` | 900 | Auth page titles |

## Colors

| Token | Hex | Usage |
|---|---|---|
| `@color/color_theme_dark` | `#57A4F6` | Primary blue, links, XP text |
| `@color/color_theme_dark_black` | `#15559A` | Deep blue, secondary |
| `@color/color_theme_light` | `#B0D4FB` | Button fill, light accents |
| `@color/color_theme_navy` | `#225692` | Button text on light fill |
| `@color/color_theme_black` | `#1D1D1D` | Icon tint (`color_theme_black`) |
| `@color/ink` | → `color_theme_black` | Primary text |
| `@color/ink_soft` | → `color_theme_dark` | Secondary/muted text, icons |
| `@color/white` / `@color/paper` | `#FFFFFF` | Card backgrounds |
| `#E53935` | — | Destructive actions (deactivate, delete) |

## Auth Pages (Login · Register · OTP)

All auth pages share:
- Root: `ConstraintLayout` + `<include layout="@layout/layout_auth_background" />`
- Scroll: `ScrollView` with `fillViewport="true"`
- Inner `LinearLayout`: `paddingHorizontal="@dimen/_24sdp"`, `paddingVertical="@dimen/_32sdp"`, `gravity="center_vertical"`

### Typography
| Element | Size | Font | Notes |
|---|---|---|---|
| Page title | `@dimen/_32ssp` | `poppins_bold` | `textAllCaps`, `includeFontPadding="false"` |
| Subtitle | `@dimen/_11ssp` | `poppins_bold` | `includeFontPadding="false"`, `marginTop _4sdp` |
| Input text | `@dimen/_12ssp` | system default | Inside `TextInputEditText` |
| Button label | `@dimen/_14ssp` | `poppins_bold` | |
| Sign-in link text | `@dimen/_10ssp` | `poppins` | |
| Sign-in link action | `@dimen/_10ssp` | `poppins_bold` | |

### Spacing
| Element | Value |
|---|---|
| Email input marginTop | `@dimen/_16sdp` |
| Password input marginTop | `@dimen/_8sdp` |
| Primary button marginTop | `@dimen/_16sdp` |
| Divider "Or" marginBottom | `@dimen/_16sdp` |
| Social buttons size | `@dimen/_36sdp` × `@dimen/_36sdp` |
| Social button corner | `@dimen/_24sdp` |
| Social icon size | `@dimen/_24sdp` |
| Guest button marginTop | `@dimen/_16sdp` |
| Sign-up row marginTop | `@dimen/_16sdp` |

### Primary Button (canonical)
```xml
android:layout_height="@dimen/_46sdp"
android:fontFamily="@font/poppins_bold"
android:textColor="@color/color_theme_navy"
android:textSize="@dimen/_14ssp"
android:textAllCaps="false"
android:letterSpacing="0"
app:backgroundTint="@color/color_theme_light"
app:cornerRadius="@dimen/_8sdp"
```

### Outlined Button (guest / resend / sign-out)
```xml
style="@style/Widget.MaterialComponents.Button.OutlinedButton"
android:layout_height="@dimen/_46sdp"
android:fontFamily="@font/poppins_bold"
android:textAllCaps="false"
android:textSize="@dimen/_12ssp"
android:textColor="@color/color_theme_dark"
android:letterSpacing="0"
app:cornerRadius="@dimen/_8sdp"
app:strokeColor="@color/color_theme_dark"
app:strokeWidth="1dp"
```

### TextInputLayout (outlined fields)
```xml
style="@style/Widget.MaterialComponents.TextInputLayout.OutlinedBox"
android:layout_height="@dimen/_46sdp"
app:boxBackgroundColor="@color/paper"
app:boxCornerRadius*="@dimen/_8sdp"
app:boxStrokeColor="@color/auth_field_stroke"
```

## Profile Page

- Background: `#EDF5FF`
- Outer margins: `layout_marginHorizontal="@dimen/_16sdp"`, `layout_marginTop="@dimen/_24sdp"`
- Section label: `@dimen/_9ssp`, `poppins_bold`, `@color/ink_soft`, `paddingVertical _8sdp`

### Section Cards
```xml
app:cardCornerRadius="@dimen/_8sdp"
app:cardElevation="0dp"
app:strokeColor="@color/color_theme_dark"
app:strokeWidth="1dp"
android:padding="@dimen/_8sdp"  (inner LinearLayout)
```

### Menu Row (canonical — matches rowEditProfile)
```xml
android:layout_height="@dimen/_36sdp"
android:gravity="center_vertical"
android:orientation="horizontal"
android:paddingHorizontal="@dimen/_8sdp"
android:clickable="true"
android:focusable="true"
```
- Icon: `@dimen/_18sdp` × `@dimen/_18sdp`, `app:tint="@color/color_theme_black"` (required — use `app:tint`, never `android:tint`)
- Label: `layout_marginHorizontal="@dimen/_8sdp"`, `@dimen/_12ssp`, `poppins_regular`, `includeFontPadding="false"`
- Chevron: `@dimen/_16sdp` × `@dimen/_16sdp`, no tint (except red destructive rows use `app:tint="#E53935"`)
- Divider: `1dp`, `@color/ink`, `layout_marginHorizontal="@dimen/_6sdp"`

### Destructive rows (Deactivate / Delete)
- Icon: `app:tint="#E53935"`
- Label: `android:textColor="#E53935"`
- Delete row chevron: `app:tint="#E53935"`

### User Header Card
- Avatar card: `@dimen/_56sdp` × `@dimen/_56sdp`, `cardCornerRadius="16dp"`, `cardBackgroundColor="@color/color_theme_light"`
- Name: `@dimen/_12ssp`, `poppins_bold`, `includeFontPadding="false"`
- Email: `@dimen/_11ssp`, `poppins_regular`
- XP: `@dimen/_11ssp`, `poppins_bold`, `@color/color_theme_dark`, `includeFontPadding="false"`

## Global Rules

- `android:tint` is **not allowed** — always use `app:tint`
- Screen orientation: all activities locked to `portrait` in `AndroidManifest.xml`
- Dimension library: `sdp` for layout sizes, `ssp` for text sizes — never raw `dp`/`sp` for sizes
- No hardcoded color hex except `#E53935` (destructive red) and `#EDF5FF` (profile background)
