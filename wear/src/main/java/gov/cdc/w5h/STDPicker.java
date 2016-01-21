package gov.cdc.w5h;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class STDPicker extends Activity implements WearableListView.ClickListener {

    private static String [] elementsStart = {"Cervicitis", "Chancroid", "Chlamydia"};
    private RelativeLayout relativeLayout;
    private Rect windowInsets = new Rect(0, 0, 0, 0);

    public static Intent newIntent(Context packageContext, String[] conditions){
        Intent i = new Intent(packageContext, STDPicker.class);
        elementsStart = conditions;
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdpicker);

        String [] elements = elementsStart;

        final WearableListView listView = (WearableListView) findViewById(R.id.wearable_list);

        listView.setAdapter(new Adapter(this, elements));
        listView.setClickListener(this);

        relativeLayout = (RelativeLayout) findViewById(R.id.std_list_relative_layout);

        relativeLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                insets = relativeLayout.onApplyWindowInsets(insets);

                boolean round = insets.isRound();

                //Store system window insets
                windowInsets.set(insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());

                if (round) {
                    windowInsets = calculateBottomInsetsOnRoundDevice(
                            getWindowManager().getDefaultDisplay(), windowInsets);
                }

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) listView.getLayoutParams();
                params.leftMargin = windowInsets.left / 2;
                params.bottomMargin = windowInsets.bottom;
                params.rightMargin = windowInsets.right / 2;
                listView.setLayoutParams(params);

                return insets;
            }
        });
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        elementsStart = new String[] {"Cervicitis", "Chancroid", "Chlamydia"};
    }

    @Override
    public void onClick(WearableListView.ViewHolder viewHolder) {
        Integer tag = (Integer) viewHolder.itemView.getTag();
        Intent i;
        if(tag == 0 & elementsStart[0].equals("Cervicitis")){
            i = new Intent(this, GridViewActivity.class);
        }
        else{
            String [] sub = {"sub 1", "sub 2", "sub 3"};
            i = STDPicker.newIntent(getApplicationContext(), sub);
        }

        startActivity(i);
    }

    @Override
    public void onTopEmptyRegionClick() {

    }
    /**
     * Calculates the square insets on a round device. If the system insets are not set
     * (set to 0) then the inner square of the circle is applied instead.
     *
     * @param display      device default display
     * @param systemInsets the system insets
     * @return adjusted square insets for use on a round device
     */
    private Rect calculateBottomInsetsOnRoundDevice(Display display, Rect systemInsets) {
        Point size = new Point();
        display.getSize(size);
        int width = size.x + systemInsets.left + systemInsets.right;
        int height = size.y + systemInsets.top + systemInsets.bottom;

        // Minimum inset to use on a round screen, calculated as a fixed percent of screen height
        int minInset = (int) (height * 0.08f);

        // Use system inset if it is larger than min inset, otherwise use min inset
        int bottomInset = systemInsets.bottom > minInset ? systemInsets.bottom : minInset;

        // Calculate left and right insets based on bottom inset
        double radius = width / 2;
        double apothem = radius - bottomInset;
        double chord = Math.sqrt(Math.pow(radius, 2) - Math.pow(apothem, 2)) * 2;
        int leftRightInset = (int) ((width - chord) / 2);

        return new Rect(leftRightInset, 0, leftRightInset, bottomInset);
    }

    public final class Adapter extends WearableListView.Adapter{
        private String[] dataSet;
        private final Context context;
        private final LayoutInflater inflater;

        public Adapter(Context context, String[] dataSet) {
            this.context = context;
            this.dataSet = dataSet;
            inflater = LayoutInflater.from(context);
        }

        public class ItemViewHolder extends WearableListView.ViewHolder{
            private TextView textView;
            public ItemViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.list_item_title);
            }
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(inflater.inflate(R.layout.std_list_item, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            TextView view= itemHolder.textView;

            view.setText(dataSet[position]);
            ((ItemViewHolder) holder).itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return dataSet.length;
        }
    }
}

