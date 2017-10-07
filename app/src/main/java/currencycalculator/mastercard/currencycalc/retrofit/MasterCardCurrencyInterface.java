package currencycalculator.mastercard.currencycalc.retrofit;

import java.util.List;

import currencycalculator.mastercard.currencycalc.pojo.Currency;
import currencycalculator.mastercard.currencycalc.pojo.ExchangeRate;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by budi on 6/10/17.
 */
public interface MasterCardCurrencyInterface {
    //get all currency
    @GET("api/currency")
    Call<List<Currency>> getAllCurrency();

    @GET("/api/exchange")
    Call<ExchangeRate> getExchangeRate(@Query("baseCode") String baseCode,
                                       @Query("targetCode") String targetCode);



}
