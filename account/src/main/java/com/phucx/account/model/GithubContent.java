package com.phucx.account.model;

import lombok.Data;
import lombok.ToString;

@Data @ToString
public class GithubContent {
    private String message;
    private Committer committer;
    private String content;
}
