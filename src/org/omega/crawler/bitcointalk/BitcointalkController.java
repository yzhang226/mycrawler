package org.omega.crawler.bitcointalk;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.omega.crawler.bean.AltCoinBean;
import org.omega.crawler.bean.MyTopicBean;
import org.omega.crawler.common.Arith;
import org.omega.crawler.common.Page;
import org.omega.crawler.common.Utils;
import org.omega.crawler.service.AltCoinService;
import org.omega.crawler.service.AltCoinTopicService;
import org.omega.crawler.service.AltCoinWatchListService;
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
	
	@Autowired
	private AltCoinService altCoinService;
	
	@Autowired
	private AltCoinTopicService altCoinTopicService;
	
	@Autowired
	private AltCoinWatchListService altCoinWatchListService;
	
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
			String[] fieldsArr = attrs.split(",");
			
			List<String[]> rows = splitToRows(ids.length, fieldsArr.length, altValues);
			
			for (int i=0; i<ids.length; i++) {
				AltCoinBean alt = altCoinService.get(Integer.valueOf(ids[i])); 
				String[] row = rows.get(i);
				String attrName = null;
				String attrValue = null;
				for (int f=0; f<fieldsArr.length; f++) {
					attrName = fieldsArr[f];
					attrValue = row[f];
					try {
						BeanUtils.setProperty(alt, attrName, attrValue);
//						log.info("Set Property[" + attrName + "] to value[" + row[f] + "].");
					} catch (Exception e) {
						log.error("Set Property[" + attrName + "] to value[" + row[f] + "] error.", e);
					}
				}
				
				if (alt.getHalfDays() == null && Utils.isPositive(alt.getHalfBlocks()) && Utils.isPositive(alt.getBlockTime()) ) {
					long halftime = alt.getHalfBlocks().longValue() * alt.getBlockTime().longValue();
					int halfDays = (int) (halftime > ONE_DAY_SECONDS ? halftime / ONE_DAY_SECONDS : 1);
					alt.setHalfDays(halfDays);
				}
				
				if (alt.getPreMined() == null && Utils.isPositive(alt.getMinedPercentage()) && Utils.isPositive(alt.getTotalAmount())) {
					alt.setPreMined( (long) Arith.multiply(alt.getTotalAmount(), alt.getMinedPercentage()) );
				}
				
				if (alt.getMinedPercentage() == null && Utils.isPositive(alt.getPreMined()) && Utils.isPositive(alt.getTotalAmount())) {
					alt.setMinedPercentage( Arith.divide(alt.getPreMined(), alt.getTotalAmount()) );
				}
				
				if (alt.getPowDays() == null &&  Utils.isPositive(alt.getBlockTime()) && Utils.isPositive(alt.getPowHeight())) {
					alt.setPowDays( Arith.divide(Arith.multiply(alt.getPowHeight(), alt.getBlockTime()), ONE_DAY_SECONDS) );
				}
				
				if (alt.getPowHeight() == null &&  Utils.isPositive(alt.getBlockTime()) && Utils.isPositive(alt.getPowDays())) {
					alt.setPowHeight( (int) Arith.divide(Arith.multiply(alt.getPowDays(), ONE_DAY_SECONDS), alt.getBlockTime()) );
				}
				
				if (Utils.isNotEmpty(alt.getAbbrName())) {
					alt.setAbbrName(alt.getAbbrName().toUpperCase());
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
	
	private List<String[]> splitToRows(int rowLength, int fieldNumber, String altValues) {
		int len = fieldNumber;
		String[] values = altValues.split(",");
		
		List<String[]> rows = new ArrayList<String[]>(rowLength);
		int idx = 0;
		for (int r=0; r<rowLength; r++) {
			String[] row = new String[len];
			
			idx = len * r;
			for (int m=0; m<len; m++) {
				row[m] = Utils.isEmpty(values[idx+m]) ? "" : values[idx+m].trim();
			}
			
			rows.add(row);
		}
		return rows;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/showdashboard.do")
	public String showDashboard(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue,
							@RequestParam(required=false) Boolean editable ) {
		String jsp = "bitcointalk/dashboard";
		
		Page<AltCoinBean> params = (Page<AltCoinBean>) request.getAttribute("params");
		
		params.setOrderBy("lastPostTime");
		params.setOrder(Page.DESC);
	
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
	@RequestMapping("/showCoins4Board.do")
	@ResponseBody
	public Page<AltCoinBean> showCoins4Board(HttpServletRequest request, 
											 @RequestParam(required=false) String condition) {
		Page<AltCoinBean> params =  null;
		try {
			params = (Page<AltCoinBean>) request.getAttribute("params");
			
			altCoinService.findCoinsInDashboard(params, condition);
		} catch (Throwable e) {
			log.error("showCoins4Board error.", e);
		}
		
		return params;
	}
	
	@RequestMapping("/changeCoinStatus.do")
	@ResponseBody
	public String changeCoinStatus(@RequestParam(required=true) String altIds, 
								   @RequestParam(required=true) byte targetStatus) {
		String resp = null;
		boolean success = true;
		try {
			String[] idArr = altIds.split(",");
			for (String idstr : idArr) {
				AltCoinBean alt = altCoinService.get(Integer.valueOf(idstr));
				alt.setStatus(targetStatus);
				altCoinService.saveOrUpdate(alt);
			}
			
			resp = "Success!";
		} catch (Throwable e) {
			resp = "changeCoinStatus error.";
			log.error(resp, e);
			
			success = false;
		}
		
		return Utils.getJsonMessage(success, resp);
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/showanncoins.do")
	public String showAltCoins(Model model, HttpServletRequest request, 
							@RequestParam(required=false) String searchField, 
							@RequestParam(required=false) String searchValue,
							@RequestParam(required=false) Boolean editable, 
							@RequestParam(required=false) String lastSearchValue) {
		String jsp = "bitcointalk/alt_coin_list";
		
		Page<AltCoinBean> params = (Page<AltCoinBean>) request.getAttribute("params");
		
		if (Utils.isNotEmpty(lastSearchValue) && Utils.isNotEmpty(searchValue) 
				&& !lastSearchValue.equals(searchValue)) {
			params.setPageNo(1);
		}
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("myTopic.publishTime, myTopic.createTime");
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
			params.setOrderBy("launchTime, interest, publishDate, createTime");
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
		
		Page<MyTopicBean> params = (Page<MyTopicBean>) request.getAttribute("params");
		
		if (Utils.isEmpty(params.getOrderBy())) {
			params.setOrderBy("publishDate");
			params.setOrder(Page.DESC);
		}
		
		List<MyTopicBean> topics = null;
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
