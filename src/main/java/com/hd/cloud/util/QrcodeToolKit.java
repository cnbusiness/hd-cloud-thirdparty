package com.hd.cloud.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: QrcodeToolKit
 * @Description: 二维码
 * @author ShengHao shenghaohao@hadoop-tech.com
 * @Company hadoop-tech
 * @date 2018年1月4日 上午10:18:31
 *
 */
@Slf4j
public class QrcodeToolKit {

	// 图片宽度的一般
	private static final int IMAGE_WIDTH = 200;
	private static final int IMAGE_HEIGHT = 200;
	private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
	private static final int FRAME_WIDTH = 2;

	// 二维码写码器
	private static MultiFormatWriter mutiWriter = new MultiFormatWriter();

	/**
	 * 
	 * @Title: imageQrcodeKent
	 * @param:
	 * @Description: 图像二维码生成
	 * @return String
	 */
	public static String imageQrcodeKent(String content, int width, int height, String srcImagePath, String savePath,
			String basePath, String filename, String url) throws Exception {
		String lastsavePath = basePath + File.separatorChar + savePath + File.separatorChar + filename;
		String resultPath = "http://" + url + File.separatorChar + savePath + File.separatorChar + filename;
		File fileDir = new File(lastsavePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		// ImageIO.write 参数 1、BufferedImage 2、输出的格式 3、输出的文件
		ImageIO.write(genBarcode(content, width, height, srcImagePath), "jpg", new File(lastsavePath));
		return resultPath;
	}

	@SuppressWarnings("deprecation")
	public static String textQrcodeKent(String content, int width, int height, String savePath, String basePath,
			String filename, String url) throws Exception {
		String lastsavePath = basePath + File.separatorChar + savePath + File.separatorChar + filename;
		String resultPath = "http://" + url + File.separatorChar + savePath + File.separatorChar + filename;
		File fileDir = new File(lastsavePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		// 二维码的图片格式
		log.debug("开始生成图片,生成图片存放路径:{},图片包含内容:{},", lastsavePath, content);
		String format = "png";
		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hint);
		File outputFile = new File(lastsavePath);

		MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile);

		boolean wantSavePath = true; // 黄芳建想要保存路径，而不是需要url，所以暂时增加这段代码
		if (wantSavePath) {
			return savePath + File.separatorChar + filename;
		} else {
			return resultPath;
		}
	}

	/**
	 * 
	 * @descripte imageQrCode
	 * @param content
	 * @param width
	 * @param height
	 * @param innerImage
	 * @param destImagePath
	 * @return
	 * @throws Exception
	 */
	public static String imageQrCode(String content, int width, int height, String innerImage, String destImagePath)
			throws Exception {
		File fileDir = new File(destImagePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		ImageIO.write(genBarcode(content, width, height, innerImage), "jpg", new File(destImagePath));

		return destImagePath;
	}

	/**
	 * @Title: textQrcode
	 * @param:
	 * @Description: 文本二维码
	 * @return void
	 */
	@SuppressWarnings("deprecation")
	public static String textQrcode(String content, int width, int height, String destImagePath) throws Exception {

		File fileDir = new File(destImagePath);
		if (!fileDir.exists()) {
			fileDir.mkdirs();
		}
		// File outputFile = new File(destImagePath);
		// 二维码的图片格式
		String format = "png";
		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hint);

		MatrixToImageWriter.writeToFile(bitMatrix, format, fileDir);
		return destImagePath;
	}

	/**
	 * 
	 * @Title: genBarcode
	 * @param: content
	 *             二维码显示的文本
	 * @param width
	 *            二维码的宽度
	 * @param height
	 *            二维码的高度
	 * @param srcImagePath
	 *            中间嵌套的图片
	 * @return BufferedImage
	 */
	private static BufferedImage genBarcode(String content, int width, int height, String srcImagePath)
			throws WriterException, IOException {
		// 读取源图像
		BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH, IMAGE_HEIGHT, false);
		int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
		for (int i = 0; i < scaleImage.getWidth(); i++) {
			for (int j = 0; j < scaleImage.getHeight(); j++) {
				srcPixels[i][j] = scaleImage.getRGB(i, j);
			}
		}

		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 生成二维码
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);

		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[width * height];

		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 读取图片
				if (x > halfW - IMAGE_HALF_WIDTH && x < halfW + IMAGE_HALF_WIDTH && y > halfH - IMAGE_HALF_WIDTH
						&& y < halfH + IMAGE_HALF_WIDTH) {
					pixels[y * width + x] = srcPixels[x - halfW + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
				}
				// 在图片四周形成边框
				else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH - IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
					pixels[y * width + x] = 0xfffffff;
				} else {
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? 0xff000000 : 0xfffffff;
				}
			}
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);

		return image;
	}

	/**
	 * 
	 * @Title: scale
	 * @param srcImageFile
	 *            源文件地址
	 * @param height
	 *            目标高度
	 * @param width
	 *            目标宽度
	 * @param hasFiller
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 * @return BufferedImage
	 */
	public static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller)
			throws IOException {
		double ratioX = 1.0; // 缩放比例
		double ratioY = 1.0; // 缩放比例
		// 获取文件格式
		String ext = srcImageFile.substring(srcImageFile.lastIndexOf(".") + 1);
		if (ext.length() <= 0) {
			ext = "jpg";
		}
		BufferedImage srcImage = null;
		Iterator<ImageReader> iterator = ImageIO.getImageReadersByFormatName(ext);
		ImageReader reader = (ImageReader) iterator.next();
		InputStream in = new FileInputStream(srcImageFile);
		ImageInputStream iis = ImageIO.createImageInputStream(in);
		reader.setInput(iis, true);
		ImageReadParam param = reader.getDefaultReadParam();
		// 为了适配不同宽高的图片填满指定区域而不显得变形所做处理
		int srcWidth = reader.getWidth(0);// 原图宽
		int srcHeight = reader.getHeight(0);// 原图高
		boolean isNeedCut = true;// 是否需要裁剪图片
		if ((srcWidth * height) == (srcHeight * width)) {// 原图宽高比等于目标宽高比
			isNeedCut = false;
		}
		Rectangle rect = null;
		if (isNeedCut) {
			int desWidth = 0;
			int desHeight = 0;
			if (srcWidth >= width && srcHeight > height) {
				desWidth = width;
				desHeight = height;
			} else {
				desWidth = srcWidth;
				desHeight = desWidth * height / width;
				if (desHeight > srcHeight) {
					desHeight = srcHeight;
					desWidth = desHeight * width / height;
				}
			}
			if (desWidth <= srcWidth && desHeight <= srcHeight) {
				int startPosX = (srcWidth - desWidth) / 2;
				int startPoxY = (srcHeight - desHeight) / 2;
				rect = new Rectangle(startPosX, startPoxY, desWidth, desHeight);
			}
		}
		if (rect != null) {
			param.setSourceRegion(rect);
			srcImage = reader.read(0, param);
		} else {
			srcImage = reader.read(0);
		}
		if (in != null) {
			in.close();
		}
		if (iis != null) {
			iis.close();
		}
		Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		// 计算比例
		if (srcImage.getHeight() == height && srcImage.getWidth() == width) {
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratioX, ratioY), null);
			destImage = op.filter(srcImage, null);
		} else {
			ratioY = (new Integer(height)).doubleValue() / srcImage.getHeight();
			ratioX = (new Integer(width)).doubleValue() / srcImage.getWidth();
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratioX, ratioY), null);
			destImage = op.filter(srcImage, null);
		}
		if (hasFiller) {// 补白
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if (width == destImage.getWidth(null))
				graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			else
				graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			graphic.dispose();
			destImage = image;
		}
		return (BufferedImage) destImage;
	}
}
