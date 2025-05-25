package com.example.flightapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import com.example.flightapp.ui.theme.*

val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Fonts,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Fonts,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        color = PrimaryBlue
    )


)
