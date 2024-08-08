package org.marcho.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.marcho.dto.ExceptionResponseDto;
import org.marcho.dto.RepositoryDto;
import org.marcho.dto.UserRepos;
import org.marcho.repository.GitHubRepository;
import org.marcho.service.GitHubService;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api")
public class GitHubEndpoint {

    @Inject
    public GitHubService gitHubService;

    @Inject
    public GitHubRepository gitHubRepository;

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
        return gitHubService.getUsernameRepositories(username);
    }

    @GET
    @Path("/check/{username}")
    public List<UserRepos> getThoseVThreads(@PathParam("username") String username) throws ExecutionException {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            CompletionService<List<UserRepos>> completionService = new ExecutorCompletionService<>(executor);
            List<Future<List<UserRepos>>> futures = new ArrayList<>();
            for (int i = 0; i < 10; ++i) {
                futures.add(completionService.submit(() -> {
                    return gitHubRepository.getRepoWithVthread(username);
                }));
            }
            List<UserRepos> result = new ArrayList<>();
            for (Future<List<UserRepos>> future : futures) {
                result.addAll(future.get());
            }
            return result;
        } catch (InterruptedException e) {
            // handle the exception, e.g. log an error message
            System.err.println("Something went wrong");
            // return a default value, or re-throw the exception if needed
            return null;
        }
    }
}

// @Operation(summary = "Test", description = "Test endpoint")
// @APIResponse(responseCode = "200", description = "Test endpoint", content =
// @Content(mediaType = "text/plain"))
// @Tag(name = "Test endpoint")
// @GET
// @Path("/test/hello")
// public Response testResponse() {
// return Response.ok("Hello test").build();
// }
