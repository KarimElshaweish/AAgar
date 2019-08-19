package com.sourcey.materiallogindemo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.sourcey.materiallogindemo.Model.OfferResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.sourcey.materiallogindemo.Shared.CityArray;
import static com.sourcey.materiallogindemo.Shared.offer;
import static com.sourcey.materiallogindemo.Shared.offerID;

public class AddResultOffer extends AppCompatActivity implements LocationListener {

    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference mReference=database.getReference("OfferResult");
    Spinner spinnerCity,spinnerType;
    EditText priceText,descText,streetText;
    RecyclerView rv;
    static final int PICK_VIDEO_REQUEST=777;
    private List<String> fileNameList;
    private List<String>fileDoneList;
    private UploadListAdapter uploadListAdapter;
    private boolean choseImage=false;
    private StorageReference mStorage;
    LinearLayout layoutForm;
    private FusedLocationProviderClient fusedLocationClient;
    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Locale locale=new Locale("ar");
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
            streetText.setText(address.get(0).getLocality()+" " +address.get(0).getThoroughfare());

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

    public void Finish(View view){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result_offer);
        mStorage=FirebaseStorage.getInstance().getReference();
        layoutForm=findViewById(R.id.layoutForm);
        rv=findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);
        fileNameList=new ArrayList<>();
        fileDoneList=new ArrayList<>();
        uploadListAdapter=new UploadListAdapter(fileNameList,fileDoneList,uriList);
        rv.setAdapter(uploadListAdapter);
        spinnerCity=findViewById(R.id.citySpinner);
        spinnerType=findViewById(R.id.spinnerType);
        priceText=findViewById(R.id.priceText);
        descText=findViewById(R.id.descText);
        streetText=findViewById(R.id.StreetText);
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
    private void setTypeSpinner(){
        String []tabsArray=new String[]{"الكل","شقةللإيجار","فيلا للبيع","إرض للبيع","فيلا للإيجار","دور للإيجار","شقة للبيع","عمارة للبيع","بيت للبيع","استراحه للإيجار","بيت للإيجار","محل للإيجار","مزرعه للبيع","عماره للإيجار"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,tabsArray);
        spinnerType.setAdapter(adapter);
    }
    private void setCity() {

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,CityArray);
        spinnerCity.setAdapter(adapter);
    }

    public void AddPicture(View view) {
        getMedia();
    }
    private void getMedia(){
        choseImage=true;
        Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"اختار صور العقار"),PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_VIDEO_REQUEST&&resultCode==RESULT_OK&&choseImage){
            choseImage=false;
            if(data.getClipData()!=null){
                int totalItemSelected=data.getClipData().getItemCount();
                for(int i=0;i<totalItemSelected;i++){
                    Uri fileUri=data.getClipData().getItemAt(i).getUri();
                    setDataToRV(fileUri);
                }
            }else if(data.getData()!=null){
                Uri fileUri=data.getData();
                setDataToRV(fileUri);
            }
        }
    }
    List<Uri>uriList=new ArrayList<>();
    private void setDataToRV(Uri fileUri){
        File file=new File(fileUri.getPath());
        String fileName=file.getName();
        fileNameList.add(fileName);
        fileDoneList.add("uploading");
        uploadListAdapter.notifyDataSetChanged();
        uriList.add(fileUri);
    }
    List<String>downloadUrl;
    public void uploadToFirebase(View view) {
        if (fileNameList.size() == 0)
            Toast.makeText(this, "من فضلك ارفع صور لهذا العقار", Toast.LENGTH_SHORT).show();
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("تحميل");
            progressDialog.show();
            downloadUrl = new ArrayList<>();
            final OfferResult offerResult = new OfferResult();
            offerResult.setDescription(descText.getText().toString());
            offerResult.setLituide(Shared.lituide);
            offerResult.setLongtuide(Shared.longtuide);
            offerResult.setSpinnerType(spinnerType.getSelectedItem().toString());
            offerResult.setPrice(priceText.getText().toString());
            offerResult.setCity(spinnerCity.getSelectedItem().toString());
            offerResult.setStreet(streetText.getText().toString());
            //   offerResult.(Shared.user.getPhoneNumber());
            offerResult.setuID(FirebaseAuth.getInstance().getUid());
            offerResult.setToID(Shared.toID);
            for (int i = 0; i < fileNameList.size(); i++) {
                if (!Shared.random) {
                    StorageReference fileUpload = mStorage.child("OfferResult").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(Shared.offerID).child(fileNameList.get(i));
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
                                        Shared.random=false;

                                    }
                                });
                            }
                        }
                    });
                }
            }

        }
    }
}
