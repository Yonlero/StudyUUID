package com.abraaofaher.studyUUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.abraaofaher.studyUUID.model.repositories", "com.abraaofaher.studyUUID.model.services", "com.abraaofaher.studyUUID.controllers", "com.abraaofaher.studyUUID.model.utils"})
public class StudyUUIDApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyUUIDApplication.class, args);
    }
}