package tien.edu.hutech.signinfirebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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

public class EmailPasswordActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private TextView mStatus;
    private TextView mDetail;
    private EditText mField_Email;
    private EditText mField_Password;
    private Button mEmail_Sign_In_Button;
    private Button   mEmail_Create_Account_Button;
    private Button   mSign_Out_Button;

    //Declare_Auth
    private FirebaseAuth mAuth;

    //Declare_Auth_Listener
    private FirebaseAuth.AuthStateListener mAuthListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View
        mStatus         = (TextView) findViewById(R.id.status);
        mDetail         = (TextView) findViewById(R.id.detail);
        mField_Email    = (EditText) findViewById(R.id.field_email);
        mField_Password = (EditText) findViewById(R.id.field_password);

        //Button
        mEmail_Sign_In_Button           = (Button) findViewById(R.id.email_sign_in_button);
        mEmail_Create_Account_Button    = (Button) findViewById(R.id.email_create_account_button);
        mSign_Out_Button                = (Button) findViewById(R.id.sign_out_button);

        //Set_On_Click_Listener
        mEmail_Sign_In_Button.setOnClickListener(this);
        mEmail_Create_Account_Button.setOnClickListener(this);
        mSign_Out_Button.setOnClickListener(this);

        //initialize_auth
        mAuth = FirebaseAuth.getInstance();

        //auth_state_listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                }
                else
                {
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
                updateUI(user);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createAccount(String email, String password)
    {
        Log.d(TAG, "createAccount: " + email);
        if(!validateForm())
        {
            return;
        }

        showProgressDialog();

        //create_user_with_email
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail: onCompelete: " + task.isSuccessful());

                        if(!task.isSuccessful())
                        {
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void signIn(String email, String password)
    {
        Log.d(TAG, "signIn: " + email);
        if(!validateForm())
        {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail: onComplete: " + task.isSuccessful());

                        if(!task.isSuccessful())
                        {
                            Toast.makeText(EmailPasswordActivity.this, "signInWithEmail: failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if(!task.isSuccessful())
                        {
                            mStatus.setText(R.string.auth_failed);
                        }

                        hideProgressDialog();
                    }
                });
    }

    private void signOut()
    {
        mAuth.signOut();
        updateUI(null);
    }

    private boolean validateForm()
    {
        boolean valid = true;

        String email = mField_Email.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            mField_Email.setError("Required.");
            valid = false;
        }
        else
        {
            mField_Email.setError(null);
        }

        String password = mField_Password.getText().toString();
        if(TextUtils.isEmpty(password))
        {
            mField_Password.setError("Required.");
            valid = false;
        }
        else
        {
            mField_Password.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user)
    {
        hideProgressDialog();
        if(user != null)
        {
            mStatus.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
            mDetail.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            mEmail_Create_Account_Button.setVisibility(View.GONE);
            mEmail_Sign_In_Button.setVisibility(View.GONE);
            mSign_Out_Button.setVisibility(View.VISIBLE);
        }
        else
        {
            mStatus.setText(R.string.signed_out);
            mDetail.setText(null);

            mEmail_Create_Account_Button.setVisibility(View.VISIBLE);
            mEmail_Sign_In_Button.setVisibility(View.VISIBLE);
            mSign_Out_Button.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        switch (i)
        {
            case R.id.email_create_account_button:
                createAccount(mField_Email.getText().toString(), mField_Email.getText().toString());
                break;
            case R.id.email_sign_in_button:
                signIn(mField_Email.getText().toString(), mField_Email.getText().toString());
                break;
            case R.id.sign_out_button:
                signOut();
                break;
        }
    }
}
