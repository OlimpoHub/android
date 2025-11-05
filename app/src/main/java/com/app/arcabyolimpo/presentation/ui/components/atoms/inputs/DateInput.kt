@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
    val sdf = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            isLenient = false
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    var showPicker by rememberSaveable { mutableStateOf(false) }
    var inputSize by remember { mutableStateOf(IntSize.Zero) } // tamaño real del StandardInput

    // Contenedor sin 'modifier' externo para que el overlay no crezca de más
    Box {

        // 1) StandardInput con 'modifier' externo + medición de tamaño
        StandardInput(
            label = label,
            placeholder = placeholder,
            value = value,
            onValueChange = { /* no-op: solo calendario */ },
            modifier = modifier
                .fillMaxWidth()
                .onSizeChanged { inputSize = it },
            trailingIcon = {
                IconButton(onClick = { showPicker = true }) {
                    CalendarIcon(tint = MaterialTheme.colorScheme.onSurface)
                }
            },
        )

        // 2) Overlay que copia el tamaño medido (sin matchParentSize)
        val interaction = remember { MutableInteractionSource() }
        if (inputSize != IntSize.Zero) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(inputSize.width.dp, inputSize.height.dp) // ← clave
                    .clickable(
                        interactionSource = interaction,
                        indication = null
                    ) { showPicker = true }
            )
        }
    }

    if (showPicker) {
        val selectable = remember(minDateUtcMillis, maxDateUtcMillis) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val afterMin = minDateUtcMillis?.let { utcTimeMillis >= it } ?: true
                    val beforeMax = maxDateUtcMillis?.let { utcTimeMillis <= it } ?: true
                    return afterMin && beforeMax
                }
                override fun isSelectableYear(year: Int): Boolean {
                    val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    val afterMin = minDateUtcMillis?.let {
                        cal.timeInMillis = it; year >= cal.get(Calendar.YEAR)
                    } ?: true
                    val beforeMax = maxDateUtcMillis?.let {
                        cal.timeInMillis = it; year <= cal.get(Calendar.YEAR)
                    } ?: true
                    return afterMin && beforeMax
                }
            }
        }

        val initialSelected = remember(value) { value.toUtcMidnightOrNull(sdf) }
        val state = rememberDatePickerState(
            initialSelectedDateMillis = initialSelected,
            selectableDates = selectable
        )

        DatePickerDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    state.selectedDateMillis?.let { millisUtc ->
                        val chosen = sdf.format(Date(millisUtc))
                        onValueChange(chosen)
                    }
                    showPicker = false
                }) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { showPicker = false }) { Text("Cancelar") }
            }
        ) { DatePicker(state = state) }
    }
}

/* helpers */
private fun String.toUtcMidnightOrNull(sdf: SimpleDateFormat): Long? =
    try {
        val date = sdf.parse(this) ?: return null
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            time = date
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        cal.timeInMillis
    } catch (_: ParseException) { null }

@Preview
@Composable
private fun PreviewDateInput_ReadOnly() {
    var text by rememberSaveable { mutableStateOf("") }
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        DateInput(
            value = text,
            onValueChange = { text = it }
        )
    }
}

