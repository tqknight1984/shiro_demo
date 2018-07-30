package com.sojson.trade.controller;


import com.sojson.common.controller.BaseController;
import com.sojson.common.model.UPermission;
import com.sojson.common.utils.DateUtil;
import com.sojson.common.utils.LoggerUtils;
import com.sojson.core.mybatis.page.Pagination;
import com.sojson.trade.service.TradeService;
import net.sf.json.JSONObject;
import okcoin.rest.StockClient_base;
import okcoin.rest.StockClient_tq;
import okcoin.rest.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Scope(value="prototype")
@RequestMapping("trade")
public class TradeController extends BaseController {
	
	@Autowired
	TradeService tradeService;
	/**
	 * 权限列表
	 * @param findContent	查询内容
	 * @param pageNo		页码
	 * @param modelMap		参数回显
	 * @return
	 */
	@RequestMapping(value="index")
	public ModelAndView index(String findContent,ModelMap modelMap,Integer pageNo){
		modelMap.put("findContent", findContent);
		System.out.println("----modelMap------->"+modelMap.toString());
		Pagination<UPermission> permissions = tradeService.findPage(modelMap,pageNo,pageSize);
		System.out.println("----tradeService.findPage------->"+permissions.getList().size());
		return new ModelAndView("trade/index","page",permissions);
	}


	/**
	 * 当前行情
	 * @param symbol
	 * @return
	 */
	@RequestMapping(value="ticker",method={RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public Map<String,Object> ticker(String symbol){
		System.out.println("----ticker--2222----->"+symbol);
		try {
			String res = StockClient_tq.getInstance().ticker();
			JSONObject res_json = JSONObject.fromObject(res);
			long date = Long.valueOf(res_json.get("date").toString());
			String curr_tm = DateUtil.getTimeString(date * 1000L);
			res_json.put("curr_tm", curr_tm);
			resultMap.put("status", 200);
			resultMap.put("message", res_json);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "添加失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "行情报错。source[%s]", symbol);
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
	public Map<String,Object> trd_post(String trd_symbol, String trd_type,  String trd_price, String trd_amount, String trd_accounts){
		System.out.println("----trade----->"+trd_accounts);
		try {

			List res_ls = new ArrayList();

			String[] accounts = trd_accounts.split(",");

			for (int i = 0; i < accounts.length; i++) {
				String account = accounts[1];
				if(StringUtil.isEmpty(account)){
					continue;
				}

				StockClient_base client = null;
				if("tian".equals(account)){
					client = StockClient_tq.getInstance();
				}
				else{
					continue;
				}

				String res = client.trade(trd_symbol, trd_type, trd_price, trd_amount);
				JSONObject res_json = JSONObject.fromObject(res);
				if(res_json.containsKey("date")){
					long date = Long.valueOf(res_json.get("date").toString());
					String curr_tm = DateUtil.getTimeString(date * 1000L);
					res_json.put("curr_tm", curr_tm);
				}
				res_json.put("account",client.getName());
				res_ls.add(res_json);
			}

			resultMap.put("status", 200);
			resultMap.put("message", res_ls);
		} catch (Exception e) {
			resultMap.put("status", 500);
			resultMap.put("message", "添加失败，请刷新后再试！");
			LoggerUtils.fmtError(getClass(), e, "交易报错。source[%s]", trd_symbol);
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
