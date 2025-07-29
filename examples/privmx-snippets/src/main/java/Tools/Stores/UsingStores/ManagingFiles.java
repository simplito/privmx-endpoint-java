package Tools.Stores.UsingStores;

import com.simplito.java.privmx_endpoint.model.File;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint_extra.model.SortOrder;

public class ManagingFiles extends ManagingStores{

    void listingFiles() {
        String storeID = "STORE_ID";
        long limit = 30L;
        long skip = 0L;

        PagingList<File> files = endpointSession.storeApi.listFiles(
                storeID,
                skip,
                limit,
                SortOrder.DESC
        );
    }

    void deletingFiles() {
        String fileID = "FILE_ID";
        endpointSession.storeApi.deleteFile(fileID);
    }
}
