package com.sourcey.materiallogindemo;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.sourcey.materiallogindemo.Model.Offer;
import com.sourcey.materiallogindemo.Model.OfferResult;
import com.sourcey.materiallogindemo.Model.User;

import java.util.ArrayList;
import java.util.List;

public class Shared {
    public static  Offer MyOffer;
    public static double longtuide=-1;public static double lituide=-1;
    public static User user;
   public static String[]Array=new String[]{"وسيط","عميل"};
   public  static List<OfferResult>listReult;
    public  static  String []CityArray=new String[]{
            "الرياض","مكة","المدينة المنورة","بريدة"
            ,"تبوك","الدمام","الاحساء","القطيف","خميس","مشيط","الطائف","نجران","حفر","الباطن","الجبيل","ضباء",
            "الخرج","الثقبة","ينبع","البحر","الخبر","عرعر","الحوية","عنيزة","سكاكا","جيزان",
            "القريات","الظهران","الباحة","الزلفي","تاروت","شروره","صبياء","الحوطة","الأفلاج","بحره"
    };
    public static Polygon polygon;
    public static String sent_id;
    public static List<LatLng>latLngList=new ArrayList<>();
    public static boolean getPolygon=false;
    public static Offer offer;

    public  static String toID;

    public  static boolean customer=false;

    public static String offerID;

    public static List<String>keyList;
    public static boolean owner=false;


    public static boolean addOffer=false;
    public static boolean random=false;


    public static Offer upload;

public static Offer AddToMap;
public static boolean fristTime=false;
    public static OfferResult offerKnow;
    public static boolean addLocation;
    public static boolean addOfferNewRandom=false;
    public static boolean notCurrent=true;

    public static  void reset(){
        customer=false;
    }
    public static boolean  AddRandomButton=false;
}
