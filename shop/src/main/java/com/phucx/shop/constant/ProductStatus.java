package com.phucx.shop.constant;

public enum ProductStatus {
    Discontinued(true),
    Coninuted(false);

    private boolean status;

    ProductStatus(boolean status){
        this.status=status;
    }

    public boolean getStatus(){
        return this.status;
    }
}
