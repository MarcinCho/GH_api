package org.marcho.dto;

public class UserRepos {

    public String name;
    public boolean fork;
    public Owner owner;

    public UserRepos(String name, boolean fork, Owner owner) {
        this.name = name;
        this.fork = fork;
        this.owner = owner;
    }

}
