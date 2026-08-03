package com.api.demo.project.config;

import com.api.demo.project.ApiDemoProjectApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = ApiDemoProjectApplication.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class CucumberSpringConfiguration {
}
