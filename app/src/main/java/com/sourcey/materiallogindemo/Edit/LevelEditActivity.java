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
import com.sourcey.materiallogindemo.Model.Level;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class LevelEditActivity extends AppCompatActivity {

    Button levelEdit;
    Level level;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_edit);
        __init_level();
        levelEdit=findViewById(R.id.levelEdit);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        levelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                level=new Level(levelNavigaiton,lrRectiption.getText().toString(),levelBathRoomsNumber.getText().toString(),levelRoomsNumber.getText().toString(),
                        llevelNumber.getText().toString(),levelBuildAge.getText().toString(),levelReadySwitchBool,
                        levelcarEnternaceSwitchBool,levelAirCondtionSwitchBool);
                Shared.editOffer.setAspect(level);
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
    Spinner levelSpinner;
    TextView lrRectiption,levelBathRoomsNumber,levelRoomsNumber,llevelNumber,levelBuildAge;
    CrystalSeekbar levelReceptionNumberSeekBar,levelBathRoomsNumberSeekBar,levelRoomsNumberSeekBar
            ,llevelNumberSeekBar,levelBuildAgeSeekBar;
    Switch levelReadySwitch,levelcarEnternaceSwitch,levelAirCondtionSwitch;
    Boolean levelReadySwitchBool=false,levelcarEnternaceSwitchBool=false,levelAirCondtionSwitchBool=false;
    String levelNavigaiton="";
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};
    ArrayAdapter<String> navigationAdapter ;
    Map<String,String>build;
    private void  __init_level(){
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
        build= (Map<String,String>) Shared.editOffer.getAspect();
        levelcarEnternaceSwitch.setChecked(checkTrue(checkTrue(build.get("levelcarEnternaceSwitch"))));
        levelBathRoomsNumber.setText(build.get("levelBathRoomsNumber"));
        lrRectiption.setText(build.get("levelReceptionNumber"));
        levelBuildAge.setText(build.get("levelBuildAge"));
        levelReadySwitch.setChecked(checkTrue(build.get("levelReadySwitch")));
        levelRoomsNumber.setText(build.get("levelRoomsNumber"));
        levelAirCondtionSwitch.setChecked(checkTrue(build.get("levelAirCondtionSwitch")));
        llevelNumber.setText(build.get("llevelNumber"));
    }
    private Boolean checkTrue(Object checking){
        if(checking=="true")
            return true;
        else
            return false;
    }

}
