package com.sourcey.materiallogindemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sourcey.materiallogindemo.Adapter.UploadListAdapter;
import com.sourcey.materiallogindemo.Model.Build;
import com.sourcey.materiallogindemo.Model.Farm;
import com.sourcey.materiallogindemo.Model.Flat;
import com.sourcey.materiallogindemo.Model.Home;
import com.sourcey.materiallogindemo.Model.Level;
import com.sourcey.materiallogindemo.Model.LinkOffer;
import com.sourcey.materiallogindemo.Model.Market;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.Ressort;
import com.sourcey.materiallogindemo.Model.Villa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.sourcey.materiallogindemo.Shared.CityArray;
import static com.sourcey.materiallogindemo.Shared.offer;
import static com.sourcey.materiallogindemo.Shared.offerID;

public class AddResultOffer extends AppCompatActivity implements LocationListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    Spinner spinnerCity, spinnerType;
    EditText priceText, descText, streetText,detials;
    RecyclerView rv;
    static final int PICK_VIDEO_REQUEST = 777;
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private boolean choseImage = false;
    private StorageReference mStorage;
    LinearLayout layoutForm;
    private FusedLocationProviderClient fusedLocationClient;
ListView listView;
    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale = new Locale("ar");
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
            streetText.setText(address.get(0).getLocality() + " " + address.get(0).getThoroughfare());

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

    public void Finish(View view) {
        finish();
    }

    CardView cv1,cv2,cv3,cv4;
    ListView list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_result_offer);
        navigationAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,naviagations);

        list2=findViewById(R.id.list2);
        cv4=findViewById(R.id.cv4);
        cv3=findViewById(R.id.cv3);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        imgViewUpload=findViewById(R.id.imgViewUpload);
        listView=findViewById(R.id.list);
        orderPrice = findViewById(R.id.orderPrice);
        orderDescription = findViewById(R.id.orderDescription);
        orderType = findViewById(R.id.orderType);
        orderPicture = findViewById(R.id.orderPicture);
        mStorage = FirebaseStorage.getInstance().getReference();
        layoutForm = findViewById(R.id.layoutForm);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, uriList);
        rv.setAdapter(uploadListAdapter);
        spinnerCity = findViewById(R.id.citySpinner);
        spinnerType = findViewById(R.id.spinnerType);
        priceText = findViewById(R.id.total_price_edit);
        detials=findViewById(R.id.detials);
        descText = findViewById(R.id.descText);
        streetText = findViewById(R.id.StreetText);
        setTypeSpinner();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
    }
    ArrayList<String>tabsArray2;

    String buildType,type;
    boolean getBuildType=false,getType=false;
     String[]tabsArray;
    private void setTypeSpinner() {
        if(!Shared.AddRandomButton)
           tabsArray =new String[]{offer.getType()};
        else{
            tabsArray =new String[]{"إيجار","شراء"};
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray);
        spinnerType.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        tabsArray2 =new ArrayList<>();
        tabsArray2.addAll(Arrays.asList(new String[]{"فيلا ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "}));
        list2.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray2);
        list2.setAdapter(adapter2);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getType=true;
                type=tabsArray[position];
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBuildType=true;
                buildType=tabsArray2.get(position);
            }
        });

    }

    private void setCity() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, CityArray);
        spinnerCity.setAdapter(adapter);
    }

    @SuppressLint("RestrictedApi")
    public void AddPicture(View view) {
        getMedia();
    }

    private void getMedia() {
        rv.setVisibility(View.VISIBLE);
        choseImage = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "اختار صور العقار"), PICK_VIDEO_REQUEST);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && choseImage) {
            imgViewUpload.setVisibility(View.GONE);
            choseImage = false;
            if (data.getClipData() != null) {
                int totalItemSelected = data.getClipData().getItemCount();
                for (int i = 0; i < totalItemSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    setDataToRV(fileUri);
                }
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                setDataToRV(fileUri);
            }
        }
    }

    List<Uri> uriList = new ArrayList<>();

    private void setDataToRV(Uri fileUri) {
        File file = new File(fileUri.getPath());
        String fileName = file.getName();
        fileNameList.add(fileName);
        fileDoneList.add("uploading");
        uploadListAdapter.notifyDataSetChanged();
        uriList.add(fileUri);
    }

    List<String> downloadUrl;

    public void uploadToFirebase(View view) {
        if (fileNameList.size() == 0)
            Toast.makeText(this, "من فضلك ارفع صور لهذا العقار", Toast.LENGTH_SHORT).show();
        else {
            uploadResult();

        }
    }

    Button btnContinue;
    TextView btnSingle,btnFamily,dailyText,monthlyTxt,annualText,receptionNumber,bathRoomsNumber,roomsNumber,levelNumber,buildAge;
    CrystalSeekbar receptionSeekBar,bathRoomsSeekBar,roomsSeekBar,levelSeekBar,buildAgeSeekBar;
    Switch readySwitch,kitchenSwitch,extentionSwitch,carEnternaceSwitch,elvatorSwitch,airCondtionSwitch;
    Boolean readySwitchbool=false
            ,kitchenSwitchbool=false
            ,extentionSwitchbool=false
            ,carEnternaceSwitchbool=false
            ,elvatorSwitchbool=false
            ,airCondtionSwitchbool=false;
    boolean family=true;
    boolean daily=true,monthly=false,annual=false;
    View flatView;
    private void __init__flat(){

        airCondtionSwitch=findViewById(R.id.airCondtionSwitch);
        airCondtionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                airCondtionSwitchbool=b;
            }
        });
        elvatorSwitch=findViewById(R.id.elvatorSwitch);
        elvatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                elvatorSwitchbool=b;
            }
        });
        carEnternaceSwitch=findViewById(R.id.carEnternaceSwitch);
        carEnternaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                carEnternaceSwitchbool=b;
            }
        });
        extentionSwitch=findViewById(R.id.extentionSwitch);
        extentionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                extentionSwitchbool=b;
            }
        });
        kitchenSwitch=findViewById(R.id.kitchenSwitch);
        kitchenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                kitchenSwitchbool=b;
            }
        });
        readySwitch=findViewById(R.id.readySwitch);
        readySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                readySwitchbool=b;
            }
        });
//        btnContinue=findViewById(R.id.btt);
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
//                        ,carEnternaceSwitchbool,airCondtionSwitchbool,dutration,receptionNumber.getText().toString(),
//                        bathRoomsNumber.getText().toString(),roomsNumber.getText().toString(),
//                        levelNumber.getText().toString(),buildAge.getText().toString());
//                time++;
//                flatView.setVisibility(View.GONE);
//                totalPriceView.setVisibility(View.VISIBLE);
//                //cv4.setVisibility(View.VISIBLE);
//                // ch
//            }
//        });

        receptionNumber=findViewById(R.id.receptionNumber);
        receptionSeekBar=findViewById(R.id.receptionSeekBar);
        btnSingle=findViewById(R.id.btnSingle);
        btnFamily=findViewById(R.id.btnFamily);

        dailyText=findViewById(R.id.dailyText);
        monthlyTxt=findViewById(R.id.monthlyTxt);
        annualText=findViewById(R.id.annualText);

        bathRoomsSeekBar=findViewById(R.id.bathRoomsSeekBar);
        bathRoomsNumber=findViewById(R.id.bathRoomsNumber);

        roomsSeekBar=findViewById(R.id.roomsSeekBar);
        roomsNumber=findViewById(R.id.roomsNumber);

        levelSeekBar=findViewById(R.id.levelSeekBar);
        levelNumber=findViewById(R.id.levelNumber);

        buildAgeSeekBar=findViewById(R.id.buildAgeSeekBar);
        buildAge=findViewById(R.id.buildAge);


        buildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                String year="سنه" ;
                if(Integer.parseInt(value.toString())==0){
                    buildAge.setText(year);
                }else{
                    buildAge.setText(value.toString()+" "+year);
                }
            }
        });
        levelSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                if(Integer.parseInt(value.toString())==0){
                    levelNumber.setText("أرضى");
                }else{
                    levelNumber.setText(value.toString());
                }
            }
        });
        roomsSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                roomsNumber.setText(value.toString());

            }
        });
        bathRoomsSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                bathRoomsNumber.setText(value.toString());
            }
        });
        receptionSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                receptionNumber.setText(value.toString());
            }
        });
        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(family){
                    family=false;
                    btnFamily.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    btnFamily.setTextColor(Color.parseColor("#000000"));

                    btnSingle.setBackgroundColor(getResources().getColor(R.color.primary));
                    btnSingle.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });


        btnFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!family){
                    family=true;
                    btnSingle.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    btnSingle.setTextColor(Color.parseColor("#000000"));

                    btnFamily.setBackgroundColor(getResources().getColor(R.color.primary));
                    btnFamily.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });

        dailyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!daily){
                    daily=true;
                    monthly=false;
                    annual=false;

                    dailyText.setBackgroundColor(getResources().getColor(R.color.primary));
                    dailyText.setTextColor(Color.parseColor("#ffffff"));

                    monthlyTxt.setTextColor(Color.parseColor("#000000"));
                    monthlyTxt.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                    annualText.setTextColor(Color.parseColor("#000000"));
                    annualText.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                }
            }
        });

        monthlyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!monthly){
                    daily=false;
                    monthly=true;
                    annual=false;

                    monthlyTxt.setBackgroundColor(getResources().getColor(R.color.primary));
                    monthlyTxt.setTextColor(Color.parseColor("#ffffff"));

                    dailyText.setTextColor(Color.parseColor("#000000"));
                    dailyText.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                    annualText.setTextColor(Color.parseColor("#000000"));
                    annualText.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                }

            }
        });

        annualText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!annual){
                    daily=false;
                    monthly=false;
                    annual=true;

                    annualText.setBackgroundColor(getResources().getColor(R.color.primary));
                    annualText.setTextColor(Color.parseColor("#ffffff"));

                    dailyText.setTextColor(Color.parseColor("#000000"));
                    dailyText.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                    monthlyTxt.setTextColor(Color.parseColor("#000000"));
                    monthlyTxt.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                }

            }
        });
    }
    Spinner Villaspinner;
    String[]naviagations=new String[]{"غرب","شمال","جنوب","شرق"};
    ArrayAdapter<String> navigationAdapter ;
    TextView villaReceptionNumber,villaBathRoomsNumber,villaStreetWidth,villaRoomsNumber,villaLevelNumber
            ,villaBuildAge;
    CrystalSeekbar villaReceptionSeekBar,villaBathRoomsSeekBar,villaStreetWidthSeekBar,villaRoomsSeekBar,villaLevelSeekBar
            ,villaBuildAgeSeekBar;
    Switch villaReceptionSwitch,villaDriverSwitch,villaBillGirlRoomSwitch,VillaPoolSwitch,villaHairRoomSwitch,VillaHallSwitch
            ,villaVaultSwitch,villaReadySwitch,villaKitchenSwitch,villaCarEnternaceSwitch,villaElvatorSwitch,villaAirCondtionSwitch,
            villaDublexSwitch;
    Boolean villaReceptionSwitchBool=false,villaDriverSwitchBool=false,villaBillGirlRoomSwitchBool=false,VillaPoolSwitchBool=false
            ,villaHairRoomSwitchBool=false,VillaHallSwitchBool=false,villaVaultSwitchBool=false,villaReadySwitchBool=false
            ,villaKitchenSwitchBool=false,villaCarEnternaceSwitchBool=false,villaElvatorSwitchBool=false,villaAirCondtionSwitchBool=false,
            villaDublexSwitchBool=false;
    String villaNavigation="";
    private void __init__villa(){
        Villaspinner=findViewById(R.id.Villaspinner);
        Villaspinner.setAdapter(navigationAdapter);
        Villaspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                villaNavigation=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        villaReceptionNumber=findViewById(R.id.villaReceptionNumber);
        villaReceptionSeekBar=findViewById(R.id.villaReceptionSeekBar);
        villaReceptionSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                villaReceptionNumber.setText(value.toString());
            }
        });
        villaBathRoomsNumber=findViewById(R.id.villaBathRoomsNumber);
        villaBathRoomsSeekBar=findViewById(R.id.villaBathRoomsSeekBar);
        villaBathRoomsSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                villaBathRoomsNumber.setText(value.toString());
            }
        });
        villaStreetWidth=findViewById(R.id.villaStreetWidth);
        villaStreetWidthSeekBar=findViewById(R.id.villaStreetWidthSeekBar);
        villaStreetWidthSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                villaStreetWidth.setText(value.toString());
            }
        });
        villaRoomsNumber=findViewById(R.id.villaRoomsNumber);
        villaRoomsSeekBar=findViewById(R.id.villaRoomsSeekBar);
        villaRoomsSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                villaRoomsNumber.setText(value.toString());
            }
        });
        villaLevelNumber=findViewById(R.id.villaLevelNumber);
        villaLevelSeekBar=findViewById(R.id.villaLevelSeekBar);
        villaLevelSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                villaLevelNumber.setText(value.toString());
            }
        });
        villaBuildAge=findViewById(R.id.villaBuildAge);
        villaBuildAgeSeekBar=findViewById(R.id.villaBuildAgeSeekBar);
        villaBuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                villaBuildAge.setText(value.toString());
            }
        });
        villaReceptionSwitch=findViewById(R.id.villaReceptionSwitch);
        villaReceptionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaReceptionSwitchBool=b;
            }
        });
        villaDriverSwitch=findViewById(R.id.villaDriverSwitch);
        villaDriverSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaDriverSwitchBool=b;
            }
        });
        villaBillGirlRoomSwitch=findViewById(R.id.villaBillGirlRoomSwitch);
        villaBillGirlRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaBillGirlRoomSwitchBool=b;
            }
        });
        VillaPoolSwitch=findViewById(R.id.VillaPoolSwitch);
        VillaPoolSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                VillaPoolSwitchBool=b;
            }
        });
        villaHairRoomSwitch=findViewById(R.id.villaHairRoomSwitch);
        villaHairRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaHairRoomSwitchBool=b;
            }
        });
        VillaHallSwitch=findViewById(R.id.VillaHallSwitch);
        VillaHallSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                VillaHallSwitchBool=b;
            }
        });
        villaVaultSwitch=findViewById(R.id.villaVaultSwitch);
        villaVaultSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaVaultSwitchBool=b;
            }
        });
        villaReadySwitch=findViewById(R.id.villaReadySwitch);
        villaReadySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaReadySwitchBool=b;
            }
        });
        villaKitchenSwitch=findViewById(R.id.villaKitchenSwitch);
        villaKitchenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaKitchenSwitchBool=false;
            }
        });
        villaCarEnternaceSwitch=findViewById(R.id.villaCarEnternaceSwitch);
        villaCarEnternaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaCarEnternaceSwitchBool=b;
            }
        });
        villaElvatorSwitch=findViewById(R.id.villaElvatorSwitch);
        villaElvatorSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaElvatorSwitchBool=b;
            }
        });
        villaAirCondtionSwitch=findViewById(R.id.villaAirCondtionSwitch);
        villaAirCondtionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaAirCondtionSwitchBool=b;
            }
        });
        villaDublexSwitch=findViewById(R.id.villaDublexSwitch);
        villaDublexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                villaDublexSwitchBool=b;
            }
        });
    }
    Spinner Buildspinner;
    TextView buildCommericeAndHomming,commeice,homing,buildStreetWidth,buildRoomsNumber,buildMarketNumber,buildRomsNumber
            ,bbuildAge;
    Boolean buildCommericeAndHommingBool=true,commeiceBool=false,homingBool=false;
    CrystalSeekbar buildStreetWidthSeekBar,buildRoomsNumberSeekBar,buildMarketNumberSeekBar,buildRomsNumberSeekBar,
            bbuildAgeSeekBar;
    Switch buildReadySwitch;
    Boolean buildReadySwitchBool=false;
    String buildNavigation="";
    private void __init_build(){
        Buildspinner=findViewById(R.id.Buildspinner);
        Buildspinner.setAdapter(navigationAdapter);
        Buildspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                buildNavigation=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buildCommericeAndHomming=findViewById(R.id.buildCommericeAndHomming);
        commeice=findViewById(R.id.commeice);
        homing=findViewById(R.id.homing);
        commeice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!commeiceBool){
                    commeiceBool=true;
                    homingBool=false;
                    buildCommericeAndHommingBool=false;
                    buildCommericeAndHomming.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    buildCommericeAndHomming.setTextColor(Color.parseColor("#000000"));

                    homing.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    homing.setTextColor(Color.parseColor("#000000"));

                    commeice.setBackgroundColor(getResources().getColor(R.color.primary));
                    commeice.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        buildCommericeAndHomming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buildCommericeAndHommingBool){
                    buildCommericeAndHommingBool=true;
                    commeiceBool=false;
                    homingBool=false;
                    commeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    commeice.setTextColor(Color.parseColor("#000000"));

                    homing.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    homing.setTextColor(Color.parseColor("#000000"));

                    buildCommericeAndHomming.setBackgroundColor(getResources().getColor(R.color.primary));
                    buildCommericeAndHomming.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        homing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!homingBool){
                    homingBool=true;
                    commeiceBool=false;
                    buildCommericeAndHommingBool=false;
                    commeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    commeice.setTextColor(Color.parseColor("#000000"));

                    buildCommericeAndHomming.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    buildCommericeAndHomming.setTextColor(Color.parseColor("#000000"));

                    homing.setBackgroundColor(getResources().getColor(R.color.primary));
                    homing.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        buildStreetWidth=findViewById(R.id.buildStreetWidth);
        buildStreetWidthSeekBar=findViewById(R.id.buildStreetWidthSeekBar);
        buildStreetWidthSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                buildStreetWidth.setText(value.toString());
            }
        });
        buildRoomsNumber=findViewById(R.id.buildRoomsNumber);
        buildRoomsNumberSeekBar=findViewById(R.id.buildRoomsNumberSeekBar);
        buildRoomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                buildRoomsNumber.setText(value.toString());
            }
        });
        buildMarketNumber=findViewById(R.id.buildMarketNumber);
        buildMarketNumberSeekBar=findViewById(R.id.buildMarketNumberSeekBar);
        buildMarketNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                buildMarketNumber.setText(value.toString());
            }
        });
        buildRomsNumber=findViewById(R.id.buildRomsNumber);
        buildRomsNumberSeekBar=findViewById(R.id.buildRomsNumberSeekBar);
        buildRomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                buildRomsNumber.setText(value.toString());
            }
        });
        bbuildAge=findViewById(R.id.bbuildAge);
        bbuildAgeSeekBar=findViewById(R.id.bbuildAgeSeekBar);
        bbuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                bbuildAge.setText(value.toString());
            }
        });
        buildReadySwitch=findViewById(R.id.buildReadySwitch);
        buildReadySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                buildReadySwitchBool=b;
            }
        });
    }
    Spinner home_spinner;
    TextView homeReceptionNumber,homeBathRomsNumber,homeRoomsNumber,homeBuildAge;
    CrystalSeekbar homeReceptionNumberSeekBar,homeBathRomsNumberSeekBar,homeRoomsNumberSeekBar,homeBuildAgeSeekBar;
    Switch homeReadySwitch,homeDriverRoomSwitch,homeBillGirlRoomSwitch,homeHairRoomSwitch,homeHailSwitch,homeKitchenSwitch;
    boolean homeReadySwitchBool=false,homeDriverRoomSwitchBool=false,homeBillGirlRoomSwitchBool=false
            ,homeHairRoomSwitchBool=false,homeHailSwitchBool=false,homeKitchenSwitchBool=false;
    String homeNavigation;
    private void __init_home(){
        home_spinner=findViewById(R.id.home_spinner);
        home_spinner.setAdapter(navigationAdapter);
        home_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                homeNavigation=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        homeReceptionNumber=findViewById(R.id.homeReceptionNumber);
        homeReceptionNumberSeekBar=findViewById(R.id.homeReceptionNumberSeekBar);
        homeReceptionNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                homeReceptionNumber.setText(value.toString());
            }
        });
        homeBathRomsNumber=findViewById(R.id.homeBathRomsNumber);
        homeBathRomsNumberSeekBar=findViewById(R.id.homeBathRomsNumberSeekBar);
        homeBathRomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                homeBathRomsNumber.setText(value.toString());
            }
        });
        homeRoomsNumber=findViewById(R.id.homeRoomsNumber);
        homeRoomsNumberSeekBar=findViewById(R.id.homeRoomsNumberSeekBar);
        homeRoomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                homeRoomsNumber.setText(value.toString());
            }
        });
        homeBuildAge=findViewById(R.id.homeBuildAge);
        homeBuildAgeSeekBar=findViewById(R.id.homeBuildAgeSeekBar);
        homeBuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                homeBuildAge.setText(value.toString());
            }
        });
        homeReadySwitch=findViewById(R.id.homeReadySwitch);
        homeReadySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                homeReadySwitchBool=b;
            }
        });
        homeDriverRoomSwitch=findViewById(R.id.homeDriverRoomSwitch);
        homeDriverRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                homeDriverRoomSwitchBool=b;
            }
        });
        homeBillGirlRoomSwitch=findViewById(R.id.homeBillGirlRoomSwitch);
        homeBillGirlRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                homeBillGirlRoomSwitchBool=b;
            }
        });
        homeHairRoomSwitch=findViewById(R.id.homeHairRoomSwitch);
        homeHairRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                homeHairRoomSwitchBool=b;
            }
        });
        homeHailSwitch=findViewById(R.id.homeHailSwitch);
        homeHailSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                homeHailSwitchBool=b;
            }
        });
        homeKitchenSwitch=findViewById(R.id.homeKitchenSwitch);
        homeKitchenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                homeKitchenSwitchBool=b;
            }
        });
    }
    Spinner levelSpinner;
    TextView lrRectiption,levelBathRoomsNumber,levelRoomsNumber,llevelNumber,levelBuildAge;
    CrystalSeekbar levelReceptionNumberSeekBar,levelBathRoomsNumberSeekBar,levelRoomsNumberSeekBar
            ,llevelNumberSeekBar,levelBuildAgeSeekBar;
    Switch levelReadySwitch,levelcarEnternaceSwitch,levelAirCondtionSwitch;
    Boolean levelReadySwitchBool=false,levelcarEnternaceSwitchBool=false,levelAirCondtionSwitchBool=false;
    String levelNavigaiton="";
    private void  __init_level(){
        levelSpinner=findViewById(R.id.levelSpinner);
        levelSpinner.setAdapter(navigationAdapter);
        levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                levelNavigaiton=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        lrRectiption=findViewById(R.id.levelReceptionNumber);
        levelReceptionNumberSeekBar=findViewById(R.id.levelReceptionNumberSeekBar);
        levelReceptionNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                lrRectiption.setText(value.toString());
            }
        });
        levelBathRoomsNumber=findViewById(R.id.levelBathRoomsNumber);
        levelBathRoomsNumberSeekBar=findViewById(R.id.levelBathRoomsNumberSeekBar);
        levelBathRoomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                levelBathRoomsNumber.setText(value.toString());
            }
        });
        levelRoomsNumber=findViewById(R.id.levelRoomsNumber);
        levelRoomsNumberSeekBar=findViewById(R.id.levelRoomsNumberSeekBar);
        levelRoomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                levelRoomsNumber.setText(value.toString());
            }
        });
        llevelNumber=findViewById(R.id.llevelNumber);
        llevelNumberSeekBar=findViewById(R.id.llevelNumberSeekBar);
        llevelNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                llevelNumber.setText(value.toString());
            }
        });
        levelBuildAge=findViewById(R.id.levelBuildAge);
        levelBuildAgeSeekBar=findViewById(R.id.levelBuildAgeSeekBar);
        levelBuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                levelBuildAge.setText(value.toString());
            }
        });
        levelReadySwitch=findViewById(R.id.levelReadySwitch);
        levelReadySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                levelReadySwitchBool=b;
            }
        });
        levelcarEnternaceSwitch=findViewById(R.id.levelcarEnternaceSwitch);
        levelcarEnternaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                levelcarEnternaceSwitchBool=b;
            }
        });
        levelAirCondtionSwitch=findViewById(R.id.levelAirCondtionSwitch);
        levelAirCondtionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                levelAirCondtionSwitchBool=b;
            }
        });
    }

    TextView farmeWaterFailNumber,treeNumber;
    CrystalSeekbar farmeWaterFailNumberSeekBar,treeNumberSeekBar;
    Switch farmHomeHairRoomSwitch;
    Boolean farmHomeHairRoomSwitchBool=false;
    private void __init_farm(){
        farmeWaterFailNumber=findViewById(R.id.farmeWaterFailNumber);
        farmeWaterFailNumberSeekBar=findViewById(R.id.farmeWaterFailNumberSeekBar);
        farmeWaterFailNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                farmeWaterFailNumber.setText(value.toString());
            }
        });
        treeNumber=findViewById(R.id.treeNumber);
        treeNumberSeekBar=findViewById(R.id.treeNumberSeekBar);
        treeNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                treeNumber.setText(value.toString());
            }
        });
        farmHomeHairRoomSwitch=findViewById(R.id.farmHomeHairRoomSwitch);
        farmHomeHairRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                farmHomeHairRoomSwitchBool=b;
            }
        });

    }
    Spinner marketSpinner;
    TextView marketStreetWitdth,marketBuildAge;
    CrystalSeekbar marketStreetWidthSeekBar,marketBuildAgeSeekBar;
    String marketNavigaiton="";
    private void __init__market(){
        marketSpinner=findViewById(R.id.marketSpinner);
        marketSpinner.setAdapter(navigationAdapter);
        marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                marketNavigaiton=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        marketStreetWitdth=findViewById(R.id.marketStreetWitdth);
        marketStreetWidthSeekBar=findViewById(R.id.marketStreetWidthSeekBar);
        marketBuildAge=findViewById(R.id.marketBuildAge);
        marketStreetWidthSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                marketStreetWitdth.setText(value.toString());
            }
        });
        marketBuildAgeSeekBar=findViewById(R.id.marketBuildAgeSeekBar);
        marketBuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                marketBuildAge.setText(value.toString());
            }
        });
    }
    Spinner sweetSpinner;
    TextView sweetReceptionNumber,sweetBathRomsNumber,sweetRoomsNumber,SweetStreetWidth,sweetBuildAge;
    CrystalSeekbar sweetReceptionNumberSeekBar,sweetRoomsSeekBar,sweetRoomsNumberSeekBar,SweetStreetWidthSeekbar,
            sweetBuildAgeSeekBar;
    Switch sweetPoolSwitch,sweetFootballGroundSwitch,sweetVolleyBallGroundSwitch,sweetHairRoomSwitch,
            sweetEntertanmentPlace,sweetBigBathSwitch;
    Boolean sweetPoolSwitchBool=false,sweetFootballGroundSwitchBool=false,sweetVolleyBallGroundSwitchBool=false,
            sweetHairRoomSwitchBool=false,sweetEntertanmentPlaceBool=false,sweetBigBathSwitchBool=false;
    String ressortNevigation="";
    private void __init__ressort(){
        sweetSpinner=findViewById(R.id.sweetSpinner);
        sweetSpinner.setAdapter(navigationAdapter);
        sweetSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ressortNevigation=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sweetReceptionNumber=findViewById(R.id.sweetReceptionNumber);
        sweetReceptionNumberSeekBar=findViewById(R.id.sweetReceptionNumberSeekBar);
        sweetReceptionNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                sweetReceptionNumber.setText(value.toString());
            }
        });
        sweetBathRomsNumber=findViewById(R.id.sweetBathRomsNumber);
        sweetRoomsSeekBar=findViewById(R.id.sweetRoomsSeekBar);
        sweetRoomsSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                sweetBathRomsNumber.setText(value.toString());
            }
        });
        sweetRoomsNumber=findViewById(R.id.sweetRoomsNumber);
        sweetRoomsNumberSeekBar=findViewById(R.id.sweetRoomsNumberSeekBar);
        sweetRoomsNumberSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                sweetRoomsNumber.setText(value.toString());
            }
        });
        SweetStreetWidth=findViewById(R.id.SweetStreetWidth);
        SweetStreetWidthSeekbar=findViewById(R.id.SweetStreetWidthSeekbar);
        SweetStreetWidthSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                SweetStreetWidth.setText(value.toString());
            }
        });
        sweetBuildAge=findViewById(R.id.sweetBuildAge);
        sweetBuildAgeSeekBar=findViewById(R.id.sweetBuildAgeSeekBar);
        sweetBuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                sweetBuildAge.setText(value.toString());
            }
        });
        sweetPoolSwitch=findViewById(R.id.sweetPoolSwitch);
        sweetPoolSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sweetPoolSwitchBool=b;
            }
        });
        sweetFootballGroundSwitch=findViewById(R.id.sweetFootballGroundSwitch);
        sweetFootballGroundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sweetFootballGroundSwitchBool=b;
            }
        });
        sweetVolleyBallGroundSwitch=findViewById(R.id.sweetVolleyBallGroundSwitch);
        sweetVolleyBallGroundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sweetVolleyBallGroundSwitchBool=b;
            }
        });
        sweetHairRoomSwitch=findViewById(R.id.sweetHairRoomSwitch);
        sweetHairRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sweetHairRoomSwitchBool=b;
            }
        });
        sweetEntertanmentPlace=findViewById(R.id.sweetEntertanmentPlace);
        sweetEntertanmentPlace.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sweetEntertanmentPlaceBool=b;
            }
        });
        sweetBigBathSwitch=findViewById(R.id.sweetBigBathSwitch);
        sweetBigBathSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sweetBigBathSwitchBool=b;
            }
        });

    }
    public void contuinue(View view){
                        String dutration="";
                if(daily){
                    dutration="daily";
                }else if(monthly){
                    dutration="monthly";
                }else{
                    dutration="annual";
                }
                flat=new Flat(family,readySwitchbool,kitchenSwitchbool,extentionSwitchbool
                        ,carEnternaceSwitchbool,airCondtionSwitchbool,dutration,receptionNumber.getText().toString(),
                        bathRoomsNumber.getText().toString(),roomsNumber.getText().toString(),
                        levelNumber.getText().toString(),buildAge.getText().toString());
                time++;
                flatView.setVisibility(View.GONE);
                //cv4.setVisibility(View.VISIBLE);
                // ch

    }
    Flat flat;
    Villa villa;
    Build build;
    Home home;
    Level level;
    Farm farm;
    Market market;
    Ressort ressort;
    private void uploadResult() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("تحميل");
        progressDialog.show();
        downloadUrl = new ArrayList<>();
        final OfferResult offerResult = new OfferResult();
        offerResult.setId(Calendar.getInstance().getTime().toString());
        offerResult.setDescription(offerResult.getId());
        offerResult.setBuildingType(buildType);
        switch (buildType){
            case "شقة ":
                String dutration="";
                if(daily){
                    dutration="daily";
                }else if(monthly){
                    dutration="monthly";
                }else{
                    dutration="annual";
                }
                flat=new Flat(family,readySwitchbool,kitchenSwitchbool,extentionSwitchbool
                        ,carEnternaceSwitchbool,airCondtionSwitchbool,dutration,receptionNumber.getText().toString(),
                        bathRoomsNumber.getText().toString(),roomsNumber.getText().toString(),
                        levelNumber.getText().toString(),buildAge.getText().toString());

                offerResult.setAspect(flat);
                break;
            case "فيلا ":
                villa=new Villa(villaNavigation,villaReceptionNumber.getText().toString(),villaBathRoomsNumber.getText().toString(),
                        villaStreetWidth.getText().toString(),villaRoomsNumber.getText().toString(),
                        villaLevelNumber.getText().toString(),villaBuildAge.getText().toString(),
                        villaReceptionSwitchBool,villaDriverSwitchBool,
                        villaBillGirlRoomSwitchBool,VillaPoolSwitchBool,villaHairRoomSwitchBool,VillaHallSwitchBool,villaVaultSwitchBool,
                        villaReadySwitchBool,villaKitchenSwitchBool,extentionSwitchbool,villaCarEnternaceSwitchBool,
                        villaElvatorSwitchBool,villaAirCondtionSwitchBool,villaDublexSwitchBool);
                offerResult.setAspect(villa);
                break;
            case "عمارة ":
                String buildTypeTrade="";
                if(commeiceBool){
                    buildTypeTrade="تجارى";
                }else if(buildCommericeAndHommingBool){
                    buildTypeTrade="تجارى وسكنى";
                }else {
                    buildTypeTrade="سكنى";
                }
                build=new Build(buildNavigation,buildTypeTrade,buildStreetWidth.getText().toString(),buildRoomsNumber.getText().toString(),
                        buildMarketNumber.getText().toString(),buildRomsNumber.getText().toString(),bbuildAge.getText().toString(),
                        buildReadySwitchBool);
                offerResult.setAspect(build);
                break;
            case "بيت ":
                home=new Home(homeNavigation,homeReceptionNumber.getText().toString(),homeBathRomsNumber.getText().toString(),
                        homeRoomsNumber.getText().toString(),homeBuildAge.getText().toString(),homeReadySwitchBool,
                        homeDriverRoomSwitchBool,homeBillGirlRoomSwitchBool,homeHairRoomSwitchBool,homeHailSwitchBool,homeKitchenSwitchBool);
                offerResult.setAspect(home);
                break;
            case "دور ":
                level=new Level(levelNavigaiton,lrRectiption.getText().toString(),levelBathRoomsNumber.getText().toString(),levelRoomsNumber.getText().toString(),
                        llevelNumber.getText().toString(),levelBuildAge.getText().toString(),levelReadySwitchBool,
                        levelcarEnternaceSwitchBool,levelAirCondtionSwitchBool);
                offerResult.setAspect(level);
                break;
            case "مزرعه ":
                farm=new Farm(farmeWaterFailNumber.getText().toString(),treeNumber.getText().toString(),farmHomeHairRoomSwitchBool);
                offerResult.setAspect(farm);
                break;
            case "محل ":
                market=new Market(marketNavigaiton,marketStreetWitdth.getText().toString(),marketBuildAge.getText().toString());
                offerResult.setAspect(market);
                break;
            case "استراحه ":
                ressort=new Ressort(ressortNevigation,sweetReceptionNumber.getText().toString(),sweetBathRomsNumber.getText().toString(),
                        sweetRoomsNumber.getText().toString(),sweetBigBathSwitch.getText().toString(),sweetBuildAge.getText().toString(),
                        sweetPoolSwitchBool,sweetFootballGroundSwitchBool,sweetVolleyBallGroundSwitchBool,sweetHairRoomSwitchBool,sweetEntertanmentPlaceBool,
                        sweetBigBathSwitchBool);
                offerResult.setAspect(ressort);
                break;

        }
        String dtFull=((EditText)(findViewById(R.id.detialsFull2))).getText().toString();
        offerResult.setFullDetials(dtFull);
        offerResult.setType(type);
        offerResult.setLituide(Shared.lituide);
        offerResult.setLongtuide(Shared.longtuide);
        offerResult.setSpinnerType(spinnerType.getSelectedItem().toString());
        String total_price=((EditText)findViewById(R.id.total_price_edit2)).getText().toString();
        offerResult.setPrice(total_price);
        offerResult.setCity(spinnerCity.getSelectedItem().toString());
        offerResult.setStreet(streetText.getText().toString());
        offerResult.setOfferID(FirebaseAuth.getInstance().getUid() + "*" + offerResult.getDescription());
        //   offerResult.(Shared.user.getPhoneNumber());
        offerResult.setuID(FirebaseAuth.getInstance().getUid());
        offerResult.setToID(Shared.toID);
        for (int i = 0; i < fileNameList.size(); i++) {
            if (!Shared.AddRandomButton) {
                StorageReference fileUpload = mStorage.child("OfferResult").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Shared.offerID).child(fileNameList.get(i));
                final int finalI = i;
                fileUpload.putFile(uriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.remove(finalI);
                        fileDoneList.add("done");
                        uploadListAdapter.notifyDataSetChanged();
                        downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                        if (finalI + 1 == fileNameList.size()) {
                            offerResult.setImageList(downloadUrl);
                            mReference.child(FirebaseAuth.getInstance().getUid()).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    LinkOffer linkOffer = new LinkOffer();
                                    linkOffer.setOfferID(Shared.offerID);
                                    linkOffer.setUserID(Shared.toID);
                                    FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddResultOffer.this, "تم الإضافه", Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance().getReference("Notification_MSG")
                                                    .child(Shared.toID)
                                                    .setValue(offerResult.getType()+" "+
                                                            offerResult.getBuildingType()+" "+
                                                            offerResult.getPrice());
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });

                                }
                            });
                        }
                    }
                });
            } else {
                StorageReference fileUpload = mStorage.child("OfferResult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("random").child(fileNameList.get(i));
                final int finalI = i;
                fileUpload.putFile(uriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.remove(finalI);
                        fileDoneList.add("done");
                        uploadListAdapter.notifyDataSetChanged();
                        downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                        if (finalI + 1 == fileNameList.size()) {
                            offerResult.setImageList(downloadUrl);
                            mReference.child(FirebaseAuth.getInstance().getUid()).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddResultOffer.this, "تم الإضافه", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                    Shared.AddRandomButton = false;

                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void changeColor(TextView last,TextView next){
        last.setTextColor(Color.parseColor("#000000"));
        last.setBackground(getResources().getDrawable(R.drawable.tab_layout));

        next.setTextColor(Color.parseColor("#ffffff"));
        next.setBackgroundColor(getResources().getColor(R.color.primary));
    }
    TextView orderPicture, orderType, orderDescription, orderPrice;

    int time=0;
    FloatingActionButton imgViewUpload;
    public void next(View view) {
        if (time == 0) {
            time++;
            hideView(cv1, cv2);
            changeColor(orderPicture, orderType);

        } else if (time == 1) {
            if (!getType)
                Toast.makeText(this, "من فضلك ادخل نوع العقار", Toast.LENGTH_SHORT).show();
            else {
                time++;
                hideView(cv2, cv3);
                changeColor(orderType, orderDescription);
            }

        } else if (time == 2) {
            if(buildType.equals("شقة ")) {
                View flatView=findViewById(R.id.flat_view);
                flatView.setVisibility(View.VISIBLE);
                __init__flat();
            }else if(buildType.equals("فيلا ")){
                View villaView=findViewById(R.id.villa_view);
                villaView.setVisibility(View.VISIBLE);
                __init__villa();
            }else if(buildType.equals("عمارة ")){
                View build_view=findViewById(R.id.build_view);
                build_view.setVisibility(View.VISIBLE);
                __init_build();
            }else if(buildType.equals("بيت ")){
                View homeView=findViewById(R.id.home_view);
                homeView.setVisibility(View.VISIBLE);
                __init_home();
            }else if(buildType.equals("دور ")){
                View levelView=findViewById(R.id.level_view);
                levelView.setVisibility(View.VISIBLE);
                __init_level();
            }else if(buildType.equals("مزرعه ")){
                View farmView=findViewById(R.id.farme_view);
                farmView.setVisibility(View.VISIBLE);
                __init_farm();
            }else if(buildType.equals("محل ")){
                View marketView=findViewById(R.id.market_view);
                marketView.setVisibility(View.VISIBLE);
                __init__market();
            }else if(buildType.equals("استراحه ")){
                View sweetView=findViewById(R.id.sweet_view);
                sweetView.setVisibility(View.VISIBLE);
                __init__ressort();
            }
            if (!getBuildType) {
                Toast.makeText(this, "من فضلك ادخل وصف المنشاءه", Toast.LENGTH_SHORT).show();
                priceText.setError("من فضلك ادخل سعر المنشاءه");
            } else {
                time++;
                hideView(cv3, cv4);
                changeColor(orderDescription, orderPrice);
            }
        } else if (time == 3) {
            String price = priceText.getText().toString();
//            if (price.equals("")) {
//                Toast.makeText(this, "من فضلك ادخل سعر المنشاءه", Toast.LENGTH_SHORT).show();
//                priceText.setError("من فضلك ادخل سعر المنشاءه");
//                View flatView=findViewById(R.id.flat_view);
//
//            } else {
                uploadResult();
         //   }
        }
        //   btn.setText("إنهاء العرض");


    }
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

}
