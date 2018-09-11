package okcoin.rest;

import okcoin.rest.future.IFutureRestApi;
import okcoin.rest.future.impl.FutureRestApiV1;
import org.apache.http.HttpException;

import java.io.IOException;

/**
 * 期货 REST API 客户端请求
 * @author zhangchi
 *
 */
public class FutureClient_base {

	protected  String name = "base";
	protected String api_key = "";  //OKCoin申请的apiKey
	protected String secret_key = "";  //OKCoin申请的secretKey
	protected String url_prex = "https://www.okex.com";  //注意：请求URL 国际站https://www.okcoin.com ; 国内站https://www.okcoin.cn
	/**
	 *  get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
	 */
	protected IFutureRestApi futureGetV1 =null;

	/**
	 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
	 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
	 * 发送post请求之前，程序会做自动加密，生成签名。
	 *
	 */
	protected IFutureRestApi futurePostV1 = null;



	public String getName() throws IOException, HttpException {
		return this.name;
	}

	public String ticker(String symbol, String contractType) throws IOException, HttpException {
		String res  = futureGetV1.future_ticker(symbol, contractType);
		return res;
	}


	public String trade(String symbol, String contractType, String price, String amount,String type) throws IOException, HttpException {
		String res  = futurePostV1.future_trade(symbol,contractType, price, amount, type, "0");
		return res;
	}

	public String trades(String symbol, String contractType) throws IOException, HttpException {
		String res  = futurePostV1.future_trades(symbol, contractType);
		return res;
	}

	public String cancel_order(String symbol,String contractType, String order_id) throws IOException, HttpException {
		String res  =  futurePostV1.future_cancel(symbol, contractType,order_id);
		return res;
	}

	//逐仓期货账户信息
	public String future_userinfo_4fix() throws IOException, HttpException {
		String res  =  futurePostV1.future_userinfo_4fix();
		return res;
	}


	//期货用户订单查询
	public String future_order_info(String symbol, String contractType, String orderId, String status, String currentPage, String pageLength) throws HttpException, IOException{
		String res  =  futurePostV1.future_order_info(symbol, contractType,orderId, status, currentPage, pageLength);
		return res;
	}

//	//期货行情信息
//	String res = futureGetV1.future_ticker("btc_usd", "this_week");
//		System.out.println("--->"+res);
//
//		//期货指数信息
//		futureGetV1.future_index("btc_usd");
//
//		//期货交易信息
//		futureGetV1.future_trades("btc_usd", "this_week");
//
//		//期货市场深度
//		futureGetV1.future_depth("btc_usd", "this_week");
//
//		//美元-人民币汇率
//		futureGetV1.exchange_rate();
//
//		//期货下单
//		String tradeResultV1 = futurePostV1.future_trade("btc_usd","this_week", "10.134", "1", "1", "0");
//		JSONObject tradeJSV1 = JSONObject.parseObject(tradeResultV1);
//		String tradeOrderV1 = tradeJSV1.getString("order_id");
//		System.out.println(tradeResultV1);
//
//		//期货用户订单查询
//		futurePostV1.future_order_info("btc_usd", "this_week",tradeOrderV1, "1", "1", "2");
//
//		//取消订单
//		futurePostV1.future_cancel("btc_usd", "this_week",tradeOrderV1);
//
//		//期货账户信息
//		futurePostV1.future_userinfo();
//
//		//逐仓期货账户信息
//		futurePostV1.future_userinfo_4fix();
//
//		//期货用户持仓查询
//		futurePostV1.future_position("btc_usd", "this_week");
//
//		//期货用户逐仓持仓查询
//	    futurePostV1.future_position_4fix("btc_usd", null);



}
