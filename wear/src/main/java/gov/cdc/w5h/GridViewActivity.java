package gov.cdc.w5h;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.ActionPage;
import android.support.wearable.view.CardFrame;
import android.support.wearable.view.CardScrollView;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

public class GridViewActivity extends FragmentActivity {

    private FrameLayout frameLayout;
    private GridViewPager pager;
    private DotsPageIndicator dotsPageIndicator;
    private Rect windowInsets = new Rect(0, 0, 0, 0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        frameLayout = (FrameLayout) findViewById(R.id.grid_pager_layout);
        pager = (GridViewPager) findViewById(R.id.grid_pager);
        dotsPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        dotsPageIndicator.setPager(pager);
        pager.setAdapter(new Adapter(this));

        frameLayout.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                insets = frameLayout.onApplyWindowInsets(insets);

                boolean round = insets.isRound();

                //Store system window insets
                windowInsets.set(insets.getSystemWindowInsetLeft(),
                        insets.getSystemWindowInsetTop(),
                        insets.getSystemWindowInsetRight(),
                        insets.getSystemWindowInsetBottom());

                if(round){
                    windowInsets = calculateBottomInsetsOnRoundDevice(
                            getWindowManager().getDefaultDisplay(), windowInsets);
                }

                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) dotsPageIndicator.getLayoutParams();
                params.bottomMargin = windowInsets.bottom;
                dotsPageIndicator.setLayoutParams(params);

                return insets;
            }
        });

    }

    public class Adapter extends GridPagerAdapter {
        private Context context;
        public Adapter(Context c){context = c;}

        @Override
        public int getRowCount() {
            return 1;
        }

        @Override
        public int getColumnCount(int i) {
            return 2;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup, int row, int col) {
            if(col == 0){
                CardScrollView cardScrollView = (CardScrollView) getLayoutInflater().inflate(
                        R.layout.std_card_layout, viewGroup, false);
                cardScrollView.setCardGravity(Gravity.BOTTOM);
                cardScrollView.setExpansionEnabled(true);
                cardScrollView.setExpansionDirection(CardFrame.EXPAND_DOWN);
                cardScrollView.setExpansionFactor(10);
                viewGroup.addView(cardScrollView);
                return cardScrollView;
            }
            else{
                ActionPage openOnPhone = (ActionPage) getLayoutInflater().inflate(R.layout.open_on_phone, viewGroup, false);
                openOnPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, ConfirmationActivity.class);
                        i.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.OPEN_ON_PHONE_ANIMATION);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(i);

                        UtilityService.startDeviceActivity(context, "Cervicitis");
                    }
                });
                viewGroup.addView(openOnPhone);
                return openOnPhone;

            }
        }

        @Override
        public void destroyItem(ViewGroup viewGroup, int row, int col, Object o) {
            viewGroup.removeView((View) o);
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view.equals(o);
        }
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
}

