package okcoin.rest;

import okcoin.rest.stock.IStockRestApi;
import okcoin.rest.stock.impl.StockRestApi;
import org.apache.http.HttpException;

import java.io.IOException;

/**
 * 现货 REST API 客户端请求
 * @author zhangchi
 *
 */
public abstract class StockClient_base {

	protected  String name = "base";

	/**
	 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
	 *
	 */
	protected  IStockRestApi stockGet;

	/**
	 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
	 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
	 * 发送post请求之前，程序会做自动加密，生成签名。
	 *
	 */
	protected  IStockRestApi stockPost;

	public void test() throws IOException, HttpException {
		System.out.printf("----->"+this.ticker("btc_usdt"));
	}

	public String getName() throws IOException, HttpException {
		return this.name;
	}

	//现货行情
	public String ticker(String symbol) throws IOException, HttpException {
		String res  = stockGet.ticker("btc_usdt");
		return res;
	}

	//现货OKCoin历史交易信息
	public String trades() throws IOException, HttpException {
		String res  = stockGet.trades("btc_usd", "20");
		return res;
	}

	//现货用户信息
	public String userinfo() throws IOException, HttpException {
		String res  = stockPost.userinfo();
		return res;
	}

	//现货下单交易
	public String trade(String symbol, String type, String price, String amount) throws IOException, HttpException {
		String res  = stockPost.trade(symbol, type, price, amount);
		return res;
	}

	//现货撤销订单
	public String cancel_order(String symbol, String order_id) throws IOException, HttpException {
		String res  =  stockPost.cancel_order(symbol, order_id);
		return res;
	}

}
