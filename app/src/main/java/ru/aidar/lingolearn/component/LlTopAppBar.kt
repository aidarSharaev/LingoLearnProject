package ru.aidar.lingolearn.component

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.ui.theme.LlFontFamily
import ru.aidar.lingolearn.ui.theme.LocalLLColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LlTopAppBar(
    isNavIconVisible: Boolean = true,
    onNavIconClick: () -> Unit = {},
    actionIcon: ImageVector? = null,
    onActionIconClick: () -> Unit = {},
) {
    val primary = LocalLLColors.current.primary
    val background = LocalLLColors.current.background
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_name),
                color = primary,
                modifier = Modifier.padding(top = 5.dp),
                style = TextStyle(
                    fontFamily = LlFontFamily,
                    fontWeight = FontWeight.Light
                ),
                fontSize = 24.sp
            )
        },
        navigationIcon = {
            if(isNavIconVisible) {
                IconButton(onClick = onNavIconClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = primary
                    )
                }
            }
        },
        actions = {
            if(actionIcon != null) {
                IconButton(onClick = onActionIconClick) {
                    Icon(
                        imageVector = actionIcon,
                        contentDescription = null,
                        tint = primary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = background
        )
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun LlTopAppBarPreview() {
    LlTopAppBar()
}