package com.sourcey.materiallogindemo.Edit;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
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
import com.sourcey.materiallogindemo.model.Home;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class HomeEditActivity extends AppCompatActivity {

    Button homeEdit;
    Home home;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_edit);
        __init_home();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        homeEdit=findViewById(R.id.homeEdit);
        homeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                home=new Home(homeNavigation,homeReceptionNumber.getText().toString(),homeBathRomsNumber.getText().toString(),
                        homeRoomsNumber.getText().toString(),homeBuildAge.getText().toString(),homeReadySwitchBool,
                        homeDriverRoomSwitchBool,homeBillGirlRoomSwitchBool,homeHairRoomSwitchBool,homeHailSwitchBool,homeKitchenSwitchBool);
                Shared.editOffer.setAspect(home);
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
    Spinner home_spinner;
    TextView homeReceptionNumber,homeBathRomsNumber,homeRoomsNumber,homeBuildAge;
    CrystalSeekbar homeReceptionNumberSeekBar,homeBathRomsNumberSeekBar,homeRoomsNumberSeekBar,homeBuildAgeSeekBar;
    Switch homeReadySwitch,homeDriverRoomSwitch,homeBillGirlRoomSwitch,homeHairRoomSwitch,homeHailSwitch,homeKitchenSwitch;
    boolean homeReadySwitchBool=false,homeDriverRoomSwitchBool=false,homeBillGirlRoomSwitchBool=false
            ,homeHairRoomSwitchBool=false,homeHailSwitchBool=false,homeKitchenSwitchBool=false;
    String homeNavigation;
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};
    ArrayAdapter<String> navigationAdapter ;
    Map<String,String>build;
        private void __init_home(){
            home_spinner=findViewById(R.id.home_spinner);
            navigationAdapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,naviagations);
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
            build= (Map<String,String>) Shared.editOffer.getAspect();
            homeKitchenSwitch.setChecked(checkTrue(build.get("homeKitchenSwitch")));
            homeReadySwitch.setChecked(checkTrue(build.get("homeReadySwitch")));
            homeBillGirlRoomSwitch.setChecked(checkTrue(build.get("homeBillGirlRoomSwitch")));
            homeHailSwitch.setChecked(checkTrue(checkTrue(build.get("homeHailSwitch"))));
            homeBathRomsNumber.setText(build.get("homeBathRomsNumber"));
            homeReceptionNumber.setText(build.get("homeReceptionNumber"));
            homeBuildAge.setText(build.get("homeBuildAge"));
            homeRoomsNumber.setText(build.get("homeRoomsNumber"));
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
