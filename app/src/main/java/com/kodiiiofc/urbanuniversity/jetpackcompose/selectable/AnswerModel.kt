package com.kodiiiofc.urbanuniversity.jetpackcompose.selectable

data class AnswerModel<T>(val list: List<T>,
                          val correctIndexes: List<Int>,
                          val answersDescriptionList : List<String> = listOf())