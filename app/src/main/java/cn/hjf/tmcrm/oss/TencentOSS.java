package cn.hjf.tmcrm.oss;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.object.DeleteObjectRequest;
import com.tencent.cos.xml.model.object.GetObjectRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;

public class TencentOSS {

	private static final String BUCKET = "tmcrm-1258098598";
	private static final String APP_ID = "1258098598";
	private static final String REGION = "ap-shanghai";
	private static final String SECRET_ID = "AKID6FQSJIKqLiz5znhyMdjdMzSnxvkOODZo";
	private static final String SECRET_KEY = "D8uU7c1FIAqKIBuRnyzpaAYklcI0c3IR";

	private static final String SCHEMA = "https://";
	private static final String DOMAIN = "https://tmcrm-1258098598.cos.ap-shanghai.myqcloud.com/";

	private Context mContext;

	private CosXmlService mCosXmlService;

	public TencentOSS(Context context) {
		mContext = context;

		//创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
		CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
				.setAppidAndRegion(APP_ID, REGION)
				.setDebuggable(true)
				.builder();

		//初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥。
		QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(SECRET_ID,
				SECRET_KEY, 3000000);
		mCosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
	}

	public void putObject(String filePath, String cosPath, final IPutObjectCallback callback) {
		PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, cosPath, filePath);

		putObjectRequest.setProgressListener(new CosXmlProgressListener() {
			@Override
			public void onProgress(long progress, long max) {
				if (callback != null) {
					callback.onProgress(progress, max);
				}
			}
		});

		// 使用异步回调上传
		mCosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
			@Override
			public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
				if (callback != null) {
					callback.onSuccess(getUrl(cosXmlResult.accessUrl));
				}
			}

			@Override
			public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
				if (callback != null) {
					if (clientException != null) {
						callback.onFail(clientException);
						return;
					}

					if (serviceException != null) {
						callback.onFail(serviceException);
						return;
					}
				}

			}
		});

	}

	public void getObject(String cosPath) {
		String savePath = "savePath";

		GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKET, cosPath, Environment.getExternalStorageDirectory().getAbsolutePath() + "/zzzzzzzdownload");
		getObjectRequest.setProgressListener(new CosXmlProgressListener() {
			@Override
			public void onProgress(long progress, long max) {
				// todo Do something to update progress...
			}
		});

		// 使用异步回调请求
		mCosXmlService.getObjectAsync(getObjectRequest, new CosXmlResultListener() {
			@Override
			public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
				// todo Get Object success
				Log.e("O_O", "success");
			}

			@Override
			public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
				// todo Get Object failed because of CosXmlClientException or CosXmlServiceException...

				Log.e("O_O", "onFail");

				if (clientException != null) {
					clientException.printStackTrace();
				}

				if (serviceException != null) {
					serviceException.printStackTrace();
				}
			}
		});

	}

	public void deleteObject(String url, final IDeleteObjectCallback callback) {
		DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(BUCKET, getCosPath(url));

		// 使用异步回调请求
		mCosXmlService.deleteObjectAsync(deleteObjectRequest, new CosXmlResultListener() {
			@Override
			public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
				if (callback != null) {
					callback.onSuccess();
				}
			}

			@Override
			public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException) {
				if (callback != null) {
					if (clientException != null) {
						callback.onFail(clientException);
						return;
					}

					if (serviceException != null) {
						callback.onFail(serviceException);
						return;
					}
				}
			}
		});
	}

	//-----------------------------------------------------------------------------------------------------------------------
	//-----------------------------------------------------------------------------------------------------------------------

	private String getUrl(String accessUrl) {
		return SCHEMA + accessUrl;
	}

	private String getCosPath(String url) {
		return url.replace(DOMAIN, "");
	}
}
