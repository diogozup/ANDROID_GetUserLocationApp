package com.pandapanda.get_user_location;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

                //limpar smepre um marcador antes de adicionar um novo. senao temos breadscrums
                mMap.clear();
                // Add um Marker na posicao do utilizador
                LatLng localUtilizador = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(localUtilizador).title("Minha Localizacao"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localUtilizador,15));

            /*
            * ##GEOCODING:
            * processo de transformar um endereco ou descricao
            * de um local em latitude/longitude
            *
            * ##REVERSE GEOCODING
            * processo de transformar latitude/longitude em um endereco
            * */

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault() );//location default usa localizacao do user

                try {
                    //List<Address> listaEndereco = geocoder.getFromLocation(latitude,longitude,1);
                    String stringEndereco = "R. do Curral, 4610-156 Margaride (Santa Eulália)";
                    List<Address> listaEndereco = geocoder.getFromLocationName(stringEndereco,1);
                    if(listaEndereco != null && listaEndereco.size()>0){
                        Address endereco = listaEndereco.get(0); // primeiro encontrado


                        /* ISTO PORQUE ESCREVI LOG.D e fui buscar aqui no log as informacoes de "endereco"
                        * onLocationChanged:
                        * Address[
                        * addressLines=[
                        * 0:"
                        * Av. Dr. Leonardo Coimbra 312,
                        *  4610-105 Felgueiras,
                        *  Portugal
                        * "],
                        * feature=312,
                        * admin=Porto,
                        * sub-admin=null,
                        * locality=Felgueiras,
                        * thoroughfare=
                        * Avenida Doutor Leonardo Coimbra,
                        * postalCode=4610-105,
                        * countryCode=PT,
                        * countryName=Portugal,
                        * hasLatitude=true,
                        * latitude=41.364060099999996,
                        * hasLongitude=true,
                        * longitude=-8.1996658,
                        * phone=null,
                        * url=null,
                        * extras=null]
                        * */

                        /*GeoCode*/
                        //Log.d("local","onLocationChanged: " + endereco.getAddressLine(0));

                        /*REVERSE GeoCode*/
                        Log.d("local","onLocationChanged: " + endereco.toString());

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }




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
