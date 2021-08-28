package com.rosewine.awsimageupload.filestore;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * uses package com.rosewine.awsimageupload.config; to get the S3 client -
 * to facilitate in storing LOCAL images to our selected S3 BUCKET.
 * NOTE:
 * 1. We may theoretically have several S3 buckets. In this course - there is only one, it is dedicated to storing images.
 * 2. Each S3 bucket may have many paths/folders - like a directory. I think that we only use a root-path - or somesuch - in this course, and the bucket contains only employee image-files.
 */
@Service    // indicates that this is the 2nd tier - the Service/business-logic tier - in our backend.
public class FileStore {

    private final AmazonS3 s3;  // Handle to our AWS S3 bucket, where our images are stored.

    @Autowired  // Indicates to Spring that this is using dependency injection.
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(
            String path, // S3 bucket-name, plus: any path we may - or nay not have
            String fileName,
            Optional<Map<String, String>> optionalMetadata,  // Metadata: content-length, content-type, & so forth
            InputStream inputStream // content/image that gets saves to actual S3 bucket
    ) {
        ObjectMetadata metadata = new ObjectMetadata();

        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                //map.forEach((key, value) -> metadata.addUserMetadata(key, value));
                map.forEach(metadata::addUserMetadata);   // Using "method reference" in lieu to the above: looks neater.
            }
        });

        try {
            s3.putObject(path, fileName, inputStream, metadata);    // Store the file
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    public byte[] download(String path, String key) {
        try {
            S3Object object = s3.getObject(path, key);
            S3ObjectInputStream inputStream = object.getObjectContent();
            return IOUtils.toByteArray(inputStream);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to download file from s3", e);
        }
    }
}
