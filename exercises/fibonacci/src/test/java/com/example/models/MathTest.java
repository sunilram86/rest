package com.example.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MathTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldSquareAnInteger() throws Exception {
        int result = Math.square(4);
        assertEquals(16, result);
    }

    @Test
    public void shouldComputeTheFactorial() throws Exception {
        int result = Math.factorial(5);
        assertEquals(120, result);
    }

    @Test
    public void shouldComputeTheFibonacci() throws Exception {
        int result = Math.fibonacci(15);
        assertEquals(610, result);
    }
}