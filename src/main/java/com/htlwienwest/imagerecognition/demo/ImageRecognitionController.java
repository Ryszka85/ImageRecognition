
package com.htlwienwest.imagerecognition.demo;

import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;

import java.nio.ByteBuffer;
import java.util.List;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.AmazonRekognitionException;
import com.amazonaws.services.rekognition.model.DetectLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.Label;

@CrossOrigin(origins = {"http://82.218.232.122:4200", "http://localhost:4200"}, methods = {RequestMethod.POST, RequestMethod.GET})

@RestController
public class ImageRecognitionController
{

    @PostMapping(path= "getlabels")
    public List<String> detectLabels(@RequestBody BArequest img)
    {
        //convert image in mpf to bytearray/bytebuffer
        ByteBuffer imageBytes= ByteBuffer.wrap(img.getImg());

        List<String> responseLabels= new ArrayList<>();

        AmazonRekognition rekognitionClient = AmazonRekognitionClientBuilder.defaultClient();

        //build AWS label-request
        DetectLabelsRequest request = new DetectLabelsRequest()
                .withImage(new Image()
                        .withBytes(imageBytes))
                .withMaxLabels(8)
                .withMinConfidence(70F);

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

        return responseLabels;
    }
}



class BArequest implements Serializable
{
    private byte[] img;

    public BArequest() {}

    public byte[] getImg() { return img; }

    public void setImg(byte[] img) { this.img = img; }
}