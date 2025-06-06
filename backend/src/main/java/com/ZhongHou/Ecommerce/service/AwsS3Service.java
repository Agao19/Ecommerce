package com.ZhongHou.Ecommerce.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class AwsS3Service {

    private final String bucketName="zhonghou-ecommerce";

    public String saveImageToS3(MultipartFile photo){
        try {
            String s3FileName=photo.getOriginalFilename();

            //create an s3 client using default credentials provider chain
            AmazonS3 s3Client= AmazonS3ClientBuilder.standard()
                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                    .withRegion(Regions.US_EAST_2)
                    .build();

            //get input stream from photo
            InputStream inputStream=photo.getInputStream();

            //set metadata for the object
            ObjectMetadata metadata=new ObjectMetadata();
            metadata.setContentType("image/jpg");

            //create a put request to upload image to s3
            PutObjectRequest putObjectRequest=new PutObjectRequest(bucketName,s3FileName,inputStream,metadata);
            s3Client.putObject(putObjectRequest);

            return "https://" + bucketName + ".s3.us-east-2.amazonaws.com/" +s3FileName;
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException("Unable to upload image to s3 bucket: "+ e.getMessage());
        }
    }
}