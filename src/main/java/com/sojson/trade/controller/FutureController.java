package com.sojson.trade.controller;


import com.sojson.common.controller.BaseController;
import com.sojson.common.model.UFuture;
import com.sojson.common.utils.DateUtil;
import com.sojson.common.utils.LoggerUtils;
import com.sojson.trade.service.FutureService;
import net.sf.json.JSONObject;
import okcoin.rest.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Scope(value="prototype")
@RequestMapping("future")
public class FutureController extends BaseController {
	
	@Autowired
	FutureService futureService;

	@RequestMapping(value="index")
	public ModelAndView index(String symbol,String site){
		List<UFuture> trade_ls = null;
		try {
//		modelMap.put("findContent", findContent);
//		System.out.println("----modelMap------->"+modelMap.toString());
//		Pagination<UFuture>  trade = futureService.findPage(modelMap,pageNo,pageSize);
			UFuture trade = new UFuture();
			trade.setSite("OKEX");
//			trade.setSymbol("btc_usdt");
			trade.setStatus("OK");
			trade_ls = futureService.selectByField(trade);
			System.out.println("----futureService.findPage------->" + trade_ls.size());
			resultMap.put("trade_ls", trade_ls);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new ModelAndView("future/index","resultMap",resultMap);
	}


	/**
	 * 当前行情
	 * @param symbol
	 * @return
	 */
	@RequestMapping(value="ticker",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> ticker(String symbol){
		try {
			String res = StockClient_tq.getInstance().ticker(symbol);
			System.out.println("行情-->"+res);
			JSONObject res_json = JSONObject.fromObject(res);
			long date = Long.valueOf(res_json.get("date").toString());
			String curr_tm = DateUtil.getTimeString(date * 1000L);
			res_json.put("curr_tm", curr_tm);
			resultMap.put("status", 200);
			resultMap.put("message", res_json);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "OKEX行情API异常，稍后再试！");
			LoggerUtils.fmtError(getClass(), e, "行情报错。source[%s]", symbol);
		}
		return resultMap;
	}

	/**
	 * 取消订单
	 * @param order_id
	 * @return
	 */
	@RequestMapping(value="cancel_order",method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> cancel_order(long trd_id,String trd_account, String symbol,String contractType, String order_id){
		try {
			FutureClient_base client = OkClientFactory.getFutureClient(trd_account);
			if(client == null){
				resultMap.put("status", 500);
				resultMap.put("message", "账户异常："+trd_account);
				return resultMap;
			}

			String res = client.cancel_order(symbol, contractType,order_id);
			System.out.println(order_id + "取消订单-->"+res);//{"result":true,"order_id":"867323062"}
			JSONObject res_json = JSONObject.fromObject(res);
			//修改数据库
			UFuture record = new UFuture();
			record.setId(trd_id);
			if(res_json.has("result") && (boolean)res_json.get("result"))
				record.setStatus("CANCEL");
			if(res_json.has("error_code") && (int)res_json.get("error_code") == 1009)
				record.setStatus("NO ORDER");
			futureService.updateFieldById(record);
			resultMap.put("status", 200);
			resultMap.put("order_id", order_id);

		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "取消订单报错，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "取消订单报错。source[%s]", order_id);
		}
		return resultMap;
	}

	/**
	 * 当前行情
	 * @param trd_symbol
	 * @return
	 */
	@RequestMapping(value="trade",method={RequestMethod.POST})
	@ResponseBody
	public Map<String,Object> trd_post(String trd_symbol, String trd_contract_type, String trd_type,  String trd_price, String trd_amount, String trd_accounts){
		System.out.println("----trade----->"+trd_accounts);
		List res_ls = new ArrayList();
		try {
			String[] accounts = trd_accounts.split(",");
			String errStr="";
			for (int i = 0; i < accounts.length; i++) {
				String account = accounts[i];
				if(StringUtil.isEmpty(account)){
					continue;
				}

				FutureClient_base client = OkClientFactory.getFutureClient(account);
				if(client == null){
					continue;
				}


				String res = client.trade(trd_symbol, trd_contract_type, trd_price, trd_amount, trd_type);
				System.out.println("挂单-->"+res);//{"result":true,"order_id":867353519}
				JSONObject res_json = JSONObject.fromObject(res);

				UFuture record = new UFuture();
				record.setSymbol(trd_symbol);
				record.setType(trd_type);
				record.setAmount(trd_amount);
				record.setPrice(trd_price);
				record.setAccount(client.getName());
				record.setCreate_tm(DateUtil.getCurrentTimeString());
				record.setStatus("OK");
				record.setSite("OKEX");
				//挂单成功
				if(res_json != null && res_json.has("result") && (boolean)res_json.get("result")){
					String order_id = String.valueOf(res_json.get("order_id"));
					record.setOrder_id(order_id);
					long id = futureService.insert(record);
					record.setId(id);
					System.out.println("挂单成功-->"+res);
				}else{
					System.out.println("挂单失败-->"+res);
					errStr += client.getName() +"挂单失败；";
				}
				JSONObject trd_json = JSONObject.fromObject(record);
				res_ls.add(trd_json);
			}
			if(!StringUtil.isEmpty(errStr)){
				resultMap.put("errStr", errStr);
			}
			resultMap.put("status", 200);
			resultMap.put("message", res_ls);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "下单失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "下单失败。res_ls[%s]", res_ls.toString());
		}
		return resultMap;
	}


//	/**
//	 * 删除权限，根据ID，但是删除权限的时候，需要查询是否有赋予给角色，如果有角色在使用，那么就不能删除。
//	 * @param id
//	 * @return
//	 */
//	@RequestMapping(value="deletePermissionById",method=RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> deleteRoleById(String ids){
//		return permissionService.deletePermissionById(ids);
//	}
}
