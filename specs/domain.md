# Спецификация: domain-слой

Пакет: `ru.kuznetsov.pitstop.domain` (`shared/src/commonMain/kotlin/ru/kuznetsov/pitstop/domain/`).

Domain-слой — чистый Kotlin без зависимостей от UI и хранилища. Из библиотек использует только `kotlinx-datetime` (`LocalDate`) и, для репозиториев, `kotlinx-coroutines-core` (`Flow`).

```
domain/
├── model/        # модели
├── usecase/      # бизнес-логика
└── repository/   # интерфейсы репозиториев (ещё не созданы)
```

## Модели

Моделей семь: три **хранимые** (то, что ляжет в базу) и четыре **вычисляемые** (считаются на лету и нигде не сохраняются).

```
Car ──(tasks, вложены)──▶ MaintenanceTask
 ▲                              │
 │ carId                        │ + пробег машины + сегодняшняя дата
 │                              ▼
ServiceHistoryEntry          TaskStat ──▶ TaskStatus
                                │
                 TaskWithStat = MaintenanceTask + TaskStat
                 CarAlerts    = счётчики TaskStatus по всем работам машины
```

### Хранимые

#### `Car`

Машина. Корень: работы живут внутри машины и отдельно от неё не существуют.

| Поле | Тип | Смысл |
|---|---|---|
| `id` | `String` | идентификатор, он же аргумент маршрута навигации |
| `brand` | `String` | марка и модель одной строкой |
| `mileageKm` | `Int` | текущий пробег |
| `vin` | `String?` | VIN; `null`, если не указан (прочерк подставляет UI) |
| `tasks` | `List<MaintenanceTask>` | регламентные работы этой машины |

#### `MaintenanceTask`

Регламентная работа — «правило»: что делать и как часто. Срок наступает по пробегу и, опционально, по времени — что раньше.

| Поле | Тип | Смысл |
|---|---|---|
| `id` | `String` | идентификатор |
| `name` | `String` | название работы |
| `intervalKm` | `Int` | интервал по пробегу |
| `lastDoneKm` | `Int` | пробег, на котором работу делали в последний раз |
| `reminderWindowKm` | `Int` | за сколько км до срока статус становится `SOON` |
| `intervalMonths` | `Int?` | интервал по времени |
| `lastDoneDate` | `LocalDate?` | дата последнего выполнения |

Срок по времени учитывается, только когда заданы и `intervalMonths`, и `lastDoneDate`.

#### `ServiceHistoryEntry`

Запись в журнале ТО — «факт»: что сделали, когда и на каком пробеге.

| Поле | Тип | Смысл |
|---|---|---|
| `id` | `String` | идентификатор |
| `carId` | `String` | машина, к которой относится запись |
| `taskName` | `String` | название работы текстом |
| `date` | `LocalDate` | дата выполнения |
| `mileageKm` | `Int` | пробег на момент выполнения |
| `costRub` | `Int?` | стоимость |
| `serviceName` | `String?` | где делали |

С работой запись не связана ссылкой: `taskName` хранится текстом, поэтому запись переживает переименование и удаление работы.

### Вычисляемые

#### `TaskStatus`

Срочность работы: `OK`, `SOON`, `DUE`. Значения объявлены по возрастанию срочности, поэтому худший из двух статусов — `maxOf(a, b)`.

#### `TaskStat`

Состояние одной работы на данный момент. Считается из работы, пробега машины и сегодняшней даты.

| Поле | Тип | Смысл |
|---|---|---|
| `dueAtKm` | `Int` | пробег, на котором наступает срок |
| `remainingKm` | `Int` | сколько км осталось; отрицательное при просрочке |
| `remainingMonths` | `Int?` | сколько месяцев осталось; `null`, если срока по времени нет; ноль или меньше при просрочке |
| `status` | `TaskStatus` | итоговый статус |
| `progress` | `Float` | доля пройденного интервала по пробегу, от 0 до 1 |

#### `TaskWithStat`

Пара «работа + её состояние» (`task`, `stat`). Нужна экрану работ, чтобы показать название и статус в одной строке.

#### `CarAlerts`

Сводка по машине.

| Поле | Тип | Смысл |
|---|---|---|
| `soon` | `Int` | сколько работ в статусе `SOON` |
| `due` | `Int` | сколько работ в статусе `DUE` |
| `taskCount` | `Int` | сколько всего работ у машины |
| `total` | `Int` | вычисляемое: `soon + due` |
| `worstStatus` | `TaskStatus` | вычисляемое: `DUE`, если есть просроченные; иначе `SOON`, если есть «скоро»; иначе `OK` |

## Use case-ы

Все три — чистые функции без состояния, зарегистрированы в `domainModule` (`di/DomainModule.kt`). Сегодняшняя дата всегда передаётся параметром `today`.

| Use case | Вход | Выход |
|---|---|---|
| `CalculateTaskStatusUseCase` | `mileageKm`, `task`, `today` | `TaskStat` |
| `SortedTaskStatsUseCase` | `car`, `today` | `List<TaskWithStat>` |
| `CarAlertsUseCase` | `car`, `today` | `CarAlerts` |

### Правила расчёта статуса (`CalculateTaskStatusUseCase`)

По пробегу:

- `dueAtKm = lastDoneKm + intervalKm`, `remainingKm = dueAtKm − mileageKm`.
- `remainingKm ≤ 0` → `DUE`; `remainingKm ≤ reminderWindowKm` → `SOON`; иначе `OK`.
- `progress = (mileageKm − lastDoneKm) / intervalKm`, ограничено диапазоном 0..1; при `intervalKm ≤ 0` равен 0.

По времени (только если заданы `intervalMonths > 0` и `lastDoneDate`):

- Прошло месяцев — число полных месяцев между `lastDoneDate` и `today`: разница по году и месяцу, минус один, если день месяца в `today` меньше, чем в `lastDoneDate`; не меньше нуля.
- `remainingMonths = intervalMonths − прошло месяцев`.
- Окно напоминания в месяцах — окно по пробегу, перенесённое на интервал по времени: `round(intervalMonths × reminderWindowKm / intervalKm)`, но не меньше 1; при `intervalKm ≤ 0` равно 1.
- `remainingMonths ≤ 0` → `DUE`; `remainingMonths ≤ окно` → `SOON`; иначе `OK`.

Итоговый статус — худший из двух. Если срока по времени нет, `remainingMonths = null` и статус берётся по пробегу.

### Сортировка (`SortedTaskStatsUseCase`)

Сначала `DUE`, затем `SOON`, затем `OK`; внутри одного статуса — по возрастанию `remainingKm`.

### Пример

Kia Rio, пробег 84 300 км. Работа «Замена масла и фильтра»: интервал 10 000 км / 12 мес, последний раз на 78 000 км 15.01.2026, окно напоминания 1 500 км. Сегодня 08.10.2026.

- По пробегу: срок на 88 000 км, осталось 3 700 км — больше окна, `OK`. `progress = 0,63`.
- По времени: прошло 8 полных месяцев, осталось 4. Окно — `round(12 × 1 500 / 10 000) = 2`, значит `OK`.
- Итог: `TaskStat(dueAtKm = 88000, remainingKm = 3700, remainingMonths = 4, status = OK, progress = 0.63)`.

## Что ещё не реализовано

- `domain/repository/` — интерфейсы `CarRepository`, `ServiceHistoryRepository`, `SettingsRepository`.
- Операция «отметить работу выполненной»: обновляет у работы `lastDoneKm` и `lastDoneDate` и добавляет `ServiceHistoryEntry` в журнал.
- Юнит-тесты на расчёт статуса.
- Не решено, откуда берётся иконка работы: в модели её нет, в макете она либо зашита в данные, либо подбирается по названию.
