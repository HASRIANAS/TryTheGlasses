package com.example.pfa;

import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationEngineListener, PermissionsListener,
        MapboxMap.OnMapClickListener{

        private MapView mapView;
        private MapboxMap map;
        private PermissionsManager permissionsManager;
        private LocationEngine locationEngine;
        private LocationLayerPlugin locationLayerPlugin;
        private Location originLocation;
        private Point originPosition;
        private Point destinationPosition;
        private Marker destinationMarker;
        private Button startButton;
        private NavigationMapRoute navigationMapRoute;
        private static final String TAG="MapActivity";



        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                Mapbox.getInstance(this,getString(R.string.access_token));
                setContentView(R.layout.activity_map);
                mapView=(MapView) findViewById(R.id.mapView);
                mapView.onCreate(savedInstanceState);
                mapView.getMapAsync(this);
                startButton=(Button) findViewById(R.id.startButton);
                startButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                NavigationLauncherOptions options= NavigationLauncherOptions.builder()
                                        .origin(originPosition)
                                        .destination(destinationPosition)
                                        .shouldSimulateRoute(true)
                                        .build();
                                NavigationLauncher.startNavigation(MapActivity.this,options);
                        }
                });

        }
        private void getRoute(Point origin,Point destination){
                NavigationRoute.builder().accessToken(Mapbox.getAccessToken())
                        .origin(origin)
                        .destination(destination)
                        .build()
                        .getRoute(new Callback<DirectionsResponse>() {
                                @Override
                                public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                                        if(response.body()==null){
                                                Log.e(TAG,"No routes found");
                                                return;
                                        }else if(response.body().routes().size()==0){
                                                Log.e(TAG,"No routes Found");
                                        }
                                        DirectionsRoute currentRoute=response.body().routes().get(0);
                                        if(navigationMapRoute!=null){
                                                navigationMapRoute.removeRoute();
                                        }else{
                                                navigationMapRoute=new NavigationMapRoute(null,mapView,map);
                                                navigationMapRoute.addRoute((currentRoute));}
                                }

                                @Override
                                public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                                        Log.e(TAG,"Error:"+t.getMessage());

                                }
                        });
        }
        @Override
        public void onMapReady(MapboxMap mapboxMap) {
                map=mapboxMap;
                map.addOnMapClickListener(this);
                enableLocation();
        }

        private void enableLocation(){
                if(PermissionsManager.areLocationPermissionsGranted(this)){
                        initialiteLocationEngine();
                        initializeLocationLayer();
                }else{
                        permissionsManager=new PermissionsManager(this);
                        permissionsManager.requestLocationPermissions(this);
                }
        }
        private void initialiteLocationEngine(){
                locationEngine=new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
                locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
                locationEngine.activate();

                Location lastlocation=locationEngine.getLastLocation();
                if(lastlocation!=null){
                        originLocation=lastlocation;
                        setCameraPosition(lastlocation);
                }else {
                        locationEngine.addLocationEngineListener(this);
                }
        }
        private void initializeLocationLayer(){
                locationLayerPlugin=new LocationLayerPlugin(mapView,map,locationEngine);
                locationLayerPlugin.setLocationLayerEnabled(true);
                locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
                locationLayerPlugin.setRenderMode(RenderMode.NORMAL);

        }
        private void setCameraPosition(Location location){
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),13.0));
        }
        @Override
        protected void onStart() {
                super.onStart();
                if(locationEngine!=null){
                        locationEngine.removeLocationUpdates();
                }
                if(locationLayerPlugin!=null){
                        locationLayerPlugin.onStart();
                }
                mapView.onStart();
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
                if(locationEngine!=null){
                        locationEngine.deactivate();
                }
                mapView.onDestroy();
        }

        @Override
        protected void onStop() {
                super.onStop();
                if(locationEngine!=null){
                        locationEngine.removeLocationUpdates();
                }
                if(locationLayerPlugin!=null){
                        locationLayerPlugin.onStop();
                }
                mapView.onStop();
        }

        @Override
        public void onLowMemory() {
                super.onLowMemory();
                mapView.onLowMemory();
        }

        @Override
        protected void onResume() {
                super.onResume();
                mapView.onResume();
        }

        @Override
        protected void onPause() {
                super.onPause();
                mapView.onPause();
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
                super.onSaveInstanceState(outState, outPersistentState);
                mapView.onSaveInstanceState(outState);
        }

        @Override
        public void onConnected() {
                locationEngine.requestLocationUpdates();
        }

        @Override
        public void onLocationChanged(Location location) {
                if(location!=null){
                        originLocation=location;
                        setCameraPosition(location);
                }
        }

        @Override
        public void onExplanationNeeded(List<String> permissionsToExplain) {

        }

        @Override
        public void onPermissionResult(boolean granted) {
                if(granted){
                        enableLocation();
                }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
        }

        @Override
        public void onMapClick(@NonNull LatLng point) {
                if(destinationPosition!=null){
                        map.removeMarker(destinationMarker);
                }
                destinationMarker=map.addMarker(new MarkerOptions().position(point));
                destinationPosition= Point.fromLngLat(point.getLongitude(),point.getLatitude());
                originPosition=Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());
                startButton.setEnabled(true);
                startButton.setBackgroundResource(R.color.mapbox_blue);
                getRoute(originPosition,destinationPosition);

        }
}
