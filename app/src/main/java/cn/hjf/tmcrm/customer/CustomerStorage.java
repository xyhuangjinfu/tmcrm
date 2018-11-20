package cn.hjf.tmcrm.customer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import cn.hjf.tmcrm.account.Account;
import cn.hjf.tmcrm.attachment.Attachment;
import cn.hjf.tmcrm.storage.BaseStorage;
import cn.hjf.tmcrm.storage.IStorageCallback;
import com.avos.avoscloud.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerStorage extends BaseStorage {

	private Map<String, String> mUuidToObjectIdMap;

	public CustomerStorage(Context context) {
		super(context);
		mUuidToObjectIdMap = new ArrayMap<>();
	}

	public void saveCustomer(final Customer customer, final IStorageCallback<Customer> callback) {
		final AVObject avObject = transfer(customer);
		avObject.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException e) {
				//记录映射关系
				mUuidToObjectIdMap.put(customer.getUuid(), avObject.getObjectId());

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

	public void queryCustomer(String customerName, final IStorageCallback<Customer> callback) {
		AVQuery<AVObject> avQuery = new AVQuery<>("Customer");
		avQuery.whereEqualTo("name", customerName);
		avQuery.findInBackground(new FindCallback<AVObject>() {
			@Override
			public void done(List<AVObject> list, AVException e) {
				if (callback != null) {
					if (e == null) {
						if (list == null || list.isEmpty()) {
							callback.onSuccess(null);
						} else {
							callback.onSuccess(transfer(list.get(0)));
						}
					} else {
						callback.onFail(e);
					}
				}
			}
		});
	}

	//--------------------------------------------------------------------------------------------------------------
	//--------------------------------------------------------------------------------------------------------------

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

	private Customer transfer(@NonNull AVObject avObject) {
		Customer customer = new Customer();
		customer.setUuid(avObject.getString("uuid"));
		customer.setName(avObject.getString("name"));
		customer.setId(avObject.getString("id"));

		List<String> l = avObject.getList("id_image_urls");
		if (l != null) {
			List<Attachment> attachmentList = new ArrayList<>();
			for (String s : l) {
				Attachment a = new Attachment();
				a.setUrl(s);

			}
			customer.setIdCardImageList(attachmentList);
		}


		return customer;
	}
}
