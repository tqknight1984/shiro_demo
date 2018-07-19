package com.sojson.common.model;

import net.sf.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**

 *   `id` bigint(20) NOT NULL AUTO_INCREMENT,
 *   `coin` varchar(20) DEFAULT NULL COMMENT '币种',
 *   `txid` varchar(64) DEFAULT NULL COMMENT '平台交易id',
 *   `price` decimal(20,0) DEFAULT NULL COMMENT '邮箱|登录帐号',
 *   `amount` decimal(20,0) DEFAULT NULL COMMENT '数量',
 *   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
 *   `status` bigint(1) DEFAULT '0' COMMENT '0:挂单，1:成交，2:流单',
 *
 */
public class UTrade implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String coin;
	private String txid;
	private BigDecimal price;
	private BigDecimal amount;
	private Date create_time;
	private int status;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}





	public String toString() {
		return JSONObject.fromObject(this).toString();
	}
}