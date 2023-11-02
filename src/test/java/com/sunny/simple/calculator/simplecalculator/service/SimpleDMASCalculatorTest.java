package com.sunny.simple.calculator.simplecalculator.service;

import com.sunny.simple.calculator.simplecalculator.service.exception.ExpressionEvaluationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleDMASCalculatorTest {

    @Test
    public void testAddition() throws ExpressionEvaluationException {
        assertEquals(5, SimpleDMASCalculator.calculate("2 + 3"));
    }

    @Test
    public void testSubtraction() throws ExpressionEvaluationException {
        assertEquals(1, SimpleDMASCalculator.calculate("5 - 4"));
    }

    @Test
    public void testMultiplication() throws ExpressionEvaluationException {
        assertEquals(6, SimpleDMASCalculator.calculate("3 * 2"));
    }

    @Test
    public void testDivision() throws ExpressionEvaluationException {
        assertEquals(2, SimpleDMASCalculator.calculate("8 / 4"));
    }

    @Test
    public void testPositiveAndNegativeNumbers() throws ExpressionEvaluationException {
        assertEquals(-1, SimpleDMASCalculator.calculate("2 + -3"));
    }

    @Test
    public void testOrderOfOperations() throws ExpressionEvaluationException {
        assertEquals(6, SimpleDMASCalculator.calculate("2 + 3 * 2 - 5 / 2"));
        assertEquals(8, SimpleDMASCalculator.calculate("2 * 3 + 4 / 2"));
    }

    @Test
    public void testDivideByZero() {
        assertThrows(ExpressionEvaluationException.class, () -> SimpleDMASCalculator.calculate("2 / 0"));
    }

    @Test
    public void testOverflow() {
        assertThrows(ExpressionEvaluationException.class, () -> SimpleDMASCalculator.calculate(Integer.MAX_VALUE + " + 1"));
    }

    @Test
    public void testUnderflow() {
        assertThrows(ExpressionEvaluationException.class, () -> SimpleDMASCalculator.calculate(Integer.MIN_VALUE + " - 1"));
    }

    @Test
    public void testInvalidExpression() {
        assertThrows(IllegalArgumentException.class, () -> SimpleDMASCalculator.calculate("2 + * 3"));
    }

    @Test
    public void testEmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> SimpleDMASCalculator.calculate(""));
    }

    @Test
    public void testNullExpression() {
        assertThrows(IllegalArgumentException.class, () -> SimpleDMASCalculator.calculate(null));
    }

    @Test
    public void testValidExpressionsWithSpaces() throws ExpressionEvaluationException {
       assertEquals(4, SimpleDMASCalculator.calculate("2 +  2"));
        assertEquals(6, SimpleDMASCalculator.calculate("4  * 1 + 2"));
        assertEquals(10, SimpleDMASCalculator.calculate("2 + 3 + 5"));
        assertEquals(-1, SimpleDMASCalculator.calculate("1 +             -2"));
        assertEquals(-3, SimpleDMASCalculator.calculate("-1 + -2   - -0"));
        assertEquals(232, SimpleDMASCalculator.calculate("-4 - 1 -  -3 + 3  * 80"));
    }


    @Test
    public void testValidExpressionsWithLeadingSpaces() throws ExpressionEvaluationException {
        assertEquals(4, SimpleDMASCalculator.calculate("   2 +  2"));
        assertEquals(6, SimpleDMASCalculator.calculate("  4  * 1 + 2"));
        assertEquals(10, SimpleDMASCalculator.calculate("    2 + 3 + 5"));
    }

    @Test
    public void testLongExpression() throws ExpressionEvaluationException {
        String longExpression = "1 + 2 * 3 - 4 / 2 + 5 * 6 - 7 / 3 + 8 * 9 - 10 / 5";
        int expectedValue = 1 + 2 * 3 - 4 / 2 + 5 * 6 - 7 / 3 + 8 * 9 - 10 / 5;
        assertEquals(expectedValue, SimpleDMASCalculator.calculate(longExpression));
    }

    @Test
    public void testExpressionWithoutAnySpaces() {
        assertThrows(IllegalArgumentException.class, () -> SimpleDMASCalculator.calculate("2+3+4*5"));
    }

}
