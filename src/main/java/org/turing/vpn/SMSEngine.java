package org.turing.vpn;

import java.util.HashMap;
import java.util.Set;
import com.cloopen.rest.sdk.CCPRestSmsSDK;

/*
 * 短信通知引擎
 * */
public class SMSEngine {
	private static SMSEngine mInstance = new SMSEngine();

	public static SMSEngine getInstance() {
		if (null == mInstance)
			mInstance = new SMSEngine();
		return mInstance;
	}

	public void sendCCPRestSDK(String[] param1) {
		HashMap<String, Object> result = null;
		CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
		restAPI.init("app.cloopen.com", "8883");
		// 初始化服务器地址和端口，生产环境配置成app.cloopen.com，端口是8883.
		restAPI.setAccount("aaf98f894e8a784b014e8b909c9a0256", "47987b3b65e44609976f91ff8a5ad6d8");
		// 初始化主账号名称和主账号令牌，登陆云通讯网站后，可在控制首页中看到开发者主账号ACCOUNT SID和主账号令牌AUTH TOKEN。
		//  8a48b5514e8a7522014e8bd5070f0300
		restAPI.setAppId("8aaf07085b3bb22e015b4cfbeb2c0960");
		// 请使用管理控制台中已创建应用的APPID。
		//166225  34385
		result = restAPI.sendTemplateSMS("15817321796,17753316675,18953392315", "166225", param1);
		System.out.println("SDKTestGetSubAccounts result=" + result);
		if ("000000".equals(result.get("statusCode"))) {
			// 正常返回输出data包体信息（map）
			HashMap<String, Object> data = (HashMap<String, Object>) result
					.get("data");
			Set<String> keySet = data.keySet();
			for (String key : keySet) {
				Object object = data.get(key);
				System.out.println(key + " = " + object);
			}
		} else {
			// 异常返回输出错误码和错误信息
			System.out.println("错误码=" + result.get("statusCode") + " 错误信息= "
					+ result.get("statusMsg"));
		}
	}
}
