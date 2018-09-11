package okcoin.rest;

import java.util.concurrent.Future;

public class OkClientFactory {

    public static StockClient_base getStockClient(String name){
        if(StringUtil.isEmpty(name))
            return null;

        if("tian".equals(name)){
            return StockClient_tq.getInstance();
        }else if("guoguo".equals(name)){
            return StockClient_guoguo.getInstance();
        }else if("xianyang".equals(name)){
            return StockClient_xianyang.getInstance();
        }
        else{
           return null;
        }
    }


    public static FutureClient_base getFutureClient(String name){
        if(StringUtil.isEmpty(name))
            return null;

        if("tian".equals(name)){
            return FutureClient_tq.getInstance();
        }
        else if("guoguo".equals(name)){
            return FutureClient_guoguo.getInstance();
        }else if("xianyang".equals(name)){
            return FutureClient_xianyang.getInstance();
        }
        else{
            return null;
        }
    }

}
