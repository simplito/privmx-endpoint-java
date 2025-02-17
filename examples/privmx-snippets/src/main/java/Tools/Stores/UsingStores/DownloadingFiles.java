package Tools.Stores.UsingStores;

import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStream;
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStreamReader;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class DownloadingFiles extends ManagingStores {
    void downloadingFiles() {
        String fileId = "FILE_ID";
        StoreFileStream.Controller controller = new StoreFileStream.Controller() {
            @Override
            public void onChunkProcessed(Long processedBytes) {
                System.out.println("Downloaded bytes: " + processedBytes);
            }
        };

        try (OutputStream outputStream = new FileOutputStream("PATH_TO_FILE")) {
            StoreFileStreamReader.openFile(
                    endpointSession.storeApi,
                    fileId,
                    outputStream,
                    controller
            );
        } catch (Exception e) {
            System.out.println("Cannot download file: " + e.getMessage());
        }
    }
}
