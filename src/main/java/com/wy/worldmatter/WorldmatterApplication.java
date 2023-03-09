package com.wy.worldmatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, GsonAutoConfiguration.class})
public class WorldmatterApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorldmatterApplication.class, args);
    }

}
