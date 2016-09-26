package tien.edu.hutech.signinfirebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;

public class GoogleSignInActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener
        ,View.OnClickListener {

    private static String TAG = "GoogleActivity";
    private static int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    GoogleApiClient mGoogleApiClient;

    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private Button btnSignIn;
    private Button btnSignOut;
    private Button btnDisconnect;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
