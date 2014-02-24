package org.omega.crawler.bitcointalk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.AltCoinTopicBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.Utils;
import org.omega.crawler.common.thread.AltCoinCrawlerThread;
import org.omega.crawler.common.thread.AltCoinTopicCrawlerThread;
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
	
	@Autowired
	private AltCoinService altCoinService;
	
	@Autowired
	private AltCoinTopicService altCoinTopicService;
	
	@RequestMapping("/initannboard.do")
	@ResponseBody
	public String initAnnBoard(Model model, HttpServletRequest request, 
								@RequestParam String baseSeedUrl, 
								@RequestParam int startgroup,
								@RequestParam int endgroup ) {
		String resp = null;
		boolean success = true;
		try {
			BitcointalkCrawler annCrawler = new BitcointalkCrawler();
			
			for (int i=startgroup; i<endgroup; i++) {
				List<AltCoinBean> anns = annCrawler.fectchAnnTopics(baseSeedUrl, i);
				
				List<Integer> parsedTopicids = altCoinService.findParsedTopicids();
				
				List<AltCoinBean> undbAnns = new ArrayList<>();
				for (AltCoinBean ann : anns) {
					if (!parsedTopicids.contains(ann.getTopicid())) {
						undbAnns.add(ann);
					}
				}
				anns.clear();
				
				CountDownLatch counter = new CountDownLatch(1);
				new AltCoinCrawlerThread(undbAnns, true, counter).start();
				counter.await();
				
				for (AltCoinBean ann : undbAnns) {
					if (ann.getPublishDate() != null) {
						ann.setIsParsed(Boolean.TRUE);
						altCoinService.saveOrUpdate(ann);
					}
				}
				
				Thread.sleep(1 * 1000);
			}
		} catch (Throwable e) {
			log.error("Init Ann Board error.", e);
			
			success = false;
			resp = "Init Ann Board error!";
		}
		
		return Utils.getJsonMessage(success, resp);
	}
	
	@RequestMapping("/updatealtcoins.do")
	@ResponseBody
	public String updateAltCoins(Model model, HttpServletRequest request, 
								@RequestParam String altIds, 
								@RequestParam String altValues) {
		String resp = null;
		boolean success = true;
		try {
			String[] ids = altIds.split(",");
			String[] values = altValues.split(",");
			for (int i=0; i<ids.length; i++) {
				String id = ids[i];
				int sIndex = i*9;
				AltCoinBean alt = altCoinService.get(Integer.valueOf(id));
		    	
				alt.setAlgo(values[sIndex]);
				if (Utils.isNotEmpty(values[sIndex+1])) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					alt.setCountDown(sdf.parse(values[sIndex+1]));
				}
				alt.setName(values[sIndex+2]);
				alt.setAbbrName(values[sIndex+3]);
				alt.setInterestLevel(Integer.valueOf(values[sIndex+4]));
				
				alt.setProof(values[sIndex+5]);
				if (1 == Integer.valueOf(values[sIndex+6])) alt.setCpuMinable(Boolean.TRUE);
				if (1 == Integer.valueOf(values[sIndex+7])) alt.setGpuMinable(Boolean.TRUE);
				if (1 == Integer.valueOf(values[sIndex+8])) alt.setAsicMinable(Boolean.TRUE);
				
				altCoinService.saveOrUpdate(alt);
			}
		} catch (Throwable e) {
			log.error("Init Ann Board error.", e);
			
			success = false;
			resp = "Init Ann Board error!";
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
				List<AltCoinTopicBean> beans = annCrawler.fectchTalkTopics(baseSeedUrl, i);
				
				List<Integer> parsedTopicids = altCoinTopicService.findParsedTopicids();
				
				List<AltCoinTopicBean> undbBeans = new ArrayList<AltCoinTopicBean>();
				for (AltCoinTopicBean ann : beans) {
					if (!parsedTopicids.contains(ann.getTopicid())) {
						undbBeans.add(ann);
					}
				}
				beans.clear();
				
				CountDownLatch counter = new CountDownLatch(1);
				new AltCoinTopicCrawlerThread(undbBeans, false, counter).start();
				counter.await();
				
				for (AltCoinTopicBean bean : undbBeans) {
					if (bean.getPublishDate() != null) {
						bean.setIsParsed(Boolean.TRUE);
						altCoinTopicService.saveOrUpdate(bean);
					}
				}
				
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
	@RequestMapping("/showanncoins.do")
	public String showAltCoins(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue,
							@RequestParam(required=false) Boolean editable ) {
		String jsp = "bitcointalk/alt_coin_list";
		
		Page<AltCoinBean> params = (Page<AltCoinBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("publishDate");
			params.setOrder(Page.DESC);
		}
		
		if (editable == null) {
			editable = Boolean.FALSE;
		}
		
//		editable = Boolean.TRUE;
		
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
	@RequestMapping("/showtalktopics.do")
	public String showTalkTopics(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue ) {
		String jsp = "bitcointalk/alt_coin_topic_list";
		
		Page<AltCoinTopicBean> params = (Page<AltCoinTopicBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("publishDate");
			params.setOrder(Page.DESC);
		}
		
		List<AltCoinTopicBean> topics = null;
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
