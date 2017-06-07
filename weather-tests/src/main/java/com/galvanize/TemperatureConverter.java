package com.galvanize;

import org.springframework.stereotype.Service;

@Service
public class TemperatureConverter {

    public double convert(double originalTemperature, String unit) {
        double convertedTemperature = 0;

        if (unit.equals("celcius")) {
            convertedTemperature = originalTemperature * 1.8 + 32;
        }
        else if (unit.equals("farenheit")) {
            convertedTemperature = (originalTemperature - 32) / 1.8;
        }

        return convertedTemperature;
    }

}
