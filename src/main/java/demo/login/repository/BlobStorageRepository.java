package demo.login.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class BlobStorageRepository {

    @Value("${blobstorage.account.name}")
    private String storageAccountName;

    @Value("${blobstorage.account.key}")
    private String storageAccountKey;

    public String uploadFile(String containerName, MultipartFile file) throws InvalidKeyException, IOException {
        if (file == null) {
            return null;
        }

        String filename = file.getOriginalFilename();
        byte[] fileBytes = file.getBytes();
        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(this.storageAccountName,
                this.storageAccountKey);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://" + this.storageAccountName + ".blob.core.windows.net").credential(credential)
                .buildClient();
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = blobContainerClient.getBlobClient(filename);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(fileBytes);
        blobClient.upload(dataStream, fileBytes.length, true);
        return blobClient.getBlobUrl();
    }

}
