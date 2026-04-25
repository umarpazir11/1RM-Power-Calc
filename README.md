# 1RM Power Calc

A clean, minimal Android app for estimating your **One-Rep Maximum (1RM)** — the maximum weight you can lift for a single repetition of a given exercise.

---

## Features

- **1RM Calculator** — Enter weight and reps to instantly estimate your one-rep max
- **Dual Formula Support** — Choose between Brzycki and Epley formulas
- **Percentage Breakdown** — Automatically calculates training weights at 70%, 75%, 80%, 85%, 90%, and 95% of your 1RM
- **Calculation History** — Saves every result locally so you can track progress over time
- **Undo Delete** — Accidentally deleted a record? Undo it with a single tap
- **Privacy Policy & About screens** — Transparent and informative

---

## Formulas

| Formula  | Equation |
|----------|----------|
| Brzycki  | `1RM = Weight / (1.0278 - 0.0278 × Reps)` |
| Epley    | `1RM = Weight × (1 + Reps / 30)` |

---

## Tech Stack

| Layer         | Technology |
|---------------|------------|
| Language      | Kotlin |
| UI            | Jetpack Compose + Material 3 |
| Architecture  | Clean Architecture (Domain / Data / Presentation) |
| DI            | Hilt |
| Database      | Room |
| Navigation    | Navigation Compose |
| State         | ViewModel + StateFlow |
| Build System  | Gradle with Version Catalogs |

---

## Project Structure

```
app/src/main/java/com/rm/powercalculator/
├── data/
│   ├── local/          # Room DAO, Entity, Database
│   └── repository/     # Repository implementations
├── di/                 # Hilt dependency injection modules
├── domain/
│   ├── model/          # Domain models (Calculation, FormulaType, PercentageBasedCalculation)
│   ├── repository/     # Repository interfaces
│   └── use_case/       # Business logic (Calculate1RM, CalculatePercentages, Save/Get/DeleteHistory)
└── presentation/
    ├── screens/        # AboutScreen, PrivacyPolicyScreen
    ├── ui/theme/       # Custom theme, typography, colors, shapes (Inter font)
    ├── viewmodel/      # OneRepMaxViewModel
    ├── OneRepMaxScreen.kt
    └── HistoryScreen.kt
```

---

## Requirements

- Android **5.0+ (API 24)**
- Target SDK: **35**
- Android Studio **Hedgehog** or later

---

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run on a device or emulator

```bash
git clone https://github.com/umerdilpazir/1RMPowerCalc.git
```

> **Note:** A `keystore.properties` file is required for signed release builds. For debug builds, no additional setup is needed.

---

## Version

**v1.1** (versionCode 8)

---

## License

This project is for personal and educational use.