package com.sourcey.materiallogindemo.Edit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.model.Flat;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class FlatEditActivity extends AppCompatActivity {

    Button addBtn;
    Flat flat;
    String dutration;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_edit);
        addBtn=findViewById(R.id.add_edit);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                switch (Shared.editOffer.getBuildingType()) {
                    case "شقة ":
                         dutration = "";
                        if (daily) {
                            dutration = "daily";
                        } else if (monthly) {
                            dutration = "monthly";
                        } else {
                            dutration = "annual";
                        }
                }
                flat=new Flat(family,readySwitchbool,kitchenSwitchbool,extentionSwitchbool
                        ,carEnternaceSwitchbool,airCondtionSwitchbool,dutration,receptionNumber.getText().toString(),
                        bathRoomsNumber.getText().toString(),roomsNumber.getText().toString(),
                        levelNumber.getText().toString(),buildAge.getText().toString());

                Shared.editOffer.setAspect(flat);
                mReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Shared.editOffer.getDescription()).setValue(Shared.editOffer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hud.dismiss();
                        Shared.Edit=true;
                        finish();
                    }
                });
            }
        });
        __init__flat();
    }
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
    Map<String,String> build;
    private void __init__flat(){
        build= (Map<String,String>) Shared.editOffer.getAspect();

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
        if(build.get("family")=="true") {
            family = true;
            btnSingle.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            btnSingle.setTextColor(Color.parseColor("#000000"));

            btnFamily.setBackgroundColor(getResources().getColor(R.color.primary));
            btnFamily.setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            family = false;
            btnFamily.setBackground(getResources().getDrawable(R.drawable.tab_layout));
            btnFamily.setTextColor(Color.parseColor("#000000"));

            btnSingle.setBackgroundColor(getResources().getColor(R.color.primary));
            btnSingle.setTextColor(Color.parseColor("#ffffff"));
        }
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

       switch (build.get("durationType")){
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
        String age=build.get("flatAge");
        buildAge.setText(age);
        String bath=build.get("bathRoom");
        bathRoomsNumber.setText(bath);
        levelNumber.setText(build.get("flatLevel"));
        roomsNumber.setText(build.get("rooms"));
        receptionNumber.setText(build.get("reception"));
        if(build.get("furnished")=="true"){
            readySwitch.setChecked(true);
        }else
            readySwitch.setChecked(false);
        if(build.get("kitchen")=="true"){
            kitchenSwitch.setChecked(true);
        }else {
            kitchenSwitch.setChecked(false);
        }
        if(build.get("extension")=="true"){
            extentionSwitch.setChecked(true);
        }else
            extentionSwitch.setChecked(false);
        if(build.get("carEnterance")=="true"){
            carEnternaceSwitch.setChecked(true);
        }else{
            carEnternaceSwitch.setChecked(false);
        }
        if(build.get("airCondition")=="true")
            airCondtionSwitch.setChecked(true);
        else
            airCondtionSwitch.setChecked(false);
    }

    public void Finish(View view) {
        finish();
    }
}
