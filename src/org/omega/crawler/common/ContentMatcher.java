package org.omega.crawler.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.omega.crawler.bean.AltCoinBean;


public final class ContentMatcher {

	private static final Pattern PATTERN_ABBR = Pattern.compile("\\[[^\\]]+\\][^\\[]*\\[([^\\]]+)\\]");
	private static final Pattern PATTERN_NAME = Pattern.compile("\\s?(\\w+\\s?coin)");
	
	private static final List<Pattern> PATTERN_TOTALs = new ArrayList<Pattern>();
	private static final List<Pattern> PATTERN_REWARDs = new ArrayList<Pattern>();
	private static final List<Pattern> PATTERN_BTIMEs = new ArrayList<Pattern>();
	private static final List<Pattern> PATTERN_PREMINEs = new ArrayList<Pattern>();
	private static final List<Pattern> PATTERN_ALGOs = new ArrayList<Pattern>();
	private static final List<Pattern> PATTERN_PREAMOUNTs = new ArrayList<Pattern>();
	private static final List<Pattern> PATTERN_LAUNCHs = new ArrayList<Pattern>();
	
	static {
		loadPatterns();
	}
	
	private static void loadPatterns() {
		String path = ContentMatcher.class.getResource("/patterns.properties").getPath();
		try {
			InputStream is = new FileInputStream(path);
			Properties pros = new Properties();
			pros.load(is);
			is.close();
			
			addPatterns(pros.getProperty("alt.total"), PATTERN_TOTALs);
			addPatterns(pros.getProperty("alt.reward"), PATTERN_REWARDs);
			addPatterns(pros.getProperty("alt.btime"), PATTERN_BTIMEs);
			addPatterns(pros.getProperty("alt.premine.percentage"), PATTERN_PREMINEs);
			addPatterns(pros.getProperty("alt.algo"), PATTERN_ALGOs);
			addPatterns(pros.getProperty("alt.premine.amount"), PATTERN_PREAMOUNTs);
			addPatterns(pros.getProperty("alt.launch.date"), PATTERN_LAUNCHs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addPatterns(String patts, List<Pattern> ll) {
		String[] pattArr = patts.split(",");
		for (String patt : pattArr) {
			ll.add(Pattern.compile(patt.trim()));
		}
	}
	
	private String title;
	private List<String> lines = null;
	public ContentMatcher(String title, List<String> lines) {
		this.title = title.toLowerCase();
		this.lines = lines;
	}
	
	public AltCoinBean buildAndMatch() {
		AltCoinBean alt = new AltCoinBean();
		
		alt.setName(buildAltName());
		alt.setAbbrName(buildAltAbbr());
		
		Long mtotal = null; 
		for (String line : lines) {
			mtotal = buildAltTotal(line);
			if (mtotal != null) {
				alt.setTotalAmount(mtotal); 
				break;
			}
		}
		
		Double mreward = null;
		for (String line : lines) {
			mreward = buildAltReward(line);
			if (mreward != null) {
				alt.setBlockReward(mreward);
				break;
			}
		}
		
		Integer mbtime = null; 
		for (String line : lines) {
			mbtime = buildAltBTime(line);
			if (mbtime != null) {
				alt.setBlockTime(mbtime);
				break;
			}
		}
		
		Double mpremine = null;
		for (String line : lines) {
			mpremine = buildAltPremine(line);
			if (mpremine != null) {
				alt.setMinedPercentage(mpremine);
				break;
			}
		}
		
		String malgo = null;
		for (String line : lines) {
			malgo = buildAltAlgo(line);
			if (malgo != null) {
				alt.setAlgo(malgo);
				break;
			}
		}
		
		Long mpreAmount = null; 
		for (String line : lines) {
			mpreAmount = buildAltPreAmount(line);
			if (mpreAmount != null) {
				alt.setPreMined(mpreAmount);
				break;
			}
		}
		
		String mlaunch = null;
		for (String line : lines) {
			mlaunch = buildAltLaunch(line);
			if (mlaunch != null) {
				alt.setLaunchRaw(mlaunch);
				break;
			}
		}
		
		return alt;
	}
	
	private String buildAltAbbr() {
		String abbr = "NA";
		
		Matcher m = PATTERN_ABBR.matcher(title);
		if (m.find()) {
			abbr = m.group(1);
		}
		
		return abbr.toUpperCase();
	}
	
	private String buildAltName() {
		String name = "NA";
		Matcher m = PATTERN_NAME.matcher(title);
		if (m.find()) {
			name = m.group(1);
			StringBuilder nb = new StringBuilder(name.length());
			nb.append(name.substring(0, 1).toUpperCase());
			nb.append(name.substring(1, name.length()-4));
			nb.append("Coin");
			
			name = Utils.replaceAll(nb.toString(), "\\s", "");
			
		}
		
		return name;
	}
	
	private Long buildAltTotal(String line) {
		String total = null;
		String unit = null;
		for (Pattern p : PATTERN_TOTALs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				total = Utils.removeChars(m.group(1));
				if (m.groupCount() >= 3) unit = m.group(3);
				break;
			}
		}
		
		Long ttx = Utils.isNotEmpty(total) ? Long.parseLong(total) : null;
		if (ttx != null && unit != null) {
			if (unit.contains("million")) ttx = ttx * 1000000;
		}
		
		return ttx;
	}
	
	private Double buildAltReward(String line) {
		String reward = null;
		for (Pattern p : PATTERN_REWARDs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				reward = m.group(1);
				reward = reward.trim();
				break;
			}
		}
		
		return Utils.isNotEmpty(reward) ? Double.parseDouble(reward) : null;
	}
	
	private Integer buildAltBTime(String line) {
		String btime = null;
		String unit = null;
		for (Pattern p : PATTERN_BTIMEs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				btime = m.group(1);
				unit = m.group(3);
				btime = btime.trim();
				break;
			}
		}
		int bt = 0;
		if (btime != null) {
			bt = Integer.parseInt(btime);
			if (unit != null && unit.contains("minute")) {
				bt = bt * 60;
			} 
		}
		return bt == 0 ? null : bt;
	}
	
	private Double buildAltPremine(String line) {
		String pre = null;
		for (Pattern p : PATTERN_PREMINEs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				pre = m.group(1).trim();
				break;
			}
		}
		
		return pre == null ? null : Double.parseDouble(pre);
	}
	
	private String buildAltAlgo(String line) {
		String algo = null;
		for (Pattern p : PATTERN_ALGOs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				algo = m.group(1).trim();
				break;
			}
		}
		
		return algo;
	}
	
	private Long buildAltPreAmount(String line) {
		String preAmount = null;
		for (Pattern p : PATTERN_PREAMOUNTs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				preAmount = m.group(1).trim();
				preAmount = Utils.removeChars(preAmount);
				break;
			}
		}
		
		return preAmount == null ? null : Long.parseLong(preAmount);
	}
	
	private String buildAltLaunch(String line) {
		String launch = null;
		for (Pattern p : PATTERN_LAUNCHs) {
			Matcher m = p.matcher(line);
			if (m.find()) {
				launch = m.group(1).trim();
				break;
			}
		}
		
		return launch;
	}
	
	
	public static void main(String[] args) throws Exception {
//		test1("D:/storage/crawler4j/pages/Nautiluscoin.htm");
//		test1("D:/Koalacoin.htm");
//		test1("D:/Antarcticcoin.htm");
//		test1("D:/storage/crawler4j/pages/NA-OT.html");
//		test1("D:/storage/crawler4j/pages/CakeCoin-DS.html");
//		test1("D:/storage/crawler4j/pages/WampumCoin-WAM.html");
//		test1("D:/storage/crawler4j/pages/WinCoin-NA.html");
//		test1("D:/storage/crawler4j/pages/WorldpeaceCoin-ANN.html");
//		test1("D:/storage/crawler4j/pages/CarCoin-X11.html");
//		test1("D:/storage/crawler4j/pages/NA-CIV.html");
		
		File dir = new File("D:/storage/crawler4j/pages");
		
		for (File f : dir.listFiles()) {
			test1(f.getAbsolutePath());
		}
		
	}
	
	private static void test1(String htmlPath) throws Exception {
		
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode page = cleaner.clean(getHtmlPage(htmlPath));
		
		String content = getSubjectContentHtml(page, cleaner);
		String[] lineArr = content.toLowerCase().split("<br />");
		List<String> lines = new ArrayList<String>(lineArr.length);
		for (String l : lineArr) {
			lines.add(cleaner.clean(l).getText().toString());
		}
		
		String title = getTitle(page, cleaner);

		ContentMatcher cm = new ContentMatcher(title, lines);
		AltCoinBean alt = cm.buildAndMatch();
		System.out.println(new File(htmlPath).getName() + " ---- " + alt.toPrintableTxt());
	}
	
	private static String getHtmlPage(String path)  {
		String html = "";
		try {
			RandomAccessFile raf = new RandomAccessFile(path, "r");
			int len = (int) raf.length();
			byte[] bs = new byte[len];
			raf.read(bs);
			raf.close();
			
			html = new String(bs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return html;
	}
	
	static String getTitle(TagNode page, HtmlCleaner cleaner) {
		Object[] ns = null;
		try {// //*[@id="top_subject"] 
			ns = page.evaluateXPath("//title");
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		
		String cont = "";
		if (ns != null && ns.length > 0) {
			TagNode n = (TagNode) ns[0];
			cont = cleaner.getInnerHtml(n);
		}
		
		return cont;
	}
	
	
	static String getSubjectContentHtml(TagNode page, HtmlCleaner cleaner) throws Exception {
		Object[] ns = null;
		try {
			ns = page.evaluateXPath("//body/div[2]/form/table/tbody/tr/td/table/tbody/tr/td/table/tbody/tr/td[2]/div[@class='post']");
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		
		String cont = "";
		if (ns != null && ns.length > 0) {
			TagNode n = (TagNode) ns[0];
			cont = cleaner.getInnerHtml(n);
		}
		
		return cont;
	}
	
}
