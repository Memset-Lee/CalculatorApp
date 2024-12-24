package com.example.calculator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String PREFS_NAME = "CalculatorPrefs";
    private static final String KEY_FIRST_NUM = "firstNum";
    private static final String KEY_OPERATOR = "operator";
    private static final String KEY_SECOND_NUM = "secondNum";
    private static final String KEY_RESULT = "result";
    private static final String KEY_SHOW_TEXT = "showText";

    // 文本视图tv_result
    private TextView tv_result;
    // 第一个操作数
    private String firstNum = "";
    // 运算符
    private String operator = "";
    // 第二个操作数
    private String secondNum = "";
    // 结果
    private String result = "";
    // 文本内容
    private String showText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取文本视图tv_result
        tv_result = findViewById(R.id.tv_result);
        tv_result.setMovementMethod(new ScrollingMovementMethod());
        // 注册点击监听器
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.btn_divide).setOnClickListener(this);
        findViewById(R.id.btn_multiply).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_seven).setOnClickListener(this);
        findViewById(R.id.btn_eight).setOnClickListener(this);
        findViewById(R.id.btn_nine).setOnClickListener(this);
        findViewById(R.id.btn_plus).setOnClickListener(this);
        findViewById(R.id.btn_four).setOnClickListener(this);
        findViewById(R.id.btn_five).setOnClickListener(this);
        findViewById(R.id.btn_six).setOnClickListener(this);
        findViewById(R.id.btn_minus).setOnClickListener(this);
        findViewById(R.id.btn_one).setOnClickListener(this);
        findViewById(R.id.btn_two).setOnClickListener(this);
        findViewById(R.id.btn_three).setOnClickListener(this);
        findViewById(R.id.btn_reciprocal).setOnClickListener(this);
        findViewById(R.id.btn_zero).setOnClickListener(this);
        findViewById(R.id.btn_dot).setOnClickListener(this);
        findViewById(R.id.btn_equal).setOnClickListener(this);
        findViewById(R.id.ib_sqrt).setOnClickListener(this);
        // 恢复之前的计算状态
        restoreState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    public void onClick(View v) {
        String inputText;
        if (v.getId() == R.id.ib_sqrt) {
            inputText = "√";
        } else {
            inputText = ((TextView) v).getText().toString();
        }
        int id = v.getId();
        if (id == R.id.btn_clear) {
            clear();
        } else if (id == R.id.btn_cancel) {
            cancel();
        } else if (id == R.id.btn_plus || id == R.id.btn_minus || id == R.id.btn_multiply || id == R.id.btn_divide) {
            if (!firstNum.equals("") && operator.equals("") && secondNum.equals("")) {
                operator = inputText;
                refreshText(showText + operator);
            }
        } else if (id == R.id.btn_equal) {
            if (!firstNum.equals("") && !operator.equals("") && !secondNum.equals("")) {
                double calculate_result = calculateFour();
                refreshOperate(String.valueOf(calculate_result));
                refreshText(showText + "=\n" + result);
            }
        } else if (id == R.id.ib_sqrt) {
            if (!firstNum.equals("") && operator.equals("") && secondNum.equals("")) {
                double sqrt_result = Math.sqrt(Double.parseDouble(firstNum));
                refreshOperate(String.valueOf(sqrt_result));
                refreshText(showText + "√=\n" + sqrt_result);
            }
        } else if (id == R.id.btn_reciprocal) {
            if (!firstNum.equals("") && operator.equals("") && secondNum.equals("")) {
                double reciprocal_result = 1.0 / Double.parseDouble(firstNum);
                refreshOperate(String.valueOf(reciprocal_result));
                refreshText(showText + "/=\n" + reciprocal_result);
            }
        } else {
            boolean flag = false;
            if (result.length() > 0 && operator.equals("")) {
                clear();
            }
            if (operator.equals("")) {
                if (checkDot(firstNum + inputText)) {
                    firstNum = firstNum + inputText;
                    flag = true;
                }
            } else {
                if (checkDot(secondNum + inputText)) {
                    secondNum = secondNum + inputText;
                    flag = true;
                }
            }
            if (showText.equals("0") && !inputText.equals(".")) {
                if (flag) {
                    refreshText(inputText);
                }
            } else {
                if (flag) {
                    refreshText(showText + inputText);
                }
            }
        }
    }

    // 四则运算
    private double calculateFour() {
        switch (operator) {
            case "+":
                return Double.parseDouble(firstNum) + Double.parseDouble(secondNum);
            case "-":
                return Double.parseDouble(firstNum) - Double.parseDouble(secondNum);
            case "×":
                return Double.parseDouble(firstNum) * Double.parseDouble(secondNum);
            default:
                return Double.parseDouble(firstNum) / Double.parseDouble(secondNum);
        }
    }

    // 清空并初始化
    private void clear() {
        refreshOperate("");
        refreshText("");
    }

    // 刷新运算结果
    private void refreshOperate(String new_result) {
        result = new_result;
        firstNum = result;
        secondNum = "";
        operator = "";
    }

    // 撤回操作
    private void cancel() {
        if (!showText.equals("")) {
            if (!secondNum.equals("")) {
                secondNum = secondNum.substring(0, secondNum.length() - 1);
                refreshText(showText.substring(0, showText.length() - 1));
            } else if (!operator.equals("")) {
                operator = operator.substring(0, operator.length() - 1);
                refreshText(showText.substring(0, showText.length() - 1));
            } else if (!firstNum.equals("")) {
                if (result.equals("")) {
                    firstNum = firstNum.substring(0, firstNum.length() - 1);
                    refreshText(showText.substring(0, showText.length() - 1));
                }
            }
        }
    }

    // 刷新文本显示
    private void refreshText(String text) {
        showText = text;
        tv_result.setText(showText);
    }

    // 检查小数点
    private boolean checkDot(String text) {
        int num = 0;
        for (char ch : text.toCharArray()) {
            if (ch == '.') {
                num++;
            }
        }
        if (text.charAt(0) == '.' || num > 1) {
            return false;
        } else {
            return true;
        }
    }

    // 存储当前状态
    private void saveState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_FIRST_NUM, firstNum);
        editor.putString(KEY_OPERATOR, operator);
        editor.putString(KEY_SECOND_NUM, secondNum);
        editor.putString(KEY_RESULT, result);
        editor.putString(KEY_SHOW_TEXT, showText);
        editor.apply();
    }

    // 恢复上次计算的状态
    private void restoreState() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        firstNum = prefs.getString(KEY_FIRST_NUM, "");
        operator = prefs.getString(KEY_OPERATOR, "");
        secondNum = prefs.getString(KEY_SECOND_NUM, "");
        result = prefs.getString(KEY_RESULT, "");
        showText = prefs.getString(KEY_SHOW_TEXT, "");
        refreshText(showText);
    }
}