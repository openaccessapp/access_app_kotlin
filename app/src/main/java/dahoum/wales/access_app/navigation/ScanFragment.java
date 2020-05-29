package dahoum.wales.access_app.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;

public class ScanFragment extends Fragment {

    private SurfaceView cameraView;
    private CameraSource cameraSource;

    public ScanFragment() {
        // Required empty public constructor
    }

    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);

        cameraView = rootView.findViewById(R.id.cameraView);

        BarcodeDetector detector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getContext(), detector).setRequestedPreviewSize(640, 480).build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException e) {
                    Log.e("CAMERA SOURCE", e.getMessage());
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

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    Toast.makeText(getContext(), barcodes.valueAt(0).displayValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
    }
}
