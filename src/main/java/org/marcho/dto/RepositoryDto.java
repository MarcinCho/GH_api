package org.marcho.dto;

import java.util.List;

public record RepositoryDto(String repositoryName, String ownerLogin, List<Branch> branches) {
}
