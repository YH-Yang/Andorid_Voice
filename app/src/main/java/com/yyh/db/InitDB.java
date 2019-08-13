package com.yyh.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;

public class InitDB {

    /*
        创建和初始化数据库
     */
    static public void createInitDB(Context context){

        /* init db */
        LitePal.initialize(context);
        SQLiteDatabase db = LitePal.getDatabase();

        /*
            创建五邑大学组织
         */
        Organization wyu = new Organization();
        wyu.setOrigan_name("五邑大学");
        wyu.save();

        /*
            创建智能制造学部
         */
        Apartment apart = new Apartment();
        apart.setApartment_code("1134901"); // TODO: 查不到相关代号，随便用院校代号(11349)+01代替
        apart.setApartment_name("智能制造学部");
        boolean success = apart.setApartment_from_organ("五邑大学");
        apart.save();
        System.out.println(success);

        /*
            创建计算机应用技术专业
         */
        Major computer = new Major();
        computer.setMajor_code("081203");
        computer.setMajor_name("计算机应用技术");
        boolean success1 = computer.setMajor_from_apartment("智能制造学部");
        computer.save();
        System.out.println(success1);


        Major computer1 = new Major();
        computer1.setMajor_code("081204");
        computer1.setMajor_name("软件工程");
        boolean success2 = computer1.setMajor_from_apartment("智能制造学部");
        computer1.save();
        System.out.println(success2);

        /*
            创建考生张三信息
         */
        Examinee zhangsan = new Examinee();
        zhangsan.setExaminee_code("105589670110735");
        zhangsan.setExaminee_name("张三");
        zhangsan.setExaminee_major("计算机应用技术");
        zhangsan.setExaminee_year(2019);
        zhangsan.save();

        /*
            创建题目
         */
        Question q1 = new Question();
        q1.setQuestion_id(1);
        q1.setQuestion_year(2019);
        q1.setQuestion_y_id(24);
        q1.setQuestion_content("操作系统可以分为哪五大功能部分");
        q1.setQuestion_major("计算机应用技术");
        q1.save();

        /*
            创建管理员
         */
        User admin = new User();
        admin.setUser_name("admin");
        admin.setUser_name1("admin");
        admin.setUser_pwd("admin");
        admin.setUser_phone("01234567890");
        admin.setUser_apart_code("1134901");
        admin.setUser_organ("五邑大学");
        admin.setUser_apartment("智能制造学部");
        admin.setUser_isadmin(true);
        admin.save();

        /*
        创建管理员
         */
        User user=new User();
        user.setUser_name("3116003642");
        user.setUser_name1("杨毅华");
        user.setUser_pwd("19970930");
        user.setUser_phone("13534846119");
        user.setUser_apart_code("1134901");
        user.setUser_organ("五邑大学");
        user.setUser_apartment("智能制造学部");
        user.setUser_isadmin(true);
        user.setUser_verify_id("yyh");
        user.save();

        /*
        创建笔录类型
         */
        RecordType recordType=new RecordType();
        recordType.setRecord_type_name("研究生复试");
        recordType.save();

        RecordType recordType1=new RecordType();
        recordType1.setRecord_type_name("普通采访");
        recordType1.save();

    }


}
