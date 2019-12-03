package com.sourcey.materiallogindemo.Edit;

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
import com.sourcey.materiallogindemo.Model.Villa;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class VillaEdit extends AppCompatActivity {

    Button add_edit;
    Villa villa;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_villa_edit);
        __init__villa();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        add_edit=findViewById(R.id.add_edit);
        add_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                villa=new Villa(villaNavigation,villaReceptionNumber.getText().toString(),villaBathRoomsNumber.getText().toString(),
                        villaStreetWidth.getText().toString(),villaRoomsNumber.getText().toString(),
                        villaLevelNumber.getText().toString(),villaBuildAge.getText().toString(),
                        villaReceptionSwitchBool,villaDriverSwitchBool,
                        villaBillGirlRoomSwitchBool,VillaPoolSwitchBool,villaHairRoomSwitchBool,VillaHallSwitchBool,villaVaultSwitchBool,
                        villaReadySwitchBool,villaKitchenSwitchBool,false,villaCarEnternaceSwitchBool,
                        villaElvatorSwitchBool,villaAirCondtionSwitchBool,villaDublexSwitchBool);
                Shared.editOffer.setAspect(villa);
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
    Map<String,String>build;
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
        build = (Map<String, String>) Shared.editOffer.getAspect();
        villaAirCondtionSwitch.setChecked(checkTrue(build.get("villaAirCondtionSwitch")));
        villaCarEnternaceSwitch.setChecked(checkTrue(build.get("villaCarEnternaceSwitch")));
        villaKitchenSwitch.setChecked(checkTrue(build.get("villaKitchenSwitch")));
        villaReadySwitch.setChecked(checkTrue(build.get("villaReadySwitch")));
        villaBillGirlRoomSwitch.setChecked(checkTrue(build.get("villaBillGirlRoomSwitch")));
        villaBathRoomsNumber.setText(build.get("villaBathRoomsNumber"));
        villaReceptionNumber.setText(build.get("villaReceptionNumber"));
        villaBuildAge.setText(build.get("villaBuildAge"));
        villaDublexSwitch.setChecked(checkTrue(build.get("villaDublexSwitch")));
        villaElvatorSwitch.setChecked(checkTrue(build.get("villaElvatorSwitch")));
        villaHairRoomSwitch.setChecked(checkTrue(build.get("villaHairRoomSwitch")));
        villaLevelNumber.setText(build.get("villaLevelNumber"));
        villaStreetWidth.setText(build.get("villaStreetWidth"));
        villaVaultSwitch.setChecked(checkTrue(build.get("villaVaultSwitch")));
    }
    private Boolean checkTrue(Object checking){
        if(checking=="true")
            return true;
        else
            return false;
    }

    public void Finish(View view) {
        finish();
    }
}
