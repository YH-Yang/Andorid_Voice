package com.yyh.db;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;
import java.util.List;

public class RecordSet extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private String Record_id; // 日期+序号组成(2019041501)(主键)

    private String Record_name; // 笔录名称
    @Column(nullable = false)
    private String Record_t_name; // 笔录类型(外键)
    @Column(nullable = false)
    private String Record_date; // 笔录日期(默认为当前日期)
    @Column(nullable = false)
    private String Record_organ; // 笔录组织机构(外键)
    @Column(nullable = false)
    private String Record_apart; // 笔录所属部门
    private String Record_major; // 笔录所属专业
    @Column(nullable = false)
    private String Record_attend_member; // 参加人员(通过表选择人员列成一行)
    private String Record_chair; // 组长
    @Column(nullable = false)
    private String Record_man; // 记录员，缺省为用户

    public String getRecord_id() {
        return Record_id;
    }

    public void setRecord_id(String record_id) {
        Record_id = record_id;
    }

    public String getRecord_name() {
        return Record_name;
    }

    public void setRecord_name(String record_name) {
        Record_name = record_name;
    }

    public String getRecord_t_name() {
        return Record_t_name;
    }

    /*
        (外键)通过查询笔录类型表判断能否设置
        返回true --> 设置成功
        返回false --> 无作为
     */
    public boolean setRecord_t_name(String record_t_name) {
        boolean success = false;
        List<RecordType> recordTypes = LitePal.where("Record_type_name = ?", record_t_name).find(RecordType.class);
        if(recordTypes != null){
            Record_t_name = record_t_name;
            success = true;
        }
        return success;
    }

    public String getRecord_date() {
        return Record_date;
    }

    public void setRecord_date(String record_date) {
        Record_date = record_date;
    }

    public String getRecord_organ() {
        return Record_organ;
    }

    /*
        (外键)通过查询组织机构信息表判断能否设置
        返回true --> 设置成功
        返回false --> 无作为
     */
    public boolean setRecord_organ(String record_organ) {
        boolean success = false;
        List<Organization> organizations = LitePal.where("Organ_name = ?", record_organ).find(Organization.class);
        if(organizations != null){
            Record_organ = record_organ;
            success = true;
        }
        return success;
    }

    public String getRecord_apart() {
        return Record_apart;
    }

    public void setRecord_apart(String record_apart) {
        Record_apart = record_apart;
    }

    public String getRecord_major() {
        return Record_major;
    }

    public void setRecord_major(String record_major) {
        Record_major = record_major;
    }

    public String getRecord_attend_member() {
        return Record_attend_member;
    }

    public void setRecord_attend_member(String record_attend_member) {
        Record_attend_member = record_attend_member;
    }

    public String getRecord_chair() {
        return Record_chair;
    }

    public void setRecord_chair(String record_chair) {
        Record_chair = record_chair;
    }

    public String getRecord_man() {
        return Record_man;
    }

    public void setRecord_man(String record_man) {
        Record_man = record_man;
    }
}
