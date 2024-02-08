package ru.aidar.lingolearn.lingolearnapp.presentation

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.navigation.MainMenuRoute
import ru.aidar.lingolearn.ui.theme.LocalLLColors
import ru.aidar.lingolearn.utils.ConstantModifier.columnModifier
import ru.aidar.lingolearn.utils.ConstantModifier.iconModifier
import ru.aidar.lingolearn.utils.ConstantModifier.textModifier
import ru.aidar.lingolearn.utils.Constants

@Composable
fun LingoLearnMainScreen(
    navigateTo: (String) -> Unit,
) {

    val context = LocalContext.current
    val background = LocalLLColors.current.background
    val primary = LocalLLColors.current.primary
    val inverseColor = LocalLLColors.current.inverseColor

    SideEffect {
        Toast.makeText(context, "LingoLearnMainScreen", Toast.LENGTH_SHORT).show()
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        this.MainScreenNavigation(
            modifier = Modifier
                .fillMaxWidth()
                .weight(Constants.ROW_WEIGHT)
                //todo
                .border(width = 1.dp, color = inverseColor)
                .background(background),
            tint = primary,
            navigateTo = navigateTo
        )
    }
}

@Composable
fun ColumnScope.MainScreenNavigation(
    modifier: Modifier,
    tint: Color,
    navigateTo: (String) -> Unit
) {
    Row(
        modifier = modifier
            .clickable { navigateTo(MainMenuRoute.NESTED_LEARN_ROUTE.route) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InformationInRow(
            drawableResource = R.drawable.nav_learn,
            stringResource = R.string.study,
            tint = tint
        )
    }
    Row(
        modifier = modifier
            .clickable { navigateTo(MainMenuRoute.NESTED_TEST_ROUTE.route) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InformationInRow(
            drawableResource = R.drawable.nav_test,
            stringResource = R.string.test_of_the_day,
            tint = tint
        )
    }
    Row(
        modifier = modifier
            .clickable { navigateTo(MainMenuRoute.NESTED_DICTIONARY_ROUTE.route) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InformationInRow(
            drawableResource = R.drawable.nav_dictionary,
            stringResource = R.string.dictionary,
            tint = tint
        )
    }
    Row(
        modifier = modifier
            .clickable { navigateTo(MainMenuRoute.NESTED_TRANSLATION_ROUTE.route) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InformationInRow(
            drawableResource = R.drawable.translate_nav,
            stringResource = R.string.translator,
            tint = tint
        )
    }
    Row(
        modifier = modifier
            .clickable { navigateTo(MainMenuRoute.NESTED_PROFILE_ROUTE.route) },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        InformationInRow(
            drawableResource = R.drawable.profile_nav,
            stringResource = R.string.profile,
            tint = tint
        )
    }
}

@Composable
private fun RowScope.mainMenuNavigation(
    @DrawableRes drawableResource: Int,
    @StringRes stringResource: Int,
    tint: Color,
) {
    Column(
        modifier = columnModifier
    ) {
        Icon(
            painterResource(id = drawableResource),
            contentDescription = null,
            tint = tint,
            modifier = iconModifier,
        )
        Text(
            text = stringResource(id = stringResource),
            modifier = textModifier,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = tint,
        )
    }
}

@Composable
private fun ArrowBackIcon(tint: Color) {
    Icon(
        imageVector = Icons.Default.KeyboardArrowRight,
        contentDescription = null,
        tint = tint,
        modifier = Modifier
            .padding(end = 25.dp)
            .size(45.dp)
    )
}

@Composable
fun RowScope.InformationInRow(
    @DrawableRes drawableResource: Int,
    @StringRes stringResource: Int,
    tint: Color,
) {
    mainMenuNavigation(
        drawableResource = drawableResource,
        stringResource = stringResource,
        tint = tint
    )
    ArrowBackIcon(tint = tint)
}


@Preview(showBackground = true)
@Composable
fun LingoLearnMainScreenPreview() {
    LingoLearnMainScreen {}
}
