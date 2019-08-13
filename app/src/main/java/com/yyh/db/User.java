package com.yyh.db;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

public class User extends LitePalSupport {

    @Column(unique = true, defaultValue = "unknown", nullable = false)
    private String User_name; // 用户ID（主键）

    @Column(nullable = false)
    private String User_name1;//用户姓名

    @Column(nullable = false)
    private String User_pwd; // 用户密码

    @Column(nullable = false)
    private String User_phone; // 手机号码

    private String User_ident_code; // 验证码
    private String User_email; // 用户邮箱
    @Column(nullable = false)
    private String User_apart_code; // 部门编码（外键）TODO:只记录不显示(没有返回？)

    @Column(nullable = false)
    private String User_organ; // 用户所属组织

    @Column(nullable = false)
    private String User_apartment; // 用户所属部门

    @Column(nullable = false)
    private boolean User_isadmin; // 用户是否为管理员（第一个注册者为管理员）

    @Column(nullable = true)
    private String User_verify_id;//用户用于识别用户的声纹ID

    public String getUser_name1(){
        return  User_name1;
    }

    public String getUser_verify_id(){
        return User_verify_id;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getUser_pwd() {
        return User_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        User_pwd = user_pwd;
    }

    public String getUser_phone() {
        return User_phone;
    }

    public void setUser_phone(String user_phone) {
        User_phone = user_phone;
    }

    public String getUser_ident_code() {
        return User_ident_code;
    }

    public void setUser_ident_code(String user_ident_code) {
        User_ident_code = user_ident_code;
    }

    public String getUser_email() {
        return User_email;
    }

    public void setUser_email(String user_email) {
        User_email = user_email;
    }

    public String getUser_apart_code() {
        return User_apart_code;
    }


    /*
        (外键) 根据外键数据库中是否有一样部门编号判断能否设置
        设置成功返回true
        设置不成功返回false
     */
    public boolean setUser_apart_code(String user_apart_code) {
        boolean success = false;
        List<Apartment> apartments = LitePal.where("Apartment_code = ?", user_apart_code).find(Apartment.class);
        if(apartments != null){
            User_apart_code = user_apart_code;
            success = true;
        }
        return success;


    }

    public String getUser_organ() {
        return User_organ;
    }

    public void setUser_organ(String user_organ) {
        User_organ = user_organ;
    }

    public String getUser_apartment() {
        return User_apartment;
    }

    public void setUser_apartment(String user_apartment) {
        User_apartment = user_apartment;
    }

    public boolean isUser_isadmin() {
        return User_isadmin;
    }

    public void setUser_isadmin(boolean user_isadmin) {
        User_isadmin = user_isadmin;
    }

    public void setUser_verify_id(String user_verify_id){User_verify_id=user_verify_id;}

    public void setUser_name1(String user_name1){User_name1=user_name1;}
}
