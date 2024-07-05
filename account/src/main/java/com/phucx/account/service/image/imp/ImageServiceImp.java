package com.phucx.account.service.image.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.phucx.account.exception.NotFoundException;
import com.phucx.account.model.UserProfile;
import com.phucx.account.service.image.ImageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImp implements ImageService{

    @Override
    public byte[] getImage(String imageName, String imageStoredLocation) throws IOException {
        log.info("getImage({})", imageName);
        String filePath = imageStoredLocation;
        if(filePath.charAt(filePath.length()-1)!='/') filePath+='/';
        Path path = Paths.get(filePath+imageName);
        return Files.readAllBytes(path);
    }

    @Override
    public String uploadImage(MultipartFile file, String imageStoredLocation) throws IOException {
        log.info("uploadImage({})", file.getOriginalFilename());
        if(file.isEmpty()) throw new NotFoundException("Image does not found");
        // set filename
        String filename = file.getOriginalFilename();
        int dotIndex = filename.lastIndexOf(".");
        String extension = filename.substring(dotIndex+1);
        String randomName = UUID.randomUUID().toString();
        String newFile = randomName + "." + extension;
        // set stored location
        Path targetPath = Path.of(imageStoredLocation, newFile);
        // copy image to stored location
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return newFile;
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort, String imageUri) {
        String host = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String url = host + ":" + serverPort + imageUri;
        return url;
    }

    @Override
    public String getMimeType(String imageName, String imageStoredLocation) throws IOException {
        log.info("getMimeType({})", imageName);
        String storedLocation = imageStoredLocation;
        if(storedLocation.charAt(storedLocation.length()-1)!='/') storedLocation+='/';
        Path path = Paths.get(storedLocation + imageName);
        String mimeType = Files.probeContentType(path);
        return mimeType;
    }


    @Override
    public String getImageName(String url) {
        log.info("getImageName({})", url);
        if(url==null) return null;
        int indexOfSlash = url.lastIndexOf("/");
        String filename = url.substring(indexOfSlash + 1);
        return filename;
    }

    @Override
    public UserProfile setUserProfileImage(UserProfile userProfile, String serverName, String imageUri) {
        log.info("setUserProfileImage({})", userProfile);
        // filtering customer 
        if(!(userProfile.getPicture()!=null && userProfile.getPicture().length()>0)) return userProfile;
        // userProfile has image
        String imageUrl = "/" + serverName + imageUri;
        userProfile.setPicture(imageUrl + "/" + userProfile.getPicture());
        return userProfile;

    }

    @Override
    public List<UserProfile> setUserProfileImage(List<UserProfile> userProfiles, String serverName, String imageUri) {
        log.info("setUserProfileImage({})", userProfiles);
        userProfiles.stream().forEach(user ->{
            if(user.getPicture()!=null && user.getPicture().length()>0){
                // setting image with image uri
                String uri = "/" + serverName + imageUri;
                user.setPicture(uri + "/" + user.getPicture());
            }
        });
        return userProfiles;
    }
}
