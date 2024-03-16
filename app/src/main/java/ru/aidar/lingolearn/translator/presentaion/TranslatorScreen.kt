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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.aidar.lingolearn.R
import ru.aidar.lingolearn.component.LlLoading
import ru.aidar.lingolearn.translator.LlTranslatorState
import ru.aidar.lingolearn.ui.theme.LlColorDarkError
import ru.aidar.lingolearn.ui.theme.LlColorLightError
import ru.aidar.lingolearn.ui.theme.LlColors
import ru.aidar.lingolearn.ui.theme.LlFontFamily
import ru.aidar.lingolearn.ui.theme.LocalLLColors
import ru.aidar.lingolearn.utils.AvailableLanguage
import ru.aidar.lingolearn.utils.Constants
import ru.aidar.lingolearn.utils.Constants.AVAILABLE_TRANSLATOR_LANGUAGES
import ru.aidar.lingolearn.utils.Constants.LL_TRANSPARENT_TEXT_FIELD_COLOR

@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel,
) {
    val llUiState by viewModel.translatorState.collectAsStateWithLifecycle()
    val llColors = LocalLLColors.current
    val scope = rememberCoroutineScope { Dispatchers.Main }

    val clipboardManager = LocalClipboardManager.current

    var showSourceBottomSheet by remember { mutableStateOf(false) }
    var showTargetBottomSheet by remember { mutableStateOf(false) }

    fun dismiss() {
        showSourceBottomSheet = false
        showTargetBottomSheet = false
    }

    fun setText(text: String) {
        clipboardManager.setText(AnnotatedString(text = text))
    }

    if(llUiState.isModelsAreLoaded != Constants.LanguagesDownloadState.Creating) {

        if(showSourceBottomSheet || showTargetBottomSheet) {
            LlTranslatorBottomSheet(
                llColors = llColors,
                dismiss = ::dismiss,
                scope = scope,
                peekSource = viewModel::peekNewSourceLanguage,
                peekTarget = viewModel::peekNewTargetLanguage,
                sourceLang = llUiState.sourceLanguage,
                targetLang = llUiState.targetLanguage,
                showSourceBottomSheet = showSourceBottomSheet,
                showTargetBottomSheet = showTargetBottomSheet
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(llColors.background)
        ) {
            LlLanguageSelectionWidget(
                llUiState = llUiState,
                llColors = llColors,
                scope = scope,
                swapLanguages = viewModel::swapLanguages,
                showTarget = {
                    showSourceBottomSheet = false
                    showTargetBottomSheet = true
                },
                showSource = {
                    showSourceBottomSheet = true
                    showTargetBottomSheet = false
                },
            )
            /*Row(
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
                IconButton(onClick = viewModel::swapLanguages) {
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
            }*/
            /**
             * текстовые поля
             * один виден всегда
             * */
            LlTranslatorTextFields(
                llColors = llColors,
                llUiState = llUiState,
                changeSourceText = viewModel::changeSourceText,
                downloadLanguage = viewModel::download,
                deleteLanguage = viewModel::delete,
                textToSpeech = viewModel::textToSpeech,
                setText = ::setText,
            )
            /*Row(
                modifier = Modifier
                    .height(150.dp)
                    .background(llColors.onBackground)
            ) {
                TextField(
                    value = llUiState.sourceText,
                    onValueChange = { viewModel.changeSourceText(it) },
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
                IconButton(onClick = { }) {
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
                IconButton(onClick = { viewModel.textToSpeech(thatsIsSourceText = true) }) {
                    Icon(
                        painterResource(id = R.drawable.volume),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
            }*/
            /*if(llUiState.targetText.isNotBlank() && llUiState.targetText.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .height(150.dp)
                        .background(llColors.onBackground)
                ) {
                    TextField(
                        readOnly = true,
                        value = llUiState.targetText, onValueChange = { },
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
                    )
                }
                */
            /*
            Row(
                modifier = Modifier
                    .height(45.dp)
                    .fillMaxWidth()
                    .background(llColors.onBackground),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
            ) {
                IconButton(onClick = { viewModel.download(llUiState.targetLanguage) }) {
                    Icon(
                        painterResource(id = if(llUiState.isTargetModelDownloaded) R.drawable.download_done else R.drawable.need_download),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
                IconButton(onClick = {  })
                {
                    Icon(
                        painterResource(id = R.drawable.bookmark),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
                IconButton(onClick = { setText(text = llUiState.targetText) }) {
                    Icon(
                        painterResource(id = R.drawable.content_copy),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
                IconButton(onClick = { viewModel.textToSpeech(thatsIsSourceText = false) }) {
                    Icon(
                        painterResource(id = R.drawable.volume),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
            }
        }*/
        }
    } else {
        LlLoading(llColors = llColors)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LlTranslatorBottomSheet(
    llColors: LlColors,
    dismiss: () -> Unit,
    scope: CoroutineScope,
    peekSource: (AvailableLanguage) -> Unit,
    peekTarget: (AvailableLanguage) -> Unit,
    showSourceBottomSheet: Boolean,
    showTargetBottomSheet: Boolean,
    sourceLang: AvailableLanguage,
    targetLang: AvailableLanguage
) {
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
                            if(
                                (showSourceBottomSheet && (tempLang.language == sourceLang.language))
                                || (showTargetBottomSheet && (tempLang.language == targetLang.language))
                            )
                                llColors.onBackground
                            else
                                llColors.background

                        )
                        .padding(start = 20.dp)
                        .clickable {
                            scope.launch {
                                if(showSourceBottomSheet)
                                    peekSource(tempLang)
                                else
                                    peekTarget(tempLang)
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

@Composable
fun LlTranslatorTextFields(
    llColors: LlColors,
    llUiState: LlTranslatorState,
    changeSourceText: (String) -> Unit,
    downloadLanguage: (AvailableLanguage, Boolean) -> Unit,
    deleteLanguage: (AvailableLanguage, Boolean) -> Unit,
    textToSpeech: (Boolean) -> Unit,
    setText: (String) -> Unit,
) {

    val readOnly = listOf(false, true)

    // 0 - is Source text, clear button
    // 1 - is Target text, read only,
    repeat(2) { current ->
        //if((current == 0) || (current == 1 && llUiState.targetText.isNotBlank() && llUiState.targetText.isNotEmpty())) {
        Row(
            modifier = Modifier
                .height(150.dp)
                .background(llColors.onBackground.copy(alpha = if(current == 0) 0.7f else 1f))
        ) {
            TextField(
                readOnly = readOnly[current],
                value = if(current == 0) llUiState.sourceText else llUiState.targetText,
                onValueChange = { if(current == 0) changeSourceText(it) },
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
                    if(current == 0) {
                        Text(
                            text = stringResource(id = R.string.enter_text),
                            style = TextStyle(
                                color = llColors.primary.copy(alpha = 0.5f),
                                fontFamily = LlFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            ),
                        )
                    }
                }
            )
            if(current == 0) {
                IconButton(
                    onClick = { changeSourceText("") },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .height(45.dp)
                .fillMaxWidth()
                .background(llColors.onBackground.copy(alpha = if(current == 0) 0.7f else 1f)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painterResource(id = R.drawable.error),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 15.dp)
                        .size(17.dp),
                    tint = when(current) {
                        0 -> if(llUiState.isSourceModelDownloaded) Color.Transparent else Color(
                            0xFFec7c26
                        )

                        else -> if(llUiState.isTargetModelDownloaded) Color.Transparent else Color.Yellow
                    }
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = stringResource(id = R.string.model_is_not_downloaded),
                    style = TextStyle(
                        color = when(current) {
                            0 -> if(llUiState.isSourceModelDownloaded) Color.Transparent else LlColorDarkError
                            else -> if(llUiState.isTargetModelDownloaded) Color.Transparent else LlColorLightError
                        },
                        fontFamily = LlFontFamily,
                        fontWeight = FontWeight.Light,
                        fontSize = 15.sp
                    )
                )
            }
            Row {
                IconButton(onClick = {
                    when {
                        (current == 0) -> if(llUiState.isSourceModelDownloaded) deleteLanguage(
                            llUiState.sourceLanguage,
                            false
                        ) else downloadLanguage(llUiState.sourceLanguage, false)

                        else -> if(llUiState.isTargetModelDownloaded) deleteLanguage(
                            llUiState.targetLanguage,
                            true
                        ) else downloadLanguage(llUiState.targetLanguage, true)
                    }
                }) {
                    Icon(
                        painterResource(
                            id = when {
                                (current == 0) -> if(llUiState.isSourceModelDownloaded) R.drawable.delete else R.drawable.need_download
                                else -> {
                                    if(llUiState.isTargetModelDownloaded) R.drawable.delete else R.drawable.need_download
                                }
                            },
                        ),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
                if(current == 0) {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            painterResource(id = R.drawable.bookmark),
                            contentDescription = null,
                            tint = llColors.primary
                        )
                    }
                }
                IconButton(onClick = { setText(if(current == 0) llUiState.sourceText else llUiState.targetText) }) {
                    Icon(
                        painterResource(id = R.drawable.content_copy),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
                IconButton(onClick = { textToSpeech(current == 0) }) {
                    Icon(
                        painterResource(id = R.drawable.volume),
                        contentDescription = null,
                        tint = llColors.primary
                    )
                }
            }
        }
    }
}
//}


@Composable
fun LlLanguageSelectionWidget(
    llUiState: LlTranslatorState,
    llColors: LlColors,
    scope: CoroutineScope,
    swapLanguages: () -> Unit,
    showTarget: () -> Unit,
    showSource: () -> Unit,
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
                scope.launch { showSource() }
            },
            color = llColors.primary,
            text = llUiState.sourceLanguage.language,
        )
        IconButton(onClick = swapLanguages) {
            Icon(
                painter = painterResource(id = R.drawable.sync_alt),
                contentDescription = null,
                tint = llColors.primary,
                modifier = Modifier.weight(0.1f),
            )
        }
        TranslationLanguage(
            showNecessaryBottomSheet = {
                scope.launch { showTarget() }
            },
            color = llColors.primary,
            text = llUiState.targetLanguage.language,
        )
    }
}
