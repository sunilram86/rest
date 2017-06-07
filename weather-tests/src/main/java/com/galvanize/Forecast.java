package com.galvanize;


public class Forecast {

    public static class Wind {
        private String speed;
        private String direction;

        public Wind(String speed, String direction) {
            this.speed = speed;
            this.direction = direction;
        }

        public String getSpeed() {
            return this.speed;
        }

        public String getDirection() {
            return this.direction;
        }
    }


    public static class Temperature {
        private String high;
        private String low;

        public Temperature(String high, String low) {
            this.high = high;
            this.low = low;
        }

        public String getHigh() {
            return this.high;
        }

        public void setHigh(String high) {
            this.high = high;
        }

        public String getLow() {
            return this.low;
        }

        public void setLow(String low) {
            this.low = low;
        }
    }

    private Wind wind;
    private Temperature temperature;

    public Forecast(Temperature temperature, Wind wind) {
        this.wind = wind;
        this.temperature = temperature;
    }

    public Temperature getTemperature() {
        return this.temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public Wind getWind() {
        return this.wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

}
