package mx.com.devare.mundi.controlador;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mx.com.devare.mundi.R;

public class MainActivity extends AppCompatActivity {

    TextView tv_latitud, tv_longitud, tv_direccion, tv_estado_gps;

    LocationManager locationManager;
    public static final int REQUEST_CODE = 1000;
    String mPermisoFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
    String mPermisoCoarseLocation = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int GRANTED = PackageManager.PERMISSION_GRANTED;
    String gpsProvider = LocationManager.GPS_PROVIDER;
    String networkProvider = LocationManager.NETWORK_PROVIDER;
    LocationManager mlocManager;
    LocalizacionListener mLocalizacionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        inicializarComponentesUI();
    }

    private void inicializarComponentesUI() {
        tv_latitud = (TextView) findViewById(R.id.tv_latitud);
        tv_longitud = (TextView) findViewById(R.id.tv_longitud);
        tv_direccion = (TextView) findViewById(R.id.tv_direccion);
        tv_estado_gps = (TextView) findViewById(R.id.tv_estado_gps);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checarPermisos();
    }

    private void checarPermisos() {
        if (checkPermission(mPermisoFineLocation) != GRANTED && checkPermission(mPermisoCoarseLocation) != GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{mPermisoFineLocation,}, REQUEST_CODE);
        } else {
            obtenerLocalizacion();
        }
    }

    private int checkPermission(String namePermisssion) {
        return ActivityCompat.checkSelfPermission(this, namePermisssion);
    }

    private void obtenerLocalizacion() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocalizacionListener = new LocalizacionListener();
        mLocalizacionListener.setMainActivity(this);

        if (!checkLocation()) {
            return;
        }

        if (checkPermission(mPermisoFineLocation) != GRANTED && checkPermission(mPermisoCoarseLocation) != GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{mPermisoFineLocation,}, REQUEST_CODE);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocalizacionListener);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocalizacionListener);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == GRANTED) {
                obtenerLocalizacion();
            } else {
                Toast.makeText(this, "EL permiso es esencial para poder continuar", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private boolean checkLocation() {
        if (!isLocationEnabled())
            showDialogConexion();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return isProviderHabilitado(gpsProvider) || isProviderHabilitado(networkProvider);
    }

    private boolean isProviderHabilitado(String provider) {
        return locationManager.isProviderEnabled(provider);
    }

    private void showDialogConexion() {

        FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragmentByTag = getSupportFragmentManager().findFragmentByTag("DialogoConexionGPS");
        if (fragmentByTag != null) {
            mFragmentTransaction.remove(fragmentByTag);
        }
        DialogoConexionGPS mDialogoConexionGPS = DialogoConexionGPS.newInstance();
        mFragmentTransaction.add(mDialogoConexionGPS, "DialogoConexionGPS");
        mFragmentTransaction.commitAllowingStateLoss();
    }


    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    tv_direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}