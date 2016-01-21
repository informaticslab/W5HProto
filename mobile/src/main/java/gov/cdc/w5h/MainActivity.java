package gov.cdc.w5h;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button) findViewById(R.id.cerv_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Condition> children = new ArrayList<Condition>() {};
                List<String> breadcrumbs = new ArrayList<String>(){};
                Condition cerv = new Condition(6, 0, "Cervicitis", "c6-r.html", "c6-t.html", children ,breadcrumbs);
                Intent i = ConditionDetailsActivity.newIntent(getApplicationContext(), cerv);
                startActivity(i);
            }
        });
    }

}
