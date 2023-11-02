package com.sunny.simple.calculator.simplecalculator.service;

import com.sunny.simple.calculator.simplecalculator.service.exception.ExpressionEvaluationException;

import java.util.Arrays;
import java.util.Stack;
import java.util.regex.Pattern;

public class SimpleDMASCalculator {

    private static final String VALID_EXPRESSION_REGEX = "^-?\\d+ *([+\\-*/] +-?\\d+ *)*$";
    private static final Pattern PATTERN_VALID_EXPRESSION = Pattern.compile(VALID_EXPRESSION_REGEX);

    public static int calculate(String expression) throws ExpressionEvaluationException {
        if (expression == null) {
            throw new IllegalArgumentException("Cannot have null expressions");
        }
        String trimmedExpression = expression.trim();
        if (!isValidExpression(trimmedExpression)) {
            throw new IllegalArgumentException(String.format("Invalid expression %s", expression));
        }

        Stack<Character> operatorStack = new Stack<>();
        Stack<Integer> operandStack = new Stack<>();

        try {
            Arrays.stream(trimmedExpression.split("\\s+")).forEach(token -> {
                if (token.matches("[/*+-]")) {
                    handleOperator(operatorStack, operandStack, token);
                } else if (token.matches("-?\\d+")) {
                    operandStack.push(Integer.parseInt(token));
                } else {
                    throw new IllegalArgumentException(String.format("Unrecognized token found in expression %s ", expression));
                }
            });
            evaluateOperandStack(operatorStack, operandStack);
            int result = operandStack.pop();
            if (result == Integer.MAX_VALUE || result == Integer.MIN_VALUE) {
                throw new ArithmeticException("Overflow or underflow detected in expression result");
            }
            return result;
        } catch (RuntimeException e) {
            throw new ExpressionEvaluationException(String.format("Evaluation of expression %s failed", expression), e);
        }
    }

    public static boolean isValidExpression(String expression) {
        return !expression.isBlank() && PATTERN_VALID_EXPRESSION.matcher(expression).matches();
    }

    private static void handleOperator(Stack<Character> operatorStack, Stack<Integer> operandStack, String token) {
        char operator = token.charAt(0);
        if (!operatorStack.empty() && getPrecedence(operator)
                < getPrecedence(operatorStack.peek())) {
            evaluateOperandStack(operatorStack, operandStack);
        }
        operatorStack.push(operator);
    }

    private static void evaluateOperandStack(Stack<Character> operatorStack, Stack<Integer> operandStack) {
        while (!operatorStack.isEmpty() && !operandStack.isEmpty() && operandStack.size() >= 2) {
            char op = operatorStack.pop();
            int operandB = operandStack.pop();
            int operandA = operandStack.pop();
            operandStack.push(performOperation(operandA, operandB, op));
        }
    }

    private static int performOperation(int operandA, int operandB, char operator) {
        return switch (operator) {
            case '/' -> operandA / operandB;
            case '*' -> operandA * operandB;
            case '+' -> operandA + operandB;
            case '-' -> operandA - operandB;
            default -> throw new IllegalArgumentException("Unrecognized operator");
        };
    }

    private static int getPrecedence(char operator) {
        return switch (operator) {
            case '/' -> 400;
            case '*' -> 300;
            case '+', '-' -> 100;
            default -> 0;
        };
    }
}
