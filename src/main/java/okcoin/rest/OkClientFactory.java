package okcoin.rest;

public class OkClientFactory {

    public static StockClient_base getClient(String name){
        if(StringUtil.isEmpty(name))
            return null;

        if("tian".equals(name)){
            return StockClient_tq.getInstance();
        }else if("guoguo".equals(name)){
            return StockClient_guoguo.getInstance();
        }
        else{
           return null;
        }
    }

}
