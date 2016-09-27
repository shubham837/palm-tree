package crossover.models;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.data.solr.core.mapping.Indexed;

import java.util.Date;
import java.util.UUID;

/**
 * Created by shubham.singhal on 26/09/16.
 */
@Table(value = "cr_rental_info")
public class RentalInfo {

    @Indexed
    @PrimaryKeyColumn(name = "id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String id;

    @Indexed
    @Field
    @PrimaryKeyColumn(name = "type", ordinal = 0, type = PrimaryKeyType.CLUSTERED)
    private String type;

    @Indexed
    @Field
    @Column(value = "city")
    private String city;

    @Indexed
    @Field
    @Column(value = "province")
    private String province;

    @Indexed
    @Field
    @Column(value = "country")
    private String country;

    @Indexed
    @Field
    @Column(value = "zip_code")
    private String zipCode;

    @Indexed
    @Field
    @Column(value = "has_air_condition")
    private boolean hasAirCondition;

    @Indexed
    @Field
    @Column(value = "has_garden")
    private boolean hasGarden;

    @Indexed
    @Field
    @Column(value = "has_pool")
    private boolean hasPool;

    @Indexed
    @Field
    @Column(value = "is_close_to_beach")
    private boolean isCloseToBeach;

    @Indexed
    @Field
    @Column(value = "daily_price")
    private double dailyPrice;

    @Indexed
    @Field
    @Column(value = "currency")
    private String currency;

    @Indexed
    @Field
    @Column(value = "rooms_number")
    private double roomsNumber;


    @Column(value = "created_ts")
    private Date createdTs;


    @Column(value = "updated_ts")
    private Date updatedTs;


    @Column(value = "last_modified_by")
    private String lastModifiedBy;

    public Date getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Date createdTs) {
        this.createdTs = createdTs;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(Date updatedTs) {
        this.updatedTs = updatedTs;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isHasAirCondition() {
        return hasAirCondition;
    }

    public void setHasAirCondition(boolean hasAirCondition) {
        this.hasAirCondition = hasAirCondition;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public boolean isHasPool() {
        return hasPool;
    }

    public void setHasPool(boolean hasPool) {
        this.hasPool = hasPool;
    }

    public boolean isCloseToBeach() {
        return isCloseToBeach;
    }

    public void setCloseToBeach(boolean closeToBeach) {
        isCloseToBeach = closeToBeach;
    }

    public double isDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRoomsNumber() {
        return roomsNumber;
    }

    public void setRoomsNumber(double roomsNumber) {
        this.roomsNumber = roomsNumber;
    }
}
