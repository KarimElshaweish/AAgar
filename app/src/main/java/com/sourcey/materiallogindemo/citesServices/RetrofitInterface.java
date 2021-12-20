package com.sourcey.materiallogindemo.citesServices;

import com.sourcey.materiallogindemo.model.Regions.Countries.Countries;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface RetrofitInterface {
    @Headers("x-rapidapi-key:67ed3628fcmsh6b648deb1347881p1638d0jsnd45e255ddb2f")
    @GET("countries/")
    Observable<Countries> getCountries();
}
