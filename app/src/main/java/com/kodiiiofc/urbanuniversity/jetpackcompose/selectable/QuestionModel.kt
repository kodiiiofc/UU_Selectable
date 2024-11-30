package com.kodiiiofc.urbanuniversity.jetpackcompose.selectable

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class QuestionModel<T>(
    val text: String,
    val answers: AnswerModel<T>,
    private val onAnswerClickListener: OnAnswerClickListener,
) {

    var isCorrect = false
    private val hasSeveralAnswers = answers.correctIndexes.size != 1

    @Composable
    fun Question() {

        val checkArray = Array(answers.list.size) { false }

        val checkedAnswers = remember {
            mutableStateListOf(*checkArray)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = text,
                    modifier = Modifier.padding(10.dp)
                )
            }

            if (answers.list[0] is Painter) {
                ImageAnswers(
                    answersList = answers.list as List<Painter>,
                    answersDescriptionList = answers.answersDescriptionList,
                    checkedAnswers = checkedAnswers
                )
            } else {
                TextAnswers(answers.list, checkedAnswers)
            }

            Button(
                modifier = Modifier.padding(10.dp),
                onClick = {

                    val mutableResultList = mutableListOf<Int>()

                    checkedAnswers.forEachIndexed { index, value ->
                        if (value) {
                            mutableResultList.add(index)
                        }
                    }

                    Log.d("TAG", "Question: $checkedAnswers")

                    isCorrect = mutableResultList.toList() == answers.correctIndexes

                    Log.d("TAG", "Question: ${mutableResultList.toList()}")
                    Log.d("TAG", "Question: ${answers.correctIndexes}")

                    Log.d("TAG", "Question: $isCorrect")

                    onAnswerClicked(onAnswerClickListener)

                    checkedAnswers.replaceAll { false }

                }
            ) {
                Text("Ответить")
            }

        }
    }

    @Composable
    fun TextAnswers(answersList: List<T>, checkedAnswers: SnapshotStateList<Boolean>) {
        answersList.forEachIndexed { index, value ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = checkedAnswers[index],
                        onValueChange = {
                            if (!hasSeveralAnswers) checkedAnswers.replaceAll { false }
                            checkedAnswers[index] = it
                        }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checkedAnswers[index],
                    onCheckedChange = {
                        checkedAnswers[index] = it
                    }
                )

                Text(
                    text = value.toString()
                )
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun ImageAnswers(
        answersList: List<Painter>,
        answersDescriptionList: List<String>,
        checkedAnswers: SnapshotStateList<Boolean>
    ) {

        FlowRow(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            answersList.forEachIndexed() { index, painter ->

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(150.dp, 200.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .border(
                            width = 4.dp,
                            color = if (checkedAnswers[index]) Color.Blue else Color.Transparent,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .selectable(
                            selected = checkedAnswers[index],
                            enabled = true,
                            onClick = {
                                val temp = !checkedAnswers[index]
                                if (!hasSeveralAnswers) checkedAnswers.replaceAll { false }
                                checkedAnswers[index] = temp
                            }
                        )
                ) {

                    Image(
                        painter = painter,
                        contentDescription = answersDescriptionList[index],
                        contentScale = ContentScale.Crop
                    )
                }

            }
        }

    }

    private fun onAnswerClicked(onAnswerClickListener: OnAnswerClickListener) {
        onAnswerClickListener.onClick(isCorrect)
    }

    fun interface OnAnswerClickListener {
        fun onClick(isCorrect: Boolean)
    }

}

@Preview(showBackground = true)
@Composable
fun TextQuestionPreview() {
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
        onAnswerClickListener = {},

        ).Question()
}


@Preview(showBackground = true)
@Composable
fun ImageQuestionPreview() {
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
        onAnswerClickListener = {},
    ).Question()
}


