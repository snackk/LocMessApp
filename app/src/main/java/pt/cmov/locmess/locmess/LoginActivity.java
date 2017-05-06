package pt.cmov.locmess.locmess;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button logInButton;
    private EditText emailText;
    private EditText passwordText;
    private TextView registerTextView;

    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LocMessDrawer.class);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
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

    public void doSignIn(){
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LocMessDrawer.class);
                    startActivity(intent);
                }
                else Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
