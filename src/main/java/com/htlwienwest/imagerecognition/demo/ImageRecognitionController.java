
package com.htlwienwest.imagerecognition.demo;

import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;
import com.amazonaws.util.IOUtils;

@RestController
public class ImageRecognitionController
{
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.parse("15MB"));
        factory.setMaxRequestSize(DataSize.parse("15MB"));
        return factory.createMultipartConfig();
    }


    @GetMapping(value = "test")
    public String test()
    {
        return "testiger test";
    }

    @PostMapping(path= "getlabels")
    public String detectLabels(@ModelAttribute MPFLabel img) throws IOException
    {
        //convert image in mpf to bytearray/bytebuffer
        ByteBuffer imageBytes= ByteBuffer.wrap(img.getImg().getInputStream().readAllBytes());

        ArrayList<String> responseLabels= new ArrayList<>();

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        //build AWS label-request
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(5)
                .withMinConfidence(75F);

        try
        {
            DetectLabelsResult result = rekognitionClient.detectLabels(request);
            List <Label> labels = result.getLabels();

            for (Label label: labels)
            {
                responseLabels.add(label.getName());
            }
        }
        catch (AmazonRekognitionException e)
        {
            e.printStackTrace();
        }

        return responseLabels.get(0);
    }
}

class MPFLabel implements Serializable
{
    private MultipartFile img;

    public MPFLabel() {}

    public MultipartFile getImg() { return img; }

    public void setImg(MultipartFile img) { this.img = img; }
}