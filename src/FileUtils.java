import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

	static ArrayList<File> javaAllFiles = new ArrayList<File>();

	static ArrayList<File> xmlAllFiles = new ArrayList<File>();

	static ArrayList<File> pngAllFiles = new ArrayList<File>();

	/**
	 * 替换文字
	 * @param keyString	过滤文字,只替换包含此字符的文件下内容
	 * @param replaceString key-原文字 value-需要替换的文字
	 */
	static void replaceStringOfJava(String keyString,
			HashMap<String, String> replaceString) {
		for (File javaFile : javaAllFiles) {
			if (!javaFile.exists()) {
				continue;
			}
			String fileContent = readToString(javaFile);
			String result = null;

			if (!fileContent.contains(keyString)) {
				continue;
			}

			for (Map.Entry<String, String> entry : replaceString.entrySet()) {
				if (fileContent.contains(entry.getKey())) {
					result = fileContent.replace(entry.getKey(),
							entry.getValue());
				}
			}
			 whiteString2File(result, javaFile);
		}
	}

	/**
	 * 删除无用xml文件
	 */
	static void delNoUseXmlFile() {
		for (File file : xmlAllFiles) {
			boolean used = false;
			for (File javaFile : javaAllFiles) {
				String fileContent = readToString(javaFile);
				if (fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if (used) {
				continue;
			}
			for (File xmlFile : xmlAllFiles) {
				if (!xmlFile.exists()) {
					continue;
				}
				String fileContent = readToString(xmlFile);
				if (fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if (used) {
				continue;
			}

			String name = getName(file);
			boolean delete = file.delete();
			System.out.println(name + " ... delete=" + delete);
		}
	}

	/**
	 * 删除无用java文件
	 */
	static void delNoUseJavaFile() {
		for (File file : javaAllFiles) {
			boolean used = false;
			for (File javaFile : javaAllFiles) {
				if (!javaFile.exists()) {
					continue;
				}
				if (file.equals(javaFile)) {
					continue;
				}
				String fileContent = readToString(javaFile);
				if (fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if (used) {
				continue;
			}
			for (File xmlFile : xmlAllFiles) {
				if (!xmlFile.exists()) {
					continue;
				}
				if (file.equals(xmlFile)) {
					continue;
				}
				String fileContent = readToString(xmlFile);
				if (fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if (used) {
				continue;
			}

			System.out.println(file.getAbsolutePath());

			// String name = getName(file);
			// boolean delete = file.delete();
			// System.out.println(name + " ... delete=" + delete);
		}
	}

	/**
	 * 删除无用png图片
	 */
	static void delNoUsePngFile() {
		for (File file : pngAllFiles) {
			boolean used = false;
			for (File javaFile : javaAllFiles) {
				String fileContent = readToString(javaFile);
				if (fileContent.contains("R.drawable." + getName(file))) {
					used = true;
					break;
				}
			}
			if (used) {
				continue;
			}
			for (File xmlFile : xmlAllFiles) {
				String fileContent = readToString(xmlFile);
				if (fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if (used) {
				continue;
			}

			// pngNoUseFiles.add(pngFile);
			// System.out.println(getName(pngFile));

			boolean delete = file.delete();
			System.out.println(getName(file) + " ... delete=" + delete);
		}
	}

	/**
	 * 获取代码行数 java xml部分
	 * 
	 * @param path
	 */
	static void getCodeLines(String path) {
		int allLines = 0;
		try {
			getAllFiles(new File(path));
			for (File javaFile : javaAllFiles) {
				int lines = getLines(javaFile);
				allLines += lines;
			}
			for (File xmlFile : xmlAllFiles) {
				int lines = getLines(xmlFile);
				allLines += lines;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(allLines);
	}

	static int getLines(File file) throws IOException {
		int lines = 0;
		FileReader fr = new FileReader(file);
		BufferedReader bufferedreader = new BufferedReader(fr);
		while ((bufferedreader.readLine()) != null) {
			lines++;
		}
		fr.close();
		return lines;
	}

	/**
	 * 获取到全部java png xml文件 并保存至对应集合
	 * 
	 * @param dir
	 * @throws Exception
	 */
	static void getAllFiles(File dir) throws Exception {
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			File file = fs[i];
			String absolutePath = file.getAbsolutePath();
			if (fs[i].isDirectory()) {
				try {
					getAllFiles(fs[i]);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (absolutePath.endsWith(".java")
						&& file.getParent().contains("\\src\\")
						&& !javaAllFiles.contains(file)) {
					javaAllFiles.add(file);
				} else if (absolutePath.endsWith(".xml")
						&& file.getParent().contains("\\res\\")
						&& !getName(file).equals("ids")
						&& !getName(file).equals("strings")
						&& !getName(file).equals("stringArrays")
						&& !xmlAllFiles.contains(file)) {
					xmlAllFiles.add(file);
				} else if (absolutePath.endsWith(".png")
						&& !pngAllFiles.contains(file)) {
					pngAllFiles.add(file);
				}
			}
		}
	}
	
	static File getXmlFileByName(String proPath, String filename) {
		File tarFile = null;
		
		try {
			FileUtils.getAllFiles(new File(proPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(File file : FileUtils.xmlAllFiles) {
			String fileName = file.getName();
			if(fileName.equals(filename)) {
				System.out.println("get the file ... " + fileName);
				tarFile = file;
				break;
			}
		}
		
		return tarFile;
	}
	
	static File getJavaFileByName(String proPath, String filename) {
		File tarFile = null;
		
		try {
			FileUtils.getAllFiles(new File(proPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(File file : FileUtils.javaAllFiles) {
			String fileName = file.getName();
			if(fileName.equals(filename)) {
				System.out.println("get the file ... " + fileName);
				tarFile = file;
				break;
			}
		}
		
		return tarFile;
	}

	static String getName(File file) {
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf("."));
		if (name.endsWith(".9")) {
			name = name.substring(0, name.lastIndexOf("."));
		}
		return name;
	}

	static String readToString(File file) {
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, Main.fileCharCode);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support ");
			e.printStackTrace();
			return null;
		}
	}

	public static String getCharSet(File file) {
		String chatSet = null;
		try {
			InputStream in = new java.io.FileInputStream(file);
			byte[] b = new byte[3];
			in.read(b);
			in.close();
			if (b[0] == -17 && b[1] == -69 && b[2] == -65)
				chatSet = "UTF-8";
			else
				chatSet = "GBK";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chatSet;
	}

	static void whiteString2File(String str, File file) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
			writer.write(str);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			writer = null;
		}
	}

}
