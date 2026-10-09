package ru.kuznetsov.pitstop.data.repository

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import ru.kuznetsov.pitstop.domain.model.Car
import ru.kuznetsov.pitstop.domain.model.DistanceUnit
import ru.kuznetsov.pitstop.domain.model.MaintenanceTask
import ru.kuznetsov.pitstop.domain.model.ServiceHistoryEntry
import ru.kuznetsov.pitstop.domain.model.Settings
import ru.kuznetsov.pitstop.domain.model.ThemeMode

/**
 * Initial content of the fake repositories: the three cars, their tasks and service history
 * from the design mockup, plus the mockup's default settings.
 */
internal object FakeSeed {
    val cars = listOf(
        Car(
            id = "c1",
            brand = "Kia Rio",
            mileageKm = 84_300,
            vin = "XWEPC811BB0012345",
            tasks = listOf(
                MaintenanceTask("t1", "Замена масла и фильтра", 10_000, 78_000, 1_500, 12, LocalDate(2026, 1, 15)),
                MaintenanceTask("t2", "Тормозные колодки (передние)", 30_000, 60_000, 3_000),
                MaintenanceTask("t3", "Воздушный фильтр", 15_000, 70_000, 2_000),
                MaintenanceTask("t4", "Ремень ГРМ", 60_000, 30_000, 5_000),
            ),
        ),
        Car(
            id = "c2",
            brand = "Toyota Camry",
            mileageKm = 152_000,
            vin = "JT2BF22K1W0123456",
            tasks = listOf(
                MaintenanceTask("t5", "Свечи зажигания", 40_000, 110_000, 2_000),
                MaintenanceTask("t6", "Замена масла и фильтра", 10_000, 143_000, 1_000, 12, LocalDate(2025, 11, 20)),
                MaintenanceTask("t7", "Салонный фильтр", 20_000, 135_000, 2_000),
            ),
        ),
        Car(
            id = "c3",
            brand = "Hyundai Solaris",
            mileageKm = 23_100,
            vin = "Z94K241CBHR123456",
            tasks = listOf(
                MaintenanceTask("t8", "Замена масла и фильтра", 10_000, 20_000, 1_500, 12, LocalDate(2026, 6, 1)),
                MaintenanceTask("t9", "Тормозная жидкость", 40_000, 0, 3_000),
            ),
        ),
    )

    val history = listOf(
        ServiceHistoryEntry("h1", "c1", "Тормозные колодки (передние)", LocalDate(2025, 1, 10), 50_000, 850, "Kia Центр"),
        ServiceHistoryEntry("h2", "c1", "Тормозные колодки (передние)", LocalDate(2025, 6, 2), 60_000, 5_200, "Kia Центр"),
        ServiceHistoryEntry("h3", "c1", "Воздушный фильтр", LocalDate(2025, 9, 10), 70_000, 900, "АвтоСервис Юг"),
        ServiceHistoryEntry("h4", "c1", "Замена масла и фильтра", LocalDate(2026, 1, 15), 78_000, 2_400, "АвтоСервис Юг"),
        ServiceHistoryEntry("h5", "c2", "Тормозная жидкость", LocalDate(2024, 11, 1), 95_000, 1_800, "Toyota Центр"),
        ServiceHistoryEntry("h6", "c2", "Свечи зажигания", LocalDate(2025, 2, 14), 110_000, 3_600, "Toyota Центр"),
        ServiceHistoryEntry("h7", "c2", "Салонный фильтр", LocalDate(2025, 8, 30), 135_000, 1_200, "АвтоДок"),
        ServiceHistoryEntry("h8", "c2", "Замена масла и фильтра", LocalDate(2025, 11, 20), 143_000, 2_600, "АвтоДок"),
        ServiceHistoryEntry("h9", "c3", "Замена масла и фильтра", LocalDate(2026, 6, 1), 20_000, 2_100, "Hyundai Центр"),
    )

    val settings = Settings(
        themeMode = ThemeMode.SYSTEM,
        distanceUnit = DistanceUnit.KILOMETERS,
        pushEnabled = true,
        defaultReminderWindowKm = 2_000,
        reminderTime = LocalTime(9, 0),
    )
}
