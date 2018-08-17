package com.example.federico.aldia.activities;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.federico.aldia.R;
import com.example.federico.aldia.activities.barcode.BarcodeScanningProcessor;
import com.example.federico.aldia.activities.barcode.CameraSource;
import com.example.federico.aldia.activities.barcode.CameraSourcePreview;
import com.example.federico.aldia.activities.barcode.GraphicOverlay;
import com.example.federico.aldia.activities.barcode.QRDetectedListener;
import com.example.federico.aldia.model.QrToken;
import com.example.federico.aldia.model.Resource;
import com.example.federico.aldia.model.Status;
import com.example.federico.aldia.network.AppController;
import com.example.federico.aldia.notifications.ReminderUtilities;
import com.example.federico.aldia.utils.Constants;
import com.example.federico.aldia.model.Periodo;
import com.example.federico.aldia.network.APIInterface;
import com.example.federico.aldia.utils.Utils;
import com.example.federico.aldia.viewmodel.CameraActivityViewModel;
import com.example.federico.aldia.widget.ScanWidgetProvider;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CameraActivity extends AppCompatActivity implements QRDetectedListener{

    private static final String BARCODE_DETECTION = "Barcode Detection";

    private CameraSource cameraSource = null;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private String selectedModel = BARCODE_DETECTION;
    private static final int PERMISSION_REQUESTS = 1;
    private APIInterface mService;
    int i = 1;


    private static final String TAG = "CameraActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        preview = findViewById(R.id.firePreview);
        if (preview == null) {
            Log.d(TAG, "Preview is null");
        }
        graphicOverlay = findViewById(R.id.fireFaceOverlay);
        if (graphicOverlay == null) {
            Log.d(TAG, "graphicOverlay is null");
        }

        if (allPermissionsGranted()) {

            createCameraSource(BARCODE_DETECTION);
            startCameraSource();

        } else {
            getRuntimePermissions();
        }

    }

    @Override
    public void QRDetected(String rawValue) {

        if (i == 1) {
            i++;

            Date currentTime = Calendar.getInstance().getTime();
            QrToken qrToken = new QrToken(rawValue, currentTime.toString());



            CameraActivityViewModel.Factory factory = new CameraActivityViewModel.Factory(AppController.get(this), qrToken);
            CameraActivityViewModel cameraActivityViewModel = ViewModelProviders.of(this,factory).get(CameraActivityViewModel.class);
            cameraActivityViewModel.postQrTokenToServer().observe(this, periodoResource -> {
                final String nombreLlamada = "postNewPeriodo";

                if (periodoResource.status == Status.FAILED) {

                    Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);

                    cameraActivityViewModel.insert(qrToken);


                    Intent returnIntent = getIntent();
                    setResult(Activity.RESULT_CANCELED, returnIntent);
                    finish();


                } else {
                    if (periodoResource.status == Status.SUCCESS) {
                        Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);


                        Periodo periodoEscaneado = periodoResource.data;

                        //Widget
                        String endOfTime = Utils.getEndOfShiftTime(periodoEscaneado.getCategoria().getHorasTrabajo());
                        //copied from https://stackoverflow.com/questions/4424723/android-appwidget-wont-update-from-activity
                        SharedPreferences prefs  = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        prefs.edit().putString(Constants.KEY_INTENT_WIDGET, endOfTime).apply();

                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ScanWidgetProvider.class));
                        if (appWidgetIds.length > 0) {
                            new ScanWidgetProvider().onUpdate(this, appWidgetManager, appWidgetIds);
                        }

                        //Scheduled Notification
                        ReminderUtilities.scheduleShiftEndReminder(this, Utils.minutesTillEndOfShift(periodoEscaneado.getCategoria().getHorasTrabajo()));

                        Intent pasarAIngresoEgreso = new Intent(CameraActivity.this, EntryExitActivity.class);
                        Gson gsonPeriodo = new Gson();
                        pasarAIngresoEgreso.putExtra(Constants.KEY_INTENT_PERIODO_INGRESO_EGRESO, gsonPeriodo.toJson(periodoEscaneado));
                        pasarAIngresoEgreso.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                        startActivity(pasarAIngresoEgreso);
                        finish();


                    } else {
                        //todo show progress bar
                    }
                }


            });

        }


//
//
//
//            final String nombreLlamada = "postNewPeriodo";
//            mService = RetrofitClient.getClient(getApplicationContext()).create(APIInterface.class);
//            TokenQR tokenQR = new TokenQR(rawValue);
//            Call<Periodo> postNewPeriodo = mService.newPeriodo(tokenQR);
//            postNewPeriodo.enqueue(new Callback<Periodo>() {
//                @Override
//                public void onResponse(Call<Periodo> call, Response<Periodo> response) {
//
//                    Log.i(TAG, getString(R.string.on_response) + nombreLlamada);
//
//                    if (response.isSuccessful()) {
//
//                        Log.i(TAG, getString(R.string.is_successful) + nombreLlamada);
//                        try {
//
//                            Periodo periodoEscaneado = response.body();
//                            Intent pasarAIngresoEgreso = new Intent(CameraActivity.this, EntryExitActivity.class);
//                            Gson gsonPeriodo = new Gson();
//                            pasarAIngresoEgreso.putExtra(Constants.KEY_INTENT_PERIODO_INGRESO_EGRESO, gsonPeriodo.toJson(periodoEscaneado));
//                            pasarAIngresoEgreso.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
//                            startActivity(pasarAIngresoEgreso);
//                            finish();
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else {
//
//                        Log.i(TAG, getString(R.string.is_not_successful) + nombreLlamada);
//                        try {
//                            Log.e(TAG, response.errorBody().string());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        Intent returnIntent = getIntent();
//                        setResult(Activity.RESULT_CANCELED, returnIntent);
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Periodo> call, Throwable t) {
//
//                    Log.i(TAG, getString(R.string.on_failure) + nombreLlamada);
//                    System.out.println("Message " + t.getMessage());
//                    Intent returnIntent = getIntent();
//                    setResult(Activity.RESULT_CANCELED, returnIntent);
//                    finish();
//                }
//            });
//
//
//        }

    }



    private void createCameraSource(String model) {
        // If there's no existing cameraSource, create one.
        if (cameraSource == null) {
            cameraSource = new CameraSource(this, graphicOverlay);
            cameraSource.setFacing(CameraSource.CAMERA_FACING_BACK);
        }
        Log.i(TAG, "Using Barcode Detector Processor");
        try {
            cameraSource.setMachineLearningFrameProcessor(new BarcodeScanningProcessor(this));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Starts or restarts the camera source, if it exists. If the camera source doesn't exist yet
     * (e.g., because onResume was called before the camera source was created), this will be called
     * again when the camera source is created.
     */
    private void startCameraSource() {
        if (cameraSource != null) {
            try {
                if (preview == null) {
                    Log.d(TAG, "resume: Preview is null");
                }
                if (graphicOverlay == null) {
                    Log.d(TAG, "resume: graphOverlay is null");
                }
                preview.start(cameraSource, graphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        preview.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    this.getPackageManager()
                            .getPackageInfo(this.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                return false;
            }
        }
        return true;
    }

    private void getRuntimePermissions() {
        List<String> allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        Log.i(TAG, "Permission granted!");
        if (allPermissionsGranted()) {
            createCameraSource(selectedModel);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Permission granted: " + permission);
            return true;
        }
        Log.i(TAG, "Permission NOT granted: " + permission);
        return false;
    }


}