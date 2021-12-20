package com.sourcey.materiallogindemo.repo;

import com.sourcey.materiallogindemo.citesServices.RetrofitInterface;
import com.sourcey.materiallogindemo.model.Regions.Countries.Countries;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Observable;

public class Repository {
    RetrofitInterface iservices;
    @Inject
    public Repository(RetrofitInterface iservices){
        this.iservices=iservices;
    }


    public Observable<Countries> getCountriesList() {
        return  iservices.getCountries();
    }
}
