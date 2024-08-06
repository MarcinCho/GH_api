package org.marcho;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.marcho.dto.Branch;
import org.marcho.dto.ExceptionResponseDto;
import org.marcho.dto.RepositoryDto;
import org.marcho.dto.UserRepos;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.ArrayList;

@Path("/api")
public class GitHubEndpoint {

    @RestClient
    @Inject
    GitHubApiClient gitHubApiClient;

    @GET
    @Path("/{username}")
    @Produces("application/json")
    public Response getResponse(@PathParam("username") String username) {
        try {
            List<UserRepos> userRepos = gitHubApiClient.getReposByUsername(username);
            List<RepositoryDto> responseDtos = new ArrayList<>();
            for (UserRepos repo : userRepos) {
                if (!repo.fork) {
                    List<Branch> repoBranches = gitHubApiClient.getRepoBranches(username, repo.name);
                    RepositoryDto responseDto = new RepositoryDto(repo.name, repo.owner.login(), repoBranches);
                    responseDtos.add(responseDto);
                }
            }
            return Response.ok(responseDtos).build();
        } catch (Exception exception) {
            if (exception.getMessage().contains("404")) {
                return Response.status(404)
                        .entity(new ExceptionResponseDto("404", "Username not found with GitHub API.")).build();
            }
        }
        return Response.serverError().entity(new ExceptionResponseDto("500", "Something went wrong, please try again"))
                .build();
    }

    @GET
    @Path("/test/hello")
    public Response testResponse() {
        return Response.ok("Hello test").build();
    }
}
