package com.bus.services.model;

import com.bus.services.model.base.BaseMongoPersistent;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = Passenger.MONGO_COLLECTION)
public class Passenger extends BaseMongoPersistent<Passenger> {
    public final static String MONGO_COLLECTION = "Passengers";
    @Indexed
    private String userName;
    private String wxId;
    private String phone;
}
