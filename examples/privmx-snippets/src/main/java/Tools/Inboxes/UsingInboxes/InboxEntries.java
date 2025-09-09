package Tools.Inboxes.UsingInboxes;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.simplito.java.privmx_endpoint.model.File;
import com.simplito.java.privmx_endpoint.model.InboxEntry;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint_extra.inboxFileStream.InboxFileStreamWriter;
import com.simplito.java.privmx_endpoint_extra.model.SortOrder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class InboxEntries extends WorkingWithInboxes {
    void sendingInboxEntryBasic() {
        // 1. Preparing Entry
        String inboxID = "INBOX_ID";

        String entryDataAnswer = "USER_PROVIDED_TEXT";
        long entryDataVersion = 1L;
        String entryDataType = "TEXT_ANSWER";

        byte[] entryData = """
                 {
                    "content": {
                        "answer": "%s"
                    },
                    "version": %d,
                    "type": "%s"
                 }
                """.formatted(entryDataAnswer, entryDataVersion, entryDataType).getBytes();

        long entryHandle = endpointSession.inboxApi.prepareEntry(
                inboxID,
                entryData,
                Collections.emptyList()
        );

        // 2. Sending Entry
        endpointSession.inboxApi.sendEntry(entryHandle);
    }

    void attachingFiles(String fileID) throws IOException {
        String inboxID = "INBOX_ID";
        byte[] publicMeta = "My public data".getBytes();
        byte[] entryData = "ENTRY_DATA".getBytes();

        String fileName = "FILE_NAME";
        String fileType = "FILE_TYPE";
        byte[] fileContent = "FILE_CONTENT".getBytes();
        long fileSize = fileContent.length;

        byte[] filePrivateMeta = """
                {
                    "name": "%s",
                    "mimetype": "%s"
                }
                """.formatted(fileName, fileType).getBytes();

        InboxFileStreamWriter inboxFileStreamWriter = InboxFileStreamWriter.createFile(
                endpointSession.inboxApi,
                publicMeta,
                filePrivateMeta,
                fileSize
        );

        long fileHandle = inboxFileStreamWriter.getFileHandle();

        // 1. Preparing Entry & sending File Contents
        long inboxHandle = endpointSession.inboxApi.prepareEntry(
                inboxID,
                entryData,
                List.of(fileHandle)
        );

        // 2. Sending File Contents
        inboxFileStreamWriter.write(
                inboxHandle,
                fileContent
        );

        // 3. Sending Entry
        endpointSession.inboxApi.sendEntry(inboxHandle);

    }

    void fetchingEntriesBasic() {
        String inboxID = "INBOX_ID";
        long startIndex = 0L;
        long pageSize = 100L;

        PagingList<InboxEntry> entriesPagingList = endpointSession.inboxApi.listEntries(
                inboxID,
                startIndex,
                pageSize,
                SortOrder.DESC
        );
    }

    void fetchingEntriesWithFiles() {
        String inboxID = "INBOX_ID";
        long startIndex = 0L;
        long pageSize = 1L;

        // getting last entry
        PagingList<InboxEntry> entriesPagingList = endpointSession.inboxApi.listEntries(
                inboxID,
                startIndex,
                pageSize,
                SortOrder.DESC
        );

        InboxEntry entry = entriesPagingList.readItems.getFirst();
        File entryFile = entry.files.getFirst();

        // This example uses com.google.code.gson:gson
        // dependency for handling JSON objects in Java

        // decoded privateMeta
        String privateMeta = new String(entryFile.privateMeta);
        JsonObject privateMetaJson = JsonParser.parseString(privateMeta).getAsJsonObject();
        String fileName = privateMetaJson.get("name").getAsString();
        String fileType = privateMetaJson.get("mimetype").getAsString();

        // decoded data
        String entryData = new String(entry.data);
        JsonObject entryDataJson = JsonParser.parseString(entryData).getAsJsonObject();
        String answer = entryDataJson
                .getAsJsonObject("content")
                .get("answer")
                .getAsString();
    }
}
