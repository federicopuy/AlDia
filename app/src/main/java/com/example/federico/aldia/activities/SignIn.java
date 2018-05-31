package com.example.federico.aldia.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.federico.aldia.R;
import com.example.federico.aldia.model.Comercio;
import com.example.federico.aldia.utils.Constantes;
import com.example.federico.aldia.model.TokenRetro;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.network.RetrofitClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity implements
        View.OnClickListener{




    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private TextView mStatusTextView;
    SharedPreferences prefs;
    APIInterface mService;


    String tokenFirebase = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);


        mStatusTextView = findViewById(R.id.status);

        prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);

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
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }

    /*-------------------------------------- Autenticacion con Google --------------------------------------------***/


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        // showProgressDialog();

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

                          //  Snackbar.make(findViewById(R.id.sign_in), "Error de Autenticación", Snackbar.LENGTH_SHORT).show();


                            updateUI(null);
                        }

                        //     hideProgressDialog();

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

        //   hideProgressDialog();

        if (user != null) {
            mStatusTextView.setText(("Email " +  user.getEmail()));
            //Snackbar.make(findViewById(R.id.sign_in), "Authentication Successful", Snackbar.LENGTH_SHORT).show();

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);


            user.getIdToken(true)
                    .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()){

                                tokenFirebase = task.getResult().getToken();

                                Log.d(TAG, "Token Firebase " + tokenFirebase);

                                prefs.edit().putString(Constantes.KEY_TOKEN_FIREBASE, tokenFirebase).apply();

                                if(!tokenFirebase.equals("")){

                                    servicioEnviarToken(user);

                                }

                            }
                        }
                    });



        } else {

           // mStatusTextView.setText(R.string.signed_out);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        }

    }

    private void servicioEnviarToken(final FirebaseUser user) {

         mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);

        TokenRetro tokenRetro = new TokenRetro(tokenFirebase);

        Call<String> authenticationCall = mService.loginUser(tokenRetro);

        authenticationCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                Log.i(TAG, "Login User OK");

                if (response.isSuccessful()) {

                    String tokenJWT = "";

                    try {


                        //todo meter en un utils

                        JSONObject root = new JSONObject(response.body());

                        String bearer = root.getString("id_token");

                        tokenJWT = "Bearer " + bearer;

                        System.out.println("JWT" + tokenJWT);

                        prefs.edit().putString(Constantes.KEY_TOKEN_JWT, tokenJWT).apply();

                        obtenerComercios(user);

                        Log.i(TAG, "Token JWT: " + tokenJWT);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "Token NULO");
                    }

                } else {

                    try {
                        Log.i(TAG, "Error en API Login " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                Log.i(TAG, "On Failure Login Usuario");

                try{

                    Log.e(TAG, t.getMessage());

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private void obtenerComercios(final FirebaseUser user) {

        Call<List<Comercio>> obtenerComerciosEmpleado = mService.getComercios();

        obtenerComerciosEmpleado.enqueue(new Callback<List<Comercio>>() {
            @Override
            public void onResponse(Call<List<Comercio>> call, Response<List<Comercio>> response) {

                Log.i(TAG, "Get Comercios OK");

                if (response.isSuccessful()) {

                    List<Comercio> listaComercios = new ArrayList<>();

                    try {

                        listaComercios = response.body();

                        for (Comercio comercio:listaComercios) {


                            Log.i(TAG, "Comercio 1 USER ID: " + comercio.getUserId());

                            Log.i(TAG, "Comercio 1 USER COMERCIO: " + comercio.getUserComercio());

                            prefs.edit().putLong(Constantes.KEY_COMERCIO_ID, comercio.getUserId()).apply();

                        }

                            Intent pasarAMainActivity = new Intent(SignIn.this, MainActivity.class);

                            final String nombreUsuario = user.getDisplayName();
                            final String email = user.getEmail();
                            final String imagenUsuario = user.getPhotoUrl().toString();

                            pasarAMainActivity.putExtra(Constantes.KEY_INTENT_NOMBRE_USUARIO, nombreUsuario);

                            pasarAMainActivity.putExtra(Constantes.KEY_INTENT_EMAIL_USUARIO, email);

                            pasarAMainActivity.putExtra(Constantes.KEY_INTENT_IMAGEN_USUARIO, imagenUsuario);

                            startActivity(pasarAMainActivity);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {

                    try {
                        Log.i(TAG, "Get Comercios IS NOT SUCCESFUL " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Comercio>> call, Throwable t) {

                Log.i(TAG, "On Failure Get Comercios");

                try{

                    Log.e(TAG, t.getMessage());

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });




    }


    /*-------------------------------------- On Clicks --------------------------------------------***/


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        }
    }

}
