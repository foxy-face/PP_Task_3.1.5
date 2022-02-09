package org.example;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class Communication {
    private static HttpHeaders httpHeaders;
    private final RestTemplate restTemplate;
    private static List<User> allUsers;
    private final String URL = "http://91.241.64.178:7081/api/users";

    @Autowired
    public Communication(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        });
        String cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", cookie);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        allUsers = responseEntity.getBody();
        System.out.println(responseEntity);
        System.out.println("Все пользователи");
        return allUsers;
    }

    public void saveUser(User user) {
        Long id = user.getId();
        if (id == null || allUsers.size()<id) {
            user.setId((long) allUsers.size() + 1);
            HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST, httpEntity, String.class);
            System.out.println(responseEntity);
            System.out.println("User is created");
        } else {
            HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.PUT, httpEntity, String.class);
            System.out.println(responseEntity);
            System.out.println("User is updated");
        }
    }

    public void deleteUser(Long id) {
        HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, entity, String.class);
        System.out.println(responseEntity);
        System.out.println("User is deleted");
    }
}
