//
// PrivMX Endpoint Java Android.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_android.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;

import java.util.ArrayList;

/**
 * Runs and manages active PrivMX Bridge connections.
 */
public class PrivmxEndpointService extends Service {
    private static final String TAG = "[PrivmxEndpointService]";
    private final PrivmxEndpointBinder binder = new PrivmxEndpointBinder();
    private final PrivmxEndpointContainer privmxEndpoint = new PrivmxEndpointContainer();

    /**
     * Defines a key for Intent extras.
     */
    public static final String CERTS_PATH_EXTRA = "com.simplito.android.privmx_endpoint_wrapper.services.PrivmxEndpointService.CERTS_PATH_EXTRA";

    /**
     * Implements Service Binder.
     */
    public class PrivmxEndpointBinder extends Binder {
        private final ArrayList<Runnable> onInit = new ArrayList<>();

        /**
         * @return Instance of PrivMX Endpoint Service
         */
        public PrivmxEndpointService getService() {
            return PrivmxEndpointService.this;
        }
    }

    /**
     * Sets callback executed when service has been successfully prepared to use.
     *
     * @param onInit callback
     */
    public void setOnInit(Runnable onInit) {
        if (privmxEndpoint.initialized()) {
            onInit.run();
        } else {
            binder.onInit.add(onInit);
        }
    }

    /**
     * Initializes {@link PrivmxEndpointContainer} with certsPath passed in intent extras.
     * If intent does not contain the path, the default value is used.
     *
     * @see Service#onBind(Intent)
     */
    @Override
    public IBinder onBind(Intent intent) {
        init(getCertsPath(intent));
        return binder;
    }

    /**
     * Initialize {@link PrivmxEndpointContainer} with certsPath passed in intent extras.
     * If intent does not contain the path, the default value is used.
     *
     * @see Service#onStartCommand(Intent, int, int)
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init(getCertsPath(intent));
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Disconnects active connections if any exist.
     *
     * @see Service#onDestroy()
     */
    @Override
    public void onDestroy() {
        try {
            privmxEndpoint.disconnectAll();
            privmxEndpoint.close();
        } catch (Exception e) {
            System.out.println("Cannot disconnect from server, reason: " + e.getMessage());
        }
        super.onDestroy();
    }

    private String getCertsPath(Intent intent) {
        String certsPath = getFilesDir() + "/cacert.pem";
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                certsPath = extras.getString(CERTS_PATH_EXTRA);
            }
        }
        return certsPath;
    }

    /**
     * Gets {@link PrivmxEndpointContainer}.
     *
     * @return Initialized container. If the service does not initialize the container successfully, it returns {@code null}
     */
    public PrivmxEndpointContainer getContainer() {
        return privmxEndpoint;
    }

    private synchronized void init(String certsPath) {
        Log.d(TAG, "PrivmxEndpoint init");
        if (privmxEndpoint.initialized()) {
            return;
        }
        try {
            privmxEndpoint.setCertsPath(certsPath);
            binder.onInit.forEach(Runnable::run);
        } catch (Exception e) {
            Log.e(TAG, "Cannot initialize lib");
        }
    }


}
