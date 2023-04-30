package tech.nobb.zeebe.task;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HumanJobWorkers {

  private static Logger log = LoggerFactory.getLogger(HumanJobWorkers.class);

  public static String ENDPOINT_TASK_ENGINE = "http://localhost:8080/api/task-assign";

  @Autowired
  private RestTemplate restTemplate;

  /*
  {
    "name":"测试创建任务并分配相应执行人",
          "checkRule":"percentage",
          "threshold":"1",
          "allocator":"serial",
          "order": ["015754","011330","011114","012901"],
    "executors": ["011114","012901","015754","011330"]
  }*/

  @JobWorker(autoComplete = false) // autoComplete = true as default value
  public void handleHumanTask(final ActivatedJob job, @Variable String a) {
    logJob(job, a);
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("name", "从Zeebe触发的任务");
    requestBody.put("checkRule", "percentage");
    requestBody.put("threshold", "1");
    requestBody.put("allocator", "serial");
    requestBody.put("order", Arrays.asList("015754","011330","011114","012901"));
    requestBody.put("zeebeJobKey", job.getKey());
    requestBody.put("executors", Arrays.asList("011114","012901","015754","011330"));

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<Map<String, Object>> r = new HttpEntity<>(requestBody, requestHeaders);
    String result = restTemplate.postForObject(ENDPOINT_TASK_ENGINE, r, String.class);

    log.info(result);
  }

  private static void logJob(final ActivatedJob job, Object parameterValue) {
    log.info(
      "complete job\n>>> [type: {}, key: {}, element: {}, workflow instance: {}]\n{deadline; {}]\n[headers: {}]\n[variable parameter: {}\n[variables: {}]",
      job.getType(),
      job.getKey(),
      job.getElementId(),
      job.getProcessInstanceKey(),
      Instant.ofEpochMilli(job.getDeadline()),
      job.getCustomHeaders(),
      parameterValue,
      job.getVariables());
  }

}
