package pt.cmov.locmess.locmess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MessageCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<String> locations = new ArrayList<String>();
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");
        locations.add("Arco do Cego");

        ArrayList<String> policy = new ArrayList<String>();
        policy.add("Whitelist");
        policy.add("Blacklist");

        Spinner sl = (Spinner) findViewById(R.id.spinner_locations);
        Spinner sp = (Spinner) findViewById(R.id.spinner_policy);
        sl.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, locations));
        sp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, policy));
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
