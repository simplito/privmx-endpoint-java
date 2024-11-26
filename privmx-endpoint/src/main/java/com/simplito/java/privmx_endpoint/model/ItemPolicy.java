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

package com.simplito.java.privmx_endpoint.model;

public class ItemPolicy {
    public final String get;
    public final String listMy;
    public final String listAll;
    public final String create;
    public final String update;
    public final String delete;

    public ItemPolicy(
            String get,
            String listMy,
            String listAll,
            String create,
            String update,
            String delete
    ) {
        this.get = get;
        this.listMy = listMy;
        this.listAll = listAll;
        this.create = create;
        this.update = update;
        this.delete = delete;
    }
}
