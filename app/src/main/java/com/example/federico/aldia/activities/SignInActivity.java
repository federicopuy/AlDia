package com.example.federico.aldia.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Business;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.model.TokenRetro;
import com.example.federico.aldia.network.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.io.IOException;
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
        //todo implementar en main
        super.onStart();
        Intent intentCerrarSesion = getIntent();
        if (intentCerrarSesion.hasExtra(Constants.KEY_INTENT_SIGN_OUT)){
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

    /*-------------------------------------- Autenticacion con Google --------------------------------------------***/


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        progressBar.setVisibility(View.VISIBLE);
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
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
                    }
                });
    }
    // [END auth_with_google]

    /*-------------------------------------- Sign IN y Sign Out --------------------------------------------***/


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
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
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()) {
                                tokenFirebase = task.getResult().getToken();
                                Log.d(TAG, "Token Firebase " + tokenFirebase);
                                prefs.edit().putString(Constants.KEY_TOKEN_FIREBASE, tokenFirebase).apply();
                                if (!tokenFirebase.equals("")) {
                                    servicioEnviarToken(user);
                                }
                            } else {
                                Snackbar.make(findViewById(R.id.sign_in), R.string.error_autenticacion, Snackbar.LENGTH_SHORT).show();
                                signInButton.setVisibility(View.VISIBLE);

                            }
                        }
                    });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        }
    }

    /*-------------------------------------- servicioEnviarToken --------------------------------------------***/

    private void servicioEnviarToken(final FirebaseUser user) {
        final String nombreLlamada = "postToken";

        TokenRetro tokenRetro = new TokenRetro(tokenFirebase);
        RetrofitClient.getClient().loginUser(tokenRetro)
        .enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
                    String tokenJWT = "";
                    System.out.println(call.toString());
                    try {
                        JSONObject root = new JSONObject(response.body());
                        String bearer = root.getString("id_token");
                        tokenJWT = "Bearer " + bearer;
                        System.out.println("JWT" + tokenJWT);
                        prefs.edit().putString(Constants.KEY_TOKEN_JWT, tokenJWT).apply();
                        Log.i(TAG, "Token JWT: " + tokenJWT);

                        Intent comesFromIntent = getIntent();
                        if (comesFromIntent.hasExtra(Constants.KEY_INTENT_WIDGET_BUTTON)){

                            Intent goToCameraDirectly = new Intent(SignInActivity.this, MainActivity.class);
                            goToCameraDirectly.putExtra(Constants.KEY_INTENT_WIDGET_BUTTON,"");
                            startActivity(goToCameraDirectly);

                        } else {
                            obtenerComercios(user);
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Token NULO");
                        signInButton.setVisibility(View.VISIBLE);
                        Snackbar.make(findViewById(R.id.sign_in), R.string.error_servidor, Snackbar.LENGTH_SHORT).show();

                    }

                } else {
                    signInButton.setVisibility(View.VISIBLE);
                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
                    Snackbar.make(findViewById(R.id.sign_in), R.string.error_servidor, Snackbar.LENGTH_SHORT).show();
                    try {
                        Log.i(TAG, "Error en API Login " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                signInButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(findViewById(R.id.sign_in), R.string.error_servidor, Snackbar.LENGTH_SHORT).show();
                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
                try {
                    Log.e(TAG, t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void obtenerComercios(final FirebaseUser user) {

        final String nombreLlamada = "obtenerComercios";

        RetrofitClient.getClient().getBusinesses()
                .enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                progressBar.setVisibility(View.INVISIBLE);

                if (response.isSuccessful()) {
                    Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
                    List<Business> businessesList;
                    try {
                        businessesList = response.body();
                            assert businessesList != null;
                            crearDialog(businessesList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                        Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
                }
            }

            @Override
            public void onFailure(Call<List<Business>> call, Throwable t) {
                Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
                progressBar.setVisibility(View.INVISIBLE);
                try {
                    Log.e(TAG, t.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void crearDialog(final List<Business> businessesList) {

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SignInActivity.this, android.R.layout.select_dialog_item);
        for (Business c : businessesList) {
            arrayAdapter.add(c.getUserComercio());
        }

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(SignInActivity.this);
        builderSingle.setTitle(R.string.seleccionar_comercio);
        builderSingle.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Business selectedBusiness = businessesList.get(which);
                prefs.edit().putLong(Constants.KEY_BUSINESS_ID, selectedBusiness.getUserId()).apply();
                prefs.edit().putString(Constants.KEY_BUSINESS_NAME, selectedBusiness.getUserComercio()).apply();
                Intent pasarAMainActivity = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(pasarAMainActivity);

            }
        });
        builderSingle.show();
}

    /*-------------------------------------- On Clicks --------------------------------------------***/

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }

}
