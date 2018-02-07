package com.example.protoui.swipemenu.data;

import com.example.protoui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengtao1 on 2018/02/07.
 */

public class RVData {
    private String title = null;
    private int pic = -1;

    public RVData(String title, int pic){
        this.title = title;
        this.pic = pic;
    }

    public String getTitle(){
        return this.title;
    }


    public int getPic(){
        return this.pic;
    }
    public static final String DIVIDER_RV = "RecyclerView_D_I_V_I_D_E_R";
    public static final int NO_PIC_ID = -1234;
    private static String[] TITLES={
            "Context Menu", "Moto Sleep", "Moto Key", "Moto Voice", "Moto Display", "Moto Actions", "Moto Place",
            DIVIDER_RV,
            "Moto Beta", "Call into meeting", "Battery performance", "Maximize storage"
    };

    private static int[] PICS={
            R.drawable.ic_place, R.drawable.ic_place, R.drawable.ic_key, R.drawable.ic_voice, R.drawable.ic_display, R.drawable.ic_actions, R.drawable.ic_place,
            NO_PIC_ID,
            NO_PIC_ID, R.drawable.ic_place, R.drawable.ic_place, R.drawable.ic_place
    };

    public static List<RVData> getData(){
        List<RVData> dataList = new ArrayList<>();
        RVData data;
        for(int i = 0; i < TITLES.length; i++){
            data = new RVData(TITLES[i], PICS[i]);
            dataList.add(data);
        }
        return dataList;
    }
}
