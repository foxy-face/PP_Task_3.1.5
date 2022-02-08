package org.example;

import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Objects;

@Component
public class Communication {
    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final String URL = "http://91.241.64.178:7081/api/users";
    private String cookie;

    @Autowired
    public Communication(RestTemplate restTemplate, HttpHeaders httpHeaders) {
        this.restTemplate = restTemplate;
        this.httpHeaders = httpHeaders;
    }

    public List<User> getAllUsers() {
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>() {
                        });
        cookie = responseEntity.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        httpHeaders.add("Cookie", cookie);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<User> allUsers = responseEntity.getBody();
        System.out.println(responseEntity);
        System.out.println(cookie);
        return allUsers;
    }

    public void saveUser(User user) {
        Long id = user.getId();
        HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
        if (getAllUsers().stream().noneMatch(user1 -> user1.getId().equals(id))) {
            user.setId((long) getAllUsers().size());
            HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, httpEntity, String.class);
            System.out.println("User is created");
            System.out.println(responseEntity);
        } else {
            HttpEntity<User> httpEntity = new HttpEntity<>(user, httpHeaders);
            restTemplate.put(URL, httpEntity);
        }
    }

    public void deleteUser(Long id) {
//        httpHeaders.set("cookie", cookie);
        HttpEntity<User> httpEntity = new HttpEntity<>(httpHeaders);
//        restTemplate.exchange(URL + "/" + id, HttpMethod.DELETE, httpHeaders);
    }
}
