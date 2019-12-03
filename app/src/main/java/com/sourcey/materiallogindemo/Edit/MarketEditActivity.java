package com.sourcey.materiallogindemo.Edit;

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
import com.sourcey.materiallogindemo.Model.Market;
import com.sourcey.materiallogindemo.R;
import com.sourcey.materiallogindemo.Shared;

import java.util.Map;

public class MarketEditActivity extends AppCompatActivity {

    Button farmEdit;
    Market market;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_edit);
        __init__market();
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setLabel("جاري التعديل ...")
                .setMaxProgress(100);
        farmEdit=findViewById(R.id.farmEdit);
        farmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                market=new Market(marketNavigaiton,marketStreetWitdth.getText().toString(),marketBuildAge.getText().toString());
                Shared.editOffer.setAspect(market);
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
    Spinner marketSpinner;
    TextView marketStreetWitdth,marketBuildAge;
    CrystalSeekbar marketStreetWidthSeekBar,marketBuildAgeSeekBar;
    String marketNavigaiton="";
    String[]naviagations=new String[]{"غير محدد","غرب","شمال","جنوب","شمال شرق","جنوب شرقى","جنوب غربى","شمال غربى","4 شوارع","3 شوارع","شرق"};
    ArrayAdapter<String> navigationAdapter ;

    Map<String,String>build;
    private void __init__market(){
        navigationAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,naviagations);
        marketSpinner=findViewById(R.id.marketSpinner);
        marketSpinner.setAdapter(navigationAdapter);
        marketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                marketNavigaiton=naviagations[i];

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        marketStreetWitdth=findViewById(R.id.marketStreetWitdth);
        marketStreetWidthSeekBar=findViewById(R.id.marketStreetWidthSeekBar);
        marketBuildAge=findViewById(R.id.marketBuildAge);
        marketStreetWidthSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                marketStreetWitdth.setText(value.toString());
            }
        });
        marketBuildAgeSeekBar=findViewById(R.id.marketBuildAgeSeekBar);
        marketBuildAgeSeekBar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                marketBuildAge.setText(value.toString());
            }
        });
        build= (Map<String,String>) Shared.editOffer.getAspect();
        marketStreetWitdth.setText(build.get("marketStreetWitdth"));
        marketBuildAge.setText(build.get("marketBuildAge"));
    }
}
