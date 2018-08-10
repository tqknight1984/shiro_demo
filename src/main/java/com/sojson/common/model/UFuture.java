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
public class UFuture implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String symbol;
	private String order_id;
	private String price;
	private String amount;
	private String create_tm;
	private String account;
	private String site;
	private String contract_type;
	private String type;
	private String status;

	public String getContract_type() {
		return contract_type;
	}

	public void setContract_type(String contract_type) {
		this.contract_type = contract_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getCreate_tm() {
		return create_tm;
	}

	public void setCreate_tm(String create_tm) {
		this.create_tm = create_tm;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}




	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
}