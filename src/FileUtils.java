import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class FileUtils {
	
	static ArrayList<File> javaAllFiles = new ArrayList<File>();
	
	static ArrayList<File> xmlAllFiles = new ArrayList<File>();
	
	static ArrayList<File> pngAllFiles = new ArrayList<File>();
	
	public static void main(String[] args) throws Exception {
		// 递归显示C盘下所有文件夹及其中文件
//		File root = new File("F:\\work\\WoChiAndroid");
//		getAllFiles(root);
//		 扫面所有图片文件,看是否在xml文件中有任何引用
//		delNoUseJavaFile();
		
		getCodeLines();
	}

	static void delNoUseXmlFile() {
		for(File file : xmlAllFiles) {
			boolean used = false;
			for(File javaFile : javaAllFiles) {
				String fileContent = readToString(javaFile);
				if(fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if(used) {
				continue;
			}
			for(File xmlFile : xmlAllFiles) {
				if(!xmlFile.exists()) {
					continue;
				}
				String fileContent = readToString(xmlFile);
				if(fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if(used) {
				continue;
			}
			
//			System.out.println(file.getAbsolutePath());
			
			String name = getName(file);
			boolean delete = file.delete();
			System.out.println(name + " ... delete=" + delete); 
		}
	}
	
	static void delNoUseJavaFile() {
		for(File file : javaAllFiles) {
			boolean used = false;
			for(File javaFile : javaAllFiles) {
				if(!javaFile.exists()) {
					continue;
				}
				if(file.equals(javaFile)) {
					continue;
				}
				String fileContent = readToString(javaFile);
				if(fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if(used) {
				continue;
			}
			
//			System.out.println(file.getAbsolutePath());
			
			String name = getName(file);
			boolean delete = file.delete();
			System.out.println(name + " ... delete=" + delete); 
		}
	}
	
	static void delNoUsePngFile() {
		for(File file : pngAllFiles) {
			boolean used = false;
			for(File javaFile : javaAllFiles) {
				String fileContent = readToString(javaFile);
				if(fileContent.contains("R.drawable." + getName(file))) {
					used = true;
					break;
				}
			}
			if(used) {
				continue;
			}
			for(File xmlFile : xmlAllFiles) {
				String fileContent = readToString(xmlFile);
				if(fileContent.contains(getName(file))) {
					used = true;
					break;
				}
			}
			if(used) {
				continue;
			}
			
//			pngNoUseFiles.add(pngFile);
//			System.out.println(getName(pngFile));
			
			boolean delete = file.delete();
			System.out.println(getName(file) + " ... delete=" + delete); 
		}
	}
	
	static void getCodeLines() {
		int allLines = 0;
		try {
			getAllFiles(new File("F:\\work\\WoChiAndroid"));
			for(File javaFile : javaAllFiles) {
				int lines = getLines(javaFile);
				allLines += lines;
			}
			for(File xmlFile : xmlAllFiles) {
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
			lines ++;
		}
		fr.close();
		return lines;
	}

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
				if(absolutePath.endsWith(".java") 
						&& file.getParent().contains("\\src\\") 
						&& !javaAllFiles.contains(file)) {
					javaAllFiles.add(file);
				} else if(absolutePath.endsWith(".xml") 
						&& file.getParent().contains("\\res\\") 
						&& !getName(file).equals("ids")
						&& !getName(file).equals("strings")
						&& !getName(file).equals("stringArrays")
						&& !xmlAllFiles.contains(file)) {
					xmlAllFiles.add(file);
				} else if(absolutePath.endsWith(".png")
						&& !pngAllFiles.contains(file)) {
					pngAllFiles.add(file);
				}
			}
		}
	}
	
	static String getName(File file) {
		String name = file.getName();
		name = name.substring(0, name.lastIndexOf("."));
		if(name.endsWith(".9")) {
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
			return new String(filecontent, "utf-8");
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support ");
			e.printStackTrace();
			return null;
		}
	}

}
