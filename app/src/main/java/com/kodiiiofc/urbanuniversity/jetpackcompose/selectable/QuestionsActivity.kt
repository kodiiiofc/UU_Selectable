package com.kodiiiofc.urbanuniversity.jetpackcompose.selectable

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import com.kodiiiofc.urbanuniversity.jetpackcompose.selectable.ui.theme.SelectableTheme

class QuestionsActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SelectableTheme {


                val currentQuestionIndex = rememberSaveable {
                    mutableIntStateOf(0)
                }

                val countCorrectAnswers = rememberSaveable {
                    mutableIntStateOf(0)
                }

                val questionList = listOf(
                    QuestionModel(
                        text = "Какие из перечисленных стилей относятся к классическим архитектурным стилям Европы? " +
                                "Выберите все подходящие варианты.",
                        answers =
                        AnswerModel(
                            listOf(
                                "Готика",
                                "Барокко",
                                "Романский стиль",
                                "Модерн",
                                "Ренессанс"
                            ),
                            listOf(0, 1, 2, 4)
                        ),
                        onAnswerClickListener = {
                            if (it) countCorrectAnswers.intValue++
                            currentQuestionIndex.intValue++
                        }
                    ),

                    QuestionModel(
                        text = "Посмотрите на следующие изображения и выберите то, которое соответствует стилю модерн",
                        answers =
                        AnswerModel(
                            listOf(
                                painterResource(R.drawable.casa_batllo),
                                painterResource(R.drawable.notre_dame_de_paris),
                                painterResource(R.drawable.saint_basils_cathedral),
                            ),
                            listOf(0),
                            answersDescriptionList = listOf(
                                "Каса-Батльо",
                                "Нотр-Дам-де-Пари",
                                "Храм Василия Блаженного"
                            )
                        ),
                        onAnswerClickListener = {
                            if (it) countCorrectAnswers.intValue++
                            currentQuestionIndex.intValue++
                        }
                    ),

                    QuestionModel(
                        text = "В каком городе находится знаменитый собор Святого Петра?",
                        answers =
                        AnswerModel(
                            listOf(
                                "Париж",
                                "Рим",
                                "Лондон",
                                "Москва"
                            ),
                            listOf(1)
                        ),
                        onAnswerClickListener = {
                            if (it) countCorrectAnswers.intValue++

                            val intent = Intent(this@QuestionsActivity, FinishActivity::class.java)
                            intent.putExtra("points", countCorrectAnswers.intValue)
                            startActivity(intent)
                            finish()
                        }
                    )
                )

                questionList[currentQuestionIndex.intValue].Question()

            }
        }
    }
}