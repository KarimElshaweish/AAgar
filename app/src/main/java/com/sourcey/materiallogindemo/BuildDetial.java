package com.sourcey.materiallogindemo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Adapter.DialogImageAdapter;
import com.sourcey.materiallogindemo.model.Deals;
import com.sourcey.materiallogindemo.model.OfferResult;
import com.sourcey.materiallogindemo.model.User;
import com.sourcey.materiallogindemo.OfferEdit.OfferEdit;
import com.sourcey.materiallogindemo.databinding.CustomeDailogBinding;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.Calendar;
import java.util.Map;


public class BuildDetial extends AppCompatActivity {

    OfferResult offerResult=new OfferResult();
    TextView type,price,city,streetWidth,reception,Btype,buildAge,marketNumber,navigation,buildready,roomNumbers,buildTypeComm,detials,
            Level,Bathrooms,AirCondtion,carEnternace,durationType,extenstion,peopleType,Furnished,kitchen,billRoom,hall,duplex,Elvator,hairRoom,
            pool,valut,waterFaillNumber,tree,bigBath,entertanmetPlalce,footballSwitch,volleyPlaygroundSwitch,ttPrice,groundArea,groundMeterPrice;
    CarouselView carouselView;
    LinearLayout fb;
    ImageView favImage;
    View Building,flatView,home,villa,level,farm,market,ressort;

    Map<String,Object> build;
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

    private String checkTrue(boolean checking){
        if(checking)
            return "نعم";
        else
            return "لا";
    }
    private void __init__(){
        ttPrice=findViewById(R.id.ttPrice);
        ttPrice.setText(offerResult.getPrice());
        switch (offerResult.getBuildingType().trim()){
            case "شقة":
                title.setText("شقة");
                __init__flat();
                break;
            case "فيلا":
                title.setText("فيلا");
                __init__villa();
                break;
            case "عمارة":
                title.setText("عمارة");
                __init__build();
                break;
            case "بيت":
                title.setText("بيت");
                __init__home();
                break;
            case "دور":
                title.setText("دور");
                __init__level();
                break;
            case "مزرعه":
                title.setText("مزرعه");
                __init__farm();
                break;
            case "محل":
                title.setText("محل");
                __init__market();
                break;
            case "استراحه":
                title.setText("استراحه");
                __init__ressort();
                break;
            case "ارض":
                title.setText("ارض");
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
        build = (Map<String, Object>) offerResult.getAspect();
        if(build!=null) {
            buildTypeComm = findViewById(R.id.groundcommtype);
            buildTypeComm.setText(build != null ? "" : build.get("groundCommType").toString());
            groundArea = findViewById(R.id.groundArea);
            groundArea.setText(build.get("groundArea").toString());
            groundMeterPrice = findViewById(R.id.groundMeterPrice);
            groundMeterPrice.setText(build.get("groundMeterPrice").toString());
            navigation = findViewById(R.id.groundNavigation);
            navigation.setText(build.get("navigation").toString());
            streetWidth = findViewById(R.id.groundStreetWidth);
            streetWidth.setText(build.get("streetWidth").toString());
        }
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
        build = (Map<String, Object>) offerResult.getAspect();
        bigBath.setText(checkTrue((boolean)build.get("sweetBigBathSwitch")));
        footballSwitch.setText(checkTrue((boolean)build.get("sweetFootballGroundSwitch")));
        volleyPlaygroundSwitch.setText(checkTrue((boolean)build.get("sweetVolleyBallGroundSwitch")));
        entertanmetPlalce.setText(checkTrue((boolean)build.get("sweetEntertanmentPlace")));
        roomNumbers.setText(build.get("sweetRoomsNumber").toString());
        Bathrooms.setText(build.get("sweetBathRomsNumber").toString());
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("ressortNavigation").toString());
        reception.setText(build.get("sweetReceptionNumber").toString());
        buildAge.setText(build.get("sweetBuildAge").toString());
        hairRoom.setText(checkTrue((boolean)build.get("sweetHairRoomSwitch")));
        pool.setText(checkTrue((boolean)build.get("sweetPoolSwitch")));
        streetWidth.setText(build.get("sweetStreetWidth").toString());
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
        build= (Map<String,Object>) offerResult.getAspect();
        buildready=findViewById(R.id.levelFurnished);
        navigation=findViewById(R.id.marketNavigation);
        streetWidth=findViewById(R.id.marketStreetWidth);
        streetWidth.setText(build.get("marketStreetWitdth").toString());
        navigation.setText(build.get("marketNavigation").toString());
        buildAge.setText(build.get("marketBuildAge").toString());
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
        build= (Map<String,Object>) offerResult.getAspect();
        tree=findViewById(R.id.farmTree);
        buildready=findViewById(R.id.levelFurnished);
        hairRoom=findViewById(R.id.farmHairRoom);
        tree.setText(build.get("treeNumber").toString());
        hairRoom.setText(checkTrue((boolean)build.get("farmHomeHairRoomSwitch")));
        waterFaillNumber.setText(build.get("farmeWaterFailNumber").toString());
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
        build= (Map<String,Object>) offerResult.getAspect();
        buildready=findViewById(R.id.levelFurnished);
        carEnternace.setText(checkTrue((boolean)build.get("levelcarEnternaceSwitch")));
        Bathrooms.setText(build.get("levelBathRoomsNumber").toString());
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("levelNavigation").toString());
        reception.setText(build.get("levelReceptionNumber").toString());
        buildAge.setText(build.get("levelBuildAge").toString());
        navigation.setText(build.get("levelNavigation").toString());
        buildready.setText((boolean)build.get("levelReadySwitch")?"نعم":"لا");
        roomNumbers.setText(build.get("levelRoomsNumber").toString());
        AirCondtion=findViewById(R.id.levelAirCondtionSwitch);
        AirCondtion.setText(checkTrue((boolean)build.get("levelAirCondtionSwitch")));
        Level=findViewById(R.id.levelLevelNumber);
        Level.setText(build.get("llevelNumber").toString());

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
        build = (Map<String, Object>) offerResult.getAspect();
        AirCondtion.setText(checkTrue((boolean)build.get("villaAirCondtionSwitch")));
        carEnternace.setText(checkTrue((boolean)build.get("villaCarEnternaceSwitch")));
        extenstion.setText(checkTrue((boolean)build.get(("extentionSwitch"))));
        kitchen.setText(checkTrue((boolean)build.get("villaKitchenSwitch")));
        buildready = findViewById(R.id.villaFurnished);
        billRoom.setText(build.get("villaBillGirlRoomSwitch") == "true" ? "نعم" : "لا");
        hall.setText(checkTrue((boolean)build.get("villaHallSwitch")));
        Bathrooms.setText(build.get("villaBathRoomsNumber").toString());
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("navigation").toString());
        reception.setText(build.get("villaReceptionNumber").toString());
        buildAge.setText(build.get("villaBuildAge").toString());
        buildready.setText((boolean)build.get("villaReadySwitch")  ? "نعم" : "لا");
        duplex.setText(checkTrue((boolean)build.get("villaDublexSwitch")));
        Elvator.setText(checkTrue((boolean)build.get("villaElvatorSwitch")));
        hairRoom.setText(checkTrue((boolean)build.get("villaHairRoomSwitch")));
        Level.setText(build.get("villaLevelNumber").toString());
        pool.setText(checkTrue((boolean)build.get("villaPoolSwitch")));
        streetWidth.setText(build.get("villaStreetWidth").toString());
        valut.setText(checkTrue((boolean)build.get("villaVaultSwitch")));
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
        build= (Map<String,Object>) offerResult.getAspect();
        kitchen.setText(checkTrue((boolean)build.get("homeKitchenSwitch")));
        buildready=findViewById(R.id.homeFurnished);
        billRoom.setText((boolean)build.get("homeBillGirlRoomSwitch")?"نعم":"لا");
        hall.setText(checkTrue((boolean)build.get("homeHailSwitch")));
        Bathrooms.setText(build.get("homeBathRomsNumber").toString());
        detials.setText(offerResult.getFullDetials());
        navigation.setText(build.get("homeNavigation").toString());
        reception.setText(build.get("homeReceptionNumber").toString());
        buildAge.setText(build.get("homeBuildAge").toString());
        navigation.setText(build.get("homeNavigation").toString());
        buildready.setText((boolean)build.get("homeReadySwitch")?"نعم":"لا");
        roomNumbers.setText(build.get("homeRoomsNumber").toString());
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
//        type.setText(offerResult.getBuildingType());
        price.setText(offerResult.getPrice());
        city.setText(offerResult.getCity());
        Btype.setText(offerResult.getType());
        build= (Map<String,Object>) offerResult.getAspect();
        detials.setText(offerResult.getFullDetials());
        buildAge.setText(build.get("bbuildAge").toString());
        marketNumber.setText(build.get("buildMarketNumber").toString());
        navigation.setText(build.get("buildNavigation").toString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            boolean ready=(boolean)build.get("buildReadySwitch");
            buildready.setText(ready?"نعم":"لا");
        }
        roomNumbers.setText(build.get("buildRomsNumber").toString());
        streetWidth.setText(build.get("buildRoomsNumber").toString());
        buildTypeComm.setText(build.get("durationType").toString());
    }

    private void __init__flat() {
        carEnternace=findViewById(R.id.flatCarEnterance);
        flatView =findViewById(R.id.flat_view);
        flatView.setVisibility(View.VISIBLE);
        Furnished=findViewById(R.id.flatFurnished);
        reception=findViewById(R.id.flatReceptionNumber);
        Level=findViewById(R.id.flatLevel);
        peopleType=findViewById(R.id.people);
        extenstion=findViewById(R.id.flatExstention);
        durationType=findViewById(R.id.flatDurationType);
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
        build= (Map<String,Object>) offerResult.getAspect();
        detials.setText(offerResult.getFullDetials());
        buildAge.setText(build.get("flatAge").toString());

        buildready.setText((boolean)build.get("furnished")?"نعم":"لا");
        roomNumbers.setText(build.get("rooms").toString());
        buildTypeComm.setText(build.get("durationType").toString());
        reception.setText(build.get("reception").toString());
        Level.setText(build.get("flatLevel").toString());
        Bathrooms.setText(build.get("bathRoom").toString());
        AirCondtion.setText((boolean)build.get("airCondition")?"نعم":"لا");
        boolean carEnterance=(boolean)build.get("carEnterance");
        carEnternace.setText(carEnterance?"نعم":"لا");
        durationType.setText(build.get("durationType").toString());
        extenstion.setText((boolean)build.get("extension")?"نعم":"لا");
        peopleType.setText((boolean)build.get("family")?"عائلى":"عزاب");
        Furnished.setText((boolean)build.get("furnished")?"نعم":"لا");
        kitchen.setText((boolean)build.get("kitchen")?"نعم":"لا");
    }

    ImageView editFabButton;
    Intent intent;
    TextView title;
    LinearLayout userNameLinearLayout;
    User userDB=new User();
    TextView userNameText;
    void getOwnerName(String uid,final TextView userNameTextFunc){
        FirebaseDatabase.getInstance().getReference().child("user").child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userDB=dataSnapshot.getValue(User.class);
                        userNameTextFunc.setText(userDB.getName());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    boolean sold=false;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_detial);
        try{
            carouselView = findViewById(R.id.carouselView);
            intent=getIntent();
            sold=intent.getBooleanExtra("sold",false);
            if(sold)
                (findViewById(R.id.soldImage)).setVisibility(View.VISIBLE);

            userNameText=findViewById(R.id.offerOrderUserName);
            title=findViewById(R.id.title);
            ImageView btnChat=findViewById(R.id.btnChat);
            RelativeLayout btnChooser=findViewById(R.id.btnChooser);
            if(!Shared.customer){
                String position=getIntent().getStringExtra("position");
                if(position!=null&& !position.equals(""))
                    position=String.valueOf(Integer.parseInt(position)+1);
                else
                    position="";
                position=" عرضى "+position;
                userNameText.setText(position);
                //   btnChat.setVisibility(View.GONE);
                CardView chatCardViwLayout=findViewById(R.id.chatCardViwLayout);
                if(!sold)
                    chatCardViwLayout.setVisibility(View.GONE);
                else {
                    TextView userNameChat=findViewById(R.id.userNameChat);
                    getOwnerName(Shared.offerKnow.getuID(), userNameChat);
                }

            }else {
                TextView userNameChat=findViewById(R.id.userNameChat);
                getOwnerName(Shared.offerKnow.getuID(),userNameChat);
                getOwnerName(Shared.offerKnow.getuID(),userNameText);
            }
            offerResult=Shared.offerKnow;
            Gson gson=new Gson();
            OfferResult offerResultJson=gson.fromJson(getIntent().getStringExtra("offer_result"),OfferResult.class);
            if(offerResultJson!=null)
                this.offerResult=offerResultJson;
            if(checkEnablesButon()){
                if(Shared.customer) {
                    btnChat.setVisibility(View.VISIBLE);
                    //  userNameLinearLayout.setVisibility(View.VISIBLE);


                }
                btnChooser.setVisibility(View.VISIBLE);
            }

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
                }
            });
            TextView countButton=findViewById(R.id.count_utton);
            String size= String.valueOf(offerResult.getImageList().size());
            countButton.setText(size);
            carouselView.setPageCount(offerResult.getImageList().size());
            carouselView.setImageListener(imageListener);

            carouselView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

    }

    private boolean checkEnablesButon() {
        return intent.hasExtra("enable")&&
                getIntent().getStringExtra("enable").equals("0");
    }

    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            Glide.with(BuildDetial.this).load(offerResult.getImageList().get(position)).into(imageView);
        }
    };

    public void Finish(View view) {
//        if(!checkEnablesButon())
//            Shared.offerKnow=null;
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
        Intent intent=new Intent(this,ShowBuildMap.class);
        intent.putExtra("type",offerResult.getType());
        intent.putExtra("build",offerResult.getBuildingType());
        intent.putExtra("price",offerResult.getPrice());
        startActivity(intent);
    }

    String user=FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public void onBackPressed() {
        //   Shared.offerKnow=null;
        finish();
    }
    public void choseOffer(View view) {


        CustomeDailogBinding customeDailogBinding= DataBindingUtil.inflate(LayoutInflater.from(this),R.layout.custome_dailog,null,false);
        final Dialog dialog=new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(customeDailogBinding.getRoot());
        dialog.show();



        String str="هل موافق ل";
        str+=Shared.MyOffer.getType()+" ";
        str+=Shared.MyOffer.getBuildingTyp()+"";
        str+=" "+ " مقابل";
        str+=Shared.MyOffer.getPrice();
        str+=" ريال سعودى ";
        customeDailogBinding.topText.setText(str);
        customeDailogBinding.cansel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        customeDailogBinding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                                        Deals deals = new Deals();
                                        deals.setPrice(Double.parseDouble(Shared.MyOffer.getPrice()));
                                        deals.setCity(Shared.MyOffer.getCity());
                                        deals.setAgree(false);
                                        deals.setOfferName(Shared.MyOffer.getBuildingTyp());
                                        deals.setOffer(Shared.MyOffer);
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
                                                        startActivity(new Intent(BuildDetial.this, MyOfferNeeded.class));
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
            }
        });
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle("هل انت موافق على السعر ؟");
//        final TextView input = new EditText(this);
//        input.setInputType(InputType.TYPE_CLASS_NUMBER);
//        input.setRawInputType(Configuration.KEYBOARD_12KEY);
//        input.setText(offerResult.getPrice());
//        alert.setView(input);
//        alert.setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String text=input.getText().toString();
//                if(!text.equals("")){
//                    KProgressHUD hud = KProgressHUD.create(BuildDetial.this)
//                            .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
//                            .setLabel("Please wait")
//                            .setMaxProgress(100)
//                            .show();
//                    FirebaseDatabase.getInstance().getReference("linkOffer").child(user).child(Shared.offerID).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            FirebaseDatabase.getInstance().getReference("linkOffer").child(user).child(Shared.offerID).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    FirebaseDatabase.getInstance().getReference("ArchiveOrder").child(user).child(Shared.MyOffer.getOfferID()).setValue(Shared.MyOffer).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Toast.makeText(BuildDetial.this, "تم إختيار هذا العرض فقط ", Toast.LENGTH_SHORT).show();
//                                            FirebaseDatabase.getInstance().getReference("OfferNeeded").child(user)
//                                                    .child(Shared.MyOffer.getOfferID()).setValue(null);
//                                            Deals deals=new Deals();
//                                            deals.setPrice(Double.parseDouble(Shared.MyOffer.getPrice()));
//                                            deals.setCity(Shared.MyOffer.getCity());
//                                            deals.setAgree(false);
//                                            deals.setOfferName(Shared.MyOffer.getBuildingTyp());
//                                            deals.setOffer(Shared.MyOffer);
//                                            FirebaseDatabase.getInstance().getReference("Deals")
//                                                    .child(offerResult.getuID())
//                                                    .child(Calendar.getInstance().getTime().toString())
//                                                    .setValue(deals);
//                                            FirebaseDatabase.getInstance().getReference("Sold")
//                                                    .child(offerResult.getuID())
//                                                    .child(offerResult.getDescription())
//                                                    .setValue(offerResult).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    FirebaseDatabase.getInstance().getReference("OfferResult")
//                                                            .child(offerResult.getuID())
//                                                            .child(offerResult.getDescription()).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            startActivity(new Intent(BuildDetial.this,MyOfferNeeded.class));
//                                                            Toast.makeText(BuildDetial.this, "End", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                    });
//                                                }
//                                            });
//                                        }
//                                    });
//
//
//                                }
//                            });
//
//                        }
//                    });
//                }else{
//                    Toast.makeText(BuildDetial.this, "من فضلك ادخل السعر", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        alert.setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                //Put actions for CANCEL button here, or leave in blank
//            }
//        });
//        alert.show();

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

    public void openImages(View view) {
        final Dialog dialog = new Dialog(BuildDetial.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_list_img);

        RecyclerView rv=dialog.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(BuildDetial.this));
        DialogImageAdapter adapterimg=new DialogImageAdapter(offerResult.getImageList(),BuildDetial.this,dialog);
        rv.setAdapter(adapterimg);
        dialog.show();
    }
}
