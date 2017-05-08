package pt.cmov.locmess.locmess.firebaseConn;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by snackk on 08/05/2017.
 */

public class FirebaseRemoteConnection {

    private static FirebaseRemoteConnection instance = null;

    private FirebaseAuth mAuth;
    public DatabaseReference dRef;
    private FirebaseUser user = null;

    private FirebaseAuth.AuthStateListener mAuthListener;
    private static List<IUserDetailsResponseListener> userDetailListeners = new ArrayList<>();
    private static List<IUserLogInListener> logInListeners = new ArrayList<>();

    private String _userFirstName = "";
    private String _userLastName = "";

    protected FirebaseRemoteConnection() {
        mAuth = FirebaseAuth.getInstance();
    }

    public static FirebaseRemoteConnection getInstance() {
        if (instance == null)
            instance = new FirebaseRemoteConnection();
        return instance;
    }

    private void notifyUserDetailListeners(){
        if(!_userFirstName.equals("") && !_userLastName.equals("")){
            for (IUserDetailsResponseListener l : userDetailListeners) {
                l.OnUserDetailsResponseListener(_userFirstName + " " + _userLastName);
            }
        }
    }

    public FirebaseAuth getFAuth(){
        return mAuth;
    }

    public FirebaseAuth.AuthStateListener getAuthListener(){
        return mAuthListener;
    }

    public void setUser(FirebaseUser fUser){
        user = fUser;
    }

    public static void addOnUserDetailsResponseListener(IUserDetailsResponseListener listener) {
        userDetailListeners.add(listener);
    }

    public static void addOnUserLoggedInListener(IUserLogInListener listener) {
        logInListeners.add(listener);
    }

    public void checkIfUserIsLogged(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                setUser(firebaseAuth.getCurrentUser());
                for (IUserLogInListener l : logInListeners) {
                    l.OnUserLoggedInListener(user != null);
                }
            }
        };
    }

    public void doSignOut(){
        mAuth.signOut();
    }

    public String getFirebaseEmail() {
        return user.getEmail();
    }

    public void getUserDetails() {
        //TODO handle serialize data
        dRef = FirebaseDatabase.getInstance().getReference();

        if (mAuth.getCurrentUser() != null) {
            dRef = dRef.child("users");

            Query queryRef = dRef.orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
            queryRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot snapshot, String previousChild) {
                    String groupKey = snapshot.getKey();

                    dRef.child(groupKey + "/firstName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            _userFirstName = snapshot.getValue().toString();
                            notifyUserDetailListeners();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    dRef.child(groupKey + "/lastName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            _userLastName = snapshot.getValue().toString();
                            notifyUserDetailListeners();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
