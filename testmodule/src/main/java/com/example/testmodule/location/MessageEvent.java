package com.example.testmodule.location;

import android.location.Address;

/**
 * MessageEvent：EventBus所使用的事件
 */
public class MessageEvent {

    private Address address = null;


    public MessageEvent(Address address) {
        this.address = address;
    }

    public Address getAddress(){
        return this.address;
    }
}
