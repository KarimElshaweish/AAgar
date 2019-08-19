package com.sourcey.materiallogindemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sourcey.materiallogindemo.Model.Offer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Add_Offers extends AppCompatActivity implements LocationListener {

    boolean addLocation = false, fillData = false;
    Spinner spinnerCity, spinnerType;
    EditText priceText, streetText;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferNeeded");
    Offer offer;

    public void Finish(View view) {
        finish();
    }

    public void AddLocation(View view) {
        offer = new Offer();
        offer.setUserName(Shared.user.getName());
        offer.setCity(spinnerCity.getSelectedItem().toString());
        offer.setPrice(priceText.getText().toString());
        offer.setType(spinnerType.getSelectedItem().toString());
        offer.setLongtuide(Shared.longtuide);
        offer.setLituide(Shared.lituide);
        offer.setStreet(streetText.getText().toString());
        offer.setUserName(Shared.user.getName());
        Shared.AddToMap = new Offer();
        Shared.AddToMap.setCity(spinnerCity.getSelectedItem().toString());
        Shared.AddToMap.setPrice(priceText.getText().toString());
        Shared.AddToMap.setType(spinnerType.getSelectedItem().toString());
        Shared.AddToMap.setLongtuide(Shared.longtuide);
        Shared.AddToMap.setLituide(Shared.lituide);
        Shared.AddToMap.setStreet(streetText.getText().toString());
        Shared.AddToMap.setUserName(Shared.user.getName());

        Shared.AddToMap.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        offer.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Shared.upload = offer;
        addLocation = true;
        Shared.addLocation = true;
        startActivity(new Intent(Add_Offers.this, MapsActivity.class));
        finish();

    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //You had this as int. It is advised to have Lat/Loing as double.
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Locale locale = new Locale("ar");
            Geocoder geoCoder = new Geocoder(Add_Offers.this, locale);
            StringBuilder builder = new StringBuilder();
            try {
                List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
                int maxLines = address.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < maxLines; i++) {
                    String addressStr = address.get(0).getAddressLine(i);
                    builder.append(addressStr);
                    builder.append(" ");
                }

                String fnialAddress = address.get(0).getAdminArea();
                String[] tabsArray =
                        new String[]{fnialAddress};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Add_Offers.this, android.R.layout.simple_expandable_list_item_1, tabsArray);
                spinnerCity.setAdapter(adapter);

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
    };
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__offers);
        spinnerCity = findViewById(R.id.citySpinner);
        spinnerType = findViewById(R.id.spinnerType);

        priceText = findViewById(R.id.priceText);
        streetText = findViewById(R.id.StreetText);
        setTypeSpinner();
        Shared.addOffer = true;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
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


    }
    private void setTypeSpinner() {
        String[] tabsArray = new String[]{"الكل", "شقةللإيجار", "فيلا للبيع", "إرض للبيع", "فيلا للإيجار", "دور للإيجار", "شقة للبيع", "عمارة للبيع", "بيت للبيع", "استراحه للإيجار", "بيت للإيجار", "محل للإيجار", "مزرعه للبيع", "عماره للإيجار"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tabsArray);
        spinnerType.setAdapter(adapter);
    }
    public void Add(View view) {
        fillData = true;
        Shared.AddToMap.setCity(spinnerCity.getSelectedItem().toString());
        Shared.AddToMap.setPrice(priceText.getText().toString());
        Shared.AddToMap.setType(spinnerType.getSelectedItem().toString());
        Shared.AddToMap.setLongtuide(Shared.longtuide);
        Shared.AddToMap.setLituide(Shared.lituide);
        Shared.AddToMap.setStreet(streetText.getText().toString());

        Shared.AddToMap.setFlit(Shared.latLngList.get(0).latitude);
        Shared.AddToMap.setFlon(Shared.latLngList.get(0).longitude);

        Shared.AddToMap.setSlit(Shared.latLngList.get(1).latitude);
        Shared.AddToMap.setSlon(Shared.latLngList.get(1).longitude);

        Shared.AddToMap.setTlit(Shared.latLngList.get(2).latitude);
        Shared.AddToMap.setTlon(Shared.latLngList.get(2).longitude);

        Shared.AddToMap.setFtlit(Shared.latLngList.get(3).latitude);
        Shared.AddToMap.setFtlon(Shared.latLngList.get(3).longitude);
        Shared.AddToMap.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        if (addLocation) {
            mReference.child(Calendar.getInstance().getTime().toString()).setValue(offer).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Add_Offers.this, "Done", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else
            Snackbar.make(view, "من فضلك ادخل النطاق الجغرافى", Snackbar.LENGTH_LONG).show();

    }

    private void setCity() {
        String []CityArray=new String[]{
"الرياض","مكة","المدينة المنورة","بريدة"
                ,"تبوك","الدمام","الاحساء","القطيف","خميس","مشيط","الطائف","نجران","حفر","الباطن","الجبيل","ضباء",
                "الخرج","الثقبة","ينبع","البحر","الخبر","عرعر","الحوية","عنيزة","سكاكا","جيزان",
                "القريات","الظهران","الباحة","الزلفي","تاروت","شروره","صبياء","الحوطة","الأفلاج","بحره"
        };
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,CityArray);
        spinnerCity.setAdapter(adapter);
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

            String fnialAddress = address.get(0).getAdminArea();
            String[] tabsArray =
                    new String[]{fnialAddress};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tabsArray);
            spinnerCity.setAdapter(adapter);
            streetText.setText(address.get(0).getLocality()+" " +address.get(0).getAdminArea());
            streetText.setEnabled(false);

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

}
