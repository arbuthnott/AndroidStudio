package com.example.w0276812.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    // define widgets
    private TextView screenTxt, historyTxt;
    private Button clearBtn, backBtn;
    private Button signBtn, decimalBtn, equalsBtn;
    private Button plusBtn, minusBtn, multBtn, divBtn;
    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9, btn0;
    
    // define working variables
    private double argA;
    private char operation;
    private boolean argASet = false;
    private boolean operationSet = false;
    private CalcLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logic = new CalcLogic();
        connectWidgets();
        connectButtonListeners();
        updateDisplay();
    }

    private void updateDisplay() {
        screenTxt.setText(logic.getScreen());
        historyTxt.setText(logic.getHist());
    }

    private void connectButtonListeners() {
        // add listeners for the buttons.
        clearBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushClear();
                updateDisplay();
            }
        });

        backBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushBack();
                updateDisplay();
            }
        });

        signBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushSign();
                updateDisplay();
            }
        });

        decimalBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushDecimal();
                updateDisplay();
            }
        });

        equalsBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushEquals();
                updateDisplay();
            }
        });

        plusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushOp('+');
                updateDisplay();
            }
        });

        minusBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushOp('-');
                updateDisplay();
            }
        });

        multBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushOp('*');
                updateDisplay();
            }
        });

        divBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushOp('/');
                updateDisplay();
            }
        });

        btn1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(1);
                updateDisplay();
            }
        });

        btn2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(2);
                updateDisplay();
            }
        });

        btn3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(3);
                updateDisplay();
            }
        });

        btn4.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(4);
                updateDisplay();
            }
        });

        btn5.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(5);
                updateDisplay();
            }
        });

        btn6.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(6);
                updateDisplay();
            }
        });

        btn7.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(7);
                updateDisplay();
            }
        });

        btn8.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(8);
                updateDisplay();
            }
        });

        btn9.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(9);
                updateDisplay();
            }
        });

        btn0.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                logic.pushNum(0);
                updateDisplay();
            }
        });
    }

    private double eval(double arg1, double arg2, char op) {
        String opStr = "" + op;
        switch (opStr) {
            case "+":
                return arg1 + arg2;
            case "-":
                return arg1 - arg2;
            case "*":
                return arg1 * arg2;
            case "/":
                if (arg2 != 0) {
                    return arg1 / arg2;
                } else {
                    return Double.POSITIVE_INFINITY;
                }
            default:
                return 42;
        }
    }

    private void pushClear() {
        operationSet = false;
        argA = 0;
        argASet = false;
        historyTxt.setText("");
        screenTxt.setText("0");
    }
    private void pushBack() {
        String numText = (String) screenTxt.getText();
        if (numText.length() <= 1) {
            numText = "0";
        } else {
            numText = numText.substring(0, numText.length() - 1);
        }
        screenTxt.setText(numText);
    }
    private void pushSign() {
        String numText = (String)screenTxt.getText();
        if (numText.charAt(0) == "-".charAt(0)) {
            numText = numText.substring(1);
        } else {
            numText = "-" + numText;
        }
        screenTxt.setText(numText);
    }
    private void pushEquals() {}
    private void pushPlus() {
        String numText = (String)screenTxt.getText();
        if (!argASet) {
            argA = Double.parseDouble(numText);
            argASet = true;
            operation = "+".charAt(0);
            operationSet = true;
            historyTxt.setText("" + argA + " " + operation);
            screenTxt.setText("0");
        } else {
            double argB = Double.parseDouble(numText);
            double answ = eval(argA, argB, operation);
            argA = answ;
            operation = "+".charAt(0);
            historyTxt.setText("" + argA + " " + "+");
            screenTxt.setText("0");
        }
    }
    private void pushMinus() {}
    private void pushMult() {}
    private void pushDiv() {}
    private void pushDecimal() {
        String numText = (String)screenTxt.getText();
        if (!numText.contains(".")) {
            numText += ".";
            screenTxt.setText(numText);
        }
    }
    private void pushNum(int num) {
        String numText = (String)screenTxt.getText();
        if (numText.equals("0")) {
            numText = "" + num;
        } else {
            numText += ("" + num);
        }
        screenTxt.setText(numText);
    }

    private void connectWidgets() {
        screenTxt = (TextView)findViewById(R.id.screenTxt);
        historyTxt = (TextView)findViewById(R.id.historyTxt);
        clearBtn = (Button)findViewById(R.id.clearBtn);
        backBtn = (Button)findViewById(R.id.backBtn);
        signBtn = (Button)findViewById(R.id.signButton);
        decimalBtn = (Button)findViewById(R.id.decimalBtn);
        equalsBtn = (Button)findViewById(R.id.equalsBtn);
        plusBtn = (Button)findViewById(R.id.plusBtn);
        minusBtn = (Button)findViewById(R.id.minusBtn);
        multBtn = (Button)findViewById(R.id.multBtn);
        divBtn = (Button)findViewById(R.id.divBtn);
        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        btn4 = (Button)findViewById(R.id.btn4);
        btn5 = (Button)findViewById(R.id.btn5);
        btn6 = (Button)findViewById(R.id.btn6);
        btn7 = (Button)findViewById(R.id.btn7);
        btn8 = (Button)findViewById(R.id.btn8);
        btn9 = (Button)findViewById(R.id.btn9);
        btn0 = (Button)findViewById(R.id.btn0);
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
}
