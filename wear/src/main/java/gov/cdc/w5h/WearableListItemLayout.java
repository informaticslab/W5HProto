package gov.cdc.w5h;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jason on 1/8/16.
 */
public class WearableListItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

    private ImageView stdImage;
    private TextView stdText;
    private final int unselectedImageColor;
    private final int selectedImageColor;
    private final int unselectedTextColor;
    private final int selectedTextColor;

    public WearableListItemLayout(Context context) {
        this(context, null);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WearableListItemLayout(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        unselectedImageColor = getResources().getColor(R.color.unselectedImageColor);
        selectedImageColor = getResources().getColor(R.color.selectedImageColor);
        unselectedTextColor = getResources().getColor(R.color.unselectedTextColor);
        selectedTextColor = getResources().getColor(R.color.selectedTextColor);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        stdImage = (ImageView) findViewById(R.id.list_item_image);
        stdText = (TextView) findViewById(R.id.list_item_title);
    }

    @Override
    public void onCenterPosition(boolean b) {
        stdText.setTextColor(selectedTextColor);
        stdImage.getDrawable().setTint(selectedImageColor);
    }

    @Override
    public void onNonCenterPosition(boolean b) {
        stdText.setTextColor(unselectedTextColor);
        stdImage.getDrawable().setTint(unselectedImageColor);
    }
}
