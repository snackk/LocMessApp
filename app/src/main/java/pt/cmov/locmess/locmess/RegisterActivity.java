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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pt.cmov.locmess.locmess.databases.User;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerButton;
    private EditText emailText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;

    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    private DatabaseReference dRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        firstNameText = (EditText) findViewById(R.id.firstNameText);
        lastNameText = (EditText) findViewById(R.id.lastNameText);

        mAuth = FirebaseAuth.getInstance();
        dRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        if(view == registerButton)
            doRegister();
    }

    public void doRegister(){
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        String firstName = firstNameText.getText().toString().trim();
        String lastName = lastNameText.getText().toString().trim();

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Fields cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()) {

                    dRef = dRef.child("users").push();
                    String email = emailText.getText().toString().trim();
                    String firstName = firstNameText.getText().toString().trim();
                    String lastName = lastNameText.getText().toString().trim();
                    User user = new User(firstName, lastName, email);
                    dRef.setValue(user);

                    finish();
                    Intent intent = new Intent(getApplicationContext(), LocMessDrawer.class);
                    startActivity(intent);
                }else Toast.makeText(RegisterActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
