package com.fluffypuppy.shop.constant;

public enum OrderStatus {
    ORDER, CANCEL;

    public boolean isOrder(){
        return this == ORDER;
    }

    public boolean isCancelled(){
        return this == CANCEL;
    }

}
