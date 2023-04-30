package tech.nobb.zeebe.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ZeebeTaskApplication {

  public static void main(final String... args) {
    SpringApplication.run(ZeebeTaskApplication.class, args);
  }

  @Bean
  @ConditionalOnMissingBean(RestTemplateBuilder.class)
  public RestTemplateBuilder restTemplateBuilder() {
    return new RestTemplateBuilder();
  }

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder.build();
  }

}
