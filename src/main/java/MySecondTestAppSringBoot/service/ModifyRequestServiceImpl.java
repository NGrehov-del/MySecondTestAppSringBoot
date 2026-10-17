package MySecondTestAppSringBoot.service;

import MySecondTestAppSringBoot.model.Request;
import MySecondTestAppSringBoot.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ModifyRequestServiceImpl implements ModifyRequestService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void modify(Request request) {
        request.setSource("Service 1");

        log.info("Отправляем запрос из Сервиса 1 в Сервис 2: {}", request);

        HttpEntity<Request> requestEntity = new HttpEntity<>(request);

        ResponseEntity<Response> response = restTemplate.exchange(
                "http://localhost:8084/feedback",
                HttpMethod.POST,
                requestEntity,
                Response.class
        );

        log.info("Получен ответ от Сервиса 2: {}", response.getBody());
    }
}
