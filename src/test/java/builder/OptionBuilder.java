package builder;

import com.bookinggo.bookinggo.representation.Option;

import java.util.ArrayList;
import java.util.List;

public class OptionBuilder {

    private String car_type;
    private Integer price;

    public Option build(){
        Option o = new Option();
        o.setCar_type(car_type);
        o.setPrice(price);
        return o;
    }

    public OptionBuilder withCarType(String car_type){
        this.car_type = car_type;
        return this;
    }

    public OptionBuilder withPrice(Integer price){
        this.price = price;
        return this;
    }


}
