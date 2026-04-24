package Tools.UsingInboxes

import com.google.gson.JsonParser
import com.simplito.java.privmx_endpoint.model.File
import com.simplito.java.privmx_endpoint.model.InboxEntry
import com.simplito.java.privmx_endpoint_extra.inboxFileStream.InboxFileStreamWriter
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import java.io.IOException
import java.util.List

class InboxEntries : WorkingWithInboxes() {
    fun sendingInboxEntryBasic() {
        // 1. Preparing Entry
        val inboxID = "INBOX_ID"

        val entryDataAnswer = "USER_PROVIDED_TEXT"
        val entryDataVersion = 1L
        val entryDataType = "TEXT_ANSWER"

        val entryData: ByteArray = """
                 {
                    "content": {
                        "answer": "%s"
                    },
                    "version": %d,
                    "type": "%s"
                 }
                
                """.trimIndent().format(entryDataAnswer, entryDataVersion, entryDataType)
            .toByteArray()

        val entryHandle = endpointSession.inboxApi.prepareEntry(
            inboxID,
            entryData,
            mutableListOf<Long?>()
        )

        // 2. Sending Entry
        endpointSession.inboxApi.sendEntry(entryHandle)
    }

    @Throws(IOException::class)
    fun attachingFiles(fileID: String?) {
        val inboxID = "INBOX_ID"
        val publicMeta = "My public data".toByteArray()

        val fileName = "FILE_NAME"
        val fileType = "FILE_TYPE"
        val fileContent = "FILE_CONTENT".toByteArray()
        val fileSize = fileContent.size.toLong()

        val entryDataAnswer = "USER_PROVIDED_TEXT"
        val entryDataVersion = 1L
        val entryDataType = "TEXT_ANSWER"

        val entryData: ByteArray = """
                 {
                    "content": {
                        "answer": "%s"
                    },
                    "version": %d,
                    "type": "%s"
                 }
                
                """.trimIndent().format(entryDataAnswer, entryDataVersion, entryDataType)
            .toByteArray()

        val filePrivateMeta: ByteArray = """
                {
                    "name": "%s",
                    "mimetype": "%s"
                }
                
                """.trimIndent().format(fileName, fileType).toByteArray()

        val inboxFileStreamWriter = InboxFileStreamWriter.createFile(
            endpointSession.inboxApi,
            publicMeta,
            filePrivateMeta,
            fileSize
        )

        val fileHandle = inboxFileStreamWriter.getFileHandle()

        // 1. Preparing Entry & sending File Contents
        val inboxHandle = endpointSession.inboxApi.prepareEntry(
            inboxID,
            entryData,
            List.of<Long?>(fileHandle)
        )

        // 2. Sending File Contents
        inboxFileStreamWriter.write(
            inboxHandle,
            fileContent
        )

        // 3. Sending Entry
        endpointSession.inboxApi.sendEntry(inboxHandle)
    }

    fun fetchingEntriesBasic() {
        val inboxID = "INBOX_ID"
        val startIndex = 0L
        val pageSize = 100L

        val entriesPagingList = endpointSession.inboxApi.listEntries(
            inboxID,
            startIndex,
            pageSize,
            SortOrder.DESC
        )
    }

    fun fetchingEntriesWithFiles() {
        val inboxID = "INBOX_ID"
        val startIndex = 0L
        val pageSize = 1L

        // getting last entry
        val entriesPagingList = endpointSession.inboxApi.listEntries(
            inboxID,
            startIndex,
            pageSize,
            SortOrder.DESC
        )

        val entry: InboxEntry = entriesPagingList.readItems[0]
        val entryFile: File = entry.files[0]

        // This example uses com.google.code.gson:gson
        // dependency for handling JSON objects in Java

        // decoded privateMeta
        val privateMeta = String(entryFile.privateMeta)
        val privateMetaJson = JsonParser.parseString(privateMeta).getAsJsonObject()
        val fileName = privateMetaJson.get("name").getAsString()
        val fileType = privateMetaJson.get("mimetype").getAsString()

        // decoded data
        val entryData = String(entry.data)
        val entryDataJson = JsonParser.parseString(entryData).getAsJsonObject()
        val answer = entryDataJson
            .getAsJsonObject("content")
            .get("answer")
            .asString
    }
}