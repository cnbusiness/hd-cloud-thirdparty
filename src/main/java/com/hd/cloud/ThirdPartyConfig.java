package com.hd.cloud;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import lombok.Data;
/**
 * 
  * @ClassName: ThirdPartyConfig
  * @Description: 第三方配置信息
  * @author ShengHao shenghaohao@hadoop-tech.com
  * @Company hadoop-tech 
  * @date 2018年4月4日 下午3:27:46
  *
 */
@Configuration
public class ThirdPartyConfig {

	@Data
	public static class SmsPropertyConfig {
		private String infoBipDomain;
		private String username;
		private String password; 
	}

	@Component
	@ConfigurationProperties(prefix = "sms.config")
	public static class ThirdPartySmsConfig extends SmsPropertyConfig {
	}

}
