package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class MyTools {

    private static final Logger log = LoggerFactory.getLogger(MyTools.class);

    @Tool(description = "Get the weather at a specific location.")
    public String weatherByLocation(String city) {
        log.info("Get the weather for: {}", city);
        if (city.contains("Stockholm")) {
            return "It is raining and the temperature is 15 degrees celsius";
        }
        if (city.contains("Barcelona")) {
            return "It is sunny and the temperature is 25 degrees celsius";
        } else {
            return "I don't know the weather in " + city + ".";
        }
    }
}
