package currencycalculator.mastercard.currencycalc.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by budi on 6/10/17.
 */
@Parcel
public class ExchangeRate implements Serializable {

    @SerializedName("baseCode")
    @Expose
    public String baseCode;
    @SerializedName("targetCode")
    @Expose
    public String targetCode;
    @SerializedName("rate")
    @Expose
    public Double rate;

}