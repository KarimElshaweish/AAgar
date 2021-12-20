package com.sourcey.materiallogindemo.OfferEdit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.UploadListAdapter;
import com.sourcey.materiallogindemo.Adapter.UploadListEdit;
import com.sourcey.materiallogindemo.model.Build;
import com.sourcey.materiallogindemo.model.Farm;
import com.sourcey.materiallogindemo.model.Flat;
import com.sourcey.materiallogindemo.model.Ground;
import com.sourcey.materiallogindemo.model.Home;
import com.sourcey.materiallogindemo.model.Level;
import com.sourcey.materiallogindemo.model.Market;
import com.sourcey.materiallogindemo.model.OfferResult;
import com.sourcey.materiallogindemo.model.Ressort;
import com.sourcey.materiallogindemo.model.Villa;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OfferEdit extends AppCompatActivity {

    Spinner spinnerType;
    String[]tabsArray;
    ListView listView;
    ArrayList<String>tabsArray2;
    ListView list2;
    boolean getBuildType=false,getType=false;
    String buildType,type;
    OfferResult offerResult=new OfferResult();
    private void setDataToRV(Uri fileUri) {
        File file = new File(fileUri.getPath());
        String fileName = file.getName();
        fileNameList.add(fileName);
        fileDoneList.add("uploading");
        uriList.add(fileUri);
        nuploadListAdapter.notifyDataSetChanged();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && choseImage) {
            //  imgViewUpload.setVisibility(View.GONE);
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

    private void setTypeSpinner() {
        tabsArray =new String[]{"إيجار","شراء"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray);
        spinnerType.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        tabsArray2 =new ArrayList<>();
        List<String>buildTypesList=Arrays.asList(offerResult.getBuildingType());
        tabsArray2.addAll(buildTypesList);
        list2.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray2);
        list2.setAdapter(adapter2);

        if(offerResult.getType().equals(tabsArray[0]))
            listView.setItemChecked(0,true);
        else
            listView.setItemChecked(1,true);

        int buildtypeIndex=buildTypesList.indexOf(offerResult.getBuildingType());

        list2.setItemChecked(buildtypeIndex,true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getType=true;
                type=tabsArray[position];
                orderType.setText(type);
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBuildType=true;
                buildType=tabsArray2.get(position);
                orderDescription.setText(buildType);
            }
        });

    }
    RecyclerView nrv;
    UploadListAdapter nuploadListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_edit);
        offerResult=Shared.editOffer;

        ((EditText)findViewById(R.id.detialsFull2)).setText(offerResult.getFullDetials());
        nrv=findViewById(R.id.nRv);
        nrv.setHasFixedSize(true);
        nrv.setLayoutManager(new LinearLayoutManager(this));
        fileDoneList=new ArrayList<>();
        fileNameList=new ArrayList<>();
        nuploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, uriList);
        nrv.setAdapter(nuploadListAdapter);
        navigationAdapter= new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, naviagations);
        Map<String, Object> fMap= (Map<String,Object>) offerResult.getAspect();
        switch (offerResult.getBuildingType()) {
                            case "شقة ":
                                View flatView=findViewById(R.id.flat_view);
                                flatView.setVisibility(View.VISIBLE);
                                flat= new Flat((boolean)(fMap.get("family")),
                                        ((boolean)fMap.get("furnished")),
                                        (boolean) (fMap.get("kitchen")),
                                        (boolean)(fMap.get("extension")),
                                        (boolean)(fMap.get("carEnterance")),
                                        (boolean)(fMap.get("airCondition")),
                                        fMap.get("durationType").toString(),
                                        fMap.get("reception").toString(),
                                        fMap.get("bathRoom").toString(),
                                        fMap.get("rooms").toString(),
                                        fMap.get("flatLevel").toString(),
                                        fMap.get("flatAge").toString());
                                __init__flat();
                                __init__flat__Detials();
                                break;
                            case "فيلا ":
                                View villaView=findViewById(R.id.villa_view);
                                villa=new Villa(fMap.get("navigation").toString(),
                                        fMap.get("villaReceptionNumber").toString(),
                                        fMap.get("villaBathRoomsNumber").toString(),
                                        fMap.get("villaStreetWidth").toString(),
                                        fMap.get("villaRoomsNumber").toString(),
                                        fMap.get("villaLevelNumber").toString(),
                                        fMap.get("villaBuildAge").toString(),
                                        (boolean)(fMap.get("villaReceptionSwitch")),
                                        (boolean)(fMap.get("villaDriverSwitch")),
                                        (boolean)(fMap.get("villaBillGirlRoomSwitch")),
                                        (boolean)(fMap.get("villaPoolSwitch")),
                                        (boolean)(fMap.get("villaHairRoomSwitch")),
                                        (boolean)(fMap.get("villaHallSwitch")),
                                        (boolean)(fMap.get("villaVaultSwitch")),
                                        (boolean)(fMap.get("villaReadySwitch")),
                                        (boolean)(fMap.get("villaKitchenSwitch")),
                                        (boolean)(fMap.get("extentionSwitch")),
                                        (boolean)(fMap.get("villaCarEnternaceSwitch")),
                                        (boolean)(fMap.get("villaElvatorSwitch")),
                                        (boolean)(fMap.get("villaAirCondtionSwitch")),
                                        (boolean)(fMap.get("villaDublexSwitch")));
                                villaView.setVisibility(View.VISIBLE);
                                __init__villa();
                                __init__villa__Detials();
                                break;
                            case "عمارة ":
                                build=new Build(fMap.get("buildNavigation").toString(),
                                        fMap.get("durationType").toString(),
                                        fMap.get("buildStreetWidth").toString(),
                                        fMap.get("buildRoomsNumber").toString(),
                                        fMap.get("buildMarketNumber").toString(),
                                        fMap.get("buildRomsNumber").toString(),
                                        fMap.get("bbuildAge").toString(),
                                        (boolean)(fMap.get("buildReadySwitch")));
                                View build_view=findViewById(R.id.build_view);
                                build_view.setVisibility(View.VISIBLE);
                                __init_build();
                                __init_build__Detials();
                                break;
                            case "بيت ":
                                home=new Home(fMap.get("homeNavigation").toString()
                                        ,fMap.get("homeReceptionNumber").toString(),
                                        fMap.get("homeBathRomsNumber").toString(),
                                        fMap.get("homeRoomsNumber").toString(),
                                        fMap.get("homeBuildAge").toString(),
                                        (boolean)(fMap.get("homeReadySwitch")),
                                        (boolean)(fMap.get("homeDriverRoomSwitch")),
                                        (boolean)(fMap.get("homeBillGirlRoomSwitch")),
                                        (boolean)(fMap.get("homeHairRoomSwitch")),
                                        (boolean)(fMap.get("homeHailSwitch")),
                                        (boolean)(fMap.get("homeKitchenSwitch")));
                                View homeView=findViewById(R.id.home_view);
                                homeView.setVisibility(View.VISIBLE);
                                __init_home();
                                __init_home__Detials();
                                break;
                            case "دور ":
                                level=new Level(fMap.get("levelNavigation").toString(),
                                        fMap.get("levelReceptionNumber").toString(),
                                        fMap.get("levelBathRoomsNumber").toString(),
                                        fMap.get("levelRoomsNumber").toString(),
                                        fMap.get("llevelNumber").toString(),
                                        fMap.get("levelBuildAge").toString(),
                                        (boolean)(fMap.get("levelReadySwitch")),
                                        (boolean)(fMap.get("levelcarEnternaceSwitch")),
                                        (boolean)(fMap.get("levelAirCondtionSwitch")));
                                View levelView=findViewById(R.id.level_view);
                                levelView.setVisibility(View.VISIBLE);
                                __init_level();
                                __init_level_Detials();
                                break;
                            case "مزرعه ":
                                farm=new Farm(fMap.get("farmeWaterFailNumber")
                                        .toString(),fMap.get("treeNumber").toString(),
                                        (boolean)(fMap.get("farmHomeHairRoomSwitch")));
                                View farmView=findViewById(R.id.farme_view);
                                farmView.setVisibility(View.VISIBLE);
                                __init_farm();
                                __init_farm__Detials();
                                break;
                            case "محل ":
                                market=new Market(fMap.get("marketNavigation").toString(),
                                        fMap.get("marketStreetWitdth").toString(),
                                        fMap.get("marketBuildAge").toString());
                                View marketView=findViewById(R.id.market_view);
                                marketView.setVisibility(View.VISIBLE);
                                __init__market();
                                __init__market__Detials();
                                break;
                            case "استراحه ":
                                ressort=new Ressort(fMap.get("ressortNavigation").toString(),
                                        fMap.get("sweetReceptionNumber").toString(),
                                        fMap.get("sweetBathRomsNumber").toString(),
                                        fMap.get("sweetRoomsNumber").toString(),
                                        fMap.get("sweetStreetWidth").toString(),
                                        fMap.get("sweetBuildAge").toString(),
                                        (boolean)(fMap.get("sweetPoolSwitch")),
                                        (boolean)(fMap.get("sweetFootballGroundSwitch")),
                                        (boolean)(fMap.get("sweetVolleyBallGroundSwitch")),
                                        (boolean)(fMap.get("sweetHairRoomSwitch")),
                                        (boolean)(fMap.get("sweetEntertanmentPlace")),
                                        (boolean)(fMap.get("sweetBigBathSwitch")));
                                View sweetView=findViewById(R.id.sweet_view);
                                sweetView.setVisibility(View.VISIBLE);
                                __init__ressort();
                                __init__ressort__Detials();
                                break;
                            case "أرض":
                                ground=new Ground(fMap.get("navigation").toString(),
                                        fMap.get("groundCommType").toString(),
                                        fMap.get("streetWidth").toString(),
                                        fMap.get("groundArea").toString(),
                                        fMap.get("groundMeterPrice").toString());
                                View groundView=findViewById(R.id.groundView);
                                groundView.setVisibility(View.VISIBLE);
                                __init__ground();
                                __init__ground__Detials();
                                break;
                        }
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        uploadListAdapter = new UploadListEdit( offerResult.getImageList(),this);
        rv.setAdapter(uploadListAdapter);
        mStorage = FirebaseStorage.getInstance().getReference();
        spinnerType = findViewById(R.id.spinnerType);
        priceText = findViewById(R.id.total_price_edit2);
        priceText.setText(Shared.editOffer.getPrice());
        detials=findViewById(R.id.detials);
        descText = findViewById(R.id.descText);
        streetText = findViewById(R.id.StreetText);
        list2=findViewById(R.id.list2);
        listView=findViewById(R.id.list);
        titleText=findViewById(R.id.titleText);
        cv4=findViewById(R.id.cv4);
        cv3=findViewById(R.id.cv3);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        orderPrice = findViewById(R.id.orderPrice);
        orderDescription = findViewById(R.id.orderDescription);
        orderType = findViewById(R.id.orderType);
        orderType.setText(offerResult.getType());
        orderDescription.setText(offerResult.getBuildingType());
        orderPicture = findViewById(R.id.orderPicture);
        orderPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(orderPicture.getText().toString());
                cv1.setVisibility(View.VISIBLE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.GONE);
                changeOnline(orderPicture);
                changeOffline(orderType);
                changeOffline(orderDescription);
                changeOffline(orderPrice);
            }
        });
        orderType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(orderType.getText().toString());
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.VISIBLE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.GONE);
                changeOnline(orderType);
                changeOffline(orderPicture);
                changeOffline(orderDescription);
                changeOffline(orderPrice);
            }
        });
        orderDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(orderDescription.getText().toString());
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.VISIBLE);
                cv4.setVisibility(View.GONE);
                changeOnline(orderDescription);
                changeOffline(orderPicture);
                changeOffline(orderType);
                changeOffline(orderPrice);
            }
        });
        orderPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleText.setText(orderPrice.getText().toString());
                cv1.setVisibility(View.GONE);
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);
                cv4.setVisibility(View.VISIBLE);
                changeOnline(orderPrice);
                changeOffline(orderPicture);
                changeOffline(orderType);
                changeOffline(orderDescription);
            }
        });
        setTypeSpinner();
    }

    public void Finish(View view) {
        finish();
    }
    CardView cv1,cv2,cv3,cv4;

    public void AddLocation(View view) {
    }
    int time=0;
    TextView titleText,adress;
    TextView orderPicture, orderType, orderDescription, orderPrice;
    EditText priceText, descText, streetText,detials;

    public void summitEdit(View v){
        uploadResult();

    }
    public void next(View view) {
            if (time == 0) {
                titleText.setText("نوع العقار");
                time++;
                hideView(cv1, cv2);
                changeColor(orderPicture, orderType);

            } else if (time == 1) {
                titleText.setText("تفاصيل الطلب");
                if (!getType)
                    Toast.makeText(this, "من فضلك ادخل نوع العقار", Toast.LENGTH_SHORT).show();
                else {
                    time++;
                    hideView(cv2, cv3);
                    changeColor(orderType, orderDescription);
                }

            } else if (time == 2) {
                titleText.setText("استلام العرض");
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
                }else if(buildType.equals("أرض")){
                    View groundView=findViewById(R.id.groundView);
                    groundView.setVisibility(View.VISIBLE);
                    __init__ground();
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
                uploadResult();
            }

    }
    public void AddPicture(View view) {
        getMedia();
    }
    RecyclerView rv;
    private boolean choseImage = false;
    static final int PICK_VIDEO_REQUEST = 777;

    private void getMedia() {
       // rv.setVisibility(View.VISIBLE);
        choseImage = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "اختار صور العقار"), PICK_VIDEO_REQUEST);
    }
    private void changeColor(TextView last, TextView next){
        last.setTextColor(Color.parseColor("#000000"));
        last.setBackground(getResources().getDrawable(R.drawable.tab_layout));

        next.setTextColor(Color.parseColor("#ffffff"));
        next.setBackgroundColor(getResources().getColor(R.color.primary));
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
    private void changeOnline(TextView online){
        online.setBackgroundColor(getResources().getColor(R.color.primary));
        online.setTextColor(Color.parseColor("#ffffff"));
    }
    private void changeOffline(TextView off){
        off.setBackground(getResources().getDrawable(R.drawable.tab_layout));
        off.setTextColor(Color.parseColor("#000000"));
    }
    List<String> downloadUrl;
    Flat flat;
    Villa villa;
    Build build;
    Home home;
    Level level;
    Farm farm;
    Market market;
    Ressort ressort;
    Ground ground;
    Spinner spinnerCity;
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
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private StorageReference mStorage;
    List<Uri> uriList = new ArrayList<>();

    @SuppressLint("RestrictedApi")
    private void uploadResult() {
        final KProgressHUD hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاى التعديل")
                .setMaxProgress(100)
                .show();
        downloadUrl = new ArrayList<>();

        offerResult.setDescription(offerResult.getId());
        if(buildType!=null)
        offerResult.setBuildingType(buildType);
        switch (offerResult.getBuildingType()){
            case "شقة ":
                String dutration="";
                if(daily){
                    dutration="daily";
                }else if(monthly){
                    dutration="monthly";
                }else{
                    dutration="annual";
                }


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
            case "أرض":
                ground=new Ground(groundNavigation,GroundType,groundStreetWidth.getText().toString(),groundArea.getText().toString(),
                        ground_meter_price.getText().toString());
                offerResult.setAspect(ground);
                break;

        }
        String dtFull=((EditText)(findViewById(R.id.detialsFull2))).getText().toString();
        offerResult.setFullDetials(dtFull);
        if(type!=null)
          offerResult.setType(type);
        if(Shared.lituide!=-1)
        offerResult.setLituide(Shared.lituide);
        if(Shared.longtuide!=-1)
        offerResult.setLongtuide(Shared.longtuide);
        offerResult.setSpinnerType(spinnerType.getSelectedItem().toString());
        String total_price=((EditText)findViewById(R.id.total_price_edit2)).getText().toString();
        if(!total_price.equals(""))
        offerResult.setPrice(total_price);
        offerResult.setStreet(streetText.getText().toString());
        if(fileNameList.size()>0)
        for (int i = 0; i < fileNameList.size(); i++) {
            StorageReference fileUpload = mStorage.child("OfferResult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("random").child(fileNameList.get(i));
            final int finalI = i;
            fileUpload.putFile(uriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileDoneList.remove(finalI);
                    fileDoneList.add("done");
                    nuploadListAdapter.notifyDataSetChanged();
                    downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                    if (finalI + 1 == fileNameList.size()) {
                        List<String>images=offerResult.getImageList();
                        images.addAll(downloadUrl);
                        offerResult.setImageList(images);
                        mReference.child(FirebaseAuth.getInstance().getUid()).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(OfferEdit.this, "تم الإضافه", Toast.LENGTH_SHORT).show();
                                hud.dismiss();
                                finish();
                                Shared.close=true;
                            }
                        });
                    }
                }
            });
        }
        else{
            mReference.child(FirebaseAuth.getInstance().getUid())
                    .child(offerResult.getDescription()).setValue(offerResult)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(OfferEdit.this, "تم التعديل", Toast.LENGTH_SHORT).show();
                    hud.dismiss();
                    finish();
                    Shared.close=true;
                }
            });
        }
    }
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");

    private UploadListEdit uploadListAdapter;

    TextView btnSingle,btnFamily,dailyText,monthlyTxt,annualText,receptionNumber,bathRoomsNumber,roomsNumber,levelNumber,buildAge;
    CrystalSeekbar receptionSeekBar,bathRoomsSeekBar,roomsSeekBar,levelSeekBar,buildAgeSeekBar;
    Switch readySwitch,kitchenSwitch,extentionSwitch,carEnternaceSwitch,elvatorSwitch,airCondtionSwitch;
    Boolean readySwitchbool=false
            ,kitchenSwitchbool=false
            ,extentionSwitchbool=false
            ,carEnternaceSwitchbool=false
            ,elvatorSwitchbool=false
            ,airCondtionSwitchbool=false;
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
                if(flat.isFamily()){
                    flat.setFamily(false);
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
                if(!flat.isFamily()){
                    flat.setFamily(true);
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
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};
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
    TextView groundArea,groundStreetWidth,ground_meter_price,building,groundCommeice,groundCommerceAndBuilding;
    Spinner groundSpinner;
    String groundNavigation="";
    CrystalSeekbar groundStreetWidthSeekBar;
    boolean groundCommerce=false,groundBuilding=true,groundCommerceAndBuildingl=false;
    String GroundType="سكنى";
    private void __init__ground(){
        groundCommerceAndBuilding=findViewById(R.id.groundCommericeAndHomming);

        groundCommeice=findViewById(R.id.groundCommeice);
        building=findViewById(R.id.groundhoming);
        building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groundBuilding){
                    groundBuilding=true;
                    groundCommerce=false;
                    groundCommerceAndBuildingl=false;
                    GroundType="سكنى";
                    groundCommerceAndBuilding.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommerceAndBuilding.setTextColor(Color.parseColor("#000000"));

                    groundCommeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommeice.setTextColor(Color.parseColor("#000000"));

                    building.setBackgroundColor(getResources().getColor(R.color.primary));
                    building.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        groundCommerceAndBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groundCommerceAndBuildingl){
                    groundCommerceAndBuildingl=true;
                    groundBuilding=false;
                    groundCommerce=false;
                    GroundType="سكنى وتجارى";
                    building.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    building.setTextColor(Color.parseColor("#000000"));

                    groundCommeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommeice.setTextColor(Color.parseColor("#000000"));

                    groundCommerceAndBuilding.setBackgroundColor(getResources().getColor(R.color.primary));
                    groundCommerceAndBuilding.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        groundCommeice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groundCommerce){
                    groundCommerce =true;
                    groundBuilding=false;
                    groundCommerceAndBuildingl=false;
                    GroundType="تجارى";
                    building.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    building.setTextColor(Color.parseColor("#000000"));

                    groundCommerceAndBuilding.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommerceAndBuilding.setTextColor(Color.parseColor("#000000"));

                    groundCommeice.setBackgroundColor(getResources().getColor(R.color.primary));
                    groundCommeice.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        groundSpinner=findViewById(R.id.groundspinner);
        groundSpinner.setAdapter(navigationAdapter);
        groundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groundNavigation=naviagations[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        groundStreetWidth=findViewById(R.id.groundStreetWidth);
        groundStreetWidthSeekBar=findViewById(R.id
                .groundStreetWidthSeekBar);
        groundStreetWidthSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                groundStreetWidth.setText(value.toString());
            }
        });
        groundArea=findViewById(R.id.ground_meter_ground);
        ground_meter_price=findViewById(R.id.ground_meter_price);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shared.editOffer=null;
    }

    private void __init_build__Detials(){
        Object map=offerResult.getAspect();
        Map<Object,Object>build1= (Map<Object,Object>) map;
        navigationAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,naviagations);
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
                    commeiceFunc();
                }
            }
        });
        buildCommericeAndHomming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!buildCommericeAndHommingBool){
                    buildCommericeAndHommingFunc();
                }
            }
        });
        homing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!homingBool){
                    homming();
                }
            }
        });
        if(build1.get("durationType")!=null)
            switch (build1.get("durationType").toString()){
                case "homing":
                    homming();
                    break;
                case "buildCommericeAndHomming":
                    buildCommericeAndHommingFunc();
                    break;
                case "commeice":
                    commeiceFunc();
                    break;
            }
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
        buildReadySwitch.setChecked(build.isBuildReadySwitch());
        buildReadySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                buildReadySwitchBool=b;
            }
        });

        bbuildAge.setText(build1.get("bbuildAge").toString());
        buildMarketNumber.setText(build1.get("buildMarketNumber").toString());
        buildMarketNumber.setText(build1.get("buildNavigation").toString());
        buildRoomsNumber.setText(build1.get("buildRomsNumber").toString());
        buildStreetWidth.setText(build1.get("buildRoomsNumber").toString());
    }
    private void commeiceFunc() {
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
    private void homming() {
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
    private void buildCommericeAndHommingFunc() {
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
    private void __init_home__Detials(){
        home_spinner=findViewById(R.id.home_spinner);
        navigationAdapter=new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,naviagations);
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
       Map<String, Object> build1= (Map<String,Object>) Shared.editOffer.getAspect();
        homeKitchenSwitch.setChecked( (boolean)(build1.get("homeKitchenSwitch")));
        homeReadySwitch.setChecked( (boolean)(build1.get("homeReadySwitch")));
        homeBillGirlRoomSwitch.setChecked( (boolean)(build1.get("homeBillGirlRoomSwitch")));
        homeHailSwitch.setChecked(( (boolean)(build1.get("homeHailSwitch"))));
        homeBathRomsNumber.setText(build1.get("homeBathRomsNumber").toString());

        bathRoomsSeekBar=findViewById(R.id.bathRoomsSeekBar);

        homeReceptionNumber.setText(build1.get("homeReceptionNumber").toString());

        homeBuildAge.setText(build1.get("homeBuildAge").toString());
        homeRoomsNumber.setText(build1.get("homeRoomsNumber").toString());
    }
    private void __init_farm__Detials(){
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
        Map<String, Object>build1= (Map<String,Object>) Shared.editOffer.getAspect();
        treeNumber.setText(build1.get("treeNumber").toString());
        farmHomeHairRoomSwitch.setChecked( (boolean)(build1.get("farmHomeHairRoomSwitch")));
        farmeWaterFailNumber.setText(build1.get("farmeWaterFailNumber").toString());
    }
    private void __init__flat__Detials(){
        airCondtionSwitch=findViewById(R.id.airCondtionSwitch);
        airCondtionSwitch.setChecked(flat.isAirCondition());
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
        carEnternaceSwitch.setChecked(flat.isCarEnterance());
        carEnternaceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                carEnternaceSwitchbool=b;
            }
        });
        extentionSwitch=findViewById(R.id.extentionSwitch);
        extentionSwitch.setChecked(flat.isExtension());
        extentionSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                extentionSwitchbool=b;
            }
        });
        kitchenSwitch=findViewById(R.id.kitchenSwitch);
        kitchenSwitch.setChecked(flat.isKitchen());
        kitchenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                kitchenSwitchbool=b;
            }
        });
        readySwitch=findViewById(R.id.readySwitch);
        readySwitch.setChecked(flat.isFurnished());
        readySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                readySwitchbool=b;
            }
        });
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
        if( flat.isFamily()) {
            flat.setFamily(true);
            btnSingle.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            btnSingle.setTextColor(Color.parseColor("#000000"));

            btnFamily.setBackgroundColor(getResources().getColor(R.color.primary));
            btnFamily.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            flat.setFamily(false);
            btnFamily.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            btnFamily.setTextColor(Color.parseColor("#000000"));

            btnSingle.setBackgroundColor(getResources().getColor(R.color.primary));
            btnSingle.setTextColor(Color.parseColor("#ffffff"));
        }
        btnSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flat.isFamily()){
                    flat.setFamily(false);
                    btnFamily.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    btnFamily.setTextColor(Color.parseColor("#000000"));

                    btnSingle.setBackgroundColor(getResources().getColor(R.color.primary));
                    btnSingle.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        switch (flat.getDurationType()){
            case "daily":
                monthly=false;
                annual=false;
                daily=true;
                dailyText.setBackgroundColor(getResources().getColor(R.color.primary));
                dailyText.setTextColor(Color.parseColor("#ffffff"));

                monthlyTxt.setTextColor(Color.parseColor("#000000"));
                monthlyTxt.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                annualText.setTextColor(Color.parseColor("#000000"));
                annualText.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                break;
            case "monthly":
                monthly=true;
                annual=false;
                daily=false;

                monthlyTxt.setBackgroundColor(getResources().getColor(R.color.primary));
                monthlyTxt.setTextColor(Color.parseColor("#ffffff"));

                dailyText.setTextColor(Color.parseColor("#000000"));
                dailyText.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                annualText.setTextColor(Color.parseColor("#000000"));
                annualText.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                break;
            case "annual":
                monthly=false;
                annual=true;
                daily=false;
                annualText.setBackgroundColor(getResources().getColor(R.color.primary));
                annualText.setTextColor(Color.parseColor("#ffffff"));

                dailyText.setTextColor(Color.parseColor("#000000"));
                dailyText.setBackground(getResources().getDrawable(R.drawable.tab_layout));

                monthlyTxt.setTextColor(Color.parseColor("#000000"));
                monthlyTxt.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                break;
        }
        btnFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!flat.isFamily()){
                    flat.setFamily(true);
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
        buildAge.setText(flat.getFlatAge());
        bathRoomsNumber.setText(flat.getBathRoom());
        levelNumber.setText(flat.getFlatLevel());
        roomsNumber.setText(flat.getRooms());
        receptionNumber.setText(flat.getReception());
    }
    private void __init__ground__Detials(){
        navigationAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,naviagations);

        groundCommerceAndBuilding=findViewById(R.id.groundCommericeAndHomming);
        groundCommeice=findViewById(R.id.groundCommeice);
        building=findViewById(R.id.groundhoming);
        building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groundBuilding){
                    groundBuilding=true;
                    groundCommerce=false;
                    groundCommerceAndBuildingl=false;
                    GroundType="سكنى";
                    groundCommerceAndBuilding.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommerceAndBuilding.setTextColor(Color.parseColor("#000000"));

                    groundCommeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommeice.setTextColor(Color.parseColor("#000000"));

                    building.setBackgroundColor(getResources().getColor(R.color.primary));
                    building.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        groundCommerceAndBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groundCommerceAndBuildingl){
                    groundCommerceAndBuildingl=true;
                    groundBuilding=false;
                    groundCommerce=false;
                    GroundType="سكنى وتجارى";
                    building.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    building.setTextColor(Color.parseColor("#000000"));

                    groundCommeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommeice.setTextColor(Color.parseColor("#000000"));

                    groundCommerceAndBuilding.setBackgroundColor(getResources().getColor(R.color.primary));
                    groundCommerceAndBuilding.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        groundCommeice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!groundCommerce){
                    groundCommerce =true;
                    groundBuilding=false;
                    groundCommerceAndBuildingl=false;
                    GroundType="تجارى";
                    building.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    building.setTextColor(Color.parseColor("#000000"));

                    groundCommerceAndBuilding.setBackground(getResources().getDrawable(R.drawable.tab_layout));
                    groundCommerceAndBuilding.setTextColor(Color.parseColor("#000000"));

                    groundCommeice.setBackgroundColor(getResources().getColor(R.color.primary));
                    groundCommeice.setTextColor(Color.parseColor("#ffffff"));
                }
            }
        });
        groundSpinner=findViewById(R.id.groundspinner);
        groundSpinner.setAdapter(navigationAdapter);
        groundSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                groundNavigation=naviagations[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        groundStreetWidth=findViewById(R.id.groundStreetWidth);
        groundStreetWidthSeekBar=findViewById(R.id
                .groundStreetWidthSeekBar);
        groundStreetWidthSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                groundStreetWidth.setText(value.toString());
            }
        });
        groundArea=findViewById(R.id.ground_meter_ground);
        ground_meter_price=findViewById(R.id.ground_meter_price);
       Map<String, Object> build1 = (Map<String, Object>) Shared.editOffer.getAspect();
        if(((build1.get("groundCommType").equals("groundCommerceAndBuilding")))){
            groundCommerceAndBuildingl=true;
            groundBuilding=false;
            groundCommerce=false;
            GroundType="سكنى وتجارى";
            building.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            building.setTextColor(Color.parseColor("#000000"));

            groundCommeice.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            groundCommeice.setTextColor(Color.parseColor("#000000"));

            groundCommerceAndBuilding.setBackgroundColor(getResources().getColor(R.color.primary));
            groundCommerceAndBuilding.setTextColor(Color.parseColor("#ffffff"));
        }else if(build1.get("groundCommType").equals("groundCommeice")){
            groundCommerce =true;
            groundBuilding=false;
            groundCommerceAndBuildingl=false;
            GroundType="تجارى";
            building.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            building.setTextColor(Color.parseColor("#000000"));

            groundCommerceAndBuilding.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            groundCommerceAndBuilding.setTextColor(Color.parseColor("#000000"));

            groundCommeice.setBackgroundColor(getResources().getColor(R.color.primary));
            groundCommeice.setTextColor(Color.parseColor("#ffffff"));
        }
        groundArea.setText(build1.get("groundArea").toString());
        ground_meter_price.setText(build1.get("groundMeterPrice").toString());
        groundStreetWidth.setText(build1.get("streetWidth").toString());
    }
    private void __init__market__Detials(){
        navigationAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,naviagations);
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
        Map<String, Object>build1= (Map<String,Object>) Shared.editOffer.getAspect();
        marketStreetWitdth.setText(build1.get("marketStreetWitdth").toString());
        marketBuildAge.setText(build1.get("marketBuildAge").toString());
    }
    private void __init__ressort__Detials(){
        navigationAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,naviagations);
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

        Map<String, Object>build1 = (Map<String, Object>) Shared.editOffer.getAspect();
        sweetBigBathSwitch.setChecked( (boolean)(build1.get("sweetBigBathSwitch")));
        sweetFootballGroundSwitch.setChecked( (boolean)(build1.get("sweetFootballGroundSwitch")));
        sweetVolleyBallGroundSwitch.setChecked( (boolean)(build1.get("sweetVolleyBallGroundSwitch")));
        sweetEntertanmentPlace.setChecked( (boolean)(build1.get("sweetEntertanmentPlace")));
        sweetRoomsNumber.setText(build1.get("sweetRoomsNumber").toString() );
        sweetBathRomsNumber.setText(build1.get("sweetBathRomsNumber").toString());
        sweetReceptionNumber.setText(build1.get("sweetReceptionNumber").toString());
        sweetBuildAge.setText(build1.get("sweetBuildAge").toString());
        sweetHairRoomSwitch.setChecked( (boolean)(build1.get("sweetHairRoomSwitch")));
        sweetPoolSwitch.setChecked( (boolean)(build1.get("sweetPoolSwitch")));
    }
    private void __init__villa__Detials(){
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
       Map<String, Object> build1 = (Map<String, Object>) Shared.editOffer.getAspect();
        villaAirCondtionSwitch.setChecked( (boolean)(build1.get("villaAirCondtionSwitch")));
        villaCarEnternaceSwitch.setChecked( (boolean)(build1.get("villaCarEnternaceSwitch")));
        villaKitchenSwitch.setChecked( (boolean)(build1.get("villaKitchenSwitch")));
        villaReadySwitch.setChecked( (boolean)(build1.get("villaReadySwitch")));
        villaBillGirlRoomSwitch.setChecked( (boolean)(build1.get("villaBillGirlRoomSwitch")));
        villaBathRoomsNumber.setText(build1.get("villaBathRoomsNumber").toString());
        villaReceptionNumber.setText(build1.get("villaReceptionNumber").toString());
        villaBuildAge.setText(build1.get("villaBuildAge").toString());
        villaDublexSwitch.setChecked( (boolean)(build1.get("villaDublexSwitch")));
        villaElvatorSwitch.setChecked( (boolean)(build1.get("villaElvatorSwitch")));
        villaHairRoomSwitch.setChecked( (boolean)(build1.get("villaHairRoomSwitch")));
        villaLevelNumber.setText(build1.get("villaLevelNumber").toString());
        villaStreetWidth.setText(build1.get("villaStreetWidth").toString());
        villaVaultSwitch.setChecked( (boolean)(build1.get("villaVaultSwitch")));
    }
    private void  __init_level_Detials(){
        navigationAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,naviagations);
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
        Map<String, Object>build1= (Map<String,Object>) Shared.editOffer.getAspect();
        levelcarEnternaceSwitch.setChecked( (boolean)((build1.get("levelcarEnternaceSwitch"))));
        levelBathRoomsNumber.setText(build1.get("levelBathRoomsNumber").toString());
        lrRectiption.setText(build1.get("levelReceptionNumber").toString());
        levelBuildAge.setText(build1.get("levelBuildAge").toString());
        levelReadySwitch.setChecked( (boolean)(build1.get("levelReadySwitch")));
        levelRoomsNumber.setText(build1.get("levelRoomsNumber").toString());
        levelAirCondtionSwitch.setChecked( (boolean)(build1.get("levelAirCondtionSwitch")));
        llevelNumber.setText(build1.get("llevelNumber").toString());
    }

    public void seeMap(View view) {
        startActivity(new Intent(this,EditMapActivity.class));
    }
}
