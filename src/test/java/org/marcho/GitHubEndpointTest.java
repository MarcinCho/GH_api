// package org.marcho;

// import java.util.ArrayList;
// import java.util.List;

// import org.eclipse.microprofile.rest.client.inject.RestClient;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.marcho.controller.GitHubEndpoint;
// import org.marcho.dto.Branch;
// import org.marcho.dto.ExceptionResponseDto;
// import org.marcho.dto.Owner;
// import org.marcho.dto.RepositoryDto;
// import org.marcho.dto.UserRepos;
// import org.marcho.repository.GitHubApiClient;
// import org.mockito.Mock;
// import static org.mockito.Mockito.when;
// import org.mockito.junit.jupiter.MockitoExtension;

// import jakarta.inject.Inject;
// import jakarta.ws.rs.core.Response;

// @ExtendWith(MockitoExtension.class)
// public class GitHubEndpointTest {

// @RestClient
// @Inject
// @Mock
// public GitHubApiClient gitHubApiClientMock;

// public GitHubEndpoint gitHubEndpoint;

// @BeforeEach
// public void setUp() {
// gitHubEndpoint = new GitHubEndpoint();
// gitHubEndpoint.gitHubApiClient = gitHubApiClientMock;
// }

// @Test
// public void testGetResponse_Success() throws Exception {
// String username = "marcincho";
// List<UserRepos> userRepos = createMockUserRepos(username);
// List<RepositoryDto> expectedResponse = createMockResponseDtos(userRepos);

// when(gitHubApiClientMock.getReposByUsername(username)).thenReturn(userRepos);

// Response response = gitHubEndpoint.getResponse(username);

// assertEquals(200, response.getStatus());
// assertEquals(expectedResponse, response.getEntity());
// }

// @Test
// public void testGetResponse_UnexpectedException() throws Exception {
// String username = "marcincho";
// Exception exception = new RuntimeException("Unexpected error");

// when(gitHubApiClientMock.getReposByUsername(username)).thenThrow(exception);

// Response response = gitHubEndpoint.getResponse(username);

// assertEquals(500, response.getStatus());
// ExceptionResponseDto entity = (ExceptionResponseDto) response.getEntity();
// assertEquals("500", entity.status());
// assertEquals("Something went wrong, please try again", entity.message());
// }

// @Test
// public void testTestResponse() {
// Response response = gitHubEndpoint.testResponse();

// assertEquals(200, response.getStatus());
// assertEquals("Hello test", response.getEntity());
// }

// private List<UserRepos> createMockUserRepos(String username) {
// List<UserRepos> userRepos = new ArrayList<>();
// UserRepos userRepo = new UserRepos(username, false, new Owner(username));
// userRepos.add(userRepo);
// return userRepos;
// }

// private List<RepositoryDto> createMockResponseDtos(List<UserRepos> userRepos)
// {
// List<RepositoryDto> responseDtos = new ArrayList<>();
// for (UserRepos userRepo : userRepos) {
// List<Branch> branches = new ArrayList<>();
// RepositoryDto responseDto = new RepositoryDto(userRepo.name,
// userRepo.owner.login(), branches);
// responseDtos.add(responseDto);
// }
// return responseDtos;
// }

// }