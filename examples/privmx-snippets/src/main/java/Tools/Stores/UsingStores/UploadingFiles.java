package Tools.Stores.UsingStores;

import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStream;
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStreamWriter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class UploadingFiles extends ManagingStores{
    void uploadingSmallFiles(){
        String storeID = "STORE_ID";
        byte[] publicMeta = "File public meta".getBytes(StandardCharsets.UTF_8);
        byte[] privateMeta = "File private meta".getBytes(StandardCharsets.UTF_8);
        byte[] fileContent = "Text file content".getBytes(StandardCharsets.UTF_8);

        try {
            Long fileHandle = endpointSession.storeApi.createFile(
                    storeID,
                    publicMeta,
                    privateMeta,
                    fileContent.length
            );
            endpointSession.storeApi.writeToFile(fileHandle,fileContent);
            endpointSession.storeApi.closeFile(fileHandle);
        }catch (Exception e){
            System.out.println("Cannot upload file: " + e.getMessage());
        }
    }

    void uploadingFilesStream(){
        String storeID = "STORE_ID";
        byte[] publicMeta = "File public meta".getBytes(StandardCharsets.UTF_8);
        byte[] privateMeta = "File private meta".getBytes(StandardCharsets.UTF_8);
        StoreFileStream.Controller controller = new StoreFileStream.Controller(){
            @Override
            public void onChunkProcessed(Long processedBytes) {
                System.out.println("Uploaded bytes: " + processedBytes);
            }
        };

        try(InputStream inputStream = new FileInputStream("PATH_TO_FILE")) {
            String fileId = StoreFileStreamWriter.createFile(
                    endpointSession.storeApi,
                    storeID,
                    publicMeta,
                    privateMeta,
                    inputStream.available(),
                    inputStream,
                    controller
            );
        }catch (Exception e){
            System.out.println("Cannot upload file: " + e.getMessage());
        }
    }
}
