package com.phucx.account.service.image;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;


public interface ImageService {
    public byte[] getImage(String imageName, String imageStoredLocation) throws IOException;
    public String uploadImage(MultipartFile file, String imageStoredLocation) throws IOException;
    public String getCurrentUrl(String requestUri, Integer serverPort, String imageUri);
    public String getMimeType(String imageName, String imageStoredLocation) throws IOException;
    public String getImageName(String url);
}
