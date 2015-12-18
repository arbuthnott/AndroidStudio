package com.example.w0276812.quizzer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

/**
 * The final/result Activity.
 * Shows quiz results and provides options to start over, quit, etc.
 */
public class Results extends Activity {

    private String user;
    private String quizTitle;

    // widget properties
    private TextView resultTitle, correctLabel, incorrectLabel, resultMessage;
    private Button againButton, homeButton, closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        connectWidgets();
        user = getIntent().getExtras().getString("Name");
        quizTitle = getIntent().getExtras().getString("Quiz");

        connectListeners();
        displayResults();
    }

    // connect UI components to widgets
    private void connectWidgets() {
        resultTitle = (TextView)findViewById(R.id.resultsTitle);
        correctLabel = (TextView)findViewById(R.id.CorrectLabel);
        incorrectLabel = (TextView)findViewById(R.id.inCorrectLabel);
        resultMessage = (TextView)findViewById(R.id.resultMessage);
        againButton = (Button)findViewById(R.id.againButton);
        homeButton = (Button)findViewById(R.id.homeButton);
        closeButton = (Button)findViewById(R.id.closeButton);
    }

    // attach listeners to widgets
    private void connectListeners() {
        againButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // start a new quiz using same username
                Intent quiz = new Intent(Results.this, Quiz.class);
                Bundle extras = new Bundle();
                extras.putString("Name", user);
                extras.putString("Quiz", quizTitle);
                quiz.putExtras(extras);
                startActivityForResult(quiz,1);
                finish();
            }
        });

        homeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // back to default welcome screen
                Intent welcome = new Intent(Results.this, Welcome.class);
                startActivityForResult(welcome, 1);
                finish();
            }
        });

        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // close the app
                finish();
            }
        });
    }

    // display results of the quiz
    private void displayResults() {
        // get the passed in data
        Bundle extras = getIntent().getExtras();
        int correct = extras.getInt("Correct");
        int total = extras.getInt("Total");
        int incorrect = total - correct;

        String titleString = user + "'s Results:";
        if (user.endsWith("s")) {
            titleString = user + "' Results:";
        }
        resultTitle.setText(titleString);
        correctLabel.setText("" + correct + " questions correct");
        incorrectLabel.setText("" + incorrect + " questions incorrect");
        if (incorrect > 0) {
            incorrectLabel.setText("" + incorrect + " questions incorrect :(");
        }
        String message = getMessage(correct, total);
        resultMessage.setText(message);
    }

    // Get a message appropriate to the user's score.
    private String getMessage(int correct, int total) {
        String output = "You answered " + correct + "/" + total + " questions correctly. ";
        String comment = "";
        float ratio = correct / (total + 0f);
        if (ratio == 0f) {
            comment = "Just put 'Quiz Again!' and we'll pretend this never happened.";
        } else if (ratio < 0.5) {
            comment = "Don't be discouraged... why not try again?";
        } else if (ratio < 0.75) {
            comment = "Not bad - keep on studying!";
        } else if (ratio < 1){
            comment = "Great Score!";
        } else {
            comment = "One Hundred Percent!";
        }
        return output + comment;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);
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
