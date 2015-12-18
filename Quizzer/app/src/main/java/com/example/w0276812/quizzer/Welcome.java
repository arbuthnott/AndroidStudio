package com.example.w0276812.quizzer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * The welcome screen Activity
 * Displays the quiz topic, and provides an input for the user's name.
 */
public class Welcome extends Activity {

    private EditText nameText;
    private Button startButton;
    private Spinner quizSpinner;
    private String selectedQuiz = "Board Games";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        connectWidgets();
        connectListeners();
    }

    // connect UI components to widgets
    private void connectWidgets() {
        nameText = (EditText)findViewById(R.id.nameText);
        startButton = (Button)findViewById(R.id.startButton);
        quizSpinner = (Spinner)findViewById(R.id.quizSpinner);
    }

    // attach listeners to widgets
    private void connectListeners() {
        // focus listener for nameText
        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus && nameText.getText().toString().equals("Einstein")) {
                    nameText.setText("");
                }
            }
        });

        // click listener for startButton
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // click behaviour here.
                Intent quiz = new Intent (Welcome.this, Quiz.class);
                Bundle extras = new Bundle();
                extras.putString("Name", nameText.getText().toString());
                extras.putString("Quiz", selectedQuiz);
                quiz.putExtras(extras);
                startActivityForResult(quiz,1);
                finish();
            }
        });

        // setup the quiz selection spinner
        final String quizNames[] = getResources().getStringArray(R.array.quiz_names);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, quizNames);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quizSpinner.setAdapter(myAdapter);
        quizSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuiz = quizNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
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
