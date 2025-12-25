package com.example.calculater;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvResult;
    String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);

        setNumberListeners();
        setOperatorListeners();
    }

    private void setNumberListeners() {
        int[] ids = {
                R.id.btn00, R.id.btn0, R.id.btn1, R.id.btn2,
                R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9, R.id.btnDot
        };

        for (int id : ids) {
            findViewById(id).setOnClickListener(v -> {
                Button b = (Button) v;
                input += b.getText().toString();
                tvResult.setText(input);
            });
        }
    }

    private void setOperatorListeners() {
        findViewById(R.id.btnPlus).setOnClickListener(v -> appendOperator("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> appendOperator("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> appendOperator("*"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> appendOperator("/"));

        findViewById(R.id.btnPercent).setOnClickListener(v -> {
            if (!input.isEmpty()) {
                double val = Double.parseDouble(input) / 100;
                input = String.valueOf(val);
                tvResult.setText(input);
            }
        });

        findViewById(R.id.btnDelete).setOnClickListener(v -> {
            if (!input.isEmpty()) {
                input = input.substring(0, input.length() - 1);
                tvResult.setText(input.isEmpty() ? "0" : input);
            }
        });

        findViewById(R.id.btnClear).setOnClickListener(v -> {
            input = "";
            tvResult.setText("0");
        });

        findViewById(R.id.btnEquals).setOnClickListener(v -> calculate());
    }

    private void appendOperator(String op) {
        if (!input.isEmpty()) {
            char lastChar = input.charAt(input.length() - 1);
            if ("+-*/".indexOf(lastChar) != -1) {
                // Replace last operator
                input = input.substring(0, input.length() - 1) + op;
            } else {
                input += op;
            }
            tvResult.setText(input);
        }
    }

    private void calculate() {
        if (input.isEmpty()) return;

        try {
            double result = evaluate(input);
            input = String.valueOf(result);
            tvResult.setText(input);
        } catch (Exception e) {
            tvResult.setText("Error");
        }
    }

    // Evaluate string with operator precedence
    private double evaluate(String expr) {
        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (char c : expr.toCharArray()) {
            if (c >= '0' && c <= '9' || c == '.') {
                sb.append(c);
            } else if ("+-*/".indexOf(c) != -1) {
                numbers.add(Double.parseDouble(sb.toString()));
                sb.setLength(0);
                operators.add(c);
            }
        }
        numbers.add(Double.parseDouble(sb.toString()));

        // Operator precedence: / -> * -> + -> -
        char[] precedence = {'/', '*', '+', '-'};

        for (char op : precedence) {
            for (int i = 0; i < operators.size(); ) {
                if (operators.get(i) == op) {
                    double a = numbers.get(i);
                    double b = numbers.get(i + 1);
                    double res = 0;
                    switch (op) {
                        case '/':
                            if (b == 0) throw new ArithmeticException("Division by zero");
                            res = a / b; break;
                        case '*': res = a * b; break;
                        case '+': res = a + b; break;
                        case '-': res = a - b; break;
                    }
                    numbers.set(i, res);
                    numbers.remove(i + 1);
                    operators.remove(i);
                } else {
                    i++;
                }
            }
        }

        return numbers.get(0);
    }
}
