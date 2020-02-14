package builder;

import com.bookinggo.bookinggo.representation.Option;
import com.bookinggo.bookinggo.representation.Response;

import java.util.ArrayList;
import java.util.List;

public class ResponseBuilder {
    private String supplier_id;
    private String pickup;
    private String dropoff;
    private List<Option> options = new ArrayList<>();

    public ResponseBuilder withSupplier_id(String supplier_id){
        this.supplier_id = supplier_id;
        return this;
    }

    public ResponseBuilder withPickup(String pickup){
        this.pickup = pickup;
        return this;
    }


    public ResponseBuilder withDropoff(String dropoff){
        this.dropoff = dropoff;
        return this;
    }

    public ResponseBuilder withOption(Option option){
        this.options.add(option);
        return this;
    }

    public Response build(){
        Response r = new Response();
        r.setDropoff(this.dropoff);
        r.setPickup(this.pickup);
        r.setSupplier_id(this.supplier_id);
        r.setOptions(this.options);
        return r;
    }

}
