package com.example.w0276812.quizzer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * The primary activity for the app.
 * Displays a definition and radio buttons with possible answers.
 */
public class Quiz extends Activity {

    // attributes
    private String user;
    private String quizTitle;
    private HashMap<String, String> items;
    private QuizLogic ql;

    // widget attributes
    private TextView quizHeader;
    private TextView questionCountText;
    private TextView defnText;
    private RadioGroup radioGroup;
    private RadioButton[] answers;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        connectWidgets();

        // load in data from extras.
        Bundle extras = getIntent().getExtras();
        if (extras!=null)
        {
            user = extras.getString("Name");
            quizTitle = extras.getString("Quiz");
            quizHeader.setText("Topic: " + quizTitle);
        }

        loadItems();
        connectListeners();
        ql = new QuizLogic(items);

        resetDisplay();
    }

    // connect UI components to widgets
    private void connectWidgets() {
        quizHeader = (TextView)findViewById(R.id.quizHeader);
        questionCountText = (TextView)findViewById(R.id.questionCountText);
        defnText = (TextView)findViewById(R.id.defnText);
        radioGroup = (RadioGroup)findViewById(R.id.answerGroup);
        answers = new RadioButton[4];
        answers[0] = (RadioButton)findViewById(R.id.radio0);
        answers[1] = (RadioButton)findViewById(R.id.radio1);
        answers[2] = (RadioButton)findViewById(R.id.radio2);
        answers[3] = (RadioButton)findViewById(R.id.radio3);
        submitButton = (Button)findViewById(R.id.submitButton);
    }

    // attach listeners to widgets
    private void connectListeners() {
        // submit button listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedId = radioGroup.getCheckedRadioButtonId();
                if (checkedId == -1) {
                    // No answer is selected.
                    Toast.makeText(getBaseContext(), "Please Choose an Answer!", Toast.LENGTH_SHORT).show();
                } else {
                    RadioButton checkedRadio = (RadioButton) findViewById(checkedId);
                    final String term = checkedRadio.getText().toString();
                    // INNER CLASS create a new OnClickListener to use on closing of dialogs.
                    // used in two dialogs below.
                    OnClickListener alertClosed = new OnClickListener() {
                        public void onClick(DialogInterface di, int wut) {
                            ql.answer(term);
                            if (ql.isDone()) {
                                endQuiz();
                            } else {
                                resetDisplay();
                            }
                        }
                    };
                    if (ql.isCorrect(term)) {
                        // right answer.  Show the dialog at bottom.
                        AlertDialog dialog = new AlertDialog.Builder(Quiz.this).setIcon(R.mipmap.check_icon).setTitle("Correct!").setMessage("Click next to continue").setPositiveButton("Next", alertClosed).create();
                        dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
                        dialog.getWindow().getAttributes().alpha = 0.8f;
                        dialog.show();
                    } else {
                        //wrong answer.  Show the dialog at bottom.
                        String correction = "The correct answer is \"" + ql.getAnswer() + "\"";
                        AlertDialog dialog = new AlertDialog.Builder(Quiz.this).setIcon(R.mipmap.ex_icon).setTitle("Wrong!").setMessage(correction).setPositiveButton("Next", alertClosed).create();
                        dialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
                        dialog.getWindow().getAttributes().alpha = 0.8f;
                        dialog.show();
                    }
                }
            }
        });
    }

    // load the questions answers from text resource into HashMap
    private void loadItems() {
        items = new HashMap<String, String>();

        String currentQuiz = "Boardgames";
        int quizId = 0;
        switch (quizTitle) {
            case "Your Family":
                quizId = R.raw.famquiz;
                break;
            case "Project Management":
                quizId = R.raw.projman;
                break;
            default:
                quizId = R.raw.bgquiz;
        }

        InputStream inStream = this.getResources().openRawResource(quizId);
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
        String str = null;

        try {
            // discard topic line.
            str = br.readLine();
            while ((str = br.readLine()) != null) {
                String item[] = str.split(":");
                items.put(item[0], item[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("FILEIO", e.getMessage());
        }
    }

    // get and display current question and answers from QuizLogic object
    private void resetDisplay() {
        int cur = ql.getCurrentNum();
        int tot = ql.getNumQuestions();
        String progressString = "Question " + cur + " of " + tot;
        questionCountText.setText(progressString);

        defnText.setText(ql.getDefinition());
        String[] ansrs = ql.getOptions();
        for (int idx = 0; idx < answers.length; idx++) {
            answers[idx].setText(ansrs[idx]);
        }
        radioGroup.clearCheck();
    }

    // Method to gather data from quizlogic and move on to results.
    private void endQuiz() {
        String scoreString = "" + ql.getCorrect() + " / " + ql.getNumQuestions();

        Intent results = new Intent (Quiz.this, Results.class);
        Bundle extras = new Bundle();
        extras.putString("Name", user);
        extras.putString("Quiz", quizTitle);
        extras.putInt("Correct", ql.getCorrect());
        extras.putInt("Total", ql.getNumQuestions());
        results.putExtras(extras);
        startActivityForResult(results,1);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
