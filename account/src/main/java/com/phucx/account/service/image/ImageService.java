package com.phucx.account.service.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.model.UserProfile;


public interface ImageService {
    public byte[] getImage(String imageName, String imageStoredLocation) throws IOException;
    public String uploadImage(MultipartFile file, String imageStoredLocation) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort, String imageUri);
    public String getMimeType(String imageName, String imageStoredLocation) throws IOException;
    public String getImageName(String url);
    // set customer image
    public UserProfile setUserProfileImage(UserProfile userProfile, String serverName, String imageUri);
    public List<UserProfile> setUserProfileImage(List<UserProfile> userProfiles, String serverName, String imageUri); 
}
