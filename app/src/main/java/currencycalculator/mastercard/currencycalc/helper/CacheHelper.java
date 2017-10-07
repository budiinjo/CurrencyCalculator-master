package currencycalculator.mastercard.currencycalc.helper;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import currencycalculator.mastercard.currencycalc.pojo.Currency;
import currencycalculator.mastercard.currencycalc.pojo.ExchangeRate;

/**
 * Created by budi on 7/10/17.
 */
public class CacheHelper {
    Context _context;
    private final String CACHE_FROM_CURRENCY = "CACHE_FROM_CURRENCY";
    private final String CACHE_TO_CURRENCY = "CACHE_TO_CURRENCY";
    private final String CACHE_CURRENCY_LIST = "CACHE_CURRENCY_LIST";

    private final String CACHE_EXCHANGE_RATE_1 = "CACHE_EXCHANGE_RATE_1";
    private final String CACHE_EXCHANGE_RATE_2 = "CACHE_EXCHANGE_RATE_2";


    public CacheHelper(Context _context){
        this._context = _context;
    }

    public synchronized void cacheFromCurrency(Currency fromCurrency) {
        FileOutputStream fos;
        try {
            fos = _context.openFileOutput(CACHE_FROM_CURRENCY,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(fromCurrency);
            os.close();
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file cacheFromCurrency not found.");
        }
    }

    public Currency loadCacheFromCurrency() {
        Currency currency = null;
        try {
            FileInputStream fis = _context
                    .openFileInput(CACHE_FROM_CURRENCY);
            if(fis!=null){
                ObjectInputStream is = new ObjectInputStream(fis);
                currency = (Currency) is.readObject();
                is.close();
            }
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file cacheFromCurrency not found.");
        }
        return currency;
    }

    public synchronized void cacheToCurrency(Currency toCurrency) {
        FileOutputStream fos;
        try {
            fos = _context.openFileOutput(CACHE_TO_CURRENCY,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(toCurrency);
            os.close();
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file cacheToCurrency not found.");
        }
    }

    public Currency loadCacheToCurrency() {
        Currency currency = null;
        try {
            FileInputStream fis = _context
                    .openFileInput(CACHE_TO_CURRENCY);
            if(fis!=null){
                ObjectInputStream is = new ObjectInputStream(fis);
                currency = (Currency) is.readObject();
                is.close();
            }
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file loadCacheToCurrency not found.");
        }
        return currency;
    }

    public synchronized void cacheCurrencyList(List<Currency> currencyList){
        FileOutputStream fos;
        try {
            fos = _context.openFileOutput(CACHE_CURRENCY_LIST,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(currencyList);
            os.close();
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file cacheCurrencyList not found.");
        }
    }


    public List<Currency> loadCacheCurrencyList() {
        List<Currency> currencyList = null;
        try {
            FileInputStream fis = _context
                    .openFileInput(CACHE_CURRENCY_LIST);
            if(fis!=null){
                ObjectInputStream is = new ObjectInputStream(fis);
                currencyList = (List<Currency>) is.readObject();
                is.close();
            }
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file loadCacheCurrencyList not found.");
        }
        return currencyList;
    }

    public synchronized void cacheExchangeRate1(ExchangeRate exchangeRate){
        FileOutputStream fos;
        try {
            fos = _context.openFileOutput(CACHE_EXCHANGE_RATE_1,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(exchangeRate);
            os.close();
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file cacheExchangeRate1 not found.");
        }
    }


    public ExchangeRate loadCacheExchangeRate1() {
        ExchangeRate exchangeRate = null;
        try {
            FileInputStream fis = _context
                    .openFileInput(CACHE_EXCHANGE_RATE_1);
            if(fis!=null){
                ObjectInputStream is = new ObjectInputStream(fis);
                exchangeRate = (ExchangeRate) is.readObject();
                is.close();
            }
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file loadCacheExchangeRate1 not found.");
        }
        return exchangeRate;
    }


    public synchronized void cacheExchangeRate2(ExchangeRate exchangeRate){
        FileOutputStream fos;
        try {
            fos = _context.openFileOutput(CACHE_EXCHANGE_RATE_2,
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(exchangeRate);
            os.close();
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file cacheExchangeRate2 not found.");
        }
    }


    public ExchangeRate loadCacheExchangeRate2() {
        ExchangeRate exchangeRate = null;
        try {
            FileInputStream fis = _context
                    .openFileInput(CACHE_EXCHANGE_RATE_2);
            if(fis!=null){
                ObjectInputStream is = new ObjectInputStream(fis);
                exchangeRate = (ExchangeRate) is.readObject();
                is.close();
            }
        } catch (Exception e) {
            Log.e("-- Budi Log --", "file loadCacheExchangeRate2 not found.");
        }
        return exchangeRate;
    }
}
