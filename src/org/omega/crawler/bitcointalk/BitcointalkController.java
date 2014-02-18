package org.omega.crawler.bitcointalk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AnnCoinBean;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.Utils;
import org.omega.crawler.main.BitcointalkAnnCrawler;
import org.omega.crawler.service.AnnCoinService;
import org.omega.crawler.web.FetchAllAnnCoinsThread;
import org.omega.crawler.web.TopicPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jsp/bitcointalk")
public class BitcointalkController {
	
	private static final Log log = LogFactory.getLog(BitcointalkController.class);
	
	@Autowired
	private AnnCoinService annCoinService;
	
	@RequestMapping("/initannboard.do")
	@ResponseBody
	public String initAnnBoard(Model model, HttpServletRequest request) {
		String resp = null;
		boolean success = true;
		try {
			BitcointalkAnnCrawler annCrawler = new BitcointalkAnnCrawler();
			List<AnnCoinBean> anns = annCrawler.fectchAnnCoins();
			
			List<Integer> parsedTopicids = annCoinService.findParsedTopicids();
			
			List<AnnCoinBean> undbAnns = new ArrayList<>();
			for (AnnCoinBean ann : anns) {
				if (!parsedTopicids.contains(ann.getTopicid())) {
					undbAnns.add(ann);
				}
			}
			
			CountDownLatch counter = new CountDownLatch(1);
			new FetchAllAnnCoinsThread(undbAnns, counter).start();
			counter.await();
			
			for (AnnCoinBean ann : undbAnns) {
				if (ann.getPublishDate() != null) {
					ann.setIsParsed(Boolean.TRUE);
					annCoinService.saveOrUpdate(ann);
				}
			}
			
		} catch (Throwable e) {
			log.error("Init Ann Board error.", e);
			
			success = false;
			resp = "Init Ann Board error!";
		}
		
		return Utils.getJsonMessage(success, resp);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/showanncoins.do")
	public String toAnnBoard(Model model, HttpServletRequest request) {
		String jsp = "bitcointalk/ann_coin_list";
		
		Page<AnnCoinBean> params = (Page<AnnCoinBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("publishDate");
			params.setOrder(Page.DESC);
		}
		
		List<AnnCoinBean> anns = annCoinService.findAnnCoins(params);
		
		model.addAttribute("anns", anns);
		model.addAttribute("params", params);
		
		return jsp;
	}
	
}
