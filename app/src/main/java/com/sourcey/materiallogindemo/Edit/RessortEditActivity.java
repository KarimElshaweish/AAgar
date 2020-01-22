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
import com.sourcey.materiallogindemo.Model.Ressort;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class RessortEditActivity extends AppCompatActivity {

    Button resseortEdit;
    Ressort ressort;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ressort_edit);
        __init__ressort();
        resseortEdit=findViewById(R.id.resseortEdit);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        resseortEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                ressort=new Ressort(ressortNevigation,sweetReceptionNumber.getText().toString(),sweetBathRomsNumber.getText().toString(),
                        sweetRoomsNumber.getText().toString(),sweetBigBathSwitch.getText().toString(),sweetBuildAge.getText().toString(),
                        sweetPoolSwitchBool,sweetFootballGroundSwitchBool,sweetVolleyBallGroundSwitchBool,sweetHairRoomSwitchBool,sweetEntertanmentPlaceBool,
                        sweetBigBathSwitchBool);
                Shared.editOffer.setAspect((Map<String, String>)ressort);
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
    Spinner sweetSpinner;
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};
    ArrayAdapter<String> navigationAdapter ;

    TextView sweetReceptionNumber,sweetBathRomsNumber,sweetRoomsNumber,SweetStreetWidth,sweetBuildAge;
    CrystalSeekbar sweetReceptionNumberSeekBar,sweetRoomsSeekBar,sweetRoomsNumberSeekBar,SweetStreetWidthSeekbar,
            sweetBuildAgeSeekBar;
    Switch sweetPoolSwitch,sweetFootballGroundSwitch,sweetVolleyBallGroundSwitch,sweetHairRoomSwitch,
            sweetEntertanmentPlace,sweetBigBathSwitch;
    Boolean sweetPoolSwitchBool=false,sweetFootballGroundSwitchBool=false,sweetVolleyBallGroundSwitchBool=false,
            sweetHairRoomSwitchBool=false,sweetEntertanmentPlaceBool=false,sweetBigBathSwitchBool=false;
    String ressortNevigation="";
    Map<String,String>build;
    private void __init__ressort(){
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

        build = (Map<String, String>) Shared.editOffer.getAspect();
        sweetBigBathSwitch.setChecked(checkTrue(build.get("sweetBigBathSwitch")));
        sweetFootballGroundSwitch.setChecked(checkTrue(build.get("sweetFootballGroundSwitch")));
        sweetVolleyBallGroundSwitch.setChecked(checkTrue(build.get("sweetVolleyBallGroundSwitch")));
        sweetEntertanmentPlace.setChecked(checkTrue(build.get("sweetEntertanmentPlace")));
        sweetRoomsNumber.setText(build.get("sweetRoomsNumber") );
        sweetBathRomsNumber.setText(build.get("sweetBathRomsNumber"));
        sweetReceptionNumber.setText(build.get("sweetReceptionNumber"));
        sweetBuildAge.setText(build.get("sweetBuildAge"));
        sweetHairRoomSwitch.setChecked(checkTrue(build.get("sweetHairRoomSwitch")));
        sweetPoolSwitch.setChecked(checkTrue(build.get("sweetPoolSwitch")));
    }
    private Boolean checkTrue(Object checking){
        if(checking=="true")
            return true;
        else
            return false;
    }
}
