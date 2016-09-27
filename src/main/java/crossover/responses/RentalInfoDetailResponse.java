package crossover.responses;

import crossover.models.RentalInfo;

import java.io.Serializable;

/**
 * Created by shubham.singhal on 26/09/16.
 */
public class RentalInfoDetailResponse extends ServiceResponse implements Serializable {
    private RentalInfo rentalInfo;

    public RentalInfo getRentalInfo() {
        return rentalInfo;
    }

    public void setRentalInfo(RentalInfo rentalInfo) {
        this.rentalInfo = rentalInfo;
    }
}

