package pt.cmov.locmess.locmess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.firebaseConn.IUserLogInListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logInButton;
    private EditText emailText;
    private EditText passwordText;
    private TextView registerTextView;

    private ProgressDialog progressDialog;

    private FirebaseRemoteConnection _firebaseConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        progressDialog = new ProgressDialog(this);

        logInButton = (Button) findViewById(R.id.logInButton);
        logInButton.setOnClickListener(this);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);

        registerTextView = (TextView) findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(this);

        _firebaseConnection = FirebaseRemoteConnection.getInstance();

        _firebaseConnection.checkIfUserIsLogged();
        _firebaseConnection.addOnUserLoggedInListener(new IUserLogInListener() {
            @Override
            public void OnUserLoggedInListener(Boolean hasUser) {
                if (hasUser) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LocMessDrawer.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        _firebaseConnection = FirebaseRemoteConnection.getInstance();
        _firebaseConnection.getFAuth().addAuthStateListener(_firebaseConnection.getAuthListener());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (_firebaseConnection.getAuthListener() != null) {
            _firebaseConnection.getFAuth().removeAuthStateListener(_firebaseConnection.getAuthListener());
        }
    }

    @Override
    public void onClick(View view){
        if(view == logInButton){
            doSignIn();
        }

        if(view == registerTextView){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }

    private void doSignIn(){
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            View view = findViewById(R.id.activity_login);
            Snackbar.make(view, "Fields cannot be empty!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        progressDialog.setMessage("Signing in ...");
        progressDialog.show();

        _firebaseConnection.getFAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    _firebaseConnection.setUser(_firebaseConnection.getFAuth().getCurrentUser());
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LocMessDrawer.class);
                    startActivity(intent);
                }
                else {
                    View view = findViewById(R.id.activity_login);
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
