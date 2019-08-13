package com.yyh.db;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class VerifyGroup extends LitePalSupport {
    @Column(unique = true, defaultValue = "unknown", nullable = false)
    private String GroupID;
    @Column(nullable = false)
    private String GroupName;

    public String getGroupID(){
        return GroupID;
    }
    public String getGroupName(){
        return GroupName;
    }

    public void setGroupID(String groupID){
        GroupID=groupID;
    }
    public void setGroupName(String groupName){
       GroupName=groupName;
    }

}
