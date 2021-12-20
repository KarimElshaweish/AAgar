package com.sourcey.materiallogindemo.Edit;


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
import com.sourcey.materiallogindemo.model.Farm;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class FarmEditActivity extends AppCompatActivity {

    Button farmEdit;
    Farm farm;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_edit);
        __init_farm();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        farmEdit=findViewById(R.id.farmEdit);
        farmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                farm=new Farm(farmeWaterFailNumber.getText().toString(),treeNumber.getText().toString(),farmHomeHairRoomSwitchBool);
                Shared.editOffer.setAspect(farm);
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
    }

    public void Finish(View view) {
        finish();
    }
    TextView farmeWaterFailNumber,treeNumber;
    CrystalSeekbar farmeWaterFailNumberSeekBar,treeNumberSeekBar;
    Switch farmHomeHairRoomSwitch;
    Boolean farmHomeHairRoomSwitchBool=false;
    Map<String,String>build;
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
        build= (Map<String,String>) Shared.editOffer.getAspect();
        treeNumber.setText(build.get("treeNumber"));
        farmHomeHairRoomSwitch.setChecked(checkTrue(build.get("farmHomeHairRoomSwitch")));
        farmeWaterFailNumber.setText(build.get("farmeWaterFailNumber"));
    }
    private Boolean checkTrue(Object checking){
        if(checking=="true")
            return true;
        else
            return false;
    }
}
