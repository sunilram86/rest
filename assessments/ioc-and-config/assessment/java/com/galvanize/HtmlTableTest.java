package com.galvanize;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "formatter.type=html",
        "formatter.html.tag_name=table",
})
public class HtmlTableTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void testFormatterWorksCorrectly() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> object = new HashMap<>();
        object.put("string", "string");
        object.put("number", 3);
        object.put("boolean", false);
        list.add(object);

        String json = mapper.writeValueAsString(list);

        RequestBuilder request = post("/format")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("<table><thead><tr><th>number</th><th>boolean</th><th>string</th></tr></thead><tbody><tr><td>3</td><td>false</td><td>string</td></tr></tbody></table>"))
        ;

    }

}
