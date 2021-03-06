package com.example.studentass.models

class TestQuestion (
    val id: Long,
    val question: String,
    val questionType: String,
    val complexity: Long,
    val fileIds: List<Long>,
    val answers: List<TestAnswer>
)