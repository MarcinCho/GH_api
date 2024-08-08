package org.marcho.repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import org.marcho.dto.UserRepos;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GitHubRepository {

    public HttpClient httpClient;

    public List<UserRepos> getRepoWithVthread(String username) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            HttpResponse<String> resp = httpClient.send(HttpRequest.newBuilder()
                    .uri(URI.create("https://api.github.com/users/" + username + "/repos")).GET().build(),
                    HttpResponse.BodyHandlers.ofString());
            List<UserRepos> userRepos = Arrays.asList(mapper.readValue(resp.body(), UserRepos[].class));
            return userRepos;
        } catch (Exception e) {
            System.err.println("Something went wrong");
            return null;
        }
    }
}
