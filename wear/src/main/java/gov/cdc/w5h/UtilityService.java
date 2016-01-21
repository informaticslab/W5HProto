package gov.cdc.w5h;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by jason on 1/14/16.
 */
public class UtilityService extends IntentService {
    private static final String OPEN_ON_PHONE = "open_on_phone";

    public UtilityService() {
        super("UtilityService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action.equals(OPEN_ON_PHONE)) {
            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .build();

            ConnectionResult connectionResult = googleApiClient.blockingConnect(
                    10, TimeUnit.SECONDS);

            if (connectionResult.isSuccess() && googleApiClient.isConnected()) {
                CapabilityApi.GetCapabilityResult result = Wearable.CapabilityApi.getCapability(
                        googleApiClient,
                        "show_std_treatments",
                        CapabilityApi.FILTER_REACHABLE)
                        .await(10, TimeUnit.SECONDS);
                if (result.getStatus().isSuccess()) {
                    Set<Node> nodes = result.getCapability().getNodes();
                    for (Node node : nodes) {
                        Wearable.MessageApi.sendMessage(
                                googleApiClient, node.getId(), "/std-wear", "Cervicitis".getBytes()).await();
                    }
                } else {
                    Log.e("UtilSvc: ", "startDeviceActivityInternal() Failed to get capabilities, status: "
                            + result.getStatus().getStatusMessage());

                    googleApiClient.disconnect();
                }
            }
        }
    }
    public static void startDeviceActivity(Context c, String conditionTitle){
        Intent intent = new Intent(c, UtilityService.class);
        intent.setAction(UtilityService.OPEN_ON_PHONE);
        intent.putExtra("conditionTitle", conditionTitle);
        c.startService(intent);

    }
}
