package com.example.protoui.travelmode.route.uber;

import android.content.Context;
import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class UberFactory extends RouteInfoFactory {
    private static final String TAG = "UberFactory";

    public static final String PACKAGE_NAME = "com.ubercab";

    public UberFactory(Context context, int type) {
        super(context, type);
    }

    @Override
    public void requestEstimateInfo() {

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("TJ30l588UrhA6wBaCYt_PM0rnRjCyQjE")
                .setClientToken("vRjgibMfivStZmJsnL3dAxcxauQ35oq7Idm48G0W")
                .build();

        UberApi uberApi = new UberApiBuilder(apiConfig).build();

        Call<EtaEstimateResponse> timeEstimateCall = uberApi.getEtas(getOrigin().latitude, getOrigin().longitude, null);

        timeEstimateCall.enqueue(new Callback<EtaEstimateResponse>() {
            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
                Eta eta = getEtaFromResponse(response.body());
                ALog.Log("UberFactory_onResponse: "+eta.toString());
                if (eta != null && eta.estimate != null) {
                    mSummary = (eta.estimate / 60) + "min";
                    mLabel = eta.display_name;
                }
                setReady(true);
            }

            @Override
            public void onFailure(Call<EtaEstimateResponse> call, Throwable t) {
                setReady(true);
            }
        });
    }

    private Eta getEtaFromResponse(EtaEstimateResponse etaEstimateResponse) {
        Eta desiredEta = null;
        if (etaEstimateResponse != null && etaEstimateResponse.times != null) {
            for (Eta eta : etaEstimateResponse.times) {
                if (eta != null && eta.estimate != null) {
                    if (desiredEta == null || eta.estimate < desiredEta.estimate) {
                        desiredEta = eta;
                    }
                }
            }
        }
        return desiredEta;
    }

    @Override
    public RouteInfo createRouteInfo() {
        if (isPackageInstalled(PACKAGE_NAME)) {
            String uri = "uber://?action=setPickup&pickup=my_location&client_id=<CLIENT_ID>";
            if (mIntent == null) {
                mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse(uri));
            }
        } else {
            String url = "https://m.uber.com/sign-up?client_id=<CLIENT_ID>";
            mIntent = new Intent(Intent.ACTION_VIEW);
            mIntent.setData(Uri.parse(url));
            mLabel = "Uber";
            mIcon = mContext.getDrawable(R.drawable.ce_ic_uber);
        }

        return new RouteInfo(PACKAGE_NAME, mIntent, mLabel, mIcon, mSummary);
    }

}
