package com.galvanize.model;

public class Counter {

    int count;

    public Counter() {
    }

    public Counter(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Counter counter = (Counter) o;

        return count == counter.count;
    }

    @Override
    public int hashCode() {
        return count;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "count=" + count +
                '}';
    }
}