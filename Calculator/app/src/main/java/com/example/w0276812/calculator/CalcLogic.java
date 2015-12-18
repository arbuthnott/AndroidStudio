package com.example.w0276812.calculator;
import java.text.DecimalFormat;

/**
 * Created by w0276812 on 9/23/2015.
 */
public class CalcLogic {

    // define working variables
    private double firstArg;
    private char operation;
    private String screenString, histString;
    private boolean firstArgSet;

    public CalcLogic() {
        firstArgSet = false;
        screenString = "0";
        histString = "";
        operation = 'n';
    }

    public String getScreen() {
        return screenString;
    }

    public String getHist() {
        return histString;
    }

    /**
     * A method to format doubles for presentation to user.  Eliminate
     * scientific notation, limit number of chars displayed to 15.
     *
     * @param dec the input double
     * @return the trimmed string.
     */
    private String trimDecimal(double dec) {
        DecimalFormat df = new DecimalFormat("#0.###############");
        String numString = df.format(dec);
        if (numString.length() > 15) {
            numString = numString.substring(0,15);
        }
        return numString;
    }

    private double eval(double arg1, double arg2, char op) {
        double answ;
        switch (op) {
            case '+':
                answ = arg1 + arg2;
                break;
            case '-':
                answ = arg1 - arg2;
                break;
            case '*':
                answ = arg1 * arg2;
                break;
            case '/':
                if (arg2 != 0) {
                    answ = arg1 / arg2;
                } else if (arg1 >= 0) {
                    answ = 999999999999999.0;
                } else {
                    answ = -99999999999999.0;
                }
                break;
            default:
                return 42;
        }
        if (answ > 999999999999999.0) {
            answ = 999999999999999.0;
        } else if (answ < -99999999999999.0) {
            answ = -99999999999999.0;
        }
        return answ;
    }

    public void pushClear() {
        firstArgSet = false;
        firstArg = 0;
        operation = 'n';
        histString = "";
        screenString = "0";
    }
    public void pushBack() {
        if (screenString.length() <= 1) {
            screenString = "0";
        } else {
            screenString = screenString.substring(0, screenString.length() - 1);
        }
    }
    public void pushSign() {
        if (screenString.charAt(0) == '-') {
            screenString = screenString.substring(1);
        } else if (screenString.length() < 15){
            screenString = "-" + screenString;
        }
    }
    public void pushEquals() {
        if (firstArgSet) {
            double secondArg = Double.parseDouble(screenString);
            double answ = eval(firstArg, secondArg, operation);
            firstArgSet = false;
            operation = 'n';
            histString = "";
            screenString = trimDecimal(answ);
        }
    }
    public void pushOp(char op) {
        if (!firstArgSet) {
            firstArg = Double.parseDouble(screenString);
            firstArgSet = true;
        } else {
            double secondArg = Double.parseDouble(screenString);
            double answ = eval(firstArg, secondArg, operation);
            firstArg = answ;
        }
        if (firstArg > 999999999999999.0) {
            firstArg = 999999999999999.0;
        } else if (firstArg < -99999999999999.0) {
            firstArg = -99999999999999.0;
        }
        operation = op;
        histString = "" + trimDecimal(firstArg) + " " + operation;
        screenString = "0";
    }

    public void pushDecimal() {
        if (!screenString.contains(".") && screenString.length() < 15) {
            screenString += ".";
        }
    }
    public void pushNum(int num) {
        if (screenString.length() < 15) {
            if (screenString.equals("0")) {
                screenString = "" + num;
            } else if (screenString.equals("-0")) {
                screenString = "-" + num;
            } else {
                screenString += ("" + num);
            }
        }
    }
}
