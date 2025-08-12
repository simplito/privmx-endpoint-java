package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint.model.KvdbEntry;

import java.nio.charset.StandardCharsets;

public class ManagingEntries extends ManagingKvdbs {
    void sendingEntry() {
        String kvdbID = "KVDB_ID";
        String kvdbEntryKey = "KVDB_ENTRY_KEY";
        byte[] privateMeta = "My private data".getBytes(StandardCharsets.UTF_8);
        byte[] publicMeta = "My public data".getBytes(StandardCharsets.UTF_8);
        byte[] entryData = "ENTRY_DATA".getBytes();

        endpointSession.kvdbApi.setEntry(
                kvdbID,
                kvdbEntryKey,
                publicMeta,
                privateMeta,
                entryData
        );
    }

    void gettingEntry(String kvdbID, String kvdbEntryKey) {
        KvdbEntry entry = endpointSession.kvdbApi.getEntry(
                kvdbID,
                kvdbEntryKey
        );
    }

    void updatingEntry(KvdbEntry entry) {
        byte[] newEntryData = "New Entry Data".getBytes();

        endpointSession.kvdbApi.setEntry(
                entry.info.kvdbId,
                entry.info.key,
                entry.publicMeta,
                entry.privateMeta,
                newEntryData,
                entry.version
        );
    }
}