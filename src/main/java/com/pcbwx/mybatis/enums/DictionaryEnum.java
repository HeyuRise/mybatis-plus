package com.pcbwx.mybatis.enums;
/**
 * 系统配置枚举类
 * @author 孙贺宇
 * @version 1.0
 *
 */
public enum DictionaryEnum {

	PAY_METHOD("pay_method","付款方式"),
	EXPRESS_STATUS("express_status","运单状态");

	private String code;
	private String descr;
	
	private DictionaryEnum(String code, String descr) {
		this.code = code;
		this.descr = descr;
	}

	public String getCode() {
		return code;
	}

	public String getDescr() {
		return descr;
	}

}
