package com.phucx.account.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.phucx.account.model.ImageFormat;
import com.phucx.account.model.User;
import com.phucx.account.model.UserInfo;
import com.phucx.account.service.image.ImageService;
import com.phucx.account.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ImageService imageService;

    @GetMapping("/userInfo")
    public ResponseEntity<UserInfo> getUserInfo(Authentication authentication){
        UserInfo user = new UserInfo();
        user.setUser(new User());
        if(authentication!=null){
            user = userService.getUserInfo(authentication.getName());
        }
        return ResponseEntity.ok().body(user);
    }

    // set image
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageFormat> uploadImage(
        @RequestBody MultipartFile file,
        @RequestHeader(name = "X-Forwarded-Uri", required = false) String requestUri,
        @RequestHeader(name = "X-Server-Port", required = false) Integer serverPort
    ) throws IOException {

        String filename = imageService.uploadImage(file);
        String imageUrl = imageService.getCurrentUrl(requestUri, serverPort) + "/" + filename;
        log.info("imageUrl: {}", imageUrl);
        ImageFormat imageFormat = new ImageFormat(imageUrl);
        return ResponseEntity.ok().body(imageFormat);
    }

    @GetMapping("/image/{imagename}")
    public ResponseEntity<byte[]> getImage(@PathVariable String imagename) throws IOException {
        byte[] image = imageService.getImage(imagename);
        String mimeType = imageService.getMimeType(imagename);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(image);
    }
}
