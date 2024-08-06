package org.marcho.dto;

import java.util.List;

public class RepositoryDto {

    public String repositoryName;
    public String ownerLogin;
    public List<Branch> branches;

    public RepositoryDto(String repositoryName, String ownerLogin, List<Branch> branches) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerLogin;
        this.branches = branches;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((repositoryName == null) ? 0 : repositoryName.hashCode());
        result = prime * result + ((ownerLogin == null) ? 0 : ownerLogin.hashCode());
        result = prime * result + ((branches == null) ? 0 : branches.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RepositoryDto other = (RepositoryDto) obj;
        if (repositoryName == null) {
            if (other.repositoryName != null)
                return false;
        } else if (!repositoryName.equals(other.repositoryName))
            return false;
        if (ownerLogin == null) {
            if (other.ownerLogin != null)
                return false;
        } else if (!ownerLogin.equals(other.ownerLogin))
            return false;
        if (branches == null) {
            if (other.branches != null)
                return false;
        } else if (!branches.equals(other.branches))
            return false;
        return true;
    }

}
