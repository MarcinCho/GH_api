package org.marcho;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class GitHubEndpoint {

    @RestClient
    @Inject
    GitHubApiClient gitHubApiClient;

    @GET
    @Path("/{username}")
    @Produces("application/json")
    @Tag(name = "User repositories", description = "Get method for reciving user repos")
    @Operation(summary = "Get repositories of a user", description = "Returns a list of repositories of a given user")
    @APIResponse(responseCode = "200", description = "Successful response", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = RepositoryDto.class)))
    @APIResponse(responseCode = "404", description = "Username not found", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExceptionResponseDto.class)))
    @APIResponse(responseCode = "403", description = "API rate limit exceeded", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExceptionResponseDto.class)))
    @APIResponse(responseCode = "500", description = "Other errors", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = ExceptionResponseDto.class)))
    public Response getResponse(@Parameter(description = "Github username") @PathParam("username") String username) {
        try {
            List<UserRepos> userRepos = gitHubApiClient.getReposByUsername(username);
            if (userRepos.isEmpty()) {
                return Response.status(404)
                        .entity(new ExceptionResponseDto("404", "User " + username + " has 0 public repositories"))
                        .build();
            }
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

    @Operation(summary = "Test", description = "Test endpoint")
    @APIResponse(responseCode = "200", description = "Test endpoint", content = @Content(mediaType = "text/plain"))
    @Tag(name = "Test endpoint")
    @GET
    @Path("/test/hello")
    public Response testResponse() {
        return Response.ok("Hello test").build();
    }
}
