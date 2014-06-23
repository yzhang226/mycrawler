package org.omega.crawler.bitcointalk;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.BitcointalkTopicBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.Utils;
import org.omega.crawler.service.AltCoinService;
import org.omega.crawler.service.AltCoinTopicService;
import org.omega.crawler.spider.BitcointalkCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jsp/bitcointalk")
public class BitcointalkController {
	
	private static final Log log = LogFactory.getLog(BitcointalkController.class);
	
	private static final long ONE_DAY_SECONDS = 24 * 60 * 60;
	private static boolean IS_FIRST_SEEK = true;
	
	@Autowired
	private AltCoinService altCoinService;
	
	@Autowired
	private AltCoinTopicService altCoinTopicService;
	
	@RequestMapping("/initannboard.do")
	@ResponseBody
	public String initAnnBoard(Model model, HttpServletRequest request, 
								@RequestParam String baseSeedUrl) {
		String resp = null;
		boolean success = true;
		try {
			BitcointalkCrawler annCrawler = new BitcointalkCrawler();
			
			if (IS_FIRST_SEEK) {
				Utils.ANN_TOTAL_PAGE_NUMBER = Utils.extractTotalPagesNumber(Utils.fetchPageByUrl(Utils.ANN_PAGE_URL));
			}
			
			List<AltCoinBean> anns = annCrawler.fectchAnnTopics(baseSeedUrl);
			List<Integer> parsedTopicids = altCoinService.findParsedTopicids();
			
			List<AltCoinBean> undbAnns = new ArrayList<>();
			List<AltCoinBean> dbedAnns = new ArrayList<>();
			for (AltCoinBean ann : anns) {
				if (!parsedTopicids.contains(ann.getTopicid())) {
					undbAnns.add(ann);
				} else {
					dbedAnns.add(ann);
				}
			}
			anns.clear();
			
			if (Utils.isNotEmpty(undbAnns)) {// insert ann info
				Timestamp curr = new Timestamp(System.currentTimeMillis());

				Map<Integer, AltCoinBean> topicIdAltCoinMap = annCrawler.fectchAnnTopicsByUrls(undbAnns);

				for (AltCoinBean alt : undbAnns) {
					if (topicIdAltCoinMap.containsKey(alt.getTopicid())) {
						copyProperties(alt, topicIdAltCoinMap.get(alt.getTopicid()));
						
						alt.setCreateTime(curr);
						alt.setIsParsed(Boolean.TRUE);
						altCoinService.saveOrUpdate(alt);
					}
				}
				topicIdAltCoinMap.clear();
			}
			
			if (IS_FIRST_SEEK && Utils.isNotEmpty(dbedAnns)) {
				for (AltCoinBean ann : dbedAnns) {
					AltCoinBean alt = altCoinService.getByTopicId(ann.getTopicid());
					alt.setTitle(ann.getTitle());
					alt.setReplies(ann.getReplies());
					alt.setViews(ann.getViews());
					alt.setLastPostTime(ann.getLastPostTime());
					
					altCoinService.saveOrUpdate(alt);
				}
			}
			
			Thread.sleep(1 * 1000);
		} catch (Throwable e) {
			log.error("Init Ann Board By URL error.", e);
			
			success = false;
			resp = "Init Ann Board By URL error!";
		}
		
		IS_FIRST_SEEK = false;
		
		return Utils.getJsonMessage(success, resp);
	}
	
	private void copyProperties(AltCoinBean src, AltCoinBean matched) {
		src.setPublishDate(matched.getPublishDate());
		
		src.setName(matched.getName());
		src.setAbbrName(matched.getAbbrName());
		
		src.setTotalAmount(matched.getTotalAmount());
		src.setBlockReward(matched.getBlockReward());
		src.setBlockTime(matched.getBlockTime());
		src.setMinedPercentage(matched.getMinedPercentage());
		
		src.setAlgo(matched.getAlgo());
		src.setPreMined(matched.getPreMined());

		
		String launchraw = matched.getLaunchRaw();
		if (Utils.isNotEmpty(launchraw) && launchraw.length() > 119) {
			launchraw = launchraw.substring(0, 119);
		}
		src.setLaunchRaw(launchraw);
	}
	
	@RequestMapping("/updateallcoins.do")
	@ResponseBody
	public String updateAllCoins(Model model, HttpServletRequest request, 
								@RequestParam String baseSeedUrl) {
		String resp = null;
		boolean success = true;
		try {
			BitcointalkCrawler annCrawler = new BitcointalkCrawler();
			
			List<AltCoinBean> anns = annCrawler.fectchAnnTopics(baseSeedUrl);
			
			List<Integer> parsedTopicids = altCoinService.findParsedTopicids();
			
			List<AltCoinBean> unNamedAnns = new ArrayList<>();
			List<AltCoinBean> dbedAnns = new ArrayList<>();
			for (AltCoinBean ann : anns) {
				if (parsedTopicids.contains(ann.getTopicid())) {
					dbedAnns.add(ann);
				}
			}
			anns.clear();
			
			if (Utils.isNotEmpty(dbedAnns)) {// update ann info
				for (AltCoinBean ann : dbedAnns) {
					AltCoinBean alt = altCoinService.getByTopicId(ann.getTopicid());
					if (Utils.isNotEmpty(alt.getName())) {
//							alt.setTitle(ann.getTitle());
//							alt.setReplies(ann.getReplies());
//							alt.setViews(ann.getViews());
//							alt.setLastPostTime(ann.getLastPostTime());
//							
//							altCoinService.save(alt);
					} else {
						unNamedAnns.add(alt);
					}
				}
			}
			
			if (Utils.isNotEmpty(unNamedAnns)) {// update ann info
				Timestamp curr = new Timestamp(System.currentTimeMillis());

				Map<Integer, AltCoinBean> topicIdAltCoinMap = annCrawler.fectchAnnTopicsByUrls(unNamedAnns);

				for (AltCoinBean alt : unNamedAnns) {
					if (topicIdAltCoinMap.containsKey(alt.getTopicid())) {
						copyProperties(alt, topicIdAltCoinMap.get(alt.getTopicid()));
						
						alt.setCreateTime(curr);
						alt.setIsParsed(Boolean.TRUE);
						altCoinService.saveOrUpdate(alt);
					}
				}
				topicIdAltCoinMap.clear();
			}
			
			Thread.sleep(1 * 1000);
		} catch (Throwable e) {
			log.error("Init Ann Board By URL error.", e);
			
			success = false;
			resp = "Init Ann Board By URL error!";
		}
		
		return Utils.getJsonMessage(success, resp);
	}
	
	@RequestMapping("/updatealtcoins.do")
	@ResponseBody
	public String updateAltCoins(Model model, HttpServletRequest request, 
								@RequestParam String attrs,
								@RequestParam String altIds, 
								@RequestParam String altValues) {
		String resp = null;
		boolean success = true;
		try {
			String[] ids = altIds.split(",");
			String[] fieldArr = attrs.split(",");
			String[] values = altValues.split(",");
			
			int len = fieldArr.length;
			
			List<String[]> rows = new ArrayList<String[]>(ids.length);
			int idx = 0;
			for (int r=0; r<ids.length; r++) {
				String[] row = new String[len];
				
				idx = idx + len * r;
				for (int m=0; m<len; m++) {
					row[m] = Utils.isEmpty(values[idx+m]) ? "" : values[idx+m].trim();
				}
				
				rows.add(row);
			}
			
			
			for (int i=0; i<ids.length; i++) {
				AltCoinBean alt = altCoinService.get(Integer.valueOf(ids[i])); 
				String[] row = rows.get(i);
				String attrName = null;
				String attrValue = null;
				for (int f=0; f<len; f++) {
					attrName = fieldArr[f];
					attrValue = row[f];
					try {
						log.debug("Before, set property[" + attrName + "] to value[" + attrValue + "]");
						BeanUtils.setProperty(alt, attrName, attrValue);
						log.debug("After,  get property[" + attrName + "]'s value[" + BeanUtils.getProperty(alt, attrName) + "]");
					} catch (Exception e) {
						log.error("Set Property[" + attrName + "] to value[" + row[f] + "] error.", e);
					}
					System.out.println("test resync funciton ..");
				}
				
				if (alt.getHalfDays() == null && Utils.isPositive(alt.getHalfBlocks()) && Utils.isPositive(alt.getBlockTime()) ) {
					long halftime = alt.getHalfBlocks().longValue() * alt.getBlockTime().longValue();
					int halfDays = (int) (halftime > ONE_DAY_SECONDS ? halftime / ONE_DAY_SECONDS : 1);
					alt.setHalfDays(halfDays);
				}
				
				if (alt.getPreMined() == null && Utils.isPositive(alt.getMinedPercentage()) && Utils.isPositive(alt.getTotalAmount())) {
					alt.setPreMined((long) (alt.getTotalAmount()*alt.getMinedPercentage()));
				}
				
				if (alt.getMinedPercentage() == null && Utils.isPositive(alt.getPreMined()) && Utils.isPositive(alt.getTotalAmount())) {
					alt.setMinedPercentage((double) (alt.getPreMined()/alt.getTotalAmount())); 
				}
				
				if (alt.getPowDays() == null &&  Utils.isPositive(alt.getBlockTime()) && Utils.isPositive(alt.getPowHeight())) {
					alt.setPowDays((int) ((alt.getBlockTime() * alt.getPowHeight())/ONE_DAY_SECONDS));
				}
				
				if (alt.getPowHeight() == null &&  Utils.isPositive(alt.getBlockTime()) && Utils.isPositive(alt.getPowDays())) {
					alt.setPowHeight( (int) ((alt.getPowDays()*ONE_DAY_SECONDS)/alt.getBlockTime()) );
				}
				
				altCoinService.saveOrUpdate(alt);
			}
			
			resp = "Success!";
		} catch (Throwable e) {
			log.error("Update Alt Coin Info error.", e);
			
			success = false;
			resp = "Update Alt Coin Info error!";
		}
		
		return Utils.getJsonMessage(success, resp);
	}
	
	@RequestMapping("/crawlertopicboard.do")
	@ResponseBody
	public String crawlerTopicBoard(Model model, HttpServletRequest request, 
								@RequestParam String baseSeedUrl, 
								@RequestParam int startgroup,
								@RequestParam int endgroup ) {
		String resp = null;
		boolean success = true;
		try {
			BitcointalkCrawler annCrawler = new BitcointalkCrawler();
			
			for (int i=startgroup; i<endgroup; i++) {
				List<BitcointalkTopicBean> beans = annCrawler.fectchTalkTopics(baseSeedUrl, i);
				
				List<Integer> parsedTopicids = altCoinTopicService.findParsedTopicids();
				
				List<BitcointalkTopicBean> undbBeans = new ArrayList<BitcointalkTopicBean>();
				for (BitcointalkTopicBean ann : beans) {
					if (!parsedTopicids.contains(ann.getTopicid())) {
						undbBeans.add(ann);
					}
				}
				beans.clear();
				
//				CountDownLatch counter = new CountDownLatch(1);
//				new AltCoinTopicCrawlerThread(undbBeans, false, counter).start();
//				counter.await();
//				
//				for (AltCoinTopicBean bean : undbBeans) {
//					if (bean.getPublishDate() != null) {
//						bean.setIsParsed(Boolean.TRUE);
//						altCoinTopicService.saveOrUpdate(bean);
//					}
//				}
				
				Thread.sleep(10 * 1000);
			}
			
		} catch (Throwable e) {
			log.error("Crawler Topic Board error.", e);
			
			success = false;
			resp = "Crawler Topic Board error!";
		}
		
		return Utils.getJsonMessage(success, resp);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/showdashboard.do")
	public String showDashboard(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue,
							@RequestParam(required=false) Boolean editable ) {
		String jsp = "bitcointalk/dashboard";
		
		Page<AltCoinBean> params = (Page<AltCoinBean>) request.getAttribute("params");
		
//		if (Utils.isEmpty(params.getOrderBy())) {
//			params.setOrderBy("publishDate, launchTime, interestLevel");
			params.setOrderBy("lastPostTime");
			params.setOrder(Page.DESC);
//		}
		
		List<AltCoinBean> alts = altCoinService.findAnnCoins(params);
		
		// by reply
		params.setOrderBy("replies, views");
		params.setOrder(Page.DESC);
		List<AltCoinBean> altsByReply = altCoinService.findAnnCoins(params);
		
		model.addAttribute("alts", alts);
		model.addAttribute("altsByReply", altsByReply);
		model.addAttribute("params", params);
		
		return jsp;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/showanncoins.do")
	public String showAltCoins(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue,
							@RequestParam(required=false) Boolean editable ) {
		String jsp = "bitcointalk/alt_coin_list";
		
		Page<AltCoinBean> params = (Page<AltCoinBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("publishDate, createTime");
			params.setOrder(Page.DESC);
		}
		
		if (editable == null) {
			editable = Boolean.FALSE;
		}
		
		List<AltCoinBean> anns = null;
		if (Utils.isNotEmpty(searchField) && Utils.isNotEmpty(searchValue)) {
			anns = altCoinService.searchAnnCoins(params, searchField, searchValue.trim());
		} else {
			anns = altCoinService.findAnnCoins(params);
		}
		
		model.addAttribute("anns", anns);
		model.addAttribute("params", params);
		model.addAttribute("searchField", searchField);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("editable", editable);
		
		return jsp;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/showinterestcoins.do")
	public String showInterestAltCoins(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue,
							@RequestParam(required=false) Integer interest,
							@RequestParam(required=false) Boolean editable ) {
		String jsp = "bitcointalk/interest_coin_list";
		
		Page<AltCoinBean> params = (Page<AltCoinBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("launchTime, interestLevel, publishDate, createTime");
			params.setOrder(Page.DESC);
		}
		
		if (editable == null) {
			editable = Boolean.FALSE;
		}
		
		if (interest == null) {
			interest = 8;
		}
		
		List<AltCoinBean> anns = null;
		if (Utils.isNotEmpty(searchField) && Utils.isNotEmpty(searchValue)) {
			anns = altCoinService.searchInterestAnnCoins(params, searchField, searchValue.trim(), interest);
		} else {
			anns = altCoinService.findInterestAnnCoins(params, interest);
		}
		
		model.addAttribute("anns", anns);
		model.addAttribute("params", params);
		model.addAttribute("searchField", searchField);
		model.addAttribute("searchValue", searchValue);
		model.addAttribute("interest", interest);
		model.addAttribute("editable", editable);
		
		return jsp;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/showtalktopics.do")
	public String showTalkTopics(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue ) {
		String jsp = "bitcointalk/alt_coin_topic_list";
		
		Page<BitcointalkTopicBean> params = (Page<BitcointalkTopicBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("publishDate");
			params.setOrder(Page.DESC);
		}
		
		List<BitcointalkTopicBean> topics = null;
		if (Utils.isNotEmpty(searchField) && Utils.isNotEmpty(searchValue)) {
			topics = altCoinTopicService.searchTalkTopics(params, searchField, searchValue.trim());
		} else {
			topics = altCoinTopicService.findTalkTopics(params);
		}
		
		model.addAttribute("topics", topics);
		model.addAttribute("params", params);
		
		return jsp;
	}
	
}
