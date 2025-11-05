package com.ZhongHou.Ecommerce.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

    private final String bucketName="zhonghou-ecommerce";


    private final static String IMAGE_DIRECTORY_FRONTEND="D:/Ecommerce/product-images/";

    private final static Path IMAGE_DIRECTORY_BACKEND =
            Paths.get(System.getProperty("user.dir"), "product-images");

//    public String saveImageToS3(MultipartFile photo){
//        try {
//            String s3FileName=photo.getOriginalFilename();
//
//            //create an s3 client using default credentials provider chain
//            AmazonS3 s3Client= AmazonS3ClientBuilder.standard()
//                    .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
//                    .withRegion(Regions.US_EAST_2)
//                    .build();
//
//            //get input stream from photo
//            InputStream inputStream=photo.getInputStream();
//
//            //set metadata for the object
//            ObjectMetadata metadata=new ObjectMetadata();
//            metadata.setContentType("image/jpg");
//
//            //create a put request to upload image to s3
//            PutObjectRequest putObjectRequest=new PutObjectRequest(bucketName,s3FileName,inputStream,metadata);
//            s3Client.putObject(putObjectRequest);
//
//            return "https://" + bucketName + ".s3.us-east-2.amazonaws.com/" +s3FileName;
//        }catch (IOException e){
//            e.printStackTrace();
//            throw new RuntimeException("Unable to upload image to s3 bucket: "+ e.getMessage());
//        }
//    }

     public String saveImageToLocal(MultipartFile image) {
        
         if (image.isEmpty() || image.getContentType() == null || !image.getContentType().startsWith("image/")){
                 throw new IllegalArgumentException("Only image file save directory");
         }

         try{
             Files.createDirectories(IMAGE_DIRECTORY_BACKEND);

             String originalFilename = StringUtils.cleanPath(image.getOriginalFilename());
             String extension = "";

             int lastDot= originalFilename.lastIndexOf('.');
             if (lastDot >= 0){
                 extension = originalFilename.substring(lastDot).trim(); //.png || .jpg
             }

             String uniqueFileName = UUID.randomUUID().toString()+ extension;
             Path destinationFile = IMAGE_DIRECTORY_BACKEND.resolve(uniqueFileName);

             try (InputStream inputStream = image.getInputStream()){
                 Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
             }

             //return imagePath;
             return "/images/" + uniqueFileName;
             //return "http://localhost:8080/images/" + uniqueFileName;
         }catch (Exception e){
             e.printStackTrace();
             throw new IllegalArgumentException("Không thể lưu file: " + e.getMessage());
         }

        }

    // public String saveImageToLocal(MultipartFile image) {
    //     if (image == null || image.getContentType() == null || !image.getContentType().startsWith("image/")) {
    //         throw new IllegalArgumentException("Only image files are allowed");
    //     }
    
    //     String original = image.getOriginalFilename();
    //     String safeName = (original == null ? "image" : original).trim().replaceAll("\\s+", "-");
    //     String uniqueFileName = UUID.randomUUID() + "_" + safeName;
    
    //     File directory = new File(IMAGE_DIRECTORY_FRONTEND);
    //     if (!directory.exists()) {
    //         directory.mkdirs();
    //     }
    
    //     File destinationFile = new File(directory, uniqueFileName);
    //     try {
    //         image.transferTo(destinationFile);
    //     } catch (Exception e) {
    //         throw new IllegalArgumentException(e.getMessage());
    //     }
    
    //     // Trả về URL BE serve
    //      return "http://localhost:8080/images/" + uniqueFileName;
    //     // return "/images/" + uniqueFileName;
    // }
    
    }