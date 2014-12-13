package com.bus.services.model;

import com.bus.services.model.base.BaseMongoPersistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = Passenger.MONGO_COLLECTION)
public class Passenger extends BaseMongoPersistent<Passenger> {
    public final static String MONGO_COLLECTION = "Passengers";
    @Indexed
    private String userName;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        return getId().equals(passenger.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
