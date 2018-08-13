package com.example.sig.lianjiang.bean;

/**
 * Created by wunaifu on 2018/7/28.
 */
public class User {
    private int id;//自增
    private String phone;//联系方式（登陆账号）
    private String password;
    private String name;
    private int gender;//性别:男（0）女（1）
    private int age;
    private String birthday;//生日
    private String headimage;//头像
    private String address;//地址
    private String typelabel;//用户性格标签
    private String PhoneId;//手机设备（推送用）
    private String registerTime;//注册时间
    private String signature;//个性签名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypelabel() {
        return typelabel;
    }

    public void setTypelabel(String typelabel) {
        this.typelabel = typelabel;
    }

    public String getPhoneId() {
        return PhoneId;
    }

    public void setPhoneId(String PhoneId) {
        this.PhoneId = PhoneId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", birthday='" + birthday + '\'' +
                ", headimage='" + headimage + '\'' +
                ", address='" + address + '\'' +
                ", typelabel='" + typelabel + '\'' +
                ", PhoneId='" + PhoneId + '\'' +
                ", registerTime='" + registerTime + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
