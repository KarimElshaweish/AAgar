package com.sourcey.materiallogindemo.Edit;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.sourcey.materiallogindemo.Model.Ground;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class LandEditActivity extends AppCompatActivity {

    Button landEdit;
    Ground ground;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_edit);
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        __init__ground();
        landEdit=findViewById(R.id.landEdit);
        landEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                ground=new Ground(groundNavigation,GroundType,groundStreetWidth.getText().toString(),groundArea.getText().toString(),
                        ground_meter_price.getText().toString());
                Shared.editOffer.setAspect(ground);
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
    TextView groundArea,groundStreetWidth,ground_meter_price,building,groundCommeice,groundCommerceAndBuilding;
    Spinner groundSpinner;
    String groundNavigation="";
    CrystalSeekbar groundStreetWidthSeekBar;
    boolean groundCommerce=false,groundBuilding=true,groundCommerceAndBuildingl=false;
    String GroundType="سكنى";
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};
    ArrayAdapter<String> navigationAdapter ;
    Map<String,String>build;
    private void __init__ground(){
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
        build = (Map<String, String>) Shared.editOffer.getAspect();
        if(((build.get("groundCommType").equals("groundCommerceAndBuilding")))){
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
        }else if(build.get("groundCommType").equals("groundCommeice")){
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
        groundArea.setText(build.get("groundArea"));
        ground_meter_price.setText(build.get("groundMeterPrice"));
        groundStreetWidth.setText(build.get("streetWidth"));
    }


}
