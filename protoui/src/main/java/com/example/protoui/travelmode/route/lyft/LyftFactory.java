package com.example.protoui.travelmode.route.lyft;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.protoui.R;
import com.example.protoui.travelmode.route.RouteInfo;
import com.example.protoui.travelmode.route.RouteInfoFactory;
import com.example.protoui.travelmode.route.lyft.data.ApiConfig;
import com.example.protoui.travelmode.route.lyft.data.LyftApi;
import com.example.protoui.travelmode.route.lyft.data.LyftApiBuilder;
import com.example.protoui.travelmode.route.lyft.data.objects.Eta;
import com.example.protoui.travelmode.route.lyft.data.objects.EtaEstimateResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangzq2 on 2017/8/21.
 */

public class LyftFactory extends RouteInfoFactory {
    private static final String TAG = "LyftFactory";

    public static final String PACKAGE_NAME = "me.lyft.android";

    public LyftFactory(Context context, int type) {
        super(context, type);
    }

    @Override
    public void requestEstimateInfo() {

        ApiConfig apiConfig = new ApiConfig.Builder()
                .setClientId("8IYAIPNJTIhd")
                .setClientToken("y5ZLY1IoY5suOsoazgvmsdwqatuVj862W2sTRr92j8qscfkyfMLBUA+1mp7Fd9KBxGQyCvQyK5b+g0CJtODC6B4eQtMQcTLZ43MU/OH1VtmNqFn/VbQGw/M=")
                .build();

        LyftApi lyftApi = new LyftApiBuilder(apiConfig).build();

        final Call<EtaEstimateResponse> etaCall = lyftApi.getEtas(getOrigin().latitude, getOrigin().longitude, null);

        etaCall.enqueue(new Callback<EtaEstimateResponse>() {

            @Override
            public void onResponse(Call<EtaEstimateResponse> call, Response<EtaEstimateResponse> response) {
                Eta eta = getEtaFromResponse(response.body());
                if (eta != null && eta.eta_seconds != null) {
                    mSummary = (eta.eta_seconds / 60) + "min";
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
        if (etaEstimateResponse != null && etaEstimateResponse.eta_estimates != null) {
            for (Eta eta : etaEstimateResponse.eta_estimates) {
                if (eta != null && eta.eta_seconds != null) {
                    if (desiredEta == null || eta.eta_seconds < desiredEta.eta_seconds) {
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
            if (mIntent == null) {
                mIntent = new Intent(Intent.ACTION_VIEW);
                mIntent.setData(Uri.parse("lyft://"));
            }
        } else {
            mIntent = new Intent(Intent.ACTION_VIEW);
            mIntent.setData(Uri.parse("https://www.lyft.com/signup/SDKSIGNUP?clientId=YOUR_CLIENT_ID&sdkName=android_direct"));
            if (mLabel == null) {
                mLabel = "Lyft";
            }
            mIcon = mContext.getDrawable(R.drawable.ce_ic_lyft);
        }
        return new RouteInfo(PACKAGE_NAME, mIntent, mLabel, mIcon, mSummary);
    }

}
