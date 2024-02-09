package ru.aidar.lingolearn.translator.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.ui.theme.LlFontFamily
import ru.aidar.lingolearn.ui.theme.LocalLLColors
import ru.aidar.lingolearn.utils.Constants.AVAILABLE_TRANSLATOR_LANGUAGES
import ru.aidar.lingolearn.utils.Constants.LL_TRANSPARENT_TEXT_FIELD_COLOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel
) {
    val context = LocalContext.current
    val llColors = LocalLLColors.current

    val clipboardManager = LocalClipboardManager.current
    val llUiState by viewModel.translatorState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope { Dispatchers.Main }

    var showSourceBottomSheet by remember { mutableStateOf(false) }
    var showTargetBottomSheet by remember { mutableStateOf(false) }

    fun dismiss() {
        showSourceBottomSheet = false
        showTargetBottomSheet = false
    }

    fun setText(text: String) {
        clipboardManager.setText(AnnotatedString(text = text))
    }


    // TODO вынести в отдельную функцию
    if(showSourceBottomSheet || showTargetBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { dismiss() },
            containerColor = llColors.background,
            //modifier = Modifier.background(onBackground),
        ) {
            LazyColumn {
                items(AVAILABLE_TRANSLATOR_LANGUAGES.size) {
                    val tempLang = AVAILABLE_TRANSLATOR_LANGUAGES[it]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .background(
                                if(tempLang.language == llUiState.sourceLanguage.language) llColors.onBackground else llColors.background
                            )
                            .padding(start = 20.dp)
                            .clickable {
                                scope.launch {
                                    if(showSourceBottomSheet)
                                        viewModel.peekNewSourceLanguage(tempLang)
                                    else
                                        viewModel.peekNewTargetLanguage(tempLang)
                                    dismiss()
                                }
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = tempLang.language,
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                color = llColors.primary,
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(llColors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(llColors.inverseColor)
                .border(1.dp, color = Color.Black),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TranslationLanguage(
                showNecessaryBottomSheet = {
                    scope.launch {
                        showTargetBottomSheet = false
                        showSourceBottomSheet = true
                    }
                },
                color = llColors.primary,
                text = llUiState.sourceLanguage.language,
            )
            IconButton(onClick = viewModel::swapSourceAndTargetLanguages) {
                Icon(
                    painter = painterResource(id = R.drawable.sync_alt),
                    contentDescription = null,
                    tint = llColors.primary,
                    modifier = Modifier.weight(0.1f),
                )
            }
            TranslationLanguage(
                showNecessaryBottomSheet = {
                    scope.launch {
                        showSourceBottomSheet = false
                        showTargetBottomSheet = true
                    }
                },
                color = llColors.primary,
                text = llUiState.targetLanguage.language,
            )
        }
        /**
        текстовое поле ввода
         */
        Row(
            modifier = Modifier
                .height(150.dp)
                .background(llColors.onBackground)
        ) {
            TextField(
                value = llUiState.sourceText, onValueChange = { viewModel.changeSourceText(it) },
                modifier = Modifier
                    .weight(0.93f)
                    .height(150.dp),
                maxLines = 5,
                textStyle = TextStyle(
                    color = llColors.primary,
                    fontFamily = LlFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                colors = LL_TRANSPARENT_TEXT_FIELD_COLOR,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.enter_text),
                        style = TextStyle(
                            color = llColors.primary,
                            fontFamily = LlFontFamily,
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        ),
                    )
                }
            )
            IconButton(
                onClick = { viewModel.changeSourceText("") },
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = llColors.primary
                )
            }
        }
        Row(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .background(llColors.onBackground),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
        ) {
            IconButton(onClick = { viewModel.download(llUiState.sourceLanguage) }) {
                Icon(
                    painterResource(id = if(llUiState.isSourceModelDownloaded) R.drawable.download_done else R.drawable.need_download),
                    contentDescription = null,
                    tint = llColors.primary
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painterResource(id = R.drawable.bookmark),
                    contentDescription = null,
                    tint = llColors.primary
                )
            }
            IconButton(onClick = { setText(text = llUiState.sourceText) }) {
                Icon(
                    painterResource(id = R.drawable.content_copy),
                    contentDescription = null,
                    tint = llColors.primary
                )
            }
            IconButton(onClick = { viewModel.textToSpeech() }) {
                Icon(
                    painterResource(id = R.drawable.volume),
                    contentDescription = null,
                    tint = llColors.primary
                )
            }
        }
    }
}

@Composable
private fun RowScope.TranslationLanguage(
    showNecessaryBottomSheet: () -> Unit,
    color: Color,
    text: String
) {
    Text(
        modifier = Modifier
            .weight(0.45f)
            .clickable {
                showNecessaryBottomSheet()
            },
        text = text,
        textAlign = TextAlign.Center,
        style = TextStyle(
            color = color,
            fontFamily = LlFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    )
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
