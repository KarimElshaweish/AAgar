package com.sourcey.materiallogindemo.viewmodel;


import android.util.Log;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sourcey.materiallogindemo.model.Regions.Countries.Countries;
import com.sourcey.materiallogindemo.repo.Repository;

import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountriesViewModel extends ViewModel {

    MutableLiveData<Countries> countriesMutableLiveData = new MutableLiveData<Countries>();


    public MutableLiveData<Countries> getCountriesList() { return countriesMutableLiveData;}

   public void getCountries() {
        repository.getCountriesList().subscribeOn(Schedulers.io()).subscribe(
                result ->
                countriesMutableLiveData.postValue(result),
                error -> Log.e("error", error.getMessage()));
    }
    Repository repository;

    @ViewModelInject
    public CountriesViewModel(Repository repository){
        this.repository=repository;
    }
}
