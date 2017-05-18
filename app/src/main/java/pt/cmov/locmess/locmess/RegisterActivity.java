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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.FirebaseDatabase;

import okhttp3.ResponseBody;
import pt.cmov.locmess.locmess.firabaseUser.User;
import pt.cmov.locmess.locmess.firebaseConn.FirebaseRemoteConnection;
import pt.cmov.locmess.locmess.restfulConn.ILocMessApi;
import pt.cmov.locmess.locmess.restfulConn.LocMessApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerButton;
    private EditText emailText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private ILocMessApi locMessApi;

    //Firebase
    private FirebaseRemoteConnection _firebaseConnection;

    private ProgressDialog progressDialog;

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

        _firebaseConnection = FirebaseRemoteConnection.getInstance();
        _firebaseConnection.dRef = FirebaseDatabase.getInstance().getReference();
        locMessApi = LocMessApi.getClient().create(ILocMessApi.class);
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
            View view = findViewById(R.id.activity_register);
            Snackbar.make(view, "Fields cannot be empty!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        progressDialog.setMessage("Registering user...");
        progressDialog.show();



        _firebaseConnection.getFAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()) {

                    _firebaseConnection.dRef = _firebaseConnection.dRef.child("users").push();
                    String email = emailText.getText().toString().trim();
                    String firstName = firstNameText.getText().toString().trim();
                    String lastName = lastNameText.getText().toString().trim();
                    User user = new User(firstName, lastName, email);
                    _firebaseConnection.dRef.setValue(user);

                    pt.cmov.locmess.locmess.restfulConn.pojo.User userR = new pt.cmov.locmess.locmess.restfulConn.pojo.User(email);
                    Call<ResponseBody> call = locMessApi.createUser(userR);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            call.cancel();
                        }
                    });
                    finish();
                    Intent intent = new Intent(getApplicationContext(), LocMessDrawer.class);
                    startActivity(intent);
                } else {
                    View view = findViewById(R.id.activity_login);
                    Snackbar.make(view, "Something went wrong.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
