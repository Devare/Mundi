package mx.com.devare.mundi.controlador;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;


/***********************************
 *  Diálogo de conexión GPS
 ***********************************/
public class DialogoConexionGPS extends DialogFragment{


    public static DialogoConexionGPS newInstance() {
        return new DialogoConexionGPS();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder conectarGPS = new AlertDialog.Builder(getActivity());
        conectarGPS.setTitle("Enable Location");
        conectarGPS.setMessage("Su ubicación esta desactivada.\n por favor active su ubicación ");
        conectarGPS.setCancelable(false);

        conectarGPS.setPositiveButton("Habilitar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface mDialogInterface, int id) {
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settingsIntent);
            }
        });

        conectarGPS.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface mDialogInterface, int id) {
                mDialogInterface.dismiss();
            }
        });
        return conectarGPS.create();
    }

}
