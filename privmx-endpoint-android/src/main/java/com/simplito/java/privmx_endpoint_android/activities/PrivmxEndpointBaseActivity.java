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

package com.simplito.java.privmx_endpoint_android.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.simplito.java.privmx_endpoint_android.services.PrivmxEndpointService;
import com.simplito.java.privmx_endpoint_extra.events.EventCallback;
import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;

/**
 * Manages {@link PrivmxEndpointService} and active connections.
 */
public abstract class PrivmxEndpointBaseActivity extends AppCompatActivity {

    private static final String TAG = "[PrivmxEndpointBaseActivity]";
    private PrivmxEndpointService privmxEndpointService = null;

    /**
     * Container instance for active connections to handle PrivMX Bridge.
     */
    protected PrivmxEndpointContainer privmxEndpointContainer = null;

    /**
     * Starting and binding to PrivmxEndpointService with passing path to .pem certificate
     * returned from {@link #getCertPath()}.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        Intent intent = new Intent(this, PrivmxEndpointService.class);
        intent.putExtra(PrivmxEndpointService.CERTS_PATH_EXTRA, getCertPath());
        startService(intent);
        bindService(intent, privmxEndpointServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unregisters all callbacks registered using {@link PrivmxEndpoint#registerCallback(Object, EventType, EventCallback)}
     * identified by this instance and unbinds {@link PrivmxEndpointService}.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (privmxEndpointService != null) {
            unbindService(privmxEndpointServiceConnection);
        }
    }

    /**
     * Method called when {@link PrivmxEndpointService} and {@link PrivmxEndpointContainer}
     * have been successfully initialized.
     * Override this method to safely work with {@link PrivmxEndpointBaseActivity#privmxEndpointContainer}.
     *
     * @noinspection EmptyMethod
     */
    protected void onPrivmxEndpointStart() {
    }

    /**
     * Override this method to set the path to your .pem certificate to create secure connection with PrivMX Bridge.
     *
     * @return Path to .pem certificate.
     */
    @Deprecated
    protected String getCertPath() {
        return null;
    }

    private final ServiceConnection privmxEndpointServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PrivmxEndpointService.PrivmxEndpointBinder binder = (PrivmxEndpointService.PrivmxEndpointBinder) iBinder;
            privmxEndpointService = binder.getService();
            privmxEndpointContainer = privmxEndpointService.getContainer();
            onPrivmxEndpointStart();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            privmxEndpointContainer = null;
        }
    };
}
