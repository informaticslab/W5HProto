package gov.cdc.w5h;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;

public class ConditionDetailsActivity extends AppCompatActivity {
    private String regimensPage;
    private String dxtxPage;
    private String title;
    private ArrayList<String> breadcrumbs;
    private TextView pageTitle;
    private ViewPager viewPager;

    public static Intent newIntent(Context packageContext, Condition condition){
        Intent intent = new Intent(packageContext, ConditionDetailsActivity.class);
        intent.putExtra("id", condition.id);
        intent.putExtra("parentId", condition.parentId);
        intent.putExtra("title", condition.title);
        intent.putExtra("regimensPage", condition.regimensPage);
        intent.putExtra("dxtxPage", condition.dxtxPage);
        intent.putStringArrayListExtra("breadcrumbs", new ArrayList<>(condition.breadcrumbs));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_condition_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Condition Details");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regimensPage = getIntent().getStringExtra("regimensPage");
        dxtxPage = getIntent().getStringExtra("dxtxPage");
        breadcrumbs = getIntent().getStringArrayListExtra("breadcrumbs");
        title = getIntent().getStringExtra("title");

/*
        regimensPage = "c6-r.html";
        dxtxPage = "c6-t.html";
        breadcrumbs = new ArrayList<String>() {};
        title = "Cervicitis";
*/

        viewPager = (ViewPager) findViewById(R.id.pager);
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        pageTitle = (TextView) findViewById(R.id.page_title);
        title = generatePageTitle();
        pageTitle.setText(title);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_host);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }

    private String generatePageTitle(){
        String pageTitle = "";
        if(breadcrumbs.size() >= 1){
            pageTitle += breadcrumbs.get(0);
            for(int i = 1; i < breadcrumbs.size(); i++){
                pageTitle += " / " +breadcrumbs.get(i);
            }
            pageTitle += " / " +title;
        }
        else{
            pageTitle = title;
        }

        return pageTitle;
    }

    private class ContentPagerAdapter extends FragmentStatePagerAdapter {
        private String [] tabTitles = {"Treatments", "More Info"};

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
            if(dxtxPage.equals("")){
                tabTitles = new String [] {"Treatments"};
            }
            if(regimensPage.equals("")){
                tabTitles = new String[] {"More Info"};
            }
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if(tabTitles.length == 1){
                if(tabTitles[0].equals("Treatments")){
                    return new ConditionDetailsFragment().newInstance(regimensPage);
                }
                else if(tabTitles[0].equals("More Info")){
                    return new ConditionDetailsFragment().newInstance(dxtxPage);
                }
            } else {
                if(position == 0){
                    return new ConditionDetailsFragment().newInstance(regimensPage);
                }
                else {
                    return new ConditionDetailsFragment().newInstance(dxtxPage);
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}

