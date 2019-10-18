package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Arrays;
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
    ListView listView;

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
        offer.setNotificationTyp(notificaionLocatian);
        Shared.AddToMap = new Offer();
        Shared.AddToMap.setCity(spinnerCity.getSelectedItem().toString());
        Shared.AddToMap.setPrice(priceText.getText().toString());
        Shared.AddToMap.setType(spinnerType.getSelectedItem().toString());
        Shared.AddToMap.setLongtuide(Shared.longtuide);
        Shared.AddToMap.setLituide(Shared.lituide);
        Shared.AddToMap.setStreet(streetText.getText().toString());
        Shared.AddToMap.setUserName(Shared.user.getName());
        Shared.AddToMap.setNotificationTyp(notificaionLocatian);
        Shared.AddToMap.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());

        offer.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
        Shared.upload = offer;
        addLocation = true;
        Shared.addLocation = true;
        startActivity(new Intent(Add_Offers.this, MapsActivity.class));
        finish();

    }

    ProgressDialog progressDialog;
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
        __init__();
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
                            progressDialog =new ProgressDialog(Add_Offers.this);
                            progressDialog.setTitle("جارى تحميل المدينة");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            // Logic to handle location object
                            onLocationChanged(location);
                        }
                    }
                });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getTyp=true;
                type= tabsArray.get(position);
                if(getTyp){
                    time++;
                    buildType.setVisibility(View.VISIBLE);
                    changeColor(orderTypeNext,buildType);
                    hideView(cv1,cv2);
                    //  listView.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getBaseContext(), "من فضلك اختار نوع العقار", Toast.LENGTH_SHORT).show();
                }

            }
        });
        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBuildType=true;
                build=tabsArray2.get(position);
                if(!getBuildType)
                    Toast.makeText(getBaseContext(), "من فضلك اختار العقار", Toast.LENGTH_SHORT).show();
                else {
                    time++;
                    priceNext1.setVisibility(View.VISIBLE);
                    btnNtextStep.setVisibility(View.VISIBLE);
                    changeColor(buildType, priceNext1);
                    hideView(cv2, cv3);
                    if(!build.equals(tabsArray2.get(3))){
                        roomlin.setVisibility(View.GONE);

                    }
                    //  linPrice.setVisibility(View.VISIBLE);
                    // list2.setVisibility(View.GONE);
                }
            }
        });
        notifcationTypListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                notificaionLocatian = tabsArray3.get(position);
                getNotification = true;


                if (!getNotification) {
                    piceTextNew.setError("من فضلك اختار نوع الاشعارات");
                } else {
                    offer = new Offer();
                    offer.setUserName(Shared.user.getName());
                    offer.setCity(fnialAddress);
                    offer.setPrice(piceTextNew.getText().toString());
                    offer.setType(type);
                    offer.setLongtuide(Shared.longtuide);
                    offer.setLituide(Shared.lituide);
                    offer.setStreet(streetText.getText().toString());
                    offer.setUserName(Shared.user.getName());
                    offer.setBuildingTyp(build);
                    Shared.AddToMap = new Offer();
                    Shared.AddToMap.setCity(fnialAddress);
                    Shared.AddToMap.setPrice(piceTextNew.getText().toString());
                    Shared.AddToMap.setType(type);
                    Shared.AddToMap.setLongtuide(Shared.longtuide);
                    Shared.AddToMap.setLituide(Shared.lituide);
                    Shared.AddToMap.setStreet(streetText.getText().toString());
                    Shared.AddToMap.setUserName(Shared.user.getName());
                    Shared.AddToMap.setBuildingTyp(build);
                    Shared.AddToMap.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    offer.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Shared.upload = offer;
                    addLocation = true;
                    Shared.addLocation = true;
                    startActivity(new Intent(Add_Offers.this, MapsActivity.class));
                    finish();
                }
            }
        });

    }
ListView notifcationTypListView;
    LinearLayout roomlin;
    FloatingActionButton btnNtextStep;
    private void __init__() {
        btnNtextStep=findViewById(R.id.btnNtextStep);
        roomlin=findViewById(R.id.roomlin);
        cv4=findViewById(R.id.cv4);
        cv3=findViewById(R.id.cv3);
        cv2=findViewById(R.id.cv2);
        cv1=findViewById(R.id.cv1);
        notifcationTypListView=findViewById(R.id.list3);
        notificationTyp=findViewById(R.id.notificationTyp);
        priceNext1=findViewById(R.id.nenxtPrice);
        Shared.latLngList=new ArrayList<>();
        list2=findViewById(R.id.list2);
        buildType=findViewById(R.id.buildType);
        addressTxt=findViewById(R.id.addressTxt);
        linPrice=findViewById(R.id.linPrice);
        piceTextNew=findViewById(R.id.minpiceTextNew);
        linearDetials=findViewById(R.id.linearDetials);
        detailsNewText=findViewById(R.id.detailsNewText);
        orderTypeNext=findViewById(R.id.orderType);
        descrtiptionNext=findViewById(R.id.orderDescription);
        //   priceNext1=findViewById(R.id.orderNext);
        listView=findViewById(R.id.list);
        spinnerCity = findViewById(R.id.citySpinner);
        spinnerType = findViewById(R.id.spinnerType);

        priceText = findViewById(R.id.priceText);
        streetText = findViewById(R.id.StreetText);
        setTypeSpinner();
        Shared.addOffer = true;
    }

    ArrayList<String>tabsArray2,tabsArray,tabsArray3;
    private void setTypeSpinner() {
        tabsArray3=new ArrayList<>();
        tabsArray3.addAll(Arrays.asList(new String[]{"داخل النطاق فقط","داخل المدينه"}));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray3);
        notifcationTypListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        notifcationTypListView.setAdapter(adapter3);
        tabsArray2 =new ArrayList<>();
        tabsArray2.addAll(Arrays.asList(new String[]{"فيلا ", "إرض ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "}));
        tabsArray=new ArrayList<>();
        tabsArray.addAll(Arrays.asList(new String[]{"شراء", "إيجار"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray);
        spinnerType.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        ArrayAdapter<String>adapter2=new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,tabsArray2);
        list2.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        list2.setAdapter(adapter2);
    }
    public void Add(View view) {
        fillData = true;
        if(notifcationTypListView.getSelectedItemPosition()==0)
            Shared.AddToMap.setInsidePolgon(true);
        else
            Shared.AddToMap.setInsidePolgon(false);
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
    String fnialAddress;
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

            fnialAddress = address.get(0).getAdminArea();
            String[] tabsArray =
                    new String[]{fnialAddress};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tabsArray);
            spinnerCity.setAdapter(adapter);
            streetText.setText(address.get(0).getLocality()+" " +address.get(0).getAdminArea());
            streetText.setEnabled(false);
            addressTxt.setText(streetText.getText());
            progressDialog.dismiss();

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
    ListView list2;
    CardView cv1,cv2,cv3,cv4;
    int time =0;
    TextView priceNext1,notificationTyp,descrtiptionNext,orderTypeNext,addressTxt,buildType;
    EditText detailsNewText,piceTextNew;
    LinearLayout linearDetials,linPrice;
    String type,build,notificaionLocatian;
    boolean getTyp=false,getBuildType=false,getNotification=false;
    private void hideView(final View view, final View appear){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                appear.setVisibility(View.VISIBLE);
            }
        });

        view.startAnimation(animation);
    }
    private void changeColor(TextView last,TextView next){
        last.setTextColor(Color.parseColor("#000000"));
        last.setBackground(getResources().getDrawable(R.drawable.tab_layout));

        next.setTextColor(Color.parseColor("#ffffff"));
        next.setBackgroundColor(getResources().getColor(R.color.primary));
    }
    public void next(View view) {
     //   Button btn=(Button)view;
        if(time==0){
        }else if(time==1) {
            if(!getBuildType)
                Toast.makeText(this, "من فضلك اختار العقار", Toast.LENGTH_SHORT).show();
            else {
                time++;
                priceNext1.setVisibility(View.VISIBLE);
                changeColor(buildType, priceNext1);
                hideView(cv2, cv3);
                if(!build.equals(tabsArray2.get(3))){
                    roomlin.setVisibility(View.GONE);
                }
                //  linPrice.setVisibility(View.VISIBLE);
                // list2.setVisibility(View.GONE);
            }
        }else if(time==2){
            String price=piceTextNew.getText().toString();
            if(price.equals("")) {
                piceTextNew.setError("من فضلك ادخل سعر الطلب");
            }else {
                hideView(cv3, cv4);
                changeColor(priceNext1, notificationTyp);
//            String details=detailsNewText.getText().toString();
//            if(details.equals("")){
//                detailsNewText.setError("من فضلك ادخل تفاصيل الطلب");
//            }else {
                linearDetials.setVisibility(View.GONE);
                time++;
                //   descrtiptionNext.setTextSize(12);
                notificationTyp.setVisibility(View.VISIBLE);
                notifcationTypListView.setVisibility(View.VISIBLE);
                linPrice.setVisibility(View.GONE);
                btnNtextStep.setVisibility(View.GONE);

            }
            //    btn.setText("تحديد الموقع على الخريطه");
//            }
        }else if(time ==3){

            if(!getNotification){
                piceTextNew.setError("من فضلك اختار نوع الاشعارات");
            }else{
                offer = new Offer();
                offer.setUserName(Shared.user.getName());
                offer.setCity(fnialAddress);
                offer.setPrice(piceTextNew.getText().toString());
                offer.setType(type);
                offer.setLongtuide(Shared.longtuide);
                offer.setLituide(Shared.lituide);
                offer.setStreet(streetText.getText().toString());
                offer.setUserName(Shared.user.getName());
                offer.setBuildingTyp(build);
                Shared.AddToMap = new Offer();
                Shared.AddToMap.setCity(fnialAddress);
                Shared.AddToMap.setPrice(piceTextNew.getText().toString());
                Shared.AddToMap.setType(type);
                Shared.AddToMap.setLongtuide(Shared.longtuide);
                Shared.AddToMap.setLituide(Shared.lituide);
                Shared.AddToMap.setStreet(streetText.getText().toString());
                Shared.AddToMap.setUserName(Shared.user.getName());
                Shared.AddToMap.setBuildingTyp(build);
                Shared.AddToMap.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());

                offer.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                Shared.upload = offer;
                addLocation = true;
                Shared.addLocation = true;
                startActivity(new Intent(Add_Offers.this, MapsActivity.class));
                finish();
            }
        }
    }

}
