package com.example.protoui.travelmode.route.uber;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.example.protoui.ALog;
import com.example.protoui.R;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfoFactory;
import com.example.protoui.travelmode.route.uber.data.ApiConfig;
import com.example.protoui.travelmode.route.uber.data.UberApi;
import com.example.protoui.travelmode.route.uber.data.UberApiBuilder;
import com.example.protoui.travelmode.route.uber.data.objects.Eta;
import com.example.protoui.travelmode.route.uber.data.objects.EtaEstimateResponse;
import com.example.protoui.travelmode.route.uber.data.objects.EtaPrice;
import com.example.protoui.travelmode.route.uber.data.objects.EtaPriceEstimateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class UberFactory extends RouteInfoFactory {
    private static final String TAG = "UberFactory ";
    private static UberFactory mUberFactory = null;
    public static final String PACKAGE_NAME = "com.ubercab";
    private UberApi mUberApi = null;

    public static UberFactory getInstance(Context context, int type){
        if(null == mUberFactory){
            mUberFactory = new UberFactory(context, type);
        }
        return mUberFactory;
    }

    private UberFactory(Context context, int type) {
        super(context, type);
        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("TJ30l588UrhA6wBaCYt_PM0rnRjCyQjE")
                .setClientToken("vRjgibMfivStZmJsnL3dAxcxauQ35oq7Idm48G0W")
                .build();

        mUberApi = new UberApiBuilder(apiConfig).build();
    }

    @Override
    public void requestEstimateInfo() {
        Call<EtaEstimateResponse> timeEstimateCall = mUberApi.getEtas(getOrigin().latitude, getOrigin().longitude, null);
        timeEstimateCall.enqueue(new Callback<EtaEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
                Eta eta = getEtaFromResponse(response.body());
                if(null != eta)ALog.Log2(TAG+"time onResponse: "+eta.toString());
                if (eta != null && eta.estimate != null) {
                    mSummary = (eta.estimate / 60) + "min";
                    mLabel = eta.display_name;
                    getPrice(eta.product_id);
                }
            }

            @Override
            public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
                if(null != mOnDataLoadListener)mOnDataLoadListener.onDataLoadFailed(t);
            }
        });
    }

    private void getPrice(final String product_id){
        Call<EtaPriceEstimateResponse> priceEstimateCall = mUberApi.getEtaPrice(getOrigin().latitude, getOrigin().longitude,
                getDest().latitude, getDest().longitude, 1);
        priceEstimateCall.enqueue(new Callback<EtaPriceEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaPriceEstimateResponse> call, Response<EtaPriceEstimateResponse> response) {
                if(null == response)return;
                ALog.Log2(TAG+"price response: "+response.body().toString());
                EtaPrice eta = getEtaFromResponse(response.body(), product_id);
                if (eta != null && eta.estimate != null) {
                    ALog.Log2(TAG+"EtaPrice_estimate: "+eta.estimate);
                    mPrice = eta.estimate;
                    mDistance = getFinalDistance(eta.distance);
                    RouteInfo mRouteInfo = createRouteInfo();
                    if(null != mOnDataLoadListener)mOnDataLoadListener.onDataLoadSuccess(mRouteInfo);
                }
                setReady(true);
            }

            @Override
            public void onFailure(Call<EtaPriceEstimateResponse> call, Throwable t) {
                if(null != mOnDataLoadListener)mOnDataLoadListener.onDataLoadFailed(t);
                setReady(true);
            }
        });
    }

    private Eta getEtaFromResponse(EtaEstimateResponse etaEstimateResponse) {
        Eta desiredEta = null;
        if (etaEstimateResponse != null && etaEstimateResponse.times != null) {
            for (Eta eta : etaEstimateResponse.times) {
                if (eta != null && eta.estimate != null) {
                    if (desiredEta == null || eta.estimate > 0 && eta.estimate < desiredEta.estimate) {
                        desiredEta = eta;
                    }
                }
            }
        }
        return desiredEta;
    }

    private EtaPrice getEtaFromResponse(EtaPriceEstimateResponse response, final String product_id) {
        if (response != null && response.prices != null && response.prices.size() > 0) {
            for (EtaPrice eta : response.prices) {
                if (eta != null && eta.product_id != null) {
                    if (eta.product_id.equals(product_id)) {
                        return eta;
                    }
                }
            }
            return response.prices.get(0);
        }
        return null;
    }

    @Override
    protected RouteInfo createRouteInfo() {
        if (isPackageInstalled(PACKAGE_NAME)) {
            String uri = "uber://?action=setPickup&pickup=my_location&client_id=<CLIENT_ID>";
            if (mIntent == null) {
                mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse(uri));
            }
            try {
                mIcon = mContext.getPackageManager().getApplicationIcon(PACKAGE_NAME);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            String url = "https://m.uber.com/sign-up?client_id=<CLIENT_ID>";
            mIntent = new Intent(Intent.ACTION_VIEW);
            mIntent.setData(Uri.parse(url));
            mLabel = "Uber";
            mIcon = mContext.getDrawable(R.drawable.ce_ic_uber);
        }

        return new RouteInfo(RouteInfo.InfoType.UBER, PACKAGE_NAME, mIntent, mLabel, mPrice, mIcon, mSummary, mDistance);
    }

}
