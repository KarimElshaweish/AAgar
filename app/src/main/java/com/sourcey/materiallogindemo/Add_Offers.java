package com.sourcey.materiallogindemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.model.Flat;
import com.sourcey.materiallogindemo.model.Offer;
import com.sourcey.materiallogindemo.model.Regions.Countries.Countries;
import com.sourcey.materiallogindemo.viewmodel.CountriesViewModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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
        String time=Calendar.getInstance().getTime().toString();
        offer.setTime(time);
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
    Flat flat;
    KProgressHUD hud;
    TextView titleText;

    int intentType=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__offers);
        Intent intent=getIntent();
        intentType=intent.getIntExtra("type", -1);
        try{
            __init__();
            hud = KProgressHUD.create(Add_Offers.this)
                    .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                    .setLabel("جارى رفع العرض")
                    .setMaxProgress(100);
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
                                hud.show();
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
                    orderTypeNext.setText(type);
                    if(getTyp){
                        time++;
                        buildType.setVisibility(View.VISIBLE);
                        changeColor(orderTypeNext,buildType);
                        hideView(cv1,cv2);
                        titleText.setText("نوع العقار");
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
                    buildType.setText(build);
                    if(!getBuildType)
                        Toast.makeText(getBaseContext(), "من فضلك اختار العقار", Toast.LENGTH_SHORT).show();
                    else {
                        time++;
                        priceNext1.setVisibility(View.VISIBLE);
                        btnNtextStep.setVisibility(View.VISIBLE);
                        changeColor(buildType, priceNext1);
                        hideView(cv2, cv3);
                        titleText.setText("تفاصيل الطلب");

                        if (!build.equals(tabsArray2.get(3))) {
                            roomlin.setVisibility(View.GONE);

                        }

                        //  linPrice.setVisibility(View.VISIBLE);
                        // list2.setVisibility(View.GONE);
                    }
                }
            });
//        btnContinue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String dutration="";
//                if(daily){
//                    dutration="daily";
//                }else if(monthly){
//                    dutration="monthly";
//                }else{
//                    dutration="annual";
//                }
//                flat=new Flat(family,readySwitchbool,kitchenSwitchbool,extentionSwitchbool
//                ,carEnternaceSwitchbool,airCondtionSwitchbool,dutration,receptionNumber.getText().toString(),
//                        bathRoomsNumber.getText().toString(),roomsNumber.getText().toString(),
//                        levelNumber.getText().toString(),buildAge.getText().toString());
//                time++;
//                flatView.setVisibility(View.GONE);
//                //cv4.setVisibility(View.VISIBLE);
//               // changeColor(priceNext1,notificationTyp);
//            }
//        });
            LinearLayout citesLayout=findViewById(R.id.citesLayout);
            notifcationTypListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                    String time = Calendar.getInstance().getTime().toString();
                    offer.setTime(time);
                    Shared.AddToMap.setTime(time);
                    if (build.equals("شقة ")) {
                        Shared.AddToMap.setAspect(flat);
                        offer.setAspect(flat);
                    }
                    offer.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    Shared.upload = offer;
                    addLocation = true;
                    Shared.addLocation = true;
                    if (position == 1) {
                       // getlistofCitest(offer);
                        citesLayout.setVisibility(View.VISIBLE);
                        Button btnAdd=findViewById(R.id.btnAddSpecial);
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                             //   getcurrentLocationCity=false;
                             //   Toast.makeText(getBaseContext(),fnialAddress,Toast.LENGTH_SHORT).show();
//                                citySelectedTextView=findViewById(R.id.city_selected);
//                                citySelectedTextView.setVisibility(View.VISIBLE);
//                                citySelectedTextView.setText(fnialAddress);

                                Shared.AddToMap.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Locale locale=new Locale("ar");
                                Shared.AddToMap.setOfferID(Calendar.getInstance(locale).getTime().toString());
                                FirebaseDatabase.getInstance().getReference("OfferNeeded").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(Calendar.getInstance().getTime().toString())
                                        .setValue(Shared.AddToMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Add_Offers.this, "تم إضافة الطلب", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(Add_Offers.this, MyOfferNeeded.class));
                                    }
                                });
                            }
                        });
                    } else {
                        citesLayout.setVisibility(View.GONE);
                        notificaionLocatian = tabsArray3.get(position);
                        getNotification = true;


                        if (!getNotification) {
                            piceTextNew.setError("من فضلك اختار نوع الاشعارات");
                        } else {

                            startActivity(new Intent(Add_Offers.this, MapsActivity.class));
                            finish();
                        }
                    }
                }
            });
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }


    }
    List<String>citesList;
    ListView notifcationTypListView;
    LinearLayout roomlin;
    FloatingActionButton btnNtextStep;
    String currentCity;
    private void __init__() {
        titleText=findViewById(R.id.titleText);
        Shared.addoffMethod=true;
        btnNtextStep=findViewById(R.id.btnNtextStep);
        roomlin=findViewById(R.id.roomlin);
        Spinner countriesSpinner=findViewById(R.id.spinner_Countries);
        List<String>countries = Arrays.asList(getResources().getStringArray(R.array.Countries_Array));
        Collections.sort(countries);
        ArrayAdapter spinnerCountreisAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,countries);
        countriesSpinner.setAdapter(spinnerCountreisAdapter);
        Spinner citesSpinner=findViewById(R.id.spinner_Cites);
        countriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter citesAdatper;
                if(i==1){
                    citesList= Arrays.asList(getResources().getStringArray(R.array.Egypt_Cites));
                }else{
                    citesList= Arrays.asList(getResources().getStringArray(R.array.Sudia_Cites));
                }
                Collections.sort(citesList);
                citesAdatper=new ArrayAdapter<>(Add_Offers.this, R.layout.support_simple_spinner_dropdown_item, citesList);
                citesSpinner.setAdapter(citesAdatper);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        citesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Shared.AddToMap.setCity(citesList.get(i));
                currentCity=citesList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        orderTypeNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText("نوع الطلب");
                cv1.setVisibility(View.VISIBLE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.GONE);
                orderTypeNext.setTextColor(Color.parseColor("#ffffff"));
                orderTypeNext.setBackgroundColor(getResources().getColor(R.color.primary));
                changeOffline(buildType);
                changeOffline(priceNext1);
                changeOffline(notificationTyp);
            }
        });
        buildType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(buildType.getText().toString());
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.VISIBLE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.GONE);
                buildType.setTextColor(Color.parseColor("#ffffff"));
                buildType.setBackgroundColor(getResources().getColor(R.color.primary));
                changeOffline(orderTypeNext);
                changeOffline(priceNext1);
                changeOffline(notificationTyp);
            }
        });
        priceNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(priceText.getText().toString());
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.VISIBLE);
                cv4.setVisibility(View.GONE);
                priceNext1.setTextColor(Color.parseColor("#ffffff"));
                priceNext1.setBackgroundColor(getResources().getColor(R.color.primary));
                changeOffline(orderTypeNext);
                changeOffline(buildType);
                changeOffline(notificationTyp);
            }
        });
        notificationTyp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(notificationTyp.getText().toString());
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.VISIBLE);
                notificationTyp.setTextColor(Color.parseColor("#ffffff"));
                notificationTyp.setBackgroundColor(getResources().getColor(R.color.primary));
                changeOffline(orderTypeNext);
                changeOffline(buildType);
                changeOffline(priceNext1);
            }
        });
        descrtiptionNext=findViewById(R.id.orderDescription);
        //   priceNext1=findViewById(R.id.orderNext);
        listView=findViewById(R.id.list);
        spinnerCity = findViewById(R.id.citySpinner);
        spinnerType = findViewById(R.id.spinnerType);

        priceText = findViewById(R.id.priceText);
        streetText = findViewById(R.id.StreetText);
        setTypeSpinner();
        Shared.addOffer = true;


        titleText.setText(buildType.getText().toString());
        cv1.setVisibility(View.GONE);
        cv2.setVisibility(View.VISIBLE);
        cv3.setVisibility(View.GONE);
        cv4.setVisibility(View.GONE);
        buildType.setTextColor(Color.parseColor("#ffffff"));
        buildType.setBackgroundColor(getResources().getColor(R.color.primary));
        changeOffline(orderTypeNext);
        changeOffline(priceNext1);
        changeOffline(notificationTyp);
    }

    ArrayList<String>tabsArray2,tabsArray,tabsArray3;
    private void setTypeSpinner() {
        tabsArray3=new ArrayList<>();
        tabsArray3.addAll(Arrays.asList(new String[]{"داخل النطاق فقط","داخل المدينه"}));
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray3);
        notifcationTypListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        notifcationTypListView.setAdapter(adapter3);



        tabsArray2 =new ArrayList<>();
        tabsArray2.addAll(Arrays.asList(new String[]{"فيلا ", "ارض ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "}));
        tabsArray=new ArrayList<>();
        tabsArray.addAll(Arrays.asList(new String[]{"شراء", "إيجار"}));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray);
        spinnerType.setAdapter(adapter);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                spinnerType.setSelection(1);

            }
        });


        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        ArrayAdapter<String>adapter2=new ArrayAdapter<>(this,android.R.layout.simple_list_item_single_choice,tabsArray2);
        list2.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        list2.setAdapter(adapter2);
        if(intentType==0){
            spinnerType.setSelection(1);
            listView.setItemChecked(1,true);
            orderTypeNext.setText(tabsArray.get(1));
            type="إيجار";

        }else if(intentType==1){
            spinnerType.setSelection(0);
            listView.setItemChecked(0,true);
            orderTypeNext.setText(tabsArray.get(0));
            type="شراء";
        }
    }

    Offer localOffer;
    private void getlistofCitest(Offer offer) {
        this.localOffer=offer;
        getcurrentLocationCity=true;
        hud.show();
         FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
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
                        else{
                            ActivityCompat.requestPermissions(Add_Offers.this, new String[]{
                                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION},
                                    1);

                        }
                    }
                });

//        ArrayList<String> list=new ArrayList<String>();
//
//        String[] locales = Locale.getISOCountries();
//
//        for (String countryCode : locales) {
//
//            Locale obj = new Locale("", countryCode);
//
//            System.out.println("Country Name = " + obj.getDisplayCountry());
//            list.add(obj.getDisplayCountry());
//
//        }
//
//        CountriesViewModel countriesViewModel= new ViewModelProvider(this).get(CountriesViewModel.class);
//        countriesViewModel.getCountries();
//        countriesViewModel.getCountriesList().observe(this, new Observer<Countries>() {
//            @Override
//            public void onChanged(Countries countries) {
//                if(countries!=null)
//                    System.out.println(countries.getData());
//            }
//        });


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
    Boolean getcurrentLocationCity=false;
    TextView citySelectedTextView;
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
            System.out.println(fnialAddress);
            String[] tabsArray =
                    new String[]{fnialAddress};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tabsArray);
            spinnerCity.setAdapter(adapter);
            streetText.setText(address.get(0).getLocality()+" " +address.get(0).getAdminArea());
            streetText.setEnabled(false);
            addressTxt.setText(streetText.getText());
            hud.dismiss();

            if(getcurrentLocationCity){
                getcurrentLocationCity=false;
                Toast.makeText(getBaseContext(),fnialAddress,Toast.LENGTH_SHORT).show();
                citySelectedTextView=findViewById(R.id.city_selected);
                citySelectedTextView.setVisibility(View.VISIBLE);
                citySelectedTextView.setText(fnialAddress);
                offer.setCity(fnialAddress);

                offer.setFlit(lat);
                offer.setFlon(lng);

                offer.setSlit(lat);
                offer.setSlon(lng);

                offer.setFtlit(lat);
                offer.setFtlon(lng);

                offer.setTlit(lat);
                offer.setTlon(lng);

                offer.setUID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                offer.setOfferID(Calendar.getInstance(locale).getTime().toString());
                FirebaseDatabase.getInstance().getReference("OfferNeeded").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Calendar.getInstance().getTime().toString())
                        .setValue(offer).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Add_Offers.this, "تم إضافة الطلب", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(new Intent(Add_Offers.this, MyOfferNeeded.class));
                    }
                });


            }

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
    GridView list2;
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
    private void changeOffline(TextView off){
        off.setTextColor(Color.parseColor("#000000"));
        off.setBackground(getResources().getDrawable(R.drawable.tab_layout));

    }
    @SuppressLint("RestrictedApi")
    public void next(View view) {
     //   Button btn=(Button)view;
        if(time==0){
        }else if(time==1) {
            titleText.setText("نوع العقار");
            if(!getBuildType)
                Toast.makeText(this, "من فضلك اختار العقار", Toast.LENGTH_SHORT).show();
            else {

                    time++;
                    priceNext1.setVisibility(View.VISIBLE);
                    changeColor(buildType, priceNext1);
                    hideView(cv2, cv3);
                titleText.setText("استلام العرض");

                if (!build.equals(tabsArray2.get(3))) {
                        roomlin.setVisibility(View.GONE);
                    }

                //  linPrice.setVisibility(View.VISIBLE);
                // list2.setVisibility(View.GONE);
            }
        }else if(time==2){
            titleText.setText("تفاصيل الطلب");
            String price=piceTextNew.getText().toString();
            if(price.equals("")) {
                piceTextNew.setError("من فضلك ادخل سعر الطلب");
            }else {
                hideView(cv3, cv4);
                titleText.setText("سعر الطلب");
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
            titleText.setText("استلام العرض");
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
                String time=Calendar.getInstance().getTime().toString();
                offer.setTime(time);
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
                Shared.AddToMap.setTime(time);
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
