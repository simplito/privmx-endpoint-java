//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information about a deleted Thread.
 *
 * @category core
 * @group Events
 */
public class ThreadDeletedEventData {

    /**
     * ID of the deleted Thread.
     */
    public final String threadId;

    /**
     * Creates instance of {@code ThreadDeletedEventData}.
     * @param threadId ID of the deleted Thread.
     */
    public ThreadDeletedEventData(
            String threadId
    ){
        this.threadId = threadId;
    }
}
