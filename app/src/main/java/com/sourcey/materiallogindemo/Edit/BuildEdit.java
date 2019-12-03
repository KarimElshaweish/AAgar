package com.sourcey.materiallogindemo.Edit;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Model.Build;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class BuildEdit extends AppCompatActivity {

    Button edit_btn;
    Build buildModel;
    KProgressHUD hud;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_edit);
        __init_build();
        edit_btn=findViewById(R.id.edit_btn);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                String buildTypeTrade="";
                if(commeiceBool){
                    buildTypeTrade="تجارى";
                }else if(buildCommericeAndHommingBool){
                    buildTypeTrade="تجارى وسكنى";
                }else {
                    buildTypeTrade="سكنى";
                }
                buildModel=new Build(buildNavigation,buildTypeTrade,buildStreetWidth.getText().toString(),buildRoomsNumber.getText().toString(),
                        buildMarketNumber.getText().toString(),buildRomsNumber.getText().toString(),bbuildAge.getText().toString(),
                        buildReadySwitchBool);
                Shared.editOffer.setAspect(build);
                mReference.child(FirebaseAuth.getInstance().getUid()).child(Shared.editOffer.getDescription()).setValue(Shared.editOffer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        hud.dismiss();
                        Shared.Edit=true;
                        finish();
                    }
                });
            }
        });
    }

    public void Finish(View view) {
        finish();
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
    ArrayAdapter<String> navigationAdapter ;
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};

    Map<String,String>build;
    private void __init_build(){
        build= (Map<String,String>) Shared.editOffer.getAspect();
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
        if(build.get("durationType")!=null)
       switch (build.get("durationType")){
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
        buildReadySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                buildReadySwitchBool=b;
            }
        });

        bbuildAge.setText(build.get("bbuildAge"));
        buildMarketNumber.setText(build.get("buildMarketNumber"));
        buildMarketNumber.setText(build.get("buildNavigation"));
        buildReadySwitch.setChecked(checkTrue(build.get("buildReadySwitch")));
        buildRoomsNumber.setText(build.get("buildRomsNumber"));
        buildStreetWidth.setText(build.get("buildRoomsNumber"));
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

    private Boolean checkTrue(Object checking){
        if(checking=="true")
            return true;
        else
            return false;
    }
}
