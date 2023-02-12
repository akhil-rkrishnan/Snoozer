package app.android.snoozer.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import app.android.snoozer.R
import app.android.snoozer.core.theme.DarkOrange
import app.android.snoozer.core.theme.Green

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val alarmState = viewModel.alarmState
    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.home_title), style = LocalTextStyle.current.copy(
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Text(
            text = stringResource(R.string.snoozer_desc), style = LocalTextStyle.current.copy(
                color = Color.DarkGray,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(
            value = alarmState.interval,
            onValueChange = { value ->
                viewModel.updateTimeInput(value)
            },
            modifier = Modifier
                .fillMaxWidth(0.5f),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ), placeholder = {
                Text(text = "Input time in minutes", color = Color.Gray)
            },
            singleLine = true,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                cursorColor = Color.Black,
                backgroundColor = Color.LightGray,
            ),
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp
            )
        )
        Button(
            onClick = {
                viewModel.scheduleAlarm()
            }, modifier = Modifier
                .fillMaxWidth(0.5f), colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black
            )
        ) {
            Text(
                text = stringResource(R.string.schedule), style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            )
        }

        Button(
            onClick = {
                viewModel.cancelAlarm()
            },
            modifier = Modifier.fillMaxWidth(0.5f),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Red
            )
        ) {
            Text(
                text = stringResource(R.string.stop_service), style = TextStyle(
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = FontFamily.SansSerif
                )
            )
        }

        AnimatedContent(targetState = viewModel.snoozerRunningStatus,
            transitionSpec = {
                if (viewModel.snoozerRunningStatus)
                    slideInVertically { it } with slideOutVertically { -it }
                else
                    slideInVertically { -it } with slideOutVertically { it }
            }) { isRunning ->
            Text(
                text = if (isRunning) stringResource(R.string.snoozer_service_status_running) else stringResource(
                    R.string.snoozer_service_status_idle
                ),
                style = LocalTextStyle.current.copy(
                    color = if (isRunning) Green else DarkOrange,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            )
        }
    }
}