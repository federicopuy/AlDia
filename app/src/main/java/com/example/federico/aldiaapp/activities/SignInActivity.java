package com.example.federico.aldiaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.federico.aldiaapp.R;
import com.example.federico.aldiaapp.model.Business;
import com.example.federico.aldiaapp.model.FirebaseToken;
import com.example.federico.aldiaapp.network.NoConnectivityException;
import com.example.federico.aldiaapp.network.RetrofitClient;
import com.example.federico.aldiaapp.utils.Constants;
import com.example.federico.aldiaapp.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    SharedPreferences prefs;
    String tokenFirebase = "";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

    }

    /*-------------------------------------- On Start --------------------------------------------***/

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.KEY_INTENT_SIGN_OUT)) {
            signOut();
        }
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    /*-------------------------------------- On Activity Result --------------------------------------------***/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                updateUI(null);
            }
        }
    }

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }
    // [END auth_with_google]

    /*-------------------------------------- Sign IN and Sign Out --------------------------------------------***/


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

    /*-------------------------------------- Update UI --------------------------------------------***/


    private void updateUI(final FirebaseUser user) {
        progressBar.setVisibility(View.VISIBLE);
        if (user != null) {
            try {
                prefs.edit().putString(Constants.KEY_USER_NAME, user.getDisplayName()).apply();
                prefs.edit().putString(Constants.KEY_EMAIL_USER, user.getEmail()).apply();
                prefs.edit().putString(Constants.KEY_PHOTO_USER, Objects.requireNonNull(user.getPhotoUrl()).toString()).apply();
            } catch (Exception e) {
                e.printStackTrace();
                signInButton.setVisibility(View.VISIBLE);

            }

            user.getIdToken(true)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            tokenFirebase = task.getResult().getToken();
                            Log.d(TAG, "Token Firebase " + tokenFirebase);
                            prefs.edit().putString(Constants.KEY_TOKEN_FIREBASE, tokenFirebase).apply();
                            if (!tokenFirebase.equals("")) {
                                getJwtTokenFromServer(user);
                            }
                        } else {
                            Snackbar.make(findViewById(R.id.sign_in), R.string.error_autenticacion, Snackbar.LENGTH_SHORT).show();
                            signInButton.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            Snackbar.make(findViewById(R.id.sign_in), R.string.error_autenticacion, Snackbar.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    /*-------------------------------------- getJwtTokenFromServer --------------------------------------------***/

    private void getJwtTokenFromServer(final FirebaseUser user) {

        //Create Object to be posted to Server
        FirebaseToken firebaseToken = new FirebaseToken(tokenFirebase);
        RetrofitClient.getClient().loginUser(firebaseToken)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Log.i(TAG, getString(R.string.is_successful) + call);
                            String tokenJWT = Utils.getJwtTokenFormatted(response.body());
                            prefs.edit().putString(Constants.KEY_TOKEN_JWT, tokenJWT).apply();
                            Log.i(TAG, "Token JWT: " + tokenJWT);
                            Intent comesFromIntent = getIntent();
                            //if user clicked the widget, it should be directed to the camera skipping main activity.
                            if (comesFromIntent.hasExtra(Constants.KEY_INTENT_WIDGET_BUTTON)) {
                                Intent goToCameraDirectly = new Intent(SignInActivity.this, MainActivity.class);
                                goToCameraDirectly.putExtra(Constants.KEY_INTENT_WIDGET_BUTTON, "");
                                startActivity(goToCameraDirectly);
                            } else {
                                //if the user is signining in regularly, we should check if it works in more than one company.
                                getBusinessesWhereUserWorks(user);
                            }
                        } else {
                            signInButton.setVisibility(View.VISIBLE);
                            Log.e(TAG, response.message());
                            showErrorDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        signInButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        if (t instanceof NoConnectivityException) {
                            Toast.makeText(SignInActivity.this, getString(R.string.error_conexion), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInActivity.this, getString(R.string.error_servidor), Toast.LENGTH_SHORT).show();
                        }
                        t.printStackTrace();
                        showErrorDialog();
                    }
                });
    }

    /**
     * If the user has no connectivity or there is some sort of error with the server, this dialog should appear,
     * giving the user the possibility to scan the QR code and save it in the DB to be posted to the server later,
     * when connectivity is resumed or the server is up again.
     */
    private void showErrorDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SignInActivity.this, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SignInActivity.this);
        }
        builder.setTitle(R.string.error)
                .setMessage(R.string.sign_in_error)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    Intent intentToCamera = new Intent(SignInActivity.this, CameraActivity.class);
                    intentToCamera.putExtra(Constants.KEY_INTENT_OFFLINE_SCAN, "");
                    startActivity(intentToCamera);
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    // do nothing
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void getBusinessesWhereUserWorks(final FirebaseUser user) {

        final String callName = "getBusinesses";
        RetrofitClient.getClient().getBusinesses()
                .enqueue(new Callback<List<Business>>() {
                    @Override
                    public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                        progressBar.setVisibility(View.INVISIBLE);

                        if (response.isSuccessful()) {
                            Log.i(TAG, getString(R.string.is_successful) + callName);
                            List<Business> businessesList;
                            try {
                                businessesList = response.body();
                                assert businessesList != null;
                                if (businessesList.size() == 1) {
                                    intentToMainActivity(businessesList.get(0));
                                } else {
                                    //if user works in more than one business, create dialog asking to choose which
                                    //business he is interested in retrieving the information from.
                                    createDialogToChoseBusiness(businessesList);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            signInButton.setVisibility(View.VISIBLE);
                            Log.e(TAG, response.message());
                            showErrorDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Business>> call, Throwable t) {
                        signInButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        try {
                            Log.e(TAG, t.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showErrorDialog();
                    }
                });
    }

    private void createDialogToChoseBusiness(final List<Business> businessesList) {

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SignInActivity.this, android.R.layout.select_dialog_item);
        for (Business c : businessesList) {
            arrayAdapter.add(c.getUserComercio());
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SignInActivity.this);
        builderSingle.setTitle(R.string.seleccionar_comercio);
        builderSingle.setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.dismiss());
        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            Business selectedBusiness = businessesList.get(which);
            intentToMainActivity(selectedBusiness);
        });
        builderSingle.show();
    }

    private void intentToMainActivity(Business selectedBusiness) {
        prefs.edit().putLong(Constants.KEY_BUSINESS_ID, selectedBusiness.getUserId()).apply();
        prefs.edit().putString(Constants.KEY_BUSINESS_NAME, selectedBusiness.getUserComercio()).apply();
        Intent goToMainActivity = new Intent(SignInActivity.this, MainActivity.class);
        startActivity(goToMainActivity);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }

}
