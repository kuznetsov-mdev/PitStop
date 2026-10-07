# План разработки PitStop (KMP)

## Контекст

Проект `PitStop` (Android + iOS, Kotlin Multiplatform) сейчас — это чистый wizard-скаффолд: `shared/src/commonMain` содержит только дефолтные `App.kt`/`Greeting.kt`/`Platform.kt`, `androidApp` и `iosApp` уже корректно хостят общий Compose-экран (`MainActivity` → `setContent { App() }`, `ContentView.swift` → `MainViewController()`), но никакой прикладной логики, экранов, темы или данных ещё нет.

Цель — пошагово построить приложение по дизайн-макету (`PitStop_design.html`): сначала design-tokens и переиспользуемые компоненты, затем экраны на фейковых данных, затем domain/data слой и связка через domain, затем уведомления и полировка. Подключение каждой новой библиотеки привязано к конкретному шагу, на котором она впервые понадобится — ничего не тянем заранее "про запас".

**Важное отличие от исходного предположения в задаче:** Compose Multiplatform **уже подключён и настроен** в `shared/build.gradle.kts` — плагины `composeMultiplatform`/`composeCompiler` применены, а `compose.{runtime,foundation,material3,ui,components.resources}` и `androidx.lifecycle.{viewmodelCompose,runtimeCompose}` уже объявлены в зависимостях `commonMain`. Отдельный шаг "подключить Compose Multiplatform" не нужен — этап 1 сразу переходит к написанию тем/токенов.

## Структура пакетов

Базовый пакет: `ru.kuznetsov.pitstop` (`shared/src/commonMain/kotlin/ru/kuznetsov/pitstop/`).

Как и просил пользователь — `features`, `domain`, `data`, `di`. Плюс один дополнительный пакет `ui` — для темы, переиспользуемых компонентов и иконок, которые не относятся ни к одной конкретной фиче и нужны до того, как появится хоть один экран:

```
ru.kuznetsov.pitstop/
├── ui/
│   ├── theme/        # Color.kt, Type.kt, Theme.kt, Dimens.kt
│   ├── components/   # PrimaryButton, Fab, IconButton, StatusChip, GaugeRing, Card-варианты...
│   └── icons/         # PitStopIcons.kt — иконки из макета как ImageVector
├── domain/
│   ├── model/         # Car, MaintenanceTask, ServiceHistoryEntry, TaskStatus, TaskStat
│   ├── usecase/        # CalculateTaskStatus, SortedTaskStats, CarAlerts, MarkTaskDone...
│   └── repository/    # интерфейсы: CarRepository, ServiceHistoryRepository, SettingsRepository
├── data/
│   ├── local/          # SQLDelight driver (expect/actual), .sq-схемы
│   ├── repository/     # Fake*RepositoryImpl (этап 5) → *RepositoryImpl на SQLDelight (этап 7)
│   └── mapper/         # маппинг SQLDelight-строк ↔ domain-модели
├── di/                # Koin-модули: uiModule, domainModule, dataModule
└── features/
    ├── navigation/      # Destinations, NavGraph
    ├── cars/            # экран "Мои авто"
    ├── addcar/          # экран "Добавление авто"
    ├── cardetail/       # экран "Регламентные работы"
    ├── history/         # экран "История ТО"
    └── settings/        # экран "Настройки"
```

ViewModel-ы — `StateFlow<UiState>`, экран подписывается через `collectAsState()`; DI — Koin, инжект через `koinViewModel()` прямо в Composable (подтверждено пользователем).

## Этапы

### Этап 1 — Тема и шрифты
**Библиотеки:** не требуются — Compose Multiplatform уже подключён.
- `ui/theme/Color.kt` — нейтральные токены (light/dark): `--bg/--surface/--surface-2/--surface-3/--ink/--ink-muted/--ink-faint/--rule`, плюс 3 статус-константы `tier-ok/soon/due` и акцент `#0ea5b7`, одинаковые в обеих темах.
- `ui/theme/Type.kt` — три роли шрифта из макета: display/sans (`FontFamily.Default`, т.к. это уже системный шрифт на каждой платформе — San Francisco на iOS, Roboto на Android, кастомные файлы не нужны) и mono (`FontFamily.Monospace`) для чисел/VIN/дат.
- `ui/theme/Theme.kt` — `PitStopTheme` поверх `MaterialTheme`, плюс свой `CompositionLocal` для tier-цветов (их нет в стандартной `ColorScheme` Material3).
- `ui/theme/Dimens.kt` — отступы/радиусы (12/14/24px и т.д.) по макету.
- Проверка: `@Preview`-композабл с палитрой и образцами трёх шрифтовых ролей в обеих темах.

### Этап 2 — Общие компоненты (ui-kit)
**Библиотеки:** не требуются.
- `ui/icons/PitStopIcons.kt` — 17 иконок из макета (car, gauge, droplet, disc, filter, wrench, gear, clock, check, chevronLeft, pencil, trash, bell, plus, minus, sun, moonToggle) как `ImageVector`, SVG `path d=...` переносится напрямую — точные path-данные уже есть в `PitStop_design.html`.
- `ui/components/` — 19 компонентов из каталога макета, по группам:
  - Кнопки: `PrimaryButton`, `Fab`, `IconButton`, `PresetChip`, `SegmentedControl`, `TabBarItem`
  - Поля: `AppTextField`, `AppNumberField`, `LabeledMiniField`, `InlineEditField`
  - Степпер: `Stepper`
  - Индикаторы: `AppSwitch`, `StatusChip`, `ProgressBar`, `GaugeRing`
  - Карточки: `ListRowCard`, `StatCard`, `NotificationPreviewCard`
- Проверка: `@Preview` каждого компонента в обеих темах (как в макете — light/dark exhibit).

### Этап 3 — Навигация
**Библиотека:** `org.jetbrains.androidx.navigation:navigation-compose:2.10.0-beta01` (JetBrains-форк Navigation Compose для Compose Multiplatform; "чистый" `androidx.navigation` 2.10.2 — Android-only). Добавить версию+alias в `gradle/libs.versions.toml`, зависимость — в `commonMain.dependencies` модуля `shared`.
- `features/navigation/Destinations.kt` — sealed-класс: `Cars`, `AddCar`, `CarDetail(carId)`, `History(carId)`, `Settings`.
- `features/navigation/NavGraph.kt` — `NavHost`, становится новым корневым composable вместо wizard'ового `App()`.
- Почему именно сейчас: экраны на следующем этапе сразу пишутся с переходами между собой, retrofit-навигацию потом не делаем.

### Этап 4 — DI (Koin)
**Библиотека:** `io.insert-koin:koin-bom:4.2.2` + `koin-core`, `koin-compose`, `koin-compose-viewmodel`. Добавить alias'ы в каталог, зависимости — в `commonMain.dependencies`.
- `di/` — `uiModule`/`domainModule`/`dataModule`, функция `initKoin()`, вызываемая из `Application` (Android) и перед `MainViewController()` (iOS).
- На этом этапе в `dataModule` регистрируются **фейковые** реализации репозиториев (см. этап 5) — реальные появятся на этапе 7 без изменений в `di`-API, только подменой binding'а.
- Почему именно сейчас: экраны (этап 6) с первого дня инжектят `ViewModel` через `koinViewModel()`, как подтверждено пользователем.

### Этап 5 — Domain-модели и бизнес-логика + фейковые данные
**Библиотеки:** `org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0` (явный `Flow` в репозиториях), `org.jetbrains.kotlinx:kotlinx-datetime:0.8.0` (разница дат для интервала по месяцам). Добавить в `commonMain.dependencies`.
- `domain/model/` — `Car`, `MaintenanceTask`, `ServiceHistoryEntry`, `TaskStatus` (OK/SOON/DUE), `TaskStat`.
- `domain/usecase/` — перенос JS-логики из макета 1:1 на Kotlin: `CalculateTaskStatusUseCase` (аналог `taskStat`/`worseStatus`), `SortedTaskStatsUseCase` (`sortedTaskStats`), `CarAlertsUseCase`/`worstStatus` — чистые функции, без зависимости от data-слоя.
- `domain/repository/` — интерфейсы `CarRepository`, `ServiceHistoryRepository`, `SettingsRepository` (методы возвращают `Flow<...>`).
- `data/repository/Fake*RepositoryImpl` — in-memory реализация, засеянная теми же тремя машинами (Kia Rio/Toyota Camry/Hyundai Solaris), что и в макете; регистрируется в `dataModule` (этап 4).
- Юнит-тесты в `commonTest` на `CalculateTaskStatusUseCase`: статус по км, по времени, "что раньше" — закрывает требование плана `.md` "перенос расчёта статуса из JS в Kotlin + unit-тесты" ещё до того, как появится UI.

### Этап 6 — Вёрстка экранов (на фейковых данных)
**Библиотеки:** не требуются — все уже подключены на этапах 2–5.
- `features/cars/` — список авто (`ui-kit` карточки + `GaugeRing`), `CarsViewModel: StateFlow<CarsUiState>`.
- `features/addcar/` — форма + preset-чипы, `AddCarViewModel`.
- `features/cardetail/` — список работ, инлайн-правка пробега, "отметить выполненной", `CarDetailViewModel`.
- `features/history/` — список истории ТО, `HistoryViewModel`.
- `features/settings/` — тема/единицы/push, `SettingsViewModel` (состояние пока в памяти — персистентность появится на этапе 7 вместе с остальными данными).
- Каждый экран: данные из `Fake*RepositoryImpl` (этап 5) через `domain`-интерфейсы, переходы через `NavGraph` (этап 3), `ViewModel` через `koinViewModel()` (этап 4).
- Проверка: ручной прогон на Android-эмуляторе (и по возможности iOS-симуляторе) по всем 5 экранам на фейковых данных — соответствует этапам 3–7 исходного `План_разработки_Одометр.md`.

### Этап 7 — Реальное хранилище (SQLDelight) и связка presentation ↔ data через domain
**Библиотека:** `app.cash.sqldelight` plugin `2.4.0` (`apply false` в корневом `build.gradle.kts`, применяется в `shared/build.gradle.kts`) + `app.cash.sqldelight:runtime`, `coroutines-extensions` (`commonMain`), `android-driver` (`androidMain`), `native-driver` (`iosMain`).
- `.sq`-схемы в `data/local/` по схеме из README: `cars`, `maintenance_tasks`, `service_history` (+ таблица/key-value для настроек из этапа 6).
- `expect/actual DatabaseDriverFactory` (Android: `AndroidSqliteDriver` с `Context` из Koin; iOS: `NativeSqliteDriver`).
- `data/repository/*RepositoryImpl` на SQLDelight + `data/mapper/` для маппинга строк в domain-модели.
- В `dataModule` (этап 4) **только подменяются binding'и** `Fake*RepositoryImpl` → реальные `*RepositoryImpl` — `ViewModel`/экраны из этапа 6 не меняются вообще, потому что изначально зависели от `domain`-интерфейсов, а не от конкретной реализации. Это и есть прямая реализация "свяжем presentation и data слои через domain" из исходного запроса.
- Проверка: тот же ручной прогон, что в этапе 6, но с проверкой, что данные переживают перезапуск приложения.

### Этап 8 — Локальные уведомления
**Библиотека:** Android — `androidx.work:work-runtime-ktx` (WorkManager, новая запись в каталоге, только `androidMain`) либо `AlarmManager` без новой зависимости; iOS — `UNUserNotificationCenter` через `expect/actual`, без новых библиотек.
- Источник данных для уведомлений — `CalculateTaskStatusUseCase`/`CarAlertsUseCase` из этапа 5.
- Анти-спам правило по README (не напоминать об одной и той же работе чаще N дней).

### Этап 9 — Полировка
Без новых библиотек: QA тёмной темы на всех экранах, анимации (`ProgressBar`/`GaugeRing` transitions), iOS-специфика (safe area, жесты навигации — Compose insets уже прокинуты через `.ignoresSafeArea()`).

### Этап 10 — Тестирование и релиз
Расширение `commonTest` (сортировка, граничные случаи дат), прогон на реальных Android/iOS устройствах, сборка Google Play / TestFlight.

## Сводная таблица "когда что подключаем"

| Этап | Библиотека | Где |
|---|---|---|
| 1 | — (Compose MP уже подключён) | — |
| 2 | — | — |
| 3 | `org.jetbrains.androidx.navigation:navigation-compose:2.10.0-beta01` | `commonMain` |
| 4 | `io.insert-koin:koin-bom:4.2.2` + core/compose/compose-viewmodel | `commonMain` |
| 5 | `kotlinx-coroutines-core:1.11.0`, `kotlinx-datetime:0.8.0` | `commonMain` |
| 6 | — | — |
| 7 | `app.cash.sqldelight:2.4.0` (plugin + runtime/coroutines-extensions/android-driver/native-driver) | plugin: root+`shared`; driver-deps: `androidMain`/`iosMain` |
| 8 | (опц.) `androidx.work:work-runtime-ktx` | `androidMain` |

## Верификация

- После этапа 2: превью всех компонентов в обеих темах рендерятся без ошибок (`./gradlew :shared:assembleDebug` или Preview в IDE).
- После этапа 6: ручной клик-тест всех 5 экранов и переходов между ними на Android-эмуляторе, на фейковых данных.
- После этапа 5 и расширений в этапе 10: `./gradlew :shared:testAndroidHostTest` / `:shared:iosSimulatorArm64Test` — юнит-тесты бизнес-логики статусов зелёные.
- После этапа 7: перезапуск приложения — данные (добавленное авто, отметка "выполнено", изменённый пробег) сохраняются.
- Критерий готовности MVP (из `План_разработки_Одометр.md`) не меняется: этапы 1–7 данного плана = этапы 1–7 исходного, завершаются рабочим приложением на Android и iOS с корректным расчётом статусов и реальным хранилищем.
