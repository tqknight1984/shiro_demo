package okcoin.rest;

import com.sojson.common.utils.DateUtil;
import okcoin.rest.stock.IStockRestApi;
import okcoin.rest.stock.impl.StockRestApi;
import org.apache.http.HttpException;
import org.springframework.stereotype.Repository;

import java.io.IOException;


public class StockClient_tq extends StockClient_base{

	private final  String api_key = "3065aaee-a58d-425d-b4e0-7bdab4175147";  //OKCoin申请的apiKey
	private final  String secret_key = "FF21A6A2FE1DADD84C8B93A9921A7E55";  //OKCoin 申请的secret_key
	private final  String url_prex = "https://www.okcoin.com";  //注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn

	private final static StockClient_tq instance = new StockClient_tq();

	private StockClient_tq() {
			this.name = "tian";
			this.stockGet = new StockRestApi(url_prex);
			this.stockPost = new StockRestApi(url_prex, api_key, secret_key);
	}

	public static StockClient_tq getInstance(){
		return instance;
	}

	public  static void main(String[] args) throws HttpException, IOException{

//		StockClient_tq.getInstance().test();

		System.out.println("--->"+System.currentTimeMillis());
		int a = 1532921788;
		System.out.println("--->"+DateUtil.getTimeString(a * 1000L));

//            //现货市场深度
//		System.out.printf("--->"+stockGet.depth("btc_usd"));

//            //现货OKCoin历史交易信息
//            stockGet.trades("btc_usd", "20");


//	    //现货用户信息
//	    stockPost.userinfo();
//
//	    //现货下单交易
//	    String tradeResult = stockPost.trade("btc_usd", "buy", "50", "0.02");
//	    System.out.println(tradeResult);
//	    JSONObject tradeJSV1 = JSONObject.parseObject(tradeResult);
//	    String tradeOrderV1 = tradeJSV1.getString("order_id");
//
//	    //现货获取用户订单信息
//            stockPost.order_info("btc_usd", tradeOrderV1);
//
//	    //现货撤销订单
//	    stockPost.cancel_order("btc_usd", tradeOrderV1);
//
//	    //现货批量下单
//	    stockPost.batch_trade("btc_usd", "buy", "[{price:50, amount:0.02},{price:50, amount:0.03}]");
//
//	    //批量获取用户订单
//	    stockPost.orders_info("0", "btc_usd", "125420341, 125420342");
//
//	    //获取用户历史订单信息，只返回最近七天的信息
//	    stockPost.order_history("btc_usd", "0", "1", "20");
		
	}

}
