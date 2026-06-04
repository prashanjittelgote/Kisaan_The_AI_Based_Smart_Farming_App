package com.mountreachsolution.kisaan.POPJO;

public class CommunityModel {
    private String id, name, mobileno, email, city, coname, count, charge;

    public CommunityModel(String id, String name, String mobileno,
                          String email, String city,
                          String coname, String count, String charge) {
        this.id = id;
        this.name = name;
        this.mobileno = mobileno;
        this.email = email;
        this.city = city;
        this.coname = coname;
        this.count = count;
        this.charge = charge;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getMobileno() { return mobileno; }
    public String getEmail() { return email; }
    public String getCity() { return city; }
    public String getConame() { return coname; }
    public String getCount() { return count; }
    public String getCharge() { return charge; }
}
