package cn.hjf.tmcrm;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;

public class TmcrmApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// 初始化参数依次为 this, AppId, AppKey
		AVOSCloud.initialize(this,"i2fnmQsM4uo5mSU5jC58aEGm-gzGzoHsz","6puEgrswMeofsGH6oWkUDNyL");
	}
}
