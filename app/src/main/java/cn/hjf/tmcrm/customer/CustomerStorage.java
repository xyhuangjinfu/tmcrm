package cn.hjf.tmcrm.customer;

import android.content.Context;
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.storage.BaseStorage;
import cn.hjf.tmcrm.storage.IStorageCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CustomerStorage extends BaseStorage {
	public CustomerStorage(Context context) {
		super(context);
	}

	public void saveCustomer(Customer customer, final IStorageCallback<Customer> callback) {
		AVObject avObject = transfer(customer);
		avObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException e) {
				if (callback != null) {
					if (e == null) {
						callback.onSuccess(null);
					} else {
						callback.onFail(e);
					}
				}
			}
		});
	}

	private AVObject transfer(Customer customer) {
		AVObject avObject = new AVObject("Customer");
		avObject.put("name", customer.getName());
		avObject.put("id", customer.getId());

		if (customer.getIdCardImageList() != null) {
			List<String> imageUrlList = new ArrayList<>();
			for (Attachment attachment : customer.getIdCardImageList()) {
				imageUrlList.add(attachment.getUrl());
			}
			avObject.put("id_image_urls", imageUrlList);
		}

		return avObject;
	}
}
