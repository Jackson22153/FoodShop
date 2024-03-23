package com.phucx.account.service.github;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.phucx.account.model.Committer;
import com.phucx.account.model.GithubContent;

@Service
public class GithubServiceImp implements GithubService{
    @Value("${githubToken}")
    private String token;
    @Value("${repository}")
    private String repository;
    @Value("${repositoryOwner}")
    private String repositoryOwner;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String uploadImage(String pictureBase64) {
        try {
            String picture = pictureBase64.split(";base64,")[1];
    
            String[] parts = pictureBase64.split(";")[0].split("/");
            String extension = parts[1]; // Use this to get the extension
    
            String url = githubApi(extension);
            // header
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "token " + token);
            headers.set("X-GitHub-Api-Version", "2022-11-28");
            // body
            Committer committer = new Committer();
            committer.setName("FoodShopImages");
            committer.setEmail("jackson22153@gmail.com");
            GithubContent content = new GithubContent();
            content.setMessage("imageUpload");
            content.setCommitter(committer);
            content.setContent(picture);
    
            HttpEntity<GithubContent> entity = new HttpEntity<>(content, headers);
    
            @SuppressWarnings("null")
            var response = restTemplate.exchange(URI.create(url), HttpMethod.PUT, entity, Object.class);
            Map<String, Object> responseData = (Map<String, Object>) response.getBody();
            Map<String, Object> responseContent = (Map<String, Object>) responseData.get("content");
            return responseContent.get("download_url").toString();
        } catch (Exception e) {
            return null;
        }
    }
    
    private String githubApi(String extension){
        String fileName = UUID.randomUUID().toString();
        String file = fileName+"." + extension;
        String url = "https://api.github.com/repos/" + repositoryOwner + "/" + repository + "/contents" + "/"  + file;
        return url;
    }
}
