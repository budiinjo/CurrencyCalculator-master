package currencycalculator.mastercard.currencycalc.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by budi on 6/10/17.
 */
@Parcel
public class Currency implements Serializable{

    @SerializedName("code")
    @Expose
    public String code;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("flagPath")
    @Expose
    public String flagPath;
    @SerializedName("rate")
    @Expose
    public Double rate;

}