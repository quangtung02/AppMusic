package app.alone.com.appmusic.network.api;

import java.util.HashMap;
import java.util.Map;

import app.alone.com.appmusic.model.CategoryTvObj;
import app.alone.com.appmusic.model.ChanelObj;
import app.alone.com.appmusic.network.TvApi;

/**
 * Created by ducthuan on 1/5/16.
 */
public class GetListCategoryTv extends TvApi {

    private static GetListCategoryTv instant;

    /*TODO
        api get category list tv
    */
    private static final String METHOD_NAME_CATEGORY = "getlistcategorytv";
    private static final String METHOD_GET_CHANEL_WITH_CATE ="getlistchanneltv";

    public static GetListCategoryTv getInstant() {
        if (instant == null) {
            synchronized (GetListCategoryTv.class) {
                instant = new GetListCategoryTv();
            }
        }
        return instant;
    }

    public String getListCategoryTv(ListObjectRequestListener<CategoryTvObj> notificationRequestListener) {
        String methodName = METHOD_NAME_CATEGORY;
        //MagicBoxApp.getInstance().cancelRequest(methodName);
        return getListDataObject(methodName, getParamsCategory(),
                notificationRequestListener, CategoryTvObj.class);
    }

    private Map<String, String> getParamsCategory() {
        Map<String, String> params = new HashMap<>();
        params.put("meps", "listcat");
        return params;
    }

    public String getListChanelWithCategory(int id_category, ListObjectRequestListener<ChanelObj> notificationRequestListener){
        String methodName = METHOD_GET_CHANEL_WITH_CATE;
        //MagicBoxApp.getInstance().cancelRequest(methodName);
        return getListDataObject(methodName, getParamsChanel(id_category),
                notificationRequestListener, ChanelObj.class);
    }

    private Map<String, String> getParamsChanel(int id_category) {
        Map<String, String> params = new HashMap<>();
        params.put("meps", "listchannel");
        params.put("catid", String.valueOf(id_category));
        return params;
    }
}