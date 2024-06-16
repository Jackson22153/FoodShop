package com.phucx.shop.service.image.imp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.phucx.shop.service.image.ImageService;

import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ImageServiceImp implements ImageService{
    @Override
    public String uploadImage(MultipartFile file, String imageLocation) throws IOException {
        log.info("uploadImage(file={}, imageLocation={})", file.getOriginalFilename(), imageLocation);
        if(file.isEmpty()) throw new NotFoundException("Image does not found");
        // set filename
        String filename = file.getOriginalFilename();
        int dotIndex = filename.lastIndexOf(".");
        String extension = filename.substring(dotIndex+1);
        String randomName = UUID.randomUUID().toString();
        String newFile = randomName + "." + extension;
        // set stored location
        Path targetPath = Path.of(imageLocation, newFile);
        // copy image to stored location
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return newFile;
    }

    @Override
    public String getCurrentUrl(String requestUri, Integer serverPort) {
        String host = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        String imageUri = this.getUri(requestUri);
        String url = host + ":" + serverPort + imageUri;
        return url;
    }

    @Override
    public String getUri(String requestUri){
        int slashIndex = requestUri.lastIndexOf("/");
        String imageUri = requestUri.substring(0, slashIndex);
        log.info("image uri: {}", imageUri);
        return imageUri;
    }

    @Override
    public String getMimeType(String imageName, String imageLocation) throws IOException {
        log.info("getMimeType(imageName={}, imageLocation={})", imageName, imageLocation);
        String storedLocation = imageLocation;
        if(storedLocation.charAt(storedLocation.length()-1)!='/') storedLocation+='/';
        Path path = Paths.get(storedLocation + imageName);
        String mimeType = Files.probeContentType(path);
        return mimeType;
    }

    @Override
    public String getImageName(String url) {
        if(url==null || url.length()==0) return null;
        int indexOfSlash = url.lastIndexOf("/");
        String filename = url.substring(indexOfSlash + 1);
        log.info("filename: {}", filename);
        return filename;
    }

    @Override
    public byte[] getImage(String imageName, String imageLocation) throws IOException {
        log.info("getImage({})", imageName);
        String filePath = imageLocation;
        if(filePath.charAt(filePath.length()-1)!='/') filePath+='/';
        Path path = Paths.get(filePath+imageName);
        return Files.readAllBytes(path);
    }
}
