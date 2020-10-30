package ec.redcode.tas.on.android.services;


import ec.redcode.tas.on.android.models.City;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by User on 24/11/2017.
 * servicio generic. reemplazar por servicio real
 */

public class Servicio {

    public Observable<Boolean> pediservicio(final City city) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                try {
                    Thread.sleep((long) (Math.random() * 3000));
                    return !city.isInteresting();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }


}
