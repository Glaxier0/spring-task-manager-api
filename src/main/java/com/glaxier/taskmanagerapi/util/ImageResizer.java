package com.glaxier.taskmanagerapi.util;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
public class ImageResizer {

    @Value("${image.width}")
    private int IMAGE_WIDTH;
    @Value("${image.height}")
    private int IMAGE_HEIGHT;

    public String resizeImage(MultipartFile avatar) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Thumbnails.of(avatar.getInputStream()).size(IMAGE_WIDTH, IMAGE_HEIGHT)
                .outputFormat("JPEG")
                .outputQuality(1)
                .toOutputStream(outputStream);

        byte[] imageByteArray = Base64.encodeBase64(outputStream.toByteArray());

        return new String(imageByteArray);
    }
}
