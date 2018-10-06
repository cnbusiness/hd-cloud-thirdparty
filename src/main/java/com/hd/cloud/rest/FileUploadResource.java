package com.hd.cloud.rest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hd.cloud.ThirdPartyServiceApplication.HdFileUploadConfig;
import com.hd.cloud.util.ErrorCode;
import com.hlb.cloud.bo.BoUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: FileUploadController
 * @Description: 上传文件
 * @author Sheng sheng.haohao@autoflysoft.com
 * @Company dctp
 * @date 2017年8月27日 下午12:31:32
 *
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class FileUploadResource {

	@Inject
	private HdFileUploadConfig fileUploadConfig;

	/**
	 * 
	 * @param 上传文件
	 * @return
	 */
	@RequestMapping(value = "/file", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public BoUtil uploadFile(@RequestParam("uploadFile") MultipartFile file, @RequestParam(value = "type") Integer type,
			@RequestParam(value = "usageType") Integer usageType) {
		log.info("###开始上传文件###");
		BoUtil boUtil = BoUtil.getDefaultFalseBo();
		if (file.isEmpty()) {
			boUtil.setMsg("请上传文件");
			boUtil.setCode(ErrorCode.UPLOAD_FILE_EMPTY);
			return boUtil;
		}
		if (type == null || usageType == null) {
			boUtil.setMsg("上传类型或文件用途不能为空");
			boUtil.setCode(ErrorCode.UPLOAD_FILE_ERROR);
			return boUtil;
		}
		Integer[] typeArr = new Integer[] { 1, 2, 3, 4 };
		List<Integer> list = Arrays.asList(typeArr);
		if (!list.contains(type)) {
			boUtil.setMsg("上传类型不正确");
			boUtil.setCode(ErrorCode.TYPE_ERROR);
			return boUtil;
		}
		Integer[] usageTypeArr = new Integer[] { 1, 2,3,4,5 };
		List<Integer> typeList = Arrays.asList(usageTypeArr);
		if (!typeList.contains(usageType)) {
			boUtil.setMsg("文件用途不正确");
			boUtil.setCode(ErrorCode.USE_ERROR);
			return boUtil;
		}
		String folder = "";
		switch (type) {
		case 1:
			folder = "/img";
			break;
		case 2:
			folder = "/sound";
			break;
		case 3:
			folder = "/text";
			break;
		case 4:
			folder = "/video";
			break;
		default:
			break;
		}
		String usageFolder = "";
		switch (usageType) {
		case 1:
			usageFolder = "/avatar/";
			break;
		case 2:
			usageFolder = "/certificate/";
			break;
		case 3:
			usageFolder = "/logo/";
			break;
		case 4:
			usageFolder = "/dynamic/";
			break;
		case 5:
			usageFolder = "/activity/";
			break;
		default:
			break;
		}
		if (StringUtils.isNotBlank(usageFolder)) {
			folder = folder + usageFolder;
		}
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			String originalFileName = file.getOriginalFilename();
			// 获取文件的后缀名
			String suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));
			// 文件上传后的路径
			String filePath = fileUploadConfig.getBasepath() + folder+df.format(System.currentTimeMillis())+"/";
			// 文件新名称
			String fileName = UUID.randomUUID() + suffixName;
	
			File dest = new File(filePath + fileName);
			// 检测是否存在目录
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();
			}
			
			file.transferTo(dest);
			String pathUrl = folder+df.format(System.currentTimeMillis())+"/"+ fileName;
			boUtil = BoUtil.getDefaultTrueBo();
			// 返回路径
			boUtil.setData(pathUrl);
			boUtil.setMsg("上传成功");
			boUtil.setCode(ErrorCode.UPLOAD_SUCCESS);
			log.info("####上传成功:{}",pathUrl);
			return boUtil;
		} catch (IllegalStateException e) {
			boUtil.setMsg("上传失败");
			boUtil.setCode(ErrorCode.UPLOAD_FAIL);
			log.error("####上传失败:{}", e.getMessage());
			return boUtil;
		} catch (Exception e) {
			boUtil.setMsg("上传失败");
			boUtil.setCode(ErrorCode.UPLOAD_FAIL);
			log.error("####上传失败:{}", e.getMessage());
			return boUtil;
		}
	}
}
