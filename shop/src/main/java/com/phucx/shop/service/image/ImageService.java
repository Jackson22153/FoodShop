package com.phucx.shop.service.image;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.phucx.shop.exceptions.NotFoundException;

public interface ImageService {

    // get image
    public byte[] getImage(String imageName, String imageLocation) throws IOException;
    // upload image
    public String uploadImage(MultipartFile file, String imageLocation) throws IOException, NotFoundException;
    // get mimetype for response
    public String getMimeType(String file, String imageLocation) throws IOException;

    public String getUri(String requestUri);
    public String getCurrentUrl(String requestUri, Integer serverPort, String imageUri);
    public String getImageName(String url);
}
