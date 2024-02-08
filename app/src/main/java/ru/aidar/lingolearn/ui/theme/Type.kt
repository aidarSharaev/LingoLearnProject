package ru.aidar.lingolearn.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ru.aidar.lingolearn.R

val LlFontFamily = FontFamily(

    Font(R.font.roboto_slab_bold, FontWeight.Bold),
    Font(R.font.roboto_slab_extra_bold, FontWeight.ExtraBold),
    Font(R.font.roboto_slab_black, FontWeight.Black),
    Font(R.font.roboto_slab_semi_bold, FontWeight.SemiBold),

    Font(R.font.roboto_slab_medium, FontWeight.Medium),
    Font(R.font.roboto_slab_regular, FontWeight.Normal),

    Font(R.font.roboto_slab_light, FontWeight.Light),
    Font(R.font.roboto_slab_extra_light, FontWeight.ExtraLight),
    Font(R.font.roboto_slab_thin, FontWeight.Thin),
)

// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = LlFontFamily,
        fontStyle = FontStyle.Italic,
        fontWeight = FontWeight.Normal,
    )
)