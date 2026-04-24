package Tools.UsingKvdbs

import java.nio.charset.StandardCharsets

class ManagingEntries : ManagingKvdbs() {
    fun sendingEntries() {
        val kvdbID = "KVDB_ID"
        val kvdbEntryKey = "KVDB_ENTRY_KEY"
        val privateMeta = "My private data".toByteArray(StandardCharsets.UTF_8)
        val publicMeta = "My public data".toByteArray(StandardCharsets.UTF_8)
        val entryData = "ENTRY_DATA".toByteArray()
        val newEntryData = "New Entry Data".toByteArray()

        // creating entry
        endpointSession.kvdbApi.setEntry(
            kvdbID,
            kvdbEntryKey,
            publicMeta,
            privateMeta,
            entryData
        )

        // read created entry
        val entry = endpointSession.kvdbApi.getEntry(
            kvdbID,
            kvdbEntryKey
        )

        // update created entry
        endpointSession.kvdbApi.setEntry(
            entry.info.kvdbId,
            entry.info.key,
            entry.publicMeta,
            entry.privateMeta,
            newEntryData,
            entry.version
        )
    }
}