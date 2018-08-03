package com.sojson.user.manager;

import java.util.*;

import com.sojson.common.model.UPermission;
import com.sojson.common.model.URole;
import com.sojson.common.model.UUser;
import com.sojson.common.utils.Base64Util;
import com.sojson.common.utils.MathUtil;


import org.apache.commons.codec.binary.Base64;

public class UserManager {
	
	/**
	 * 加工密码，和登录一致。
	 * @param user
	 * @return
	 */
	public static UUser md5Pswd(UUser user){
		//密码为   email + '#' + pswd，然后MD5
		user.setPswd(md5Pswd(user.getEmail(),user.getPswd()));
		return user;
	}
	/**
	 * 字符串返回值
	 * @param email
	 * @param pswd
	 * @return
	 */
	public static String md5Pswd(String email ,String pswd){
		pswd = String.format("%s#%s", email,pswd);
		pswd = MathUtil.getMD5(pswd);
		return pswd;
	}

	public static void main(String[] args) {
//		String  pswd = String.format("%s#%s", "sdfsdf","sdfsdff");
//		pswd = MathUtil.getMD5(pswd);
//		System.out.println(pswd);

		String str = "xxxxx";
		str = Base64Util.jdkBase64Encoder(str);
		System.out.println("加密后的字符串为：" + str);
		str = Base64Util.jdkBase64Decoder(str);
		if (str != null) {
			System.out.println("解密后的字符串：" + str);
		} else {
			System.out.println("解密失败");
		}
	}


	/**
	 * 把查询出来的roles 转换成bootstarp 的 tree数据
	 * @param roles
	 * @return
	 */
	public static List<Map<String,Object>> toTreeData(List<URole> roles){
		List<Map<String,Object>> resultData = new LinkedList<Map<String,Object>>();
		for (URole u : roles) {
			//角色列表
			Map<String,Object> map = new LinkedHashMap<String, Object>();
			map.put("text", u.getName());//名称
			map.put("href", "javascript:void(0)");//链接
			List<UPermission> ps = u.getPermissions();
			map.put("tags",  new Integer[]{ps.size()});//显示子数据条数
			if(null != ps && ps.size() > 0){
				List<Map<String,Object>> list = new LinkedList<Map<String,Object>>();
				//权限列表
				for (UPermission up : ps) {
					Map<String,Object> mapx = new LinkedHashMap<String, Object>();
					mapx.put("text", up.getName());//权限名称
					mapx.put("href", up.getUrl());//权限url
					//mapx.put("tags", "0");//没有下一级
					list.add(mapx);
				}
				map.put("nodes", list);
			}
			resultData.add(map);
		}
		return resultData;
		
	}
	
	
}
