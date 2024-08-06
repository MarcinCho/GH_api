package org.marcho;

import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.marcho.dto.Branch;
import org.marcho.dto.UserRepos;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

import java.util.List;

@RegisterRestClient(baseUri = "https://api.github.com/")
// @ClientHeaderParam(name = "a", value = "b ")
public interface GitHubApiClient {

        @GET
        @Path("/users/{username}/repos")
        List<UserRepos> getReposByUsername(@PathParam("username") String username);

        @GET
        @Path("/repos/{username}/{repository}/branches")
        List<Branch> getRepoBranches(@PathParam("username") String username,
                        @PathParam("repository") String repository);

}
