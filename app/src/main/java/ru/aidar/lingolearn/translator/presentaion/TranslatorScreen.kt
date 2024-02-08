package ru.aidar.lingolearn.translator.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.ui.theme.LlFontFamily
import ru.aidar.lingolearn.ui.theme.LocalLLColors
import ru.aidar.lingolearn.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel
) {
    val context = LocalContext.current
    val primary = LocalLLColors.current.primary
    val background = LocalLLColors.current.background
    val onBackground = LocalLLColors.current.onBackground
    val additional = LocalLLColors.current.inverseColor

    val llUiState by viewModel.translatorState.collectAsStateWithLifecycle()

    /**
     * перенести во ViewModel*/
    var pickedSource = remember { mutableIntStateOf(3) }
    var pickedTarget = remember { mutableIntStateOf(3) }
    var pickedLanguage = remember {
        mutableStateOf("RUSSIAN")
    }
    var sourceLanguage = remember {
        mutableStateOf("ENGLISH")
    }
    val scope = rememberCoroutineScope()

    var showSourceBottomSheet by remember { mutableStateOf(false) }
    var showTargetBottomSheet by remember { mutableStateOf(false) }


    // TODO вынести в отдельную функцию
    if(showSourceBottomSheet || showTargetBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSourceBottomSheet = false }) {
            LazyColumn {
                items(Constants.AVAILABLE_TRANSLATOR_LANGUAGES.size) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                background.copy(
                                    alpha = if(it == pickedSource.intValue) 0.5f else 1f
                                )
                            )
                            .padding(start = 20.dp)
                            .clickable {
                                scope.launch {
                                    pickedSource.intValue = it
                                    pickedLanguage.value =
                                        Constants.AVAILABLE_TRANSLATOR_LANGUAGES[it].language
                                    showSourceBottomSheet = false
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = Constants.AVAILABLE_TRANSLATOR_LANGUAGES[it].language,
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                color = primary,
                                fontFamily = LlFontFamily,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }

    /**
     * BottomSheetScaffold(
    scaffoldState = sheetState,
    sheetPeekHeight = 0.dp,
    sheetContainerColor = background,
    modifier = Modifier.border(1.dp, Color.Yellow),
    sheetContent = {
    LazyColumn {
    items(Constants.AVAILABLE_TRANSLATOR_LANGUAGES.size) {
    Row(
    modifier = Modifier
    .fillMaxWidth()
    .height(40.dp)
    .background(
    background.copy(
    alpha = if(it == pickedSource.intValue) {
    Toast
    .makeText(context, "ASDASD", Toast.LENGTH_SHORT)
    .show()
    0.5f
    } else {
    1f
    }
    )
    )
    .padding(start = 20.dp)
    .clickable {
    scope.launch {
    pickedSource.intValue = it
    pickedLanguage.value =
    Constants.AVAILABLE_TRANSLATOR_LANGUAGES[it].language
    sheetState.bottomSheetState.hide()
    }
    }
    .border(1.dp, additional),
    verticalAlignment = Alignment.CenterVertically,
    ) {
    Text(
    text = Constants.AVAILABLE_TRANSLATOR_LANGUAGES[it].language,
    textAlign = TextAlign.Start,
    style = TextStyle(
    color = onBackground,
    fontFamily = LingoLearnFontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 16.sp
    )
    )
    }
    }
    }
    },
    ) {*/

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(additional)
                .border(1.dp, color = Color.Black),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                modifier = Modifier
                    .weight(0.45f)
                    .clickable {
                        scope.launch {
                            showTargetBottomSheet = false
                            showSourceBottomSheet = true
                        }
                    },
                text = pickedLanguage.value,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = primary,
                    fontFamily = LlFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                )
            )
            IconButton(onClick = {}) {
                Icon(
                    painter = painterResource(id = R.drawable.sync_alt),
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.weight(0.1f),
                )
            }
            Text(
                modifier = Modifier
                    .weight(0.45f)
                    .clickable {
                        scope.launch {
                            showSourceBottomSheet = false
                            showTargetBottomSheet = true
                        }
                        //sheetState.bottomSheetState.expand()
                    },
                text = sourceLanguage.value,
                textAlign = TextAlign.Center,
                style = TextStyle(
                    color = primary,
                    fontFamily = LlFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )
            )
        }
        Row(
            modifier = Modifier
                .height(150.dp)
                .background(onBackground)
        ) {
            TextField(
                value = llUiState.sourceText, onValueChange = { llUiState.sourceText = it },
                modifier = Modifier
                    .weight(0.93f)
                    .height(150.dp),
                maxLines = 5,
                textStyle = TextStyle(
                    color = primary,
                    fontFamily = LlFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.padding(top = 10.dp)) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = primary
                )
            }
        }
        Row(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .background(onBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painterResource(id = R.drawable.content_copy),
                    contentDescription = null,
                    tint = primary
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painterResource(id = R.drawable.volume),
                    contentDescription = null,
                    tint = primary
                )
            }
        }
    }
}

/*
@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
fun TranslatorScreenViewModel() {
    //val viewModel = hiltViewModel<TranslatorViewModel>()
    //val viewodel = TranslatorViewModel(LingoLearnTranslator())
    TranslatorScreen()
}*/
