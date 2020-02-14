package com.bookinggo.bookinggo.representation;

public class Car extends Option {
    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Car(String provider, Option option){
        this.setProvider(provider);
        this.setCar_type(option.getCar_type());
        this.setPrice(option.getPrice());

    }


}
