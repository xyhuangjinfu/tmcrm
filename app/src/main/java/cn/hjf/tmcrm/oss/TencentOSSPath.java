package cn.hjf.tmcrm.oss;

import android.text.TextUtils;
import cn.hjf.tmcrm.customer.Customer;

public class TencentOSSPath {

	public static String getCosPath(Customer customer) {
		if (TextUtils.isEmpty(customer.getId())) {
			return customer.getName();
		}
		return customer.getName() + "-" + customer.getId();
	}
}
