package core.dynamicrestresources.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 5/15/16.
 */
public class DynamicResourceContainer {

    List<DynamicResourceMetaData> apiList;

    public DynamicResourceContainer(){
        apiList = new ArrayList<>();
    }

    public List<DynamicResourceMetaData> getApiList() {
        return apiList;
    }

    public void setApiList(List<DynamicResourceMetaData> apiList) {
        this.apiList = apiList;
    }
}

