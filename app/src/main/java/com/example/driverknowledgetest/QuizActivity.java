package com.example.driverknowledgetest;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.ColorStateList;
import android.graphics.Color;


import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;



public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "extraScore";
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private TextView textViewCorrectAnswer;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button buttonConfirmNext;
    private long backPressedTime;

    private ColorStateList textColorDefaultRb;
    private Drawable textColorDefaultButton;
    private Drawable textColorDefaultAnswer;

    private List<Question> questionList;
    private int questionCounter;
    private int questionCountTotal;
    private Question currentQuestion;

    private int score;
    private boolean answered;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewCorrectAnswer = findViewById(R.id.text_view_correctAnswer);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultButton = rb1.getBackground();


        QuizDbHelper dbHelper = new QuizDbHelper(this);
        questionList = dbHelper.getAllQuestions();
        questionCountTotal = 20;
        Collections.shuffle(questionList);

        showNextQuestion();

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showNextQuestion();
                    textViewCorrectAnswer.setText("");
                    textViewCorrectAnswer.setBackground(textColorDefaultAnswer);
                }
            }
        });
    }


    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);


        rb1.setBackground(textColorDefaultButton);
        rb2.setBackground(textColorDefaultButton);
        rb3.setBackground(textColorDefaultButton);
        rb4.setBackground(textColorDefaultButton);


        rbGroup.clearCheck();

        if (questionCounter < questionCountTotal) {
            currentQuestion = questionList.get(questionCounter);

            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            buttonConfirmNext.setText("Confirm");
        } else {
            finishQuiz();
        }
    }

    private void checkAnswer() {
        answered = true;

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId());
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) {
            score++;
            textViewScore.setText("Score: " + score);
            textViewCorrectAnswer.setText("Your Answer is Correct");
            textViewCorrectAnswer.setTextColor(GREEN);

        }
        else {
            textViewCorrectAnswer.setText("Your Answer is Incorrect");
            textViewCorrectAnswer.setTextColor(RED);
        }
        showSolution();
    }

    private void showSolution() {

        rb1.setBackgroundColor(Color.RED);
        rb2.setBackgroundColor(Color.RED);
        rb3.setBackgroundColor(Color.RED);
        rb4.setBackgroundColor(Color.RED);


        switch (currentQuestion.getAnswerNr()) {
            case 1:
                rb1.setBackgroundColor(GREEN);

                break;
            case 2:
                rb2.setBackgroundColor(GREEN);


                break;
            case 3:
                rb3.setBackgroundColor(GREEN);


                break;
            case 4:

                rb4.setBackgroundColor(GREEN);


        }

        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next");

        } else {
            buttonConfirmNext.setText("Finish");

        }

    }

    private void finishQuiz() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish", Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }
}