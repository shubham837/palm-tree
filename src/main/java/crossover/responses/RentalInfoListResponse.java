package crossover.responses;

import crossover.models.RentalInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shubham.singhal on 26/09/16.
 */
public class RentalInfoListResponse extends ServiceResponse implements Serializable {
    private List<RentalInfo> rentalInfos;

    public List<RentalInfo> getRentalInfos() {
        return rentalInfos;
    }

    public void setRentalInfos(List<RentalInfo> rentalInfos) {
        this.rentalInfos = rentalInfos;
    }
}
