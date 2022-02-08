package org.example;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class Communication {
    private final RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private final String URL = "http://91.241.64.178:7081/api/users";

    @Autowired
    public Communication(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
        httpHeaders.set("Cookie",
                String.join(";", Objects.requireNonNull(restTemplate.headForHeaders(URL).get("Set-Cookie"))));
    }

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        });
        List<User> allUsers = responseEntity.getBody();
        return allUsers;
    }

    public void saveUser(User user) {
        Long id = user.getId();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity httpEntity = new HttpEntity(user, httpHeaders);
        if (getAllUsers().stream().noneMatch(user1 -> user1.getId() == id)) {
            user.setId((long) getAllUsers().size());
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, httpEntity, String.class);
            System.out.println("User is created");
            System.out.println(responseEntity);
        } else {
            restTemplate.put(URL, httpEntity);
        }
    }

    public void deleteUser(Long id) {
        restTemplate.delete(URL + "/" + id);
    }
}
