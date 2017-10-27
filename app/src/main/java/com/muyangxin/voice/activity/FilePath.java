package com.muyangxin.voice.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FilePath {

	public static final String DATA_DIRECTORY = Environment
			.getExternalStorageDirectory() + "/";

	/**
	 * 创建不同的目录
	 */
	public static void createDirectory() {
		if (sdCardExists()) {
			File file = new File(DATA_DIRECTORY);
			if (!file.exists()) {
				file.mkdirs();
			}
		} else {
			File file = new File(DATA_DIRECTORY);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}


	/**
	 * 创建不同的目录
	 */
	public static void createDirectory(String childtaskid) {
		if (sdCardExists()) {
			File file = new File(DATA_DIRECTORY + "/" + childtaskid);
			if (!file.exists()) {
				file.mkdirs();
			}
		}else {
			File file = new File(DATA_DIRECTORY+ "/" + childtaskid);
			if (!file.exists()) {
				file.mkdirs();
			}
		}
	}

	/***返回创建文件夹的路径*/
	public static String getFilePath(){
		return  Environment.getExternalStorageDirectory() + "";
	}


	public static List<File> fileList;
	/***返回指定路径的文件*/
	public static List<File> getFileList(String str) {

		 File path = new File(getFilePath()+str);

		try {
			fileList = allFile(path);//此时获得的是默认存储位置的文件
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileList;
	}

	/*** 返回文件夹中所有文件*/
	public static List<File> allFile(File file) {
		List<File> mFileList = new ArrayList<File>();
		File[] fileArray = file.listFiles();

		for (File f : fileArray) {
			if (f.isFile()) {
				mFileList.add(f);
			} else {
				allFile(f);
			}
		}
		return mFileList;
	}




	/**
	* 判断手机是否有SD卡。
	* 
	* @return 有SD卡返回true，没有返回false。
	*/
	public static boolean hasSDCard() {
	return Environment.MEDIA_MOUNTED.equals(Environment
	.getExternalStorageState());
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}
	/**
	 * 清空文件夹
	 * 
	 * @param childtaskid
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteDirs(String childtaskid) {
		if (sdCardExists()) {
			File file = new File(DATA_DIRECTORY + "/" + childtaskid);
			if (file.exists()) {
				try {
					clearCache(file);
				} catch (Exception e) {
				}
			}
		}
		return false;
	}
	
	/**
	 * 清空文件夹
	 * @return 文件删除成功返回true，否则返回false
	 */
	public static boolean deleteDirs() {
		if (sdCardExists()) {
			File file = new File(DATA_DIRECTORY);
			if (file.exists()) {
				try {
					clearCache(file);
				} catch (Exception e) {
				}
			}
		}
		return false;
	}








	/**
	 * 
	 * @Description 清空文件夹
	 * @author DataTang
	 */
	private static  void clearCache(File file) throws Exception {
		File[] files = file.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				clearCache(files[i]);
			else {
				files[i].delete();
			}
		}
		file.delete();
	}

	public static boolean sdCardExists() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	@SuppressLint("SimpleDateFormat")
	public static String millis2CalendarString(long millis, String format) {
		if (millis > 0) {
			SimpleDateFormat yFormat = new SimpleDateFormat(format);
			Calendar yCalendar = Calendar.getInstance();
			yCalendar.setTimeInMillis(millis);

			try {
				return yFormat.format(yCalendar.getTime());
			} catch (Exception e) {

			} finally {
				yCalendar = null;
				yFormat = null;
				format = null;
			}
		}

		return "";
	}

}
