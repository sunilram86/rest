package com.galvanize;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(TemperatureController.class)
public class BasicRequestTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TemperatureConverter temperatureConverter;

    @Test
    public void homePageWorks() throws Exception {
        this.mockMvc.perform(get("/temperatures"))
        .andExpect(status().isOk())
        .andExpect(content().string("Temperatures"));
    }

    @Test
    public void queryStringsWork() throws Exception {
        this.mockMvc.perform(get("/temperatures/geo?lat=41.000&lng=54.000"))
            .andExpect(status().isOk())
            .andExpect(content().string("Showing temperature Latitude 41.000 and Longitude 54.000"));

        this.mockMvc.perform(get("/temperatures/geo?lat=40.710&lng=74.006"))
            .andExpect(status().isOk())
            .andExpect(content().string("Showing temperature Latitude 40.710 and Longitude 74.006"));
    }

	@Test
	public void pathVariablesWork() throws Exception {
	    this.mockMvc.perform(get("/temperatures/CO/Boulder/80304"))
    	    .andExpect(status().isOk())
    	    .andExpect(content().string("Showing temperature for Boulder, CO 80304"));

	    this.mockMvc.perform(get("/temperatures/NY/Cooperstown/13326"))
	        .andExpect(status().isOk())
	        .andExpect(content().string("Showing temperature for Cooperstown, NY 13326"));
	}

    @Test
    public void returningJSONWorks() throws Exception {
        this.mockMvc.perform(get("/temperatures/daily"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.wind.speed", equalTo("5mph")))
            .andExpect(jsonPath("$.wind.direction", equalTo("W")))
            .andExpect(jsonPath("$.temperature.high", equalTo("45")))
            .andExpect(jsonPath("$.temperature.low", equalTo("32")));
    }

    @Test
    public void postingFormParametersWorks() throws Exception {
        given(this.temperatureConverter.convert(0, "celcius")).willReturn(42.0);

        this.mockMvc.perform(
                post("/temperatures/convert")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("temperature", "0.0")
                .param("unit", "celcius"))
            .andExpect(status().isOk())
            .andExpect(content().string("42.0"));
    }

    @Test
    public void postingJSONWorks() throws Exception {
        given(this.temperatureConverter.convert(0, "celcius")).willReturn(54.3);

        this.mockMvc.perform(
                post("/temperatures/convertjson")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("{\"temperature\": \"0.0\", \"unit\": \"celcius\"}"))
            .andExpect(status().isOk())
            .andExpect(content().string("54.3"));
    }

}
