package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint.model.KvdbEntry;
import com.simplito.java.privmx_endpoint_extra.model.SortOrder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ManagingEntries extends ManagingKvdbs {
    void sendingEntries() {
        String kvdbID = "KVDB_ID";
        String kvdbEntryKey = "KVDB_ENTRY_KEY";
        byte[] privateMeta = "My private data".getBytes(StandardCharsets.UTF_8);
        byte[] publicMeta = "My public data".getBytes(StandardCharsets.UTF_8);
        byte[] entryData = "Entry Data".getBytes();

        // creating entry
        endpointSession.kvdbApi.setEntry(
                kvdbID,
                kvdbEntryKey,
                publicMeta,
                privateMeta,
                entryData
        );

        // read created entry
        KvdbEntry entry = endpointSession.kvdbApi.getEntry(
                kvdbID,
                kvdbEntryKey
        );
    }

    void updateEntry() {
        String kvdbID = "KVDB_ID";
        String kvdbEntryKey = "KVDB_ENTRY_KEY";
        byte[] newEntryData = "New Entry Data".getBytes();
        KvdbEntry entry = endpointSession.kvdbApi.getEntry(kvdbID, kvdbEntryKey);

        endpointSession.kvdbApi.setEntry(
                kvdbID,
                kvdbEntryKey,
                entry.publicMeta,
                entry.privateMeta,
                newEntryData,
                entry.version
        );
    }

    void listEntries() {
        String kvdbId = "KVDB_ID";
        long startIndex = 0;
        long pageSize = 30;

        List<KvdbEntry> entries = endpointSession.kvdbApi.listEntries(
                kvdbId,
                startIndex,
                pageSize,
                SortOrder.DESC
        ).readItems;
    }

    void deleteEntry() {
        String kvdbID = "KVDB_ID";
        String kvdbEntryKey = "KVDB_ENTRY_KEY";

        endpointSession.kvdbApi.deleteEntry(kvdbID, kvdbEntryKey);
    }
}