package com.sojson.common.model;

import net.sf.json.JSONObject;

import java.io.Serializable;

/**

 *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
 *   `coin` varchar(20) DEFAULT NULL COMMENT '币种',
 *   `txid` varchar(64) DEFAULT NULL COMMENT '平台交易id',
 *   `price` decimal(20,0) DEFAULT NULL COMMENT '邮箱|登录帐号',
 *   `amount` decimal(20,0) DEFAULT NULL COMMENT '数量',
 *   `create_tm` datetime DEFAULT NULL COMMENT '创建时间',
 *   `status` bigint(1) DEFAULT '0' COMMENT '0:挂单，1:成交，2:流单',
 *
 */
public class UOptLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private String create_tm;
	private String msg;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreate_tm() {
		return create_tm;
	}

	public void setCreate_tm(String create_tm) {
		this.create_tm = create_tm;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
}