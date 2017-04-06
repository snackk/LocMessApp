package pt.cmov.locmess.locmess;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class LocMessActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView loggedUserTextView;
    private DatabaseReference dRef;
    private FirebaseUser loggedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loc_mess);

        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() == null){
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }else{
            loggedUserTextView = (TextView) findViewById(R.id.loggedUserTextView);
            loggedUserTextView.setText(mAuth.getCurrentUser().getEmail());
        }
        loggedUser = mAuth.getCurrentUser();
        //dRef.child(loggedUser.getUid()).setValue(CLASS);
    }
}
