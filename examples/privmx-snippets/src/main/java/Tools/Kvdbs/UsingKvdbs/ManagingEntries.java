package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint.model.KvdbEntry;

import java.nio.charset.StandardCharsets;

public class ManagingEntries extends ManagingKvdbs {
    void sendingEntries() {
        String kvdbID = "KVDB_ID";
        String kvdbEntryKey = "KVDB_ENTRY_KEY";
        byte[] privateMeta = "My private data".getBytes(StandardCharsets.UTF_8);
        byte[] publicMeta = "My public data".getBytes(StandardCharsets.UTF_8);
        byte[] entryData = "ENTRY_DATA".getBytes();
        byte[] newEntryData = "New Entry Data".getBytes();

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

        // update created entry
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