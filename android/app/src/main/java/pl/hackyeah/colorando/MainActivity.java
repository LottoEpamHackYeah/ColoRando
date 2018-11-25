package pl.hackyeah.colorando;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import pl.hackyeah.colorando.repository.dto.CodeResult;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {

    private SurfaceView surfaceView;
    private TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String intentData = "";
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
    }

    private void initialiseDetectorsAndSources() {
        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                Manifest.permission.CAMERA
                        }, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(() -> {
                        if (barcodes.valueAt(0).email != null) {
                            txtBarcodeValue.removeCallbacks(null);
                            intentData = barcodes.valueAt(0).email.address;
                        } else {
                            intentData = barcodes.valueAt(0).displayValue;
                        }

                        Log.i("ADDRESS", intentData);
                        PostScannedQRCode service = RetrofitClient.getInstance("http://10.250.194.105:8080/")
                                .create(PostScannedQRCode.class);

                        Call<CodeResult> c = service.postQRCode(intentData, "krakow-market-square");

                        c.enqueue(new Callback<CodeResult>() {
                            @Override
                            public void onResponse(Call<CodeResult> call, Response<CodeResult> response) {
                                int code = response.code();

                                Log.i("CODE", String.valueOf(code));

                                switch (code) {
                                    case 200:
                                        CodeResult cr = response.body();
                                        if (cr.isValid()) {
                                            txtBarcodeValue.setText("Valid");
                                            cameraSource.stop();
                                            if (alert == null) {
                                                alert = new AlertDialog.Builder(MainActivity.this)
                                                        .setTitle("Payment")
                                                        .setMessage("Please, confirm payment?")
                                                        .setPositiveButton("Yes",
                                                                (dialog, which) -> {
                                                                    startActivity(GameActivity.class);
                                                                    dialog.cancel();
                                                                })
                                                        .setNegativeButton("No",
                                                                (dialog, which) -> {
                                                                    try {
                                                                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                                                            cameraSource.start(surfaceView.getHolder());
                                                                        } else {
                                                                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                                                                    Manifest.permission.CAMERA
                                                                            }, REQUEST_CAMERA_PERMISSION);
                                                                        }
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    dialog.cancel();
                                                                })
                                                        .create();
                                            }

                                            if (!alert.isShowing()) {
                                                alert.show();
                                            }
                                        }
                                        break;
                                }
                            }

                            @Override
                            public void onFailure(Call<CodeResult> call, Throwable t) {
                                t.printStackTrace();
                                txtBarcodeValue.setText(t.getMessage());
                            }
                        });
                    });

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
        txtBarcodeValue.setText(R.string.code_result);
    }
}
