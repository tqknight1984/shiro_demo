package com.sojson.trade.controller;

import com.sojson.common.controller.BaseController;
import com.sojson.common.model.UPermission;
import com.sojson.common.utils.LoggerUtils;
import com.sojson.core.mybatis.page.Pagination;
import com.sojson.trade.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
		System.out.printf("----modelMap------->"+modelMap.toString());
		Pagination<UPermission> permissions = tradeService.findPage(modelMap,pageNo,pageSize);
		System.out.printf("----tradeService.findPage------->"+permissions.getList().size());
		return new ModelAndView("trade/index","page",permissions);
	}


//	/**
//	 * 权限添加
//	 * @param role
//	 * @return
//	 */
//	@RequestMapping(value="addPermission",method=RequestMethod.POST)
//	@ResponseBody
//	public Map<String,Object> addPermission(UPermission psermission){
//		try {
//			UPermission entity = permissionService.insertSelective(psermission);
//			resultMap.put("status", 200);
//			resultMap.put("entity", entity);
//		} catch (Exception e) {
//			resultMap.put("status", 500);
//			resultMap.put("message", "添加失败，请刷新后再试！");
//			LoggerUtils.fmtError(getClass(), e, "添加权限报错。source[%s]", psermission.toString());
//		}
//		return resultMap;
//	}
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
