@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.CalendarIcon
import com.app.arcabyolimpo.presentation.util.toUtcMidnightOrNull
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.White
import okhttp3.internal.wait
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
    val dateFormatter: SimpleDateFormat =
        remember {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
                isLenient = false
                timeZone = TimeZone.getTimeZone("UTC")
            }
        }

    var showPicker by rememberSaveable { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    StandardInput(
        label = label,
        placeholder = placeholder,
        value = value,
        onValueChange = { /* bloqueado para escritura */ },
        modifier =
            modifier
                // evita que el TextField pueda enfocarse (no hay cursor ni teclado)
                .focusProperties { canFocus = false }
                // por si alguna vista le da foco, lo limpiamos
                .onFocusChanged { if (it.isFocused) focusManager.clearFocus() },
        trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                CalendarIcon(tint = White)
            }
        },
    )

    /*
     * trailingIcon = {
            IconButton(onClick = { showPicker = true }) {
                CalendarIcon(tint = White)
            }
        },*/

    if (showPicker) {
        val selectableDates: SelectableDates =
            remember(minDateUtcMillis, maxDateUtcMillis) {
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val okMin = minDateUtcMillis?.let { utcTimeMillis >= it } ?: true
                        val okMax = maxDateUtcMillis?.let { utcTimeMillis <= it } ?: true
                        return okMin && okMax
                    }

                    override fun isSelectableYear(year: Int): Boolean {
                        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        val okMin =
                            minDateUtcMillis?.let {
                                cal.timeInMillis = it
                                year >= cal.get(Calendar.YEAR)
                            } ?: true
                        val okMax =
                            maxDateUtcMillis?.let {
                                cal.timeInMillis = it
                                year <= cal.get(Calendar.YEAR)
                            } ?: true
                        return okMin && okMax
                    }
                }
            }

        val initialSelectedUtc: Long? = remember(value) { value.toUtcMidnightOrNull(dateFormatter) }
        val pickerState =
            rememberDatePickerState(
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

@Suppress("ktlint:standard:function-naming")
@Preview(showSystemUi = false, showBackground = false, backgroundColor = 0xFF1E1F23)
@Composable
private fun DateInputPreview_Dark() {
    var text by rememberSaveable { mutableStateOf("") }
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        DateInput(value = text, onValueChange = { text = it })
    }
}
