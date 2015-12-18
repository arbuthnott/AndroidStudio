package com.nscc.gameoflife.jareds_machine;

import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Widgets / Objects
    private Board board;
    private Button runPauseButton;
    private Button clearButton;
    private Spinner patternSpinner;
    private EditText generationInput;
    private TextView generationReport;

    private int colors[] = {Color.RED};

    // State data:
    private boolean isRunning;
    private int generations, maxGenerations;

    // for thread
    private Handler h;
    private final int FRAME_RATE = 100;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        h = new Handler();


        board = (Board) findViewById(R.id.signature_canvas);

        generations = 0;
        maxGenerations = 10; // or whatever
        isRunning = false;
        connectWidgets();
        connectListeners();

        newSim();
        generations = 0;
        generationInput.setText("100");
        updateReport();
        //newSim();
        clearCanvas();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private Runnable r = new Runnable() {
        @Override
        public void run() {
            // run generation logic
            for (int row = 0; row < board.cells.length; row++) {
                for (int col = 0; col < board.cells.length; col++) {
                    board.cells[row][col].lifeCheck(board.cells, row, col);
                }
            }

            for (int row = 0; row < board.cells.length; row++) {
                for (int col = 0; col < board.cells.length; col++) {
                    board.cells[row][col].updateAlive();
                }
            }

            // draw cells
            board.invalidate();

            generations++;
            updateReport();

            if (generations >= maxGenerations)
                stopSim();
            else
                h.postDelayed(r, FRAME_RATE);
        }
    };


    private void updateReport() {
        generationReport.setText("Generations: " + generations);
    }

    private void stopSim() {

        // TODO: code to stop the simulation
        h.removeCallbacks(r);

        // update the ui behavour and local properties.
        runPauseButton.setText("Run");
        generationInput.setEnabled(true);
        patternSpinner.setEnabled(true);
        isRunning = false;
    }

    private void startSim() {

        // choose a color
        Random rand = new Random();
        board.color = colors[rand.nextInt(colors.length)];

        // create new cells objetcs
        //board.resetLineAndCellArrays();
        //generations = 0;
        //set starting cells
        //setStartPositions();
        // start runnable thread
        h.postAtTime(r, SystemClock.uptimeMillis() + 400);


        // update the ui behavour and local properties.
        runPauseButton.setText("Pause");
        generationInput.setEnabled(false);
        patternSpinner.setEnabled(false);
        isRunning = true;
    }

//    private void stopSim() {
//
//        // TODO: code to stop the simulation
//        h.removeCallbacks(r);
//
//        // update the ui behavour and local properties.
//        runPauseButton.setText("Run");
//        generationInput.setEnabled(true);
//        patternSpinner.setEnabled(true);
//        isRunning = false;
//    }
//
//    public void startSim()
//    {
//        h.postAtTime(r, SystemClock.uptimeMillis() + 400);
//        // update the ui behavour and local properties.
//        runPauseButton.setText("Pause");
//        generationInput.setEnabled(false);
//        patternSpinner.setEnabled(false);
//        isRunning = true;
//    }

    private void newSim() {
        // create new cells objetcs
        board.resetLineAndCellArrays();
        generations = 0;
        setStartPositions();
    }

    public void setStartPositions() {
        // TODO File I/O
        // stubbed for now
        for (int i = 0; i < 30; i++) {
            board.cells[i][15].alive = true;
        }
    }

    public void clearCanvas() {
        board.clearCanvas();
    }

    private void loadFile(String file) {
        try {
            InputStream inStream = getResources().openRawResource(getResources().getIdentifier(file.toLowerCase(), "raw", getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            String line;
            String[] coords;
            int posx, posy;
            while ((line = br.readLine()) != null) {
                coords = line.split(":");
                posx = Integer.parseInt(coords[0]);
                posy = Integer.parseInt(coords[1]);
                //TODO: make cell at posx,posy alive.
                board.cells[posx][posy].alive = true;

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("File", "File not Found");
        }
    }


    private void connectWidgets() {
        board = (Board) findViewById(R.id.signature_canvas);
        runPauseButton = (Button) findViewById(R.id.btnRun);
        clearButton = (Button) findViewById(R.id.btnClear);
        patternSpinner = (Spinner) findViewById(R.id.spinnerSetup);
        generationInput = (EditText) findViewById(R.id.etGenerations);
        generationReport = (TextView) findViewById(R.id.tvGenCount);

        // initialize the pattern spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spn_template, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        patternSpinner.setAdapter(adapter);

        patternSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String template = "";
                if (position != 0) { // this is the prompt to choose a file
                    // stop the sim and clear the board
                    if (isRunning) {
                        stopSim();
                    }
                    generations = 0;
                    generationInput.setText("100");
                    updateReport();
                    newSim();
                    clearCanvas();

                    String temp[] = parent.getItemAtPosition(position).toString().split(" ");
                    for (int idx = 0; idx < temp.length; idx++) {
                        template += temp[idx];
                    }
                    loadFile(template);
                } else {
                    //hasFile = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void connectListeners() {
        // listener for runPauseButton:
        runPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    stopSim();
                } else {
                    //get the max generation from the input
                    String input = generationInput.getText().toString();
                    int inputGens;
                    if (input.equals(""))
                        inputGens = 0;
                    else
                        inputGens = Integer.parseInt(input);
                    if (inputGens == -1) { // bad input
                        Toast.makeText(getBaseContext(), "Generations must be a positive integer", Toast.LENGTH_SHORT).show();
                    } else {
                        maxGenerations = inputGens;
                        startSim();
                    }
                }
            }
        });

        // listener for clearButton:
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRunning) {
                    stopSim();
                }
                generations = 0;
                generationInput.setText("100");
                updateReport();
                newSim();
                clearCanvas();

            }
        });

        // item click listener for the pattern selection spinner
//        patternSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                if (!isRunning) { // just in case
//
//                    //TODO: code to load the selected pattern from file (i'th pattern)
//
//                }
//
//            }
//        });
//
//        // touch listener for the board is created in the board class itself... I think!!
    }

    private int extractInteger(String s) {
        for (int idx = 0; idx < s.length(); idx++) {
            if (!Character.isDigit(s.charAt(idx))) {
                return -1;
            }
        }
        return Integer.parseInt(s);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.nscc.gameoflife.jareds_machine/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.nscc.gameoflife.jareds_machine/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
