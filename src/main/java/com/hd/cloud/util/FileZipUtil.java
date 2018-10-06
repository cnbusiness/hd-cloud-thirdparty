package com.hd.cloud.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @ClassName: FileZipUtil
 * @Description: java生成zip文件打包操作代码
 * @author Sheng sheng.haohao@autoflysoft.com
 * @Company dctp
 * @date 2017年8月27日 下午12:24:40
 *
 */
@Slf4j
public class FileZipUtil {
	/**
	 * 将存放在sourceFilePath目录下的源文件，打包成fileName名称的zip文件，并存放到zipFilePath路径下
	 * 
	 * @param sourceFilePath
	 *            :待压缩的文件路径
	 * @param zipFilePath
	 *            :压缩后存放路径
	 * @param fileName
	 *            :压缩后文件的名称
	 * @return
	 */
	public static boolean fileToZip(String sourceFilePath, String zipFilePath, String fileName) {
		boolean flag = false;
		File sourceFile = new File(sourceFilePath);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		log.info("###########zipFilePath:{},sourceFilePath:{}", zipFilePath, sourceFilePath);
		if (sourceFile.exists() == false) {
			log.info("###########待压缩的文件目录:{}不存在.", sourceFilePath);
		} else {
			try {
				File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
				if (zipFile.exists()) {
					log.info("###########{}目录下存在名字为:{}.zip 打包文件.", zipFilePath, fileName);
				} else {
					File[] sourceFiles = sourceFile.listFiles();
					if (null == sourceFiles || sourceFiles.length < 1) {
						log.info("###########待压缩的文件目录：{} 里面不存在文件，无需压缩.", sourceFilePath);
					} else {
						fos = new FileOutputStream(zipFile);
						zos = new ZipOutputStream(new BufferedOutputStream(fos));
						byte[] bufs = new byte[1024 * 10];
						for (int i = 0; i < sourceFiles.length; i++) {
							//判断如果是文件夹 进行再次打包
							if (sourceFiles[i].isDirectory()) {
								String sourcePath=sourceFiles[i].getAbsolutePath();
								File sourceTreeFile = new File(sourcePath);
								File[] sourceFileLists = sourceTreeFile.listFiles();
								for (int j = 0; j < sourceFileLists.length; j++) {
									ZipEntry zipEntry = new ZipEntry(URLDecoder.decode(sourceFileLists[j].getName()));
									zos.putNextEntry(zipEntry);
									// 读取待压缩的文件并写进压缩包里
									fis = new FileInputStream(sourceFileLists[j]);
									bis = new BufferedInputStream(fis, 1024 * 10);
									int read = 0;
									while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
										zos.write(bufs, 0, read);
									}
								}
							} else {
								// 创建ZIP实体，并添加进压缩包
								ZipEntry zipEntry = new ZipEntry(URLDecoder.decode(sourceFiles[i].getName()));
								zos.putNextEntry(zipEntry);
								// 读取待压缩的文件并写进压缩包里
								fis = new FileInputStream(sourceFiles[i]);
								bis = new BufferedInputStream(fis, 1024 * 10);
								int read = 0;
								while ((read = bis.read(bufs, 0, 1024 * 10)) != -1) {
									zos.write(bufs, 0, read);
								}
							}
						}
						flag = true;
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} finally {
				// 关闭流
				try {
					if (null != zos)
						zos.close();
					if (null != fos)
						fos.close();
					if (null != fis)
						fis.close();
					if (null != bis)
						bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * 解压缩(压缩文件中包含多个文件) ZipInputStream类 当我们需要解压缩多个文件的时候，ZipEntry就无法使用了，
	 * 如果想操作更加复杂的压缩文件，我们就必须使用ZipInputStream类 zipContraMultiFile("f:/zippath.zip",
	 * "d:/");
	 */
	public static void zipContraMultiFile(String zippath, String outzippath) {
		System.out.println("zippath：" + zippath + ",outzippath:" + outzippath);
		log.info("zippath：" + zippath + ",outzippath:" + outzippath);
		try {
			File file = new File(zippath);
			File outFile = null;
			ZipFile zipFile = new ZipFile(file);
			log.info("=====ZipFile====");
			ZipInputStream zipInput = new ZipInputStream(new FileInputStream(file));
			log.info("=====zipInput====");
			ZipEntry entry = null;
			InputStream input = null;
			OutputStream output = null;
			while ((entry = zipInput.getNextEntry()) != null) {
				if (entry.getName().contains(".xml")) {// 不解压xml文件 bill add
					continue;
				}
				System.out.println(zippath + "解压缩: " + entry.getName() + "文件");
				outFile = new File(outzippath + File.separator + entry.getName());
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdir();
				}
				if (!outFile.exists()) {
					outFile.createNewFile();
				}
				input = zipFile.getInputStream(entry);
				output = new FileOutputStream(outFile);
				int temp = 0;
				while ((temp = input.read()) != -1) {
					output.write(temp);
				}
				input.close();
				output.close();
			}
			log.info("=====zipContraMultiFile end!====");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		String sourceFilePath="D:\\qrcode\\1";
		String zipFilePath="D:\\zip";
		String fileName="qrcode";
		FileZipUtil.fileToZip(sourceFilePath, zipFilePath, fileName);
	}
}
