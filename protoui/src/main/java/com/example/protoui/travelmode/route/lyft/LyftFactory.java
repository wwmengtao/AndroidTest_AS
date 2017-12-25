package com.example.protoui.travelmode.route.lyft;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.example.protoui.ALog;
import com.example.protoui.R;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfoFactory;
import com.example.protoui.travelmode.route.lyft.data.ApiConfig;
import com.example.protoui.travelmode.route.lyft.data.LyftApi;
import com.example.protoui.travelmode.route.lyft.data.LyftApiBuilder;
import com.example.protoui.travelmode.route.lyft.data.objects.Eta;
import com.example.protoui.travelmode.route.lyft.data.objects.EtaEstimateResponse;
import com.example.protoui.travelmode.route.lyft.data.objects.EtaPrice;
import com.example.protoui.travelmode.route.lyft.data.objects.EtaPriceEstimateResponse;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class LyftFactory extends RouteInfoFactory {
    private static final String TAG = "LyftFactory";
    private static LyftFactory mLyftFactory = null;
    public static final String PACKAGE_NAME = "me.lyft.android";
    private LyftApi mLyftApi = null;

    public static LyftFactory getInstance(Context context, int type){
        if(null == mLyftFactory){
            mLyftFactory = new LyftFactory(context, type);
        }
        return mLyftFactory;
    }

    private LyftFactory(Context context, int type) {
        super(context, type);
        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("8IYAIPNJTIhd")
                .setClientToken("y5ZLY1IoY5suOsoazgvmsdwqatuVj862W2sTRr92j8qscfkyfMLBUA+1mp7Fd9KBxGQyCvQyK5b+g0CJtODC6B4eQtMQcTLZ43MU/OH1VtmNqFn/VbQGw/M=")
                .build();
        mLyftApi = new LyftApiBuilder(apiConfig).build();
    }

    @Override
    public void requestEstimateInfo() {
        final Call<EtaEstimateResponse> etaCall = mLyftApi.getEtas(getOrigin().latitude, getOrigin().longitude, "lyft");
        etaCall.enqueue(new Callback<EtaEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
                if(null == response)return;
                Eta eta = getEtaFromResponse(response.body());
                if(null != eta)ALog.Log1("LyftFactory_onResponse_time: "+eta.toString());
                if (eta != null && eta.eta_seconds != null) {
                    mSummary = (eta.eta_seconds / 60) + "min";
                    mLabel = eta.display_name;
                    getPrice(eta.ride_type);
                }
            }

            @Override
            public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
                if(null != mOnDataLoadListener)mOnDataLoadListener.onDataLoadFailed(t);
            }
        });
    }

    private void getPrice(String ride_type){
        Call<EtaPriceEstimateResponse> priceEstimateCall = mLyftApi.getCosts(getOrigin().latitude, getOrigin().longitude,
                ride_type, getDest().latitude, getDest().longitude);
        priceEstimateCall.enqueue(new Callback<EtaPriceEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaPriceEstimateResponse> call, Response<EtaPriceEstimateResponse> response) {
                EtaPrice eta = getEtaFromResponse(response.body());
                if (eta != null) {
                    ALog.Log1("LyftFactory_Price: "+eta.toString()+Thread.currentThread().toString());
                    mPrice = getFinalPrice(eta);
                    mDistance = getFinalDistance(eta.estimated_distance_miles);
                    RouteInfo mRouteInfo = createRouteInfo();
                    if(null != mOnDataLoadListener)mOnDataLoadListener.onDataLoadSuccess(mRouteInfo);
                }
                setReady(true);
            }

            @Override
            public void onFailure(Call<EtaPriceEstimateResponse> call, Throwable t) {
                setReady(true);
                if(null != mOnDataLoadListener)mOnDataLoadListener.onDataLoadFailed(t);
            }
        });
    }

    private String getFinalPrice(EtaPrice eta){
        StringBuilder finalPrice = new StringBuilder();
        String price = null;
        DecimalFormat df = new DecimalFormat("0.00");
        int estimated_cost_cents_min = eta.estimated_cost_cents_min;
        int estimated_cost_cents_max = eta.estimated_cost_cents_min;
        String priceMin = df.format((float)estimated_cost_cents_min/(float)100);
        String priceMax = df.format((float)estimated_cost_cents_max/(float)100);

        if(estimated_cost_cents_min == estimated_cost_cents_max){
            price = priceMin;
        }else{
            price = priceMin + "-" + priceMax;
        }
        if(eta.currency.equals("USD")){
            finalPrice.append("$").append(price);
        }
        return finalPrice.toString();
    }

    private Eta getEtaFromResponse(EtaEstimateResponse etaEstimateResponse) {
        Eta desiredEta = null;
        if (etaEstimateResponse != null && etaEstimateResponse.eta_estimates != null) {
            for (Eta eta : etaEstimateResponse.eta_estimates) {
                if (eta != null && eta.eta_seconds != null) {
                    if (desiredEta == null || eta.eta_seconds > 0 && eta.eta_seconds < desiredEta.eta_seconds) {
                        desiredEta = eta;
                    }
                }
            }
        }
        return desiredEta;
    }

    private EtaPrice getEtaFromResponse(EtaPriceEstimateResponse response) {
        EtaPrice desiredEta = null;
        if (response != null && response.prices != null) {
            ALog.Log1("response.toString: "+response.toString());
            for (EtaPrice eta : response.prices) {
                if (eta != null) {
                    if (desiredEta == null) {
                        desiredEta = eta;
                    }
                }
            }
        }
        return desiredEta;
    }

    @Override
    protected RouteInfo createRouteInfo() {
        if (isPackageInstalled(PACKAGE_NAME)) {
            if (mIntent == null) {
                mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse("lyft://"));
            }
            try {
                mIcon = mContext.getPackageManager().getApplicationIcon(PACKAGE_NAME);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            mIntent = new Intent(Intent.ACTION_VIEW);
            mIntent.setData(Uri.parse("https://www.lyft.com/signup/SDKSIGNUP?clientId=YOUR_CLIENT_ID&sdkName=android_direct"));
            if (mLabel == null) {
                mLabel = "Lyft";
            }
            mIcon = mContext.getDrawable(R.drawable.ce_ic_lyft);
        }
        return new RouteInfo(RouteInfo.InfoType.LYFT, PACKAGE_NAME, mIntent, mLabel, mPrice, mIcon, mSummary, mDistance);
    }

}
