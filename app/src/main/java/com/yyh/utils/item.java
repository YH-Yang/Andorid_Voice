package com.yyh.utils;

public class item {
    private String item_name;
    private int item_image;

    public item(String item_name,int item_image){
     this.item_name=item_name;
     this.item_image=item_image;
    }
    public String getItem_name(){
        return item_name;
    }
    public int getItem_image(){
        return  item_image;
    }
}
