package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import utils.FileUtils;
import utils.XmlUtil;


public class Main {
	private static File dirFile = new File("temp" + File.separator + "reptile");
	private static File imgCsvFile = new File(dirFile, "bcgm_imgs.csv");
	private static File mainCsvFile = new File(dirFile, "bcgm_main.csv");

	public static void main(String[] args) throws Exception {
		
//		AndroidUtils.autoCreateActivity();
		// AndroidUtils.autoCreateAdapter();
//		 JsonUtils.parseJson2Java();

		// TempUtils.autoCreateSizeSet(true);

//		List<CaoMain> cms = BCGMUtils.getMainUrl();
//		OfficeUtils.saveCVS(cms, mainCsvFile);
		
//		List<CaoImg> caoImgs = new ArrayList<CaoImg>();
//		for(int i=1; i<=28; i++) {
//			String url = BCGMUtils.IMG_URL + (i==1 ? "" : "/" + i);
//			caoImgs.addAll(BCGMUtils.getImgData(url));
//			System.out.println(url + " 解析完成，当前总数为 " + caoImgs.size());
//		}
//		OfficeUtils.saveCVS(caoImgs, new File(
//				"temp" + File.separator + "reptile" + File.separator + "bcgm_imgs.csv"));
		
//		try {
//			List<CaoImg> cis = OfficeUtils.readDatasFromCSV(imgCsvFile, CaoImg.class);
//			List<CaoMain> cms = OfficeUtils.readDatasFromCSV(mainCsvFile, CaoMain.class);
//			List<Cao> caos = new ArrayList<Cao>();
//			for(CaoMain cm : cms) {
//				Cao cao = new Cao();
//				cao.setType(cm.getType());
//				cao.setName(cm.getName());
//				cao.setHref(cm.getHref());
//				
//				int index = cis.indexOf(cm);
//				if(index != -1) {
//					CaoImg ci = cis.get(index);
//					cao.setImg(ci.getImg());
//				}
//				
//				caos.add(cao);
//			}
//			OfficeUtils.saveCVS(caos, new File(dirFile, "bcgm.csv"));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
//		final List<Cao> caos = OfficeUtils.readDatasFromCSV(new File(dirFile, "bcgm.csv"), Cao.class);
//		for(int i=0; i<caos.size(); i++) {
//			final Cao cao = caos.get(i);
//			threadPool.execute(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						loadedCount ++;
//						String detail = BCGMUtils.getDetailData(cao.getName());
//						if(detail != null) {
//							cao.setDetail(detail);
//							System.out.println(loadedCount + "/" + caos.size() + " 数据detail获取成功");
//						} else {
//							System.out.println(loadedCount + "/" + caos.size() + " 数据detail获取失败");
//						}
//						
//						if(loadedCount == caos.size()) {
//							OfficeUtils.saveCVS(caos, new File(dirFile, "bcgm_detail.csv"));
//							threadPool.shutdown();
//						}
//					} catch (Exception e) {
//						loadedCount ++;
//						System.out.println(loadedCount + "/" + caos.size() + " 数据detail获取失败");
//						e.printStackTrace();
//						
//						if(loadedCount == caos.size()) {
//							OfficeUtils.saveCVS(caos, new File(dirFile, "bcgm_detail.csv"));
//							threadPool.shutdown();
//						}
//					}
//					
//				}
//			});
//		}
//		OfficeUtils.saveCVS(caos, new File(dirFile, "bcgm_detail.csv"));
		
//		List<Cao> caos = OfficeUtils.readDatasFromCSV(new File(dirFile, "bcgm_detail.csv"), Cao.class);
//		System.out.println(caos);
	}
//	static ExecutorService threadPool = Executors.newFixedThreadPool(10);
//	static int loadedCount = 0;
	
}
