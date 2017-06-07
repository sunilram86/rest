package com.example.models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldComputeDistanceBetweenTwoPoints() throws Exception {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(7, 8);
        double distance = Point.distance(p1, p2);
        assertEquals(5.65, distance, 0.1);
    }

    @Test
    public void shouldComputeSlopeBetweenTwoPoints() throws Exception {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(7, 8);
        double slope = Point.slope(p1, p2);
        assertEquals(1, slope, 0.1);
    }

    @Test
    public void shouldComputeInterceptOfTwoPoints() throws Exception {
        Point p1 = new Point(3, 4);
        Point p2 = new Point(7, 8);
        double intercept = Point.intercept(p1, p2);
        assertEquals(1, intercept, 0.1);
    }
}