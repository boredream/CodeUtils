package test;

import utils.AndroidUtils;


public class Main {

	public static void main(String[] args) throws Exception {
		
		String projectPath = "C:\\Users\\moyun\\Documents\\GitHub\\AndroidEspressoTest";
		String activityPath = "\\app\\src\\main\\java\\com\\boredream\\androidespressotest\\MainActivity.java";
		String content4EspressoTest = AndroidUtils.createActivityContent4EspressoTest(projectPath, activityPath);
		System.out.println(content4EspressoTest);
	}
	
}
