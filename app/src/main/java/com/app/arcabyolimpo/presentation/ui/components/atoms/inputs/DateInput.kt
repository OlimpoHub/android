@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Campo de **fecha** controlado (state hoisting) que abre un DatePicker.
 *
 * - No permite teclear: al tocar abre el calendario.
 * - Al confirmar, emite `dd/MM/yyyy` por [onValueChange].
 * - Puede deshabilitar visualmente fechas fuera de [minDateUtcMillis] / [maxDateUtcMillis].
 *
 * @param label Texto de etiqueta del campo.
 * @param placeholder Texto de ayuda cuando está vacío (formato).
 * @param value Valor de la fecha en formato `dd/MM/yyyy`.
 * @param onValueChange Callback cuando el usuario selecciona fecha.
 * @param modifier Modificador Compose.
 * @param minDateUtcMillis Límite inferior (medianoche UTC), opcional.
 * @param maxDateUtcMillis Límite superior (medianoche UTC), opcional.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun DateInput(
    label: String = "Fecha de Elaboración",
    placeholder: String = "dd/MM/yyyy",
    value: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minDateUtcMillis: Long? = null,
    maxDateUtcMillis: Long? = null,
) {
    // Formateador compatible con minSdk 24 (sin java.time)
    val dateFormatter: SimpleDateFormat = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    var showPicker: Boolean by rememberSaveable { mutableStateOf(false) }
    var inputSize: IntSize by remember { mutableStateOf(IntSize.Zero) }

    Box {
        // Campo visible: solo se actualiza desde el picker
        StandardInput(
            label = label,
            placeholder = placeholder,
            value = value,
            onValueChange = { /* bloqueado para escritura */ },
            modifier = modifier
                .fillMaxWidth()
                .onSizeChanged { newSize -> inputSize = newSize },
            trailingIcon = {
                IconButton(onClick = { showPicker = true }) {
                    CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
                }
            },
        )

        // Overlay estrictamente del tamaño del input (evita capturar toda la pantalla)
        if (inputSize != IntSize.Zero) {
            val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(inputSize.width.dp, inputSize.height.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                    ) {
                        showPicker = true
                    },
            )
        }
    }

    if (showPicker) {
        val selectableDates: SelectableDates = remember(minDateUtcMillis, maxDateUtcMillis) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val okMin: Boolean = minDateUtcMillis?.let { utcTimeMillis >= it } ?: true
                    val okMax: Boolean = maxDateUtcMillis?.let { utcTimeMillis <= it } ?: true
                    return okMin && okMax
                }

                override fun isSelectableYear(year: Int): Boolean {
                    val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    val okMin: Boolean = minDateUtcMillis?.let {
                        cal.timeInMillis = it; year >= cal.get(Calendar.YEAR)
                    } ?: true
                    val okMax: Boolean = maxDateUtcMillis?.let {
                        cal.timeInMillis = it; year <= cal.get(Calendar.YEAR)
                    } ?: true
                    return okMin && okMax
                }
            }
        }

        val initialSelectedUtc: Long? = remember(value) { value.toUtcMidnightOrNull(dateFormatter) }

        val pickerState: DatePickerState = rememberDatePickerState(
            initialSelectedDateMillis = initialSelectedUtc,
            selectableDates = selectableDates,
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    pickerState.selectedDateMillis?.let { millis ->
                        onValueChange(dateFormatter.format(Date(millis)))
                    }
                    showPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancelar") }
            },
        ) {
            DatePicker(state = pickerState)
        }
    }
}

/** Convierte "dd/MM/yyyy" a epoch millis (medianoche UTC), o null si no parsea. */
private fun String.toUtcMidnightOrNull(dateFormatter: SimpleDateFormat): Long? =
    try {
        val parsed: Date = dateFormatter.parse(this) ?: return null
        val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = parsed
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        cal.timeInMillis
    } catch (_: ParseException) {
        null
    }

@Suppress("ktlint:standard:function-naming")
@Preview
@Composable
private fun DateInputPreview_Dark() {
    var text: String by rememberSaveable { mutableStateOf("") }
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        DateInput(value = text, onValueChange = { text = it })
    }
}
