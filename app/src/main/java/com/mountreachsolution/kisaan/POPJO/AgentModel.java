package com.mountreachsolution.kisaan.POPJO;

public class AgentModel {
    String id, name, city, phone, email, image;

    public AgentModel(String id, String name, String city,
                      String phone, String email, String image) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.phone = phone;
        this.email = email;
        this.image = image;
    }

    public String getName() { return name; }
    public String getCity() { return city; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getImage() { return image; }
}
