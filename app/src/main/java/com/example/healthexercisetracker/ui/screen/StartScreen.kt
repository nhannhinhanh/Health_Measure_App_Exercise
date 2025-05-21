package com.example.healthexercisetracker.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.example.healthexercisetracker.viewmodel.ExerciseViewModel
import androidx.compose.ui.text.font.FontFamily
@Composable
fun StartScreen(
    viewModel: ExerciseViewModel,
    onStartExercise: () -> Unit,
    onOpenHistory: () -> Unit
) {
    var exerciseName by remember { mutableStateOf(viewModel.exerciseName) }

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF666666)), // trắng ngà
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BasicTextField(
                    value = exerciseName,
                    onValueChange = {
                        exerciseName = it
                        viewModel.exerciseName = it
                    },
                    textStyle = TextStyle(
                        fontSize = 20.sp,
                        color = Color(0xFFFFA500), // vàng cam
                        fontFamily = FontFamily.Serif
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color(0xFFFFA500)),
                    modifier = Modifier
                        .background(Color(0xFF666666))
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .padding(bottom = 10.dp)
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )

                Button(
                    onClick = onStartExercise,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFFA500), // vàng cam
                        contentColor = Color.Black
                    ),
                    modifier = Modifier
                        .size(width =80.dp, height = 30.dp), // hình vuông nhỏ hơn
                    shape = MaterialTheme.shapes.small // bo góc nhẹ
                ) {
                    Text(
                        "Start",
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "History",
                    color = Color.White,
                    fontFamily = FontFamily.Serif,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clickable(onClick = onOpenHistory)
                        .padding(top = 8.dp)
                )
            }
        }
    }
}
