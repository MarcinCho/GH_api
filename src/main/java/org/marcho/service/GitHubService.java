
package org.marcho.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.marcho.dto.Branch;
import org.marcho.dto.ExceptionResponseDto;
import org.marcho.dto.RepositoryDto;
import org.marcho.dto.UserRepos;
import org.marcho.repository.IGitHubRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class GitHubService {

    @RestClient
    @Inject
    public IGitHubRepository gitHubApiClient;

    public Response getUsernameRepositories(String username) {
        try {
            List<UserRepos> userRepos = gitHubApiClient.getReposByUsername(username);
            if (userRepos.isEmpty()) {
                return Response.status(404)
                        .entity(new ExceptionResponseDto("404", "User " + username + " has 0 public repositories"))
                        .build();
            }
            List<RepositoryDto> responseDtos = new ArrayList<>();
            userRepos.stream().filter(userRepos -> !userRepos.fork());
            for (UserRepos repo : userRepos) {
                if (!repo.fork()) {
                    List<Branch> repoBranches = gitHubApiClient.getRepoBranches(username, repo.name());
                    RepositoryDto responseDto = new RepositoryDto(repo.name(), repo.owner().login(), repoBranches);
                    responseDtos.add(responseDto);
                }
            }
            return Response.ok(responseDtos).build();
        } catch (Exception exception) {
            if (exception.getMessage().contains("404")) {
                return Response.status(404)
                        .entity(new ExceptionResponseDto("404", "Username not found with GitHub API.")).build();
            } else if (exception.getMessage().contains("API rate limit exceeded for")) {
                return Response.status(403)
                        .entity(new ExceptionResponseDto("403",
                                "API rate limit esceeded please chceck the README.md for solution."))
                        .build();
            }
        }
        return Response.serverError().entity(new ExceptionResponseDto("500", "Something went wrong, please try again"))
                .build();
    }
}
