package com.hd.cloud.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Builder;

/**
 * 
 * @ClassName: QrcodeVo
 * @Description: 二维码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年1月4日 上午11:34:30
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrcodeVo {

	// 店铺id
	private long id;

	// 保存路径
	private String savePath;

	// 子路径
	private String path;
}
