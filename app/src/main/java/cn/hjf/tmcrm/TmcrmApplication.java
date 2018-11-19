package cn.hjf.tmcrm;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;

public class TmcrmApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化参数依次为 this, AppId, AppKey
		AVOSCloud.initialize(this, "i2fnmQsM4uo5mSU5jC58aEGm-gzGzoHsz", "6puEgrswMeofsGH6oWkUDNyL");

		// 放在 SDK 初始化语句 AVOSCloud.initialize() 后面，只需要调用一次即可
		AVOSCloud.setDebugLogEnabled(true);
	}
}
