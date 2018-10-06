package com.hd.cloud.rest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hd.cloud.ThirdPartyServiceApplication.HdFileUploadConfig;
import com.hd.cloud.service.QrCodeService;
import com.hd.cloud.vo.QrcodeVo;
import com.hlb.cloud.bo.BoUtil;

/**
 * 
 * @ClassName: QrcodeResource
 * @Description: 二维码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年1月4日 上午11:32:48
 *
 */
@RestController
@RequestMapping("/qrcode")
public class QrcodeResource {

	@Inject
	private QrCodeService qrCodeService;

	@Inject
	private HdFileUploadConfig hlbFileUploadConfig;

	@Autowired
	HttpServletRequest request;

	@Autowired
	HttpServletResponse response;

	/**
	 * 
	 * @Title: generateQrCode
	 * @param:
	 * @Description: 生成二维码
	 * @return BoUtil
	 */
	@ResponseBody
	@RequestMapping(value = "/nofilter", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public BoUtil generateQrCode(@RequestBody QrcodeVo qrcodeVo) {
		BoUtil boUtil = BoUtil.getDefaultTrueBo();
		String path = qrCodeService.generateTextQrcode(qrcodeVo.getId(), qrcodeVo.getSavePath(), qrcodeVo.getPath());
		boUtil.setData(path);
		return boUtil;
	}

	/**
	 * 
	 * @Title: getDownloadQrcode
	 * @param:
	 * @Description: 批量下载qrcode
	 * @return void
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "/nofilter", method = RequestMethod.GET, produces = "application/json", consumes = "application/*")
	public void getDownloadQrcode(@QueryParam("merchantId") Long merchantId, @QueryParam("companyId") Long companyId)
			throws Exception {
		merchantId = merchantId == null ? 0 : merchantId;
		companyId = companyId == null ? 0 : companyId;
		// 生成zip包
		qrCodeService.reportShopQrcode(merchantId, companyId);
		request.setCharacterEncoding("UTF-8");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		String fileName = "qrcode.zip";
		String downLoadPath = hlbFileUploadConfig.getBasepath() + "/showqrcode/zip/" + merchantId + companyId + "/"
				+ fileName;
		long fileLength = new File(downLoadPath).length();
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Content-disposition",
				"attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		response.setHeader("Content-Length", String.valueOf(fileLength));
		bis = new BufferedInputStream(new FileInputStream(downLoadPath));
		bos = new BufferedOutputStream(response.getOutputStream());
		byte[] buff = new byte[2048];
		int bytesRead;
		while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff, 0, bytesRead);
		}
		bis.close();
		bos.close();
	}
}
