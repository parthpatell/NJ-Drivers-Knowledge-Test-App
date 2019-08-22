package com.example.driverknowledgetest;


import android.provider.BaseColumns;


final class QuizContract {

    private QuizContract() {
    }

        static class QuestionsTable implements BaseColumns {
        static final String TABLE_NAME = "quiz_questions";
        static final String COLUMN_QUESTION = "question";
        static final String COLUMN_OPTION1 = "option1";
        static final String COLUMN_OPTION2 = "option2";
        static final String COLUMN_OPTION3 = "option3";
        static final String COLUMN_OPTION4 = "option4";
        static final String COLUMN_ANSWER_NR = "answer_nr";


    }
}