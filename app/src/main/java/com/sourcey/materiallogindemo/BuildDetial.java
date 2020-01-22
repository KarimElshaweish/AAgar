package com.sourcey.materiallogindemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Edit.BuildEdit;
import com.sourcey.materiallogindemo.Edit.FarmEditActivity;
import com.sourcey.materiallogindemo.Edit.FlatEditActivity;
import com.sourcey.materiallogindemo.Edit.HomeEditActivity;
import com.sourcey.materiallogindemo.Edit.LandEditActivity;
import com.sourcey.materiallogindemo.Edit.LevelEditActivity;
import com.sourcey.materiallogindemo.Edit.MarketEditActivity;
import com.sourcey.materiallogindemo.Edit.RessortEditActivity;
import com.sourcey.materiallogindemo.Edit.VillaEdit;
import com.sourcey.materiallogindemo.Model.Build;
import com.sourcey.materiallogindemo.Model.Deals;
import com.sourcey.materiallogindemo.Model.Farm;
import com.sourcey.materiallogindemo.Model.Flat;
import com.sourcey.materiallogindemo.Model.Home;
import com.sourcey.materiallogindemo.Model.Level;
import com.sourcey.materiallogindemo.Model.Market;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.Ressort;
import com.sourcey.materiallogindemo.Model.Villa;
import com.sourcey.materiallogindemo.OfferEdit.OfferEdit;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static com.sourcey.materiallogindemo.Shared.offer;


public class BuildDetial extends AppCompatActivity {

    OfferResult offerResult=new OfferResult();
    TextView type,price,city,streetWidth,reception,Btype,buildAge,marketNumber,navigation,buildready,roomNumbers,buildTypeComm,detials,
    Level,Bathrooms,AirCondtion,carEnternace,durationType,extenstion,peopleType,Furnished,kitchen,billRoom,hall,duplex,Elvator,hairRoom,
    pool,valut,waterFaillNumber,tree,bigBath,entertanmetPlalce,footballSwitch,volleyPlaygroundSwitch,ttPrice,groundArea,groundMeterPrice;
    CarouselView carouselView;
    LinearLayout fb;
    ImageView favImage;
    View Building,flatView,home,villa,level,farm,market,ressort;

    Map<String,String> build;
    @Override
    protected void onResume() {
        super.onResume();
        if(Shared.close){
            Shared.close=false;
            finish();
        }
        if(Shared.Edit){
            Shared.Edit=false;
            finish();
        }
    }

    private String checkTrue(Object checking){
        if(checking=="true")
            return "نعم";
        else
            return "لا";
    }
    private void __init__(){

        ttPrice=findViewById(R.id.ttPrice);
        ttPrice.setText(offerResult.getPrice());
        switch (offerResult.getBuildingType()){
            case "شقة ":
                __init__flat();
                break;
            case "فيلا ":
                __init__villa();
                break;
            case "عمارة ":
                __init__build();
                break;
            case "بيت ":
                __init__home();
                break;
            case "دور ":
                __init__level();
                break;
            case "مزرعه ":
                __init__farm();
                break;
            case "محل ":
                __init__market();

                break;
            case "استراحه ":
                __init__ressort();
                break;
            case "أرض":
                __init__ground();
                break;

        }

        favImage=findViewById(R.id.faviamge);
        fb=findViewById(R.id.bottomlin);
    }
    View ground;
    private void __init__ground(){
        ground=findViewById(R.id.groundDetials);
        ground.setVisibility(View.VISIBLE);
        type=findViewById(R.id.groundType);
        type.setText(offerResult.getType());
        city=findViewById(R.id.groundCity);
        city.setText(offerResult.getCity());
        price=findViewById(R.id.groundPrice);
        price.setText(offerResult.getPrice());
        build = (Map<String, String>) offerResult.getAspect();
        buildTypeComm=findViewById(R.id.groundcommtype);
        buildTypeComm.setText(build.get("groundCommType"));
        groundArea=findViewById(R.id.groundArea);
        groundArea.setText(build.get("groundArea"));
        groundMeterPrice=findViewById(R.id.groundMeterPrice);
        groundMeterPrice.setText(build.get("groundMeterPrice"));
        navigation=findViewById(R.id.groundNavigation);
        navigation.setText(build.get("navigation"));
        streetWidth=findViewById(R.id.groundStreetWidth);
        streetWidth.setText(build.get("streetWidth"));
        detials=findViewById(R.id.detialsFull);
        detials.setText(offerResult.getFullDetials());


    }
    private void __init__ressort(){
        ressort = findViewById(R.id.ressort_details_view);
        ressort.setVisibility(View.VISIBLE);
        entertanmetPlalce=findViewById(R.id.entertanmetPlalce);
        detials = findViewById(R.id.sweetdetialsFull);
        type = findViewById(R.id.sweetType);
        volleyPlaygroundSwitch=findViewById(R.id.volleyPlaygroundSwitch);
        footballSwitch=findViewById(R.id.footballSwitch);
        price = findViewById(R.id.sweetPrice);
        city = findViewById(R.id.sweetCity);
        Btype = findViewById(R.id.sweetcommtype);
        buildAge = findViewById(R.id.sweetAge);
        streetWidth = findViewById(R.id.sweetStreetWidth);
        reception = findViewById(R.id.sweetReceptionNumber);
        navigation = findViewById(R.id.sweetNavigation);
        Bathrooms = findViewById(R.id.sweetBathrooms);
        roomNumbers = findViewById(R.id.sweetRoomNumber);
        bigBath=findViewById(R.id.bigBathSwitch);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        pool=findViewById(R.id.sweetPool);
        hairRoom=findViewById(R.id.sweetHairRoomSwitch);
        build = (Map<String, String>) offerResult.getAspect();
        bigBath.setText(checkTrue(build.get("sweetBigBathSwitch")));
        footballSwitch.setText(checkTrue(build.get("sweetFootballGroundSwitch")));
        volleyPlaygroundSwitch.setText(checkTrue(build.get("sweetVolleyBallGroundSwitch")));
        entertanmetPlalce.setText(checkTrue(build.get("sweetEntertanmentPlace")));
        roomNumbers.setText(build.get("sweetRoomsNumber") == "true" ? "نعم" : "لا");
        Bathrooms.setText(build.get("sweetBathRomsNumber"));
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("ressortNavigation"));
        reception.setText(build.get("sweetReceptionNumber"));
        buildAge.setText(build.get("sweetBuildAge"));
        hairRoom.setText(checkTrue(build.get("sweetHairRoomSwitch")));
        pool.setText(checkTrue(build.get("sweetPoolSwitch")));
        streetWidth.setText(build.get("sweetStreetWidth"));
    }
    private void __init__market(){
        market=findViewById(R.id.market_view_detials);
        market.setVisibility(View.VISIBLE);
        detials=findViewById(R.id.marketdetialsFull);
        type=findViewById(R.id.marketType);
        price=findViewById(R.id.marketPrice);
        city=findViewById(R.id.marketCity);
        Btype=findViewById(R.id.marketcommtype);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        buildAge=findViewById(R.id.marketAge);
        build= (Map<String,String>) offerResult.getAspect();
        buildready=findViewById(R.id.levelFurnished);
        navigation=findViewById(R.id.marketNavigation);
        streetWidth=findViewById(R.id.marketStreetWidth);
        streetWidth.setText(build.get("marketStreetWitdth"));
        navigation.setText(build.get("marketNavigation"));
        buildAge.setText(build.get("marketBuildAge"));
        detials.setText(offerResult.getFullDetials());
    }
    private void __init__farm(){
        farm=findViewById(R.id.farm_view_details);
        farm.setVisibility(View.VISIBLE);
        detials=findViewById(R.id.farmdetialsFull);
        waterFaillNumber=findViewById(R.id.farmWaterllFailNumber);
        type=findViewById(R.id.farmType);
        price=findViewById(R.id.farmPrice);
        city=findViewById(R.id.farmCity);
        Btype=findViewById(R.id.farmcommtype);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        build= (Map<String,String>) offerResult.getAspect();
        tree=findViewById(R.id.farmTree);
        buildready=findViewById(R.id.levelFurnished);
        hairRoom=findViewById(R.id.farmHairRoom);
        tree.setText(build.get("treeNumber"));
        hairRoom.setText(checkTrue(build.get("farmHomeHairRoomSwitch")));
        waterFaillNumber.setText(build.get("farmeWaterFailNumber"));
        detials.setText(offerResult.getFullDetials());
    }
    private void __init__level(){
        level=findViewById(R.id.level_view);
        level.setVisibility(View.VISIBLE);
        detials=findViewById(R.id.leveldetialsFull);
        type=findViewById(R.id.levelType);
        price=findViewById(R.id.levelPrice);
        city=findViewById(R.id.levelCity);
        Btype=findViewById(R.id.levelcommtype);
        buildAge=findViewById(R.id.levelAge);
        roomNumbers=findViewById(R.id.levelRoomsNumber);
        reception=findViewById(R.id.levelReceptionNumber);
        navigation=findViewById(R.id.levelNavigation);
        Bathrooms=findViewById(R.id.levelBathrooms);
        carEnternace=findViewById(R.id.levelCarEnterance);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        build= (Map<String,String>) offerResult.getAspect();
        buildready=findViewById(R.id.levelFurnished);
        carEnternace.setText(checkTrue(build.get("levelcarEnternaceSwitch")));
        Bathrooms.setText(build.get("levelBathRoomsNumber"));
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("levelNavigation"));
        reception.setText(build.get("levelReceptionNumber"));
        buildAge.setText(build.get("levelBuildAge"));
        navigation.setText(build.get("levelNavigation"));
        buildready.setText(build.get("levelReadySwitch")=="true"?"نعم":"لا");
        roomNumbers.setText(build.get("levelRoomsNumber"));
        AirCondtion=findViewById(R.id.levelAirCondtionSwitch);
        AirCondtion.setText(checkTrue(build.get("levelAirCondtionSwitch")));
        Level=findViewById(R.id.levelLevelNumber);
        Level.setText(build.get("llevelNumber"));

    }
    private void __init__villa() {
        villa = findViewById(R.id.home_view);
        villa.setVisibility(View.VISIBLE);
        detials = findViewById(R.id.homedetialsFull);
        type = findViewById(R.id.villaType);
        price = findViewById(R.id.villaPrice);
        city = findViewById(R.id.villaCity);
        Btype = findViewById(R.id.villacommtype);
        buildAge = findViewById(R.id.villaAge);
        navigation = findViewById(R.id.villaNavigation);
        streetWidth = findViewById(R.id.streetWidth);
        reception = findViewById(R.id.villaReceptionNumber);
        navigation = findViewById(R.id.homeNavigation);
        Bathrooms = findViewById(R.id.homeBathrooms);
        billRoom = findViewById(R.id.villaBathrooms);
        hall = findViewById(R.id.villaHall);
        kitchen = findViewById(R.id.villaKitchen);
        extenstion=findViewById(R.id.villaExtention);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        pool=findViewById(R.id.villPool);
        AirCondtion=findViewById(R.id.villaAirCondtionSwitch);
        carEnternace=findViewById(R.id.villaCarEnterance);
        duplex=findViewById(R.id.villaDublexSwitch);
        Elvator=findViewById(R.id.villaElvatorSwitch);
        hairRoom=findViewById(R.id.villaHairRoomSwitch);
        Level=findViewById(R.id.villLevelNumber);
        streetWidth=findViewById(R.id.villaStreetWidth);
        valut=findViewById(R.id.villaVaultSwitch);
        build = (Map<String, String>) offerResult.getAspect();
        AirCondtion.setText(checkTrue(build.get("villaAirCondtionSwitch")));
        carEnternace.setText(checkTrue(build.get("villaCarEnternaceSwitch")));
        extenstion.setText(checkTrue(build.get(("extentionSwitch"))));
        kitchen.setText(checkTrue(build.get("villaKitchenSwitch")));
        buildready = findViewById(R.id.villaFurnished);
        billRoom.setText(build.get("villaBillGirlRoomSwitch") == "true" ? "نعم" : "لا");
        hall.setText(checkTrue(checkTrue(build.get("villaHallSwitch"))));
        Bathrooms.setText(build.get("villaBathRoomsNumber"));
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("villaNavigation"));
        reception.setText(build.get("villaReceptionNumber"));
        buildAge.setText(build.get("villaBuildAge"));
        buildready.setText(build.get("villaReadySwitch") == "true" ? "نعم" : "لا");
        duplex.setText(checkTrue(build.get("villaDublexSwitch")));
        Elvator.setText(checkTrue(build.get("villaElvatorSwitch")));
        hairRoom.setText(checkTrue(build.get("villaHairRoomSwitch")));
        Level.setText(build.get("villaLevelNumber"));
        pool.setText(checkTrue(build.get("villaPoolSwitch")));
        streetWidth.setText(build.get("villaStreetWidth"));
        valut.setText(checkTrue(build.get("villaVaultSwitch")));
    }

    private void __init__home() {
        home=findViewById(R.id.home_view);
        home.setVisibility(View.VISIBLE);
        detials=findViewById(R.id.homedetialsFull);
        type=findViewById(R.id.homeType);
        price=findViewById(R.id.homePrice);
        city=findViewById(R.id.homeCity);
        Btype=findViewById(R.id.homecommtype);
        buildAge=findViewById(R.id.homeAge);
        navigation=findViewById(R.id.navigation);
        roomNumbers=findViewById(R.id.homeroomNumbers);
        streetWidth=findViewById(R.id.streetWidth);
        reception=findViewById(R.id.homeReceptionNumber);
        navigation=findViewById(R.id.homeNavigation);
        Bathrooms=findViewById(R.id.homeBathrooms);
        billRoom=findViewById(R.id.homeBillRoom);
        hall=findViewById(R.id.homeHall);
        kitchen=findViewById(R.id.homeKitchen);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        build= (Map<String,String>) offerResult.getAspect();
        kitchen.setText(checkTrue(build.get("homeKitchenSwitch")));
        buildready=findViewById(R.id.homeFurnished);
        billRoom.setText(build.get("homeBillGirlRoomSwitch")=="true"?"نعم":"لا");
        hall.setText(checkTrue(build.get("homeHailSwitch")));
        Bathrooms.setText(build.get("homeBathRomsNumber"));
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("homeNavigation"));
        reception.setText(build.get("homeReceptionNumber"));
        buildAge.setText(build.get("homeBuildAge"));
        navigation.setText(build.get("homeNavigation"));
        buildready.setText(build.get("homeReadySwitch")=="true"?"نعم":"لا");
        roomNumbers.setText(build.get("homeRoomsNumber"));
    }

    private void __init__build() {
        Building=findViewById(R.id.Building);
        Building.setVisibility(View.VISIBLE);
        detials=findViewById(R.id.detialsFull);
        type=findViewById(R.id.buildType);
        price=findViewById(R.id.buildPrice);
        city=findViewById(R.id.buildCity);
        Btype=findViewById(R.id.commtype);
        buildAge=findViewById(R.id.buildAge);
        marketNumber=findViewById(R.id.marketNumber);
        navigation=findViewById(R.id.navigation);
        buildready=findViewById(R.id.buildready);
        roomNumbers=findViewById(R.id.roomNumbers);
        streetWidth=findViewById(R.id.streetWidth);
        buildTypeComm=findViewById(R.id.buildTypeComm);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        build= (Map<String,String>) offerResult.getAspect();
        detials.setText(offerResult.getFullDetials());
        buildAge.setText(build.get("bbuildAge"));
        marketNumber.setText(build.get("buildMarketNumber"));
        navigation.setText(build.get("buildNavigation"));
        buildready.setText(build.get("buildReadySwitch")=="true"?"نعم":"لا");
        roomNumbers.setText(build.get("buildRomsNumber"));
        streetWidth.setText(build.get("buildRoomsNumber"));
        buildTypeComm.setText(build.get("durationType"));
    }

    private void __init__flat() {
        flatView =findViewById(R.id.flat_view);
        flatView.setVisibility(View.VISIBLE);
        Furnished=findViewById(R.id.flatFurnished);
        reception=findViewById(R.id.flatReceptionNumber);
        Level=findViewById(R.id.flatLevel);
        peopleType=findViewById(R.id.people);
        extenstion=findViewById(R.id.flatExstention);
        durationType=findViewById(R.id.flatDurationType);
        carEnternace=findViewById(R.id.homeCarEnterance);
        AirCondtion=findViewById(R.id.flatAirCondtion);
        Bathrooms=findViewById(R.id.flatBathrooms);
        detials=findViewById(R.id.detialsFull);
        type=findViewById(R.id.flatType);
        price=findViewById(R.id.flatPrice);
        city=findViewById(R.id.flatCity);
        Btype=findViewById(R.id.commtype);
        buildAge=findViewById(R.id.flatAge);
        marketNumber=findViewById(R.id.marketNumber);
        navigation=findViewById(R.id.navigation);
        buildready=findViewById(R.id.buildready);
        roomNumbers=findViewById(R.id.flatBathrooms);
        streetWidth=findViewById(R.id.streetWidth);
        buildTypeComm=findViewById(R.id.buildTypeComm);
        kitchen=findViewById(R.id.flatKitchen);
        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        build= (Map<String,String>) offerResult.getAspect();
        detials.setText(offerResult.getFullDetials());
        buildAge.setText(build.get("flatAge"));
        buildready.setText(build.get("furnished")=="true"?"نعم":"لا");
        roomNumbers.setText(build.get("rooms"));
        buildTypeComm.setText(build.get("durationType"));
        reception.setText(build.get("reception"));
        Level.setText(build.get("flatLevel"));
        Bathrooms.setText(build.get("bathRoom"));
        AirCondtion.setText(build.get("airCondition")=="true"?"نعم":"لا");
        carEnternace.setText(build.get("carEnterance")=="true"?"نعم":"لا");
        durationType.setText(build.get("durationType"));
        extenstion.setText(build.get("extension")=="true"?"نعم":"لا");
        peopleType.setText(build.get("family")=="true"?"عائلى":"عزاب");
        Furnished.setText(build.get("furnished")=="true"?"نعم":"لا");
        kitchen.setText(build.get("kitchen")=="true"?"نعم":"لا");
    }

    FloatingActionButton editFabButton;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detial);
        offerResult=Shared.offerKnow;
        editFabButton=findViewById(R.id.fab_edit);
        __init__();
        if(offerResult.isFav()){
            favImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_yallewo));
        }else{
            favImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_white));
        }
        if(Shared.user.getType().equals(Shared.Array[0]))
            fb.setVisibility(View.GONE);
        else
            editFabButton.setVisibility(View.GONE);
        editFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shared.editOffer=new OfferResult();
                Shared.editOffer=offerResult;
                startActivity(new Intent(BuildDetial.this, OfferEdit.class));
//
//                }

            }
        });
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(offerResult.getImageList().size());
        carouselView.setImageListener(imageListener);

    }
        ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                Glide.with(BuildDetial.this).load(offerResult.getImageList().get(position)).into(imageView);
            }
        };

    public void Finish(View view) {
        Shared.offerKnow=null;
        finish();
    }

    public void Chat(View view) {
        Intent intent=new Intent(this,ChatAct.class);
        Shared.sent_id=offerResult.getuID();
        Shared.offerKnow=offerResult;
        Shared.fristTime=true;
        startActivity(intent);
    }

    public void Map(View view) {
        startActivity(new Intent(this,ShowBuildMap.class));
    }

    String user=FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onBackPressed() {
       Shared.offerKnow=null;
       finish();
    }


    public void choseOffer(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("هل انت موافق على السعر ؟");
        final TextView input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        input.setText(offerResult.getPrice());
        alert.setView(input);
        alert.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String text=input.getText().toString();
                if(!text.equals("")){
                    KProgressHUD hud = KProgressHUD.create(BuildDetial.this)
                            .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                            .setLabel("Please wait")
                            .setMaxProgress(100)
                            .show();
                    FirebaseDatabase.getInstance().getReference("linkOffer").child(user).child(Shared.offerID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("linkOffer").child(user).child(Shared.offerID).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    FirebaseDatabase.getInstance().getReference("ArchiveOrder").child(user).child(Shared.MyOffer.getOfferID()).setValue(Shared.MyOffer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(BuildDetial.this, "تم إختيار هذا العرض فقط ", Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance().getReference("OfferNeeded").child(user)
                                                    .child(Shared.MyOffer.getOfferID()).setValue(null);
                                            Deals deals=new Deals();
                                            deals.setPrice(Double.parseDouble(Shared.MyOffer.getPrice()));
                                            deals.setCity(Shared.MyOffer.getCity());
                                            deals.setAgree(false);
                                            deals.setOfferName(Shared.MyOffer.getBuildingTyp());
                                            FirebaseDatabase.getInstance().getReference("Deals")
                                                    .child(offerResult.getuID())
                                                    .child(Calendar.getInstance().getTime().toString())
                                                    .setValue(deals);
                                            FirebaseDatabase.getInstance().getReference("Sold")
                                                    .child(offerResult.getuID())
                                                    .child(offerResult.getDescription())
                                                    .setValue(offerResult).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    FirebaseDatabase.getInstance().getReference("OfferResult")
                                                            .child(offerResult.getuID())
                                                            .child(offerResult.getDescription()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            startActivity(new Intent(BuildDetial.this,MyOfferNeeded.class));
                                                            Toast.makeText(BuildDetial.this, "End", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });


                                }
                            });

                        }
                    });
                }else{
                    Toast.makeText(BuildDetial.this, "من فضلك ادخل السعر", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Put actions for CANCEL button here, or leave in blank
            }
        });
        alert.show();

    }
    ImageView fav;
    ProgressDialog progressDialog;
    public void AddToFavoutit(View view) {
        progressDialog=new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        fav=(ImageView)view;
        if(offerResult.isFav()){
            offerResult.setFav(false);
            progressDialog.setTitle("جارى الحذف من المفضله");
            progressDialog.show();
            FirebaseDatabase.getInstance().getReference("Fav")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(offerResult.getId())
                    .setValue(null)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference("linkOffer")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(Shared.offerID)
                            .child(offerResult.getDescription())
                            .setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_white));
                        }
                    });

                }
            });
        }else{
            progressDialog.setTitle("اضافة الى المفضله....");
            progressDialog.show();
            offerResult.setFav(true);
            FirebaseDatabase.getInstance().getReference("Fav").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(offerResult.getId()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    FirebaseDatabase.getInstance().getReference("Fav").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child(offerResult.getId()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            FirebaseDatabase.getInstance().getReference("linkOffer")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(Shared.offerID)
                                    .child(offerResult.getDescription())
                                    .setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    fav.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_yallewo));

                                }
                            });
                        }
                    });
                }
            });
        }
    }

    private String m_Text = "";
    public void offerFeedBack(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("الشكوى");
// Set up the input
        final EditText input = new EditText(this);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("إرسال", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                FirebaseDatabase.getInstance().getReference("OfferFeedBack")
                        .child(offerResult.getOfferID())
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Calendar.getInstance().getTime().toString())
                        .setValue(m_Text).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getBaseContext(),"تم إرسال الشكوى",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        builder.setNegativeButton("إالغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
