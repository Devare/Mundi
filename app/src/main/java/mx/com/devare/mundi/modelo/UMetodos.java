package mx.com.devare.mundi.modelo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import static mx.com.devare.mundi.modelo.Constantes.EXTRA_MSJ_BROADCASTRECEIVER;
import static mx.com.devare.mundi.modelo.Constantes.EXTRA_RESULTADO;

public class UMetodos {

    public static void  enviarBroadcast(boolean estado, String mensaje,Context context) {
        Intent intentLocal = new Intent(Intent.ACTION_SYNC);
        intentLocal.putExtra(EXTRA_RESULTADO, estado);
        intentLocal.putExtra(EXTRA_MSJ_BROADCASTRECEIVER, mensaje);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intentLocal);
    }


}
