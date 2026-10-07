package ru.kuznetsov.pitstop.features.cars

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import pitstop.shared.generated.resources.Res
import pitstop.shared.generated.resources.action_add_car
import pitstop.shared.generated.resources.action_open_car
import pitstop.shared.generated.resources.screen_cars
import ru.kuznetsov.pitstop.features.navigation.PlaceholderScreen
import ru.kuznetsov.pitstop.ui.components.ComponentPreview
import ru.kuznetsov.pitstop.ui.components.buttons.Fab
import ru.kuznetsov.pitstop.ui.components.buttons.PrimaryButton
import ru.kuznetsov.pitstop.ui.icons.PitStopIcons
import ru.kuznetsov.pitstop.ui.theme.PitStopTheme

// Sample id for the stub — real ids come from the car list in step 6.
private const val SAMPLE_CAR_ID = "c1"

/** "My cars" tab — stub until step 6: one button opening a sample car and the add-car FAB. */
@Composable
fun CarsScreen(
    onOpenCar: (carId: String) -> Unit,
    onAddCar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        PlaceholderScreen(title = stringResource(Res.string.screen_cars)) {
            PrimaryButton(
                text = stringResource(Res.string.action_open_car),
                onClick = { onOpenCar(SAMPLE_CAR_ID) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Fab(
            icon = PitStopIcons.Plus,
            onClick = onAddCar,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(PitStopTheme.dimens.spaceL),
            contentDescription = stringResource(Res.string.action_add_car),
        )
    }
}

@Preview
@Composable
private fun CarsScreenLightPreview() = ComponentPreview(darkTheme = false) {
    CarsScreen(onOpenCar = {}, onAddCar = {})
}

@Preview
@Composable
private fun CarsScreenDarkPreview() = ComponentPreview(darkTheme = true) {
    CarsScreen(onOpenCar = {}, onAddCar = {})
}
