package currencycalculator.mastercard.currencycalc.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import currencycalculator.mastercard.currencycalc.pojo.Currency;

/**
 * Created by budi on 7/10/17.
 */
public class SharedPreferencesHelper {

    SharedPreferences sharedPref;
    Activity activity;


    private final String KEY_FROM_CURRENCY_AMOUNT = "KEY_FROM_CURRENCY_AMOUNT";
    private final String KEY_TO_CURRENCY_AMOUNT = "KEY_TO_CURRENCY_AMOUNT";

    public SharedPreferencesHelper(Activity activity){
        sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public double getFromCurrencyAmount(){
        //SharedPreferences.Editor editor = sharedPref.edit();
        return getDouble(sharedPref,KEY_FROM_CURRENCY_AMOUNT,0);
    }

    public void setFromCurrencyAmount(double amount){
        SharedPreferences.Editor editor = sharedPref.edit();
        putDouble( editor, KEY_FROM_CURRENCY_AMOUNT, amount).commit();

    }

    public double getToCurrencyAmount(){
        return getDouble(sharedPref,KEY_TO_CURRENCY_AMOUNT,0);
    }

    public void setToCurrencyAmount(double amount){
        SharedPreferences.Editor editor = sharedPref.edit();
        putDouble( editor, KEY_TO_CURRENCY_AMOUNT, amount).commit();

    }



    SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }
}
