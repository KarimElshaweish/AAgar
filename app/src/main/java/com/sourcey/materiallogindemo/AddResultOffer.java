package com.sourcey.materiallogindemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sourcey.materiallogindemo.Adapter.UploadListAdapter;
import com.sourcey.materiallogindemo.Model.LinkOffer;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.sourcey.materiallogindemo.Shared.CityArray;
import static com.sourcey.materiallogindemo.Shared.offer;
import static com.sourcey.materiallogindemo.Shared.offerID;

public class AddResultOffer extends AppCompatActivity implements LocationListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mReference = database.getReference("OfferResult");
    Spinner spinnerCity, spinnerType;
    EditText priceText, descText, streetText;
    RecyclerView rv;
    static final int PICK_VIDEO_REQUEST = 777;
    private List<String> fileNameList;
    private List<String> fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private boolean choseImage = false;
    private StorageReference mStorage;
    LinearLayout layoutForm;
    private FusedLocationProviderClient fusedLocationClient;
ListView listView;
    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale = new Locale("ar");
        Geocoder geoCoder = new Geocoder(this, locale);
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = address.get(0).getAdminArea();
            String[] tabsArray =
                    new String[]{fnialAddress};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, tabsArray);
            spinnerCity.setAdapter(adapter);
            streetText.setText(address.get(0).getLocality() + " " + address.get(0).getThoroughfare());

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void Finish(View view) {
        finish();
    }

    CardView cv1,cv2,cv3,cv4;
    ListView list2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_result_offer);
        list2=findViewById(R.id.list2);
        cv4=findViewById(R.id.cv4);
        cv3=findViewById(R.id.cv3);
        cv1=findViewById(R.id.cv1);
        cv2=findViewById(R.id.cv2);
        imgViewUpload=findViewById(R.id.imgViewUpload);
        listView=findViewById(R.id.list);
        orderPrice = findViewById(R.id.orderPrice);
        orderDescription = findViewById(R.id.orderDescription);
        orderType = findViewById(R.id.orderType);
        orderPicture = findViewById(R.id.orderPicture);
        mStorage = FirebaseStorage.getInstance().getReference();
        layoutForm = findViewById(R.id.layoutForm);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, uriList);
        rv.setAdapter(uploadListAdapter);
        spinnerCity = findViewById(R.id.citySpinner);
        spinnerType = findViewById(R.id.spinnerType);
        priceText = findViewById(R.id.priceText);
        descText = findViewById(R.id.descText);
        streetText = findViewById(R.id.StreetText);
        setTypeSpinner();


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            onLocationChanged(location);
                        }
                    }
                });
    }
    ArrayList<String>tabsArray2;

    String buildType,type;
    boolean getBuildType=false,getType=false;
     String[]tabsArray;
    private void setTypeSpinner() {
        if(!Shared.AddRandomButton)
           tabsArray =new String[]{offer.getType()};
        else{
            tabsArray =new String[]{"إيجار","شراء"};
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray);
        spinnerType.setAdapter(adapter);
        listView.setChoiceMode(listView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter);
        tabsArray2 =new ArrayList<>();
        tabsArray2.addAll(Arrays.asList(new String[]{"فيلا ", "إرض ", "دور ", "شقة ", "عمارة ", "بيت ", "استراحه ", "محل ", "مزرعه "}));
        list2.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, tabsArray2);
        list2.setAdapter(adapter2);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getType=true;
                type=tabsArray[position];
            }
        });

        list2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getBuildType=true;
                buildType=tabsArray2.get(position);
            }
        });

    }

    private void setCity() {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, CityArray);
        spinnerCity.setAdapter(adapter);
    }

    @SuppressLint("RestrictedApi")
    public void AddPicture(View view) {
        getMedia();
    }

    private void getMedia() {
        rv.setVisibility(View.VISIBLE);
        choseImage = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "اختار صور العقار"), PICK_VIDEO_REQUEST);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && choseImage) {
            imgViewUpload.setVisibility(View.GONE);
            choseImage = false;
            if (data.getClipData() != null) {
                int totalItemSelected = data.getClipData().getItemCount();
                for (int i = 0; i < totalItemSelected; i++) {
                    Uri fileUri = data.getClipData().getItemAt(i).getUri();
                    setDataToRV(fileUri);
                }
            } else if (data.getData() != null) {
                Uri fileUri = data.getData();
                setDataToRV(fileUri);
            }
        }
    }

    List<Uri> uriList = new ArrayList<>();

    private void setDataToRV(Uri fileUri) {
        File file = new File(fileUri.getPath());
        String fileName = file.getName();
        fileNameList.add(fileName);
        fileDoneList.add("uploading");
        uploadListAdapter.notifyDataSetChanged();
        uriList.add(fileUri);
    }

    List<String> downloadUrl;

    public void uploadToFirebase(View view) {
        if (fileNameList.size() == 0)
            Toast.makeText(this, "من فضلك ارفع صور لهذا العقار", Toast.LENGTH_SHORT).show();
        else {
            uploadResult();

        }
    }

    private void uploadResult() {
        final ProgressDialog progressDialog = new ProgressDialog(this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("تحميل");
        progressDialog.show();
        downloadUrl = new ArrayList<>();
        final OfferResult offerResult = new OfferResult();
        offerResult.setId(Calendar.getInstance().getTime().toString());
        offerResult.setDescription(offerResult.getId());
        offerResult.setBuildingType(buildType);
        offerResult.setType(type);
        offerResult.setLituide(Shared.lituide);
        offerResult.setLongtuide(Shared.longtuide);
        offerResult.setSpinnerType(spinnerType.getSelectedItem().toString());
        offerResult.setPrice(priceText.getText().toString());
        offerResult.setCity(spinnerCity.getSelectedItem().toString());
        offerResult.setStreet(streetText.getText().toString());
        offerResult.setOfferID(FirebaseAuth.getInstance().getUid() + "*" + offerResult.getDescription());
        //   offerResult.(Shared.user.getPhoneNumber());
        offerResult.setuID(FirebaseAuth.getInstance().getUid());
        offerResult.setToID(Shared.toID);
        for (int i = 0; i < fileNameList.size(); i++) {
            if (!Shared.AddRandomButton) {
                StorageReference fileUpload = mStorage.child("OfferResult").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child(Shared.offerID).child(fileNameList.get(i));
                final int finalI = i;
                fileUpload.putFile(uriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.remove(finalI);
                        fileDoneList.add("done");
                        uploadListAdapter.notifyDataSetChanged();
                        downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                        if (finalI + 1 == fileNameList.size()) {
                            offerResult.setImageList(downloadUrl);
                            mReference.child(FirebaseAuth.getInstance().getUid()).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    LinkOffer linkOffer = new LinkOffer();
                                    linkOffer.setOfferID(Shared.offerID);
                                    linkOffer.setUserID(Shared.toID);
                                    FirebaseDatabase.getInstance().getReference("linkOffer").child(Shared.toID).child(Shared.offerID).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(AddResultOffer.this, "تم الإضافه", Toast.LENGTH_SHORT).show();
                                            FirebaseDatabase.getInstance().getReference("Notification_MSG")
                                                    .child(Shared.toID)
                                                    .setValue(offerResult.getType()+" "+
                                                            offerResult.getBuildingType()+" "+
                                                            offerResult.getPrice());
                                            progressDialog.dismiss();
                                            finish();
                                        }
                                    });

                                }
                            });
                        }
                    }
                });
            } else {
                StorageReference fileUpload = mStorage.child("OfferResult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("random").child(fileNameList.get(i));
                final int finalI = i;
                fileUpload.putFile(uriList.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileDoneList.remove(finalI);
                        fileDoneList.add("done");
                        uploadListAdapter.notifyDataSetChanged();
                        downloadUrl.add(taskSnapshot.getDownloadUrl().toString());
                        if (finalI + 1 == fileNameList.size()) {
                            offerResult.setImageList(downloadUrl);
                            mReference.child(FirebaseAuth.getInstance().getUid()).child(offerResult.getDescription()).setValue(offerResult).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(AddResultOffer.this, "تم الإضافه", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                    Shared.AddRandomButton = false;

                                }
                            });
                        }
                    }
                });
            }
        }
    }

    private void changeColor(TextView last,TextView next){
        last.setTextColor(Color.parseColor("#000000"));
        last.setBackground(getResources().getDrawable(R.drawable.tab_layout));

        next.setTextColor(Color.parseColor("#ffffff"));
        next.setBackgroundColor(getResources().getColor(R.color.primary));
    }
    TextView orderPicture, orderType, orderDescription, orderPrice;

    int time=0;
    FloatingActionButton imgViewUpload;
    public void next(View view) {
        if (time == 0) {
            time++;
            hideView(cv1, cv2);
            changeColor(orderPicture, orderType);

        } else if (time == 1) {
            if (!getType)
                Toast.makeText(this, "من فضلك ادخل نوع العقار", Toast.LENGTH_SHORT).show();
            else {
                time++;
                hideView(cv2, cv3);
                changeColor(orderType, orderDescription);
            }

        } else if (time == 2) {

            if (!getBuildType) {
                Toast.makeText(this, "من فضلك ادخل وصف المنشاءه", Toast.LENGTH_SHORT).show();
                priceText.setError("من فضلك ادخل سعر المنشاءه");
            } else {
                time++;
                hideView(cv3, cv4);
                changeColor(orderDescription, orderPrice);
            }
        } else if (time == 3) {
            String price = priceText.getText().toString();
            if (price.equals("")) {
                Toast.makeText(this, "من فضلك ادخل سعر المنشاءه", Toast.LENGTH_SHORT).show();
                priceText.setError("من فضلك ادخل سعر المنشاءه");
            } else {
                uploadResult();
            }
        }
        //   btn.setText("إنهاء العرض");


    }
    private void hideView(final View view, final View appear){
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_down);
        //use this to make it longer:  animation.setDuration(1000);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
                appear.setVisibility(View.VISIBLE);
            }
        });

        view.startAnimation(animation);
    }

}
