package gov.cdc.w5h;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jason on 1/13/16.
 */
public class WearListenerService extends WearableListenerService {

    private static final String STD_WEAR_PATH = "/std-wear";
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v("MListener: ", "messageReceived: " +messageEvent);
        if(messageEvent.getPath().equals(STD_WEAR_PATH)){
            List<Condition> children = new ArrayList<Condition>() {};
            List<String> breadcrumbs = new ArrayList<String>(){};
            Condition cerv = new Condition(6, 0, "Cervicitis", "c6-r.html", "c6-t.html", children ,breadcrumbs);
            Intent i = ConditionDetailsActivity.newIntent(getApplicationContext(), cerv);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);

        }
    }
}
