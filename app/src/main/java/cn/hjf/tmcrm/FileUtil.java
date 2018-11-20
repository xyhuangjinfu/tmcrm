package cn.hjf.tmcrm;

import android.text.TextUtils;

import java.io.*;

public final class FileUtil {

	public static byte[] read(InputStream is) {
		if (is == null) {
			return new byte[0];
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];

		try {
			int readCount = 0;

			while (readCount != -1) {
				readCount = is.read(buffer);
				if (readCount != -1) {
					baos.write(buffer, 0, readCount);
				}
			}

			byte[] result = baos.toByteArray();

			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				baos.close();
			} catch (IOException e) {
			}
		}

		return new byte[0];
	}

	public static boolean save(String path, byte[] data) {
		if (TextUtils.isEmpty(path) || data == null) {
			return false;
		}

		File file = new File(path);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			fos.write(data);
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}

		return true;
	}

	public static boolean rename(String oldPath, String newPath) {
		File file = new File(oldPath);
		return file.renameTo(new File(newPath));
	}
}
