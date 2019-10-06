package com.sourcey.materiallogindemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.sourcey.materiallogindemo.Adapter.ListAdapter;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import br.com.joinersa.oooalertdialog.Animation;
import br.com.joinersa.oooalertdialog.OnClickListener;
import br.com.joinersa.oooalertdialog.OoOAlertDialog;

import static com.sourcey.materiallogindemo.Shared.AddRandomButton;
import static com.sourcey.materiallogindemo.Shared.Array;
import static com.sourcey.materiallogindemo.Shared.offer;
import static com.sourcey.materiallogindemo.Shared.user;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    Boolean mark=false;
    private GoogleMap mMap;
    TabLayout tb;
    boolean polygon = false;
    Polygon polygon1;

    List<LatLng> latLngList;

    Button confirmOffer;
    TextView cityName;
    FloatingActionButton fabAdd;
    TextView type,name;
    CardView cvDelet;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        if(user.getType().equals(Array[0])) {
            cvDelet=findViewById(R.id.cvDelet);
            cvDelet.setVisibility(View.VISIBLE);
            type = findViewById(R.id.type);
            name = findViewById(R.id.userName);

            if(!Shared.addOfferNewRandom) {
                type.setText("النوع :"+Shared.offer.getType());
                name.setText("إسم العميل : "+ Shared.offer.getUserName());
            }
            fabAdd = findViewById(R.id.fabAdd);
            fabAdd.setVisibility(View.VISIBLE);
            cityName = findViewById(R.id.cityName);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Shared.longtuide == -1 || Shared.lituide == -1) {
                        if (!mark)
                            Toast.makeText(MapsActivity.this, "من فضلك حدد مكان على الخريطه", Toast.LENGTH_SHORT).show();

                    }  else {
                            if (Shared.user.getType().equals(Array[0])) {
                                finish();
                                startActivity(new Intent(MapsActivity.this, AddResultOffer.class));
                            } else {
                                startActivity(new Intent(MapsActivity.this, Add_Offers.class));
                            }
                        }
                    }

            });
            Shared.addOfferNewRandom=false;
        }else{
            RelativeLayout rl=findViewById(R.id.rl);
            rl.setVisibility(View.GONE);
        }

        latLngList = new ArrayList<>();
        confirmOffer = findViewById(R.id.confirmOffer);
        confirmOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (Shared.latLngList.size() == 0)
                    Toast.makeText(MapsActivity.this, "من فضلك حدد النطاف", Toast.LENGTH_SHORT).show();
                else {
                    Shared.AddToMap.setFlit(Shared.latLngList.get(0).latitude);
                    Shared.AddToMap.setFlon(Shared.latLngList.get(0).longitude);

                    Shared.AddToMap.setSlit(Shared.latLngList.get(1).latitude);
                    Shared.AddToMap.setSlon(Shared.latLngList.get(1).longitude);

                    Shared.AddToMap.setTlit(Shared.latLngList.get(2).latitude);
                    Shared.AddToMap.setTlon(Shared.latLngList.get(2).longitude);

                    Shared.AddToMap.setFtlit(Shared.latLngList.get(3).latitude);
                    Shared.AddToMap.setFtlon(Shared.latLngList.get(3).longitude);
                    Shared.AddToMap.setOfferID(Calendar.getInstance().getTime().toString());
                        FirebaseDatabase.getInstance().getReference("OfferNeeded").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Shared.AddToMap.getOfferID()).setValue(Shared.AddToMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MapsActivity.this, "تم إضافة الطلب", Toast.LENGTH_SHORT).show();
                                confirmOffer.setVisibility(View.GONE);
                                finish();
                                startActivity(new Intent(MapsActivity.this, MyOfferNeeded.class));
                            }
                        });
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tb = findViewById(R.id.tablayout);
        String[] tabsArray = new String[]{"الكل", "شقةللإيجار", "فيلا للبيع", "إرض للبيع", "فيلا للإيجار", "دور للإيجار", "شقة للبيع", "عمارة للبيع", "بيت للبيع", "استراحه للإيجار", "بيت للإيجار", "محل للإيجار", "مزرعه للبيع", "عماره للإيجار"};
        for (String s :
                tabsArray) {
            tb.addTab(tb.newTab().setText(s));

        }
        tb.setTabGravity(TabLayout.GRAVITY_FILL);
        if(Shared.addLocation)
            tb.setVisibility(View.GONE);
        tb.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mMap.clear();
                createPolygon();
                if (tab.getPosition() == 0)
                    getData();
                else {
                    //getData(tab.getText().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            onLocationChanged(location);
                        }
                    }
                });

        if(Shared.addLocation){
            polygon = true;
            if (Shared.addOffer) {

                confirmOffer.setVisibility(View.VISIBLE);
                Shared.addOffer = false;
            }
        }
    }
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onPause() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.remove(mapFragment);
        ft.commit();
        super.onPause();

    }

    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale=new Locale("ar");
        Geocoder geoCoder = new Geocoder(this, locale);
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = address.get(0).getAdminArea()+" "+address.get(0).getLocality();//This is the complete address.
            cityName.setText(fnialAddress);


        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private LocationManager locationManager;
    private String provider;

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(!Shared.random &&Shared.user.getType().equals(Shared.Array[0])) {
            getData(Shared.offer.getType(), offer.getBuildingTyp());
            LinearLayout li =findViewById(R.id.li);
            li.setVisibility(View.VISIBLE);
        }
        else{
            Shared.random=false;
        }
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                if (location != null) {
                                    try {
                                        if(Shared.notCurrent){
                                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                                .zoom(17)
                                                .bearing(90)
                                                .tilt(40)
                                                .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));}
                                    }catch (Exception ex){
                                        Toast.makeText(MapsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        throw ex;
                                    }
                                }
                            }
                        }
                    });

            if (Shared.user.getType().equals(Array[0]) && Shared.offer != null&&!Shared.AddRandomButton) {

                createPolygon();
                Shared.getPolygon = !Shared.getPolygon;
                try {
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Shared.offer.getTlit(), Shared.offer.getTlon()), 13));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(new LatLng(Shared.offer.getTlit(), Shared.offer.getTlon()))
                                .zoom(17)
                                .bearing(90)
                                .tilt(40)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
                catch (Exception ex){
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    throw ex;
                }
            }
            final Map<LatLng,Marker> hashMap=new HashMap<>();
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (AddRandomButton) {
                        mMap.clear();
                        latLngList.clear();
                    }
                    if (hashMap.size() > 0) {
                        Map.Entry<LatLng, Marker> entry = hashMap.entrySet().iterator().next();
                        LatLng key = entry.getKey();
                        Marker value = entry.getValue();
                        value.remove();
                        hashMap.clear();
                    }
                    try {


                        mark = true;
                        Shared.lituide = latLng.latitude;
                        Shared.longtuide = latLng.longitude;
                        Marker marker = mMap.addMarker(new MarkerOptions().position(latLng));
                        latLngList.add(latLng);
                        hashMap.put(latLng, marker);
                        if (latLngList.size() >= 4 && !Shared.AddRandomButton) {

                            polygon1 = mMap.addPolygon(new PolygonOptions()
                                    .add(new LatLng(latLngList.get(0).latitude, latLngList.get(0).longitude),
                                            new LatLng(latLngList.get(1).latitude, latLngList.get(1).longitude),
                                            new LatLng(latLngList.get(2).latitude, latLngList.get(2).longitude),
                                            new LatLng(latLngList.get(3).latitude, latLngList.get(3).longitude))
                                    .strokeColor(Color.parseColor("#000000")).fillColor(Color.parseColor("#26E12929"))
                                    .strokeWidth(2));
                            if (!Shared.notCurrent) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLngList.get(0).latitude, latLngList.get(0).longitude), 13));
                                Shared.notCurrent = true;
                            }
                            for (LatLng latLng1 : latLngList) {

                                Shared.latLngList.add(latLng1);
                            }
                            latLngList.clear();
                            Shared.polygon = polygon1;
                            if (Array[0].equals(user.getType())) {
                                mOfferReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                            Offer offer = dataSnapshot1.getValue(Offer.class);
                                            if(offer!=null) {
                                                LatLng latLng = new LatLng(offer.getLituide(), offer.getLongtuide());
                                                MarkerOptions marker = new MarkerOptions().position(latLng);
                                                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.buildmarker)).title(offer.getType());
                                                mMap.addMarker(marker);
                                                marker.snippet(offer.getPrice());
                                            }
                                        }


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else
                                getData();
                        }

                    }catch (Exception ex){
                        finish();
                    }
                }
            });

//            mMap.setBuildingsEnabled(true);
//            mMap.getUiSettings().setZoomControlsEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
        }


    }

    private void createPolygon() {
        MarkerOptions marker = new MarkerOptions().position(new LatLng(Shared.offer.getFlit(), Shared.offer.getFlon()));
        mMap.addMarker(marker);


        marker = new MarkerOptions().position(new LatLng(Shared.offer.getSlit(), Shared.offer.getSlon()));
        mMap.addMarker(marker);


        marker = new MarkerOptions().position(new LatLng(Shared.offer.getTlit(), Shared.offer.getTlon()));
        mMap.addMarker(marker);


        marker = new MarkerOptions().position(new LatLng(Shared.offer.getFtlit(), Shared.offer.getFtlon()));
        mMap.addMarker(marker);

        polygon1 = mMap.addPolygon(new PolygonOptions()
                .add(new LatLng(Shared.offer.getFlit(), Shared.offer.getFlon()),
                        new LatLng(Shared.offer.getSlit(), Shared.offer.getSlon()),
                        new LatLng(Shared.offer.getTlit(), Shared.offer.getTlon()),
                        new LatLng(Shared.offer.getFtlit(), Shared.offer.getFtlon()))
                .strokeColor(Color.parseColor("#000000")).fillColor(Color.parseColor("#26E12929"))
                .strokeWidth(2));
    }

    public void navOffer(View view) {
        startActivity(new Intent(this, goodOffersAct.class));
    }

    public void openBuilding(View view) {
        startActivity(new Intent(this, BuildingAndConstrauct.class));
    }

    public void AddAds(View view) {
        startActivity(new Intent(this, Add_Offers.class));
    }

    public void RemoveMarker(View view) {
        mMap.clear();
        latLngList.clear();
    }

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    DatabaseReference mOfferReference = database.getReference("OfferNeeded");
    List<com.sourcey.materiallogindemo.Model.OfferResult> list;

    HashMap<LatLng, OfferResult> hashMap;

    private void getData() {
        list = new ArrayList<>();
        hashMap = new HashMap<>();
        Shared.listReult = new ArrayList<>();
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    if (dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        for (DataSnapshot dt1 : dt.getChildren()) {
                            OfferResult offer = dt1.getValue(OfferResult.class);
                            Shared.listReult.add(offer);
                            LatLng latLng = new LatLng(offer.getLituide(), offer.getLongtuide());
                            hashMap.put(latLng, offer);

                            IconGenerator iconGen = new IconGenerator(MapsActivity.this);
                            iconGen.setTextAppearance(R.style.iconGenText);
                            MarkerOptions markerOptions = new MarkerOptions().
                                    icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(offer.getPrice()))).
                                    position(latLng).
                                    anchor(iconGen.getAnchorU(), iconGen.getAnchorV());
                            mMap.addMarker(markerOptions);
                        }
                    }
                }
                if(user.getType().equals(Array[0]))
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {
                        final OfferResult noffer = hashMap.get(marker.getPosition());
                        if(noffer!=null) {
                            new OoOAlertDialog.Builder(MapsActivity.this)
                                    .setTitle(noffer.getDescription())
                                    .setMessage(noffer.getPrice())
                                    .setImage(R.drawable.villa)
                                    .setAnimation(Animation.POP)
                                    .setPositiveButton("إرسال الطلب", new OnClickListener() {
                                        @Override
                                        public void onClick() {
                                            FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(noffer.getDescription())){
                                                        Toast.makeText(MapsActivity.this, "تم اضافه هذا العرض من قبل ", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(noffer.getDescription()).setValue(noffer)
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Toast.makeText(MapsActivity.this, "تمت الاضافه", Toast.LENGTH_SHORT).show();
                                                                        new OoOAlertDialog.Builder(MapsActivity.this)
                                                                                .setTitle("تمت الإضافه")
                                                                                .setImage(R.drawable.villa)
                                                                                .setAnimation(Animation.POP)
                                                                                .build();
                                                                        IconGenerator icnGenerator = new IconGenerator(MapsActivity.this);
                                                                        icnGenerator.setTextAppearance(R.style.iconGenText);
                                                                        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buildmarker));
                                                                    }
                                                                });
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton("إلغاء", new OnClickListener() {
                                        @Override
                                        public void onClick() {
                                            Toast.makeText(MapsActivity.this, "الغاء", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setPositiveButtonColor(R.color.primary)
                                    .setNegativeButtonColor(R.color.jet)
                                    .build();

                        }
                            return false;

                    }
                });
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        //  startActivity(new Intent(MapsActivity.this,OfferShare.class));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void getData(final String category,final  String buildType) {
        list = new ArrayList<>();
        hashMap = new HashMap<>();
        Shared.listReult = new ArrayList<>();
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dt : dataSnapshot.getChildren()) {
                    if(dt.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    for (DataSnapshot dt1 : dt.getChildren()) {
                        OfferResult offer = dt1.getValue(OfferResult.class);
                        if (offer.getSpinnerType().equals(category)&&offer.getBuildingType().equals(buildType)) {
                            Shared.listReult.add(offer);
                            LatLng latLng = new LatLng(offer.getLituide(), offer.getLongtuide());
//                                    MarkerOptions marker = new MarkerOptions().position(latLng);
//                                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.buildmarker)).title(offer.GetSpinnerType());
//                                    mMap.addMarker(marker).showInfoWindow();
//                                    marker.snippet(offer.GetPrice());
                            hashMap.put(latLng, offer);

                            IconGenerator iconGen = new IconGenerator(MapsActivity.this);
                            iconGen.setTextAppearance(R.style.iconGenText);
                            MarkerOptions markerOptions = new MarkerOptions().
                                    icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(offer.getPrice()))).
                                    position(latLng).
                                    anchor(iconGen.getAnchorU(), iconGen.getAnchorV());
                            mMap.addMarker(markerOptions);
                        }
                    }
                }
             //   if(list.size()>0)
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(final Marker marker) {

                        final OfferResult noffer = hashMap.get(marker.getPosition());
                        if(noffer!=null) {
                            OoOAlertDialog.Builder builder = new OoOAlertDialog.Builder(MapsActivity.this);
                            builder.setTitle(noffer.getBuildingType() + " " + noffer.getType());
                            builder.setMessage("السعر " + noffer.getPrice() + " ريال ");
                            builder.setImage(R.drawable.villa);
                            builder.setAnimation(Animation.POP);
                            builder.setPositiveButton("إرسال الطلب", new OnClickListener() {
                                @Override
                                public void onClick() {

                                    FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (!dataSnapshot.hasChild(noffer.getDescription()))
                                                FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(noffer.getDescription()).setValue(noffer)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(MapsActivity.this, "تمت الاضافه", Toast.LENGTH_SHORT).show();
                                                                new OoOAlertDialog.Builder(MapsActivity.this)
                                                                        .setTitle("تمت الإضافه")
                                                                        .setImage(R.drawable.villa)
                                                                        .setAnimation(Animation.POP)
                                                                        .build();
                                                                IconGenerator icnGenerator = new IconGenerator(MapsActivity.this);
                                                                icnGenerator.setTextAppearance(R.style.iconGenText);
                                                                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buildmarker));
                                                            }
                                                        });
                                            else {
                                                Toast.makeText(MapsActivity.this, "تم اضافه هذ العرض من قبل ", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                            builder.setNegativeButton("إلغاء", new OnClickListener() {
                                @Override
                                public void onClick() {
                                    Toast.makeText(MapsActivity.this, "الغاء", Toast.LENGTH_SHORT).show();
                                }
                            });
                            builder.setPositiveButtonColor(R.color.primary);
                            builder.setNegativeButtonColor(R.color.jet);
                            builder.build();

                        }
                        return false;
                    }
                });
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {

                        //  startActivity(new Intent(MapsActivity.this,OfferShare.class));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
    }
}
