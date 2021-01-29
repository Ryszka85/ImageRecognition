
package com.htlwienwest.imagerecognition.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

@RestController
public class ImageRecognitionController {

    @GetMapping(value = "test")
    public void deleteFromLibrary(@RequestBody MultipartFile multipartFile) throws IOException {
        // test server for imageRecognition
        byte[] bytes = multipartFile.getInputStream().readAllBytes();
    }
}


