package core.dynamic.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nitina on 5/15/16.
 */
public class DynamicResourceContainer {

    List<Api> apiList;

    public DynamicResourceContainer(){
        apiList = new ArrayList<>();
    }

    public List<Api> getApiList() {
        return apiList;
    }

    public void setApiList(List<Api> apiList) {
        this.apiList = apiList;
    }
}

