package com.pandapanda.get_user_location;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] permissoes = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private LocationManager locationManager;
    private LocationListener locationListener;


    //----------------------------------------------------------- onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Validar Permissoes
        Permissoes.validarPermissoes(permissoes, this, 1);

        //Get objecto responsael por gerenciar a localizacao do user
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Localizacao", "onLocationChanged" + location.toString());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //CODIGO QUE PERMITE OBTER LOCALIZACAO DO USER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,   //user GPS como Location provider
                        0,                     //0 milisec significa que nao quero receber periodicamente
                        0,                  //este valor significa que a cada "X" metros que me mova, faz novo update
                        locationListener
                );
            }
        }









        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //----------------------------------------------------------- on Map Ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Get objecto responsael por gerenciar a localizacao do user
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("Localizacao", "onLocationChanged" + location.toString());

                Double latitude = location.getLatitude();
                Double longitude = location.getLongitude();


                // Add um Marker na posicao do utilizador
                LatLng localUtilizador = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(localUtilizador).title("Minha Localizacao"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUtilizador,15));


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //CODIGO QUE PERMITE OBTER LOCALIZACAO DO USER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,   //user GPS como Location provider
                        0,                     //0 milisec significa que nao quero receber periodicamente
                        0,                  //este valor significa que a cada "X" metros que me mova, faz novo update
                        locationListener
                );
            }
        }

    }


















    //----------------------------------------------------------------------------- onRequestePermissionResult
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                // ALERTA
                alertaValidacaoPermissao();
            } else if (permissaoResultado == PackageManager.PERMISSION_GRANTED) {
                // GET Localizar User

                /* -- O que vamos precisar para para este Ojecto para ober localizacao
                 * 1) Provedor da localizacao
                 * 2) Tempo minimo entre updates da localizacao
                 * 3) Distancia minima entre updates de localizacao
                 * 4) Localizacao listener (para recebermos os updates de localizcao)
                 * */

                //CODIGO QUE PERMITE OBTER LOCALIZACAO DO USER
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,   //user GPS como Location provider
                                0,                     //0 milisec significa que nao quero receber periodicamente
                                0,                  //este valor significa que a cada "X" metros que me mova, faz novo update
                                locationListener
                        );
                    }
                }




            }
        }

    }


    //----------------------------------------------------------------- AUX METHODS
    private void alertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes Negadas");
        builder.setMessage("Para utilizar o app, é necessario aceitar as permissoes GPS");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
   }






}
