package org.marcho;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.marcho.dto.Branch;
import org.marcho.dto.UserRepos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@RegisterRestClient(baseUri = "https://api.github.com/")
public interface GitHubApiClient {

        @GET
        @Path("/users/{username}/repos")
        @Operation(summary = "Get User Repositories", description = "Given username, retrieves user repositories from github.com.")
        List<UserRepos> getReposByUsername(@PathParam("username") String username);

        @GET
        @Path("/repos/{username}/{repository}/branches")
        @Operation(summary = "Get Repository Branches", description = "Given username and repository, retrieves repository branches and last sha")
        List<Branch> getRepoBranches(@PathParam("username") String username,
                        @PathParam("repository") String repository);

}
