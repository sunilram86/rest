package com.galvanize;

import java.util.Map;

import com.galvanize.Forecast.Temperature;
import com.galvanize.Forecast.Wind;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/temperatures")
public class TemperatureController {

    private TemperatureConverter converter;

    public TemperatureController(TemperatureConverter converter) {
        this.converter = converter;
    }

    @GetMapping("")
    public String getTemperatures() {
        return "Temperatures";
    }

    @GetMapping("/geo")
    public String getTemperaturesForLocation(@RequestParam String lat, @RequestParam String lng) {
        return String.format("Showing temperature Latitude %s and Longitude %s", lat, lng);
    }

    @GetMapping("/{state}/{city}/{zip}")
    public String getTemperaturesForCity(@PathVariable String state, @PathVariable String city, @PathVariable String zip) {
        return String.format("Showing temperature for %s, %s %s", city, state, zip);
    }

    @GetMapping("/daily")
    public Forecast getForecastJson() {
        return new Forecast(new Temperature("45", "32"), new Wind("5mph", "W"));
    }

    @PostMapping("/convert")
    public String convertTemperatureForm(@RequestParam Map<String, String> formValues) {
        double originalTemperature = Double.parseDouble(formValues.get("temperature"));
        String unit = formValues.get("unit");
        double convertedTemperature = this.converter.convert(originalTemperature, unit);
        return String.valueOf(convertedTemperature);
    }

    @PostMapping("/convertjson")
    public String convertTemperatureJSON(@RequestBody Map<String, String> formValues) {
        double originalTemperature = Double.parseDouble(formValues.get("temperature"));
        String unit = formValues.get("unit");
        double convertedTemperature = this.converter.convert(originalTemperature, unit);
        return String.valueOf(convertedTemperature);
    }

}
