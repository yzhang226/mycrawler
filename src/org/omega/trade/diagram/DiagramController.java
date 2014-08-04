package org.omega.trade.diagram;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.omega.crawler.common.Constants;
import org.omega.crawler.common.Utils;
import org.omega.trade.entity.WatchListItem;
import org.omega.trade.service.MarketTradeService;
import org.omega.trade.service.TradeStatisticsService;
import org.omega.trade.service.WatchListItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jsp/diagram")
public class DiagramController {
	
//	private static final Log log = LogFactory.getLog(DiagramController.class);
	
	@Autowired
	private MarketTradeService marketTradeService;
	
	@Autowired
	private TradeStatisticsService tradeStatisticsService;
	
	@Autowired
	private WatchListItemService watchListItemService;
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/toDiagramPage.do")
	public String toDiagramPage(Model model, HttpServletRequest request ) {
		String jsp = "diagram/diagram";
		
		List<WatchListItem> items = watchListItemService.find("from WatchListItem where status = ?", Constants.STATUS_ACTIVE);
		
		Set<String> operators = new TreeSet<>();
		Set<String> wathcedSymbols = new TreeSet<>();
		Set<String> exSymbols = new TreeSet<>();
		for (WatchListItem wi : items) {
			operators.add(wi.getOperator());
			wathcedSymbols.add(wi.getWatchedSymbol());
			exSymbols.add(wi.getExchangeSymbol());
		}
		
		model.addAttribute("its", items);
		model.addAttribute("operators", operators);
		model.addAttribute("wathcedSymbols", wathcedSymbols);
		model.addAttribute("exchangeSymbols", exSymbols);
		
		return jsp;
	}
	
	@RequestMapping("/fetchTradeData.do")
	@ResponseBody
	public Object[][] fetchTradeData(@RequestParam String operator, 
											@RequestParam String watchedSymbol, 
											@RequestParam String exchangeSymbol) {
		WatchListItem item = new WatchListItem(operator, watchedSymbol, exchangeSymbol);
		String table = Utils.getMarketTradeTable(item);
		
		StringBuilder highLowSql = new StringBuilder("select max(price) as high, min(price) as low  from ");
		highLowSql.append(table).append(" where trade_time > ? and trade_time < ?");
		StringBuilder openSql = new StringBuilder("select price from ");
		openSql.append(table).append(" where trade_time > ? and trade_time < ? order by trade_time asc limit 1");
		StringBuilder closeSql = new StringBuilder("select price from ");
		closeSql.append(table).append(" where trade_time > ? and trade_time < ? order by trade_time desc limit 1");
		StringBuilder watchedVolSql = new StringBuilder("select sum(total_units) as watchedVolume, max(total_cost) as exchangeVolume from ");
		watchedVolSql.append(table).append(" where trade_time > ? and trade_time < ?");
		StringBuilder countTradeSql = new StringBuilder("select count(total_units) countTrade from ");
		countTradeSql.append(table).append(" where trade_time > ? and trade_time < ?");
		
		
		
		String maxMTimeSql = new StringBuilder("select max(trade_time), min(trade_time) from ").append(table).toString();
		Object[] maxMin = (Object[]) findUnique(maxMTimeSql);
		long maxMTimeSec = ((BigInteger) maxMin[0]).longValue();
		long minMTimeSec = ((BigInteger) maxMin[1]).longValue();
		
		int maxDiffMilliSec = (int) (maxMTimeSec - minMTimeSec);
		int maxHours = maxDiffMilliSec / 1000 / 60 / 60;
//		int num = maxHours > 6 ?  6 * 60 / 5 : maxHours * 60 / 5;
		int num = maxHours * 60;
		int increment = 60 * 1000;// five minutes
		
		long first = Utils.getOneMinuteRangeEnd(maxMTimeSec);
		long second = Utils.getOneMinuteRangeStart(maxMTimeSec);
		
		Object[][] arr = new Object[num][8];
		Double open = null;
		Object[] highLow = null;
		Double close = null;
		Object[] vols = null;
		BigInteger countTrade = null;
		long nfirst, nsecond;
		int arrIdx = 0;
		for (int i=num-1; i>=0; i--) {
			nfirst = first - increment * i;
			nsecond = second - increment * i;
			
			open = (Double) findUnique(openSql.toString(), nsecond, nfirst);
			if (open == null) {
//				if (arrIdx == 0) {
//					open = 0.0;
//				} else {
//					open = (Double) arr[arrIdx-1][1];
//				}
//				
//				highLow = new Double[]{open, open};
//				close = open;
//				vols = new Double[]{0.0, 0.0};
//				countTrade = new BigInteger("0");
				continue;
			} else {
				highLow = (Object[]) findUnique(highLowSql.toString(), nsecond, nfirst);
				close = (Double) findUnique(closeSql.toString(), nsecond, nfirst);
				vols = (Object[]) findUnique(watchedVolSql.toString(), nsecond, nfirst);
				countTrade = (BigInteger) findUnique(countTradeSql.toString(), nsecond, nfirst);
			}
			
			
			
			arr[arrIdx][0] = nsecond;
			arr[arrIdx][1] = open;
			arr[arrIdx][2] = (Double) highLow[0];
			arr[arrIdx][3] = (Double) highLow[1];
			arr[arrIdx][4] = close;
			arr[arrIdx][5] = (Double) vols[0];
			arr[arrIdx][6] = (Double) vols[1];
			arr[arrIdx][7] = countTrade.intValue();
			
			arrIdx++;
		}
		System.out.println("arr.length is " + arr.length);
		
		arr = Arrays.copyOf(arr, arrIdx);
		System.out.println("arr.length is " + arr.length);
		
		return arr;
	}
	
	private Object findUnique(String sql, Object... params) {
		return marketTradeService.createSQLQuery(sql, params).uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getStatisticsByItemId.do")
	@ResponseBody
	public List<Object> getStatisticsByItemId(@RequestParam Integer itemId) {
		// 20_mintpal_btc_uro
		String sql = "select start_time, open, high, low, close, watched_vol, exchange_vol, count from trade_statistics_one_minute where item_id = ? order by start_time asc";
//		String hql = "select startTime, open, high, low, close, watchedVol, exchangeVol, count from TradeStatistics where itemId = ?";
		
//		List<Object> resu = tradeStatisticsService.find(hql, itemId);
		List<Object> resu = tradeStatisticsService.findSql(sql, itemId);
		
		return resu;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getOneDayStatistics.do")
	@ResponseBody
	public Object[] getOneDayStatistics(@RequestParam String operator, 
			  @RequestParam String symbol,
			  @RequestParam String exchange, 
			  @RequestParam(required=false) Integer nthDay) {
		// 20_mintpal_btc_uro
		if (nthDay == null || nthDay <= 0) {
			nthDay = 1;
		}
		nthDay = nthDay  - 1;
		
//		DateTime curr = new DateTime(DateTimeZone.UTC);
		
		
		Date gmtCurr = Utils.convertDateZone(new Date(), Utils.TIME_ZONE_GMT);
		DateTime gmtdt = new DateTime(gmtCurr);
		gmtdt = gmtdt.withSecondOfMinute(0).withMillisOfSecond(0).minusDays(nthDay);
		
		String hql = "from WatchListItem where operator = ? and watchedSymbol = ? and exchangeSymbol = ?";
		WatchListItem item = (WatchListItem) watchListItemService.findUnique(hql, operator, symbol, exchange);
//		WatchListItem item = watchListItemService.get(itemId);
		
		Object[] stat = new Object[6];
		
		String lastTradePriceSql = "select price from " + item.toMarketTradeTable() + " order by trade_time desc limit 1";
		List<Object> resu = marketTradeService.findSql(lastTradePriceSql);
		stat[0] = resu.get(0);
		
		String sql = "select max(high), min(low), sum(watched_vol), sum(exchange_vol), sum(count) from trade_statistics_one_minute "
						+ "where item_id = ? and start_time >= ? and end_time < ? order by start_time desc";
		System.out.println("" + gmtdt.minusDays(1).getMillis() + ", " + gmtdt.getMillis());
		resu = tradeStatisticsService.findSql(sql, item.getId(), gmtdt.minusDays(1).getMillis(), gmtdt.getMillis());
		
		System.arraycopy(resu.get(0), 0, stat, 1, 5);
		
		
		return stat;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getStatisticsBySymbol.do")
	@ResponseBody
	public List<Object> getStatisticsBySymbol(@RequestParam String operator, 
											  @RequestParam String symbol,
											  @RequestParam String exchange) {
		String hql = "from WatchListItem where operator = ? and watchedSymbol = ? and exchangeSymbol = ?";
		WatchListItem item = (WatchListItem) watchListItemService.findUnique(hql, operator, symbol, exchange);
		if (item == null) {
			return new ArrayList<>(0);
		}
		String sql = "select start_time, open, high, low, close, watched_vol, exchange_vol, count from trade_statistics_one_minute where item_id = ? order by start_time asc";
		List<Object> resu = tradeStatisticsService.findSql(sql, item.getId());
		
		return resu;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getAllInfos.do")
	@ResponseBody
	public Map<String, Object> getAllInfos() {
		
		
		String hql = "from WatchListItem where status = ? order by operator, watchedSymbol, exchangeSymbol";
		List<WatchListItem> activeItems = watchListItemService.find(hql, Constants.STATUS_ACTIVE);
		
		Set<String> operators = new TreeSet<>();
		Set<String> wathcedSymbols = new TreeSet<>();
		Set<String> exSymbols = new TreeSet<>();
		
		Map<String, Set<String>> listedSymbolMap = new LinkedHashMap<>();
		Set<String> listedSymbols = new TreeSet<>();
		
		Map<String, Set<String>> listedExSymbolMap = new LinkedHashMap<>();
		Set<String> listedExSymbols = null;
		
		String key = null;
		for (WatchListItem wi : activeItems) {
			operators.add(wi.getOperator());
			wathcedSymbols.add(wi.getWatchedSymbol());
			exSymbols.add(wi.getExchangeSymbol());
			
			listedSymbols = listedSymbolMap.get(wi.getOperator());
			if (listedSymbols == null) {
				listedSymbols = new TreeSet<>();
				listedSymbolMap.put(wi.getOperator(), listedSymbols);
			}
			listedSymbols.add(wi.getWatchedSymbol());
			
			key = wi.getOperator() + "_" + wi.getWatchedSymbol();
			listedExSymbols = listedExSymbolMap.get(key);
			if (listedExSymbols == null) {
				listedExSymbols = new TreeSet<>();
				listedExSymbolMap.put(key, listedExSymbols);
			}
			listedExSymbols.add(wi.getExchangeSymbol());
		}
		
		Map<String, Object> infos = new LinkedHashMap<String, Object>();
		infos.put("operators", operators);
		infos.put("wathcedSymbols", wathcedSymbols);
		infos.put("exSymbols", exSymbols);
		infos.put("listedSymbolMap", listedSymbolMap);
		infos.put("listedExSymbolMap", listedExSymbolMap);
		
		return infos;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getActiveOperatorsAndExSymbols.do")
	@ResponseBody
	public List<String> getActiveOperatorsAndExSymbols(@RequestParam String symbol) {
		List<String> operators = watchListItemService.find("select distinct operator from WatchListItem where status = ? and watchedSymbol = ? ", Constants.STATUS_ACTIVE, symbol);
		List<String> exchangeSymbols = watchListItemService.find("select distinct exchangeSymbol from WatchListItem where status = ? and watchedSymbol = ? ", Constants.STATUS_ACTIVE, symbol);
		
		List<String> resu = new ArrayList<>(operators.size()+exchangeSymbols.size());
		
		resu.addAll(operators);
		resu.addAll(exchangeSymbols);
		
		return resu;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/getActiveWatchedAndExSymbols.do")
	@ResponseBody
	public List<String> getActiveWatchedAndExSymbols(@RequestParam String operator) {
		List<String> watchedSymbols = watchListItemService.find("select distinct watchedSymbol from WatchListItem where status = ? and operator = ? ", Constants.STATUS_ACTIVE, operator);
		List<String> exchangeSymbols = watchListItemService.find("select distinct exchangeSymbol from WatchListItem where status = ? and operator = ? ", Constants.STATUS_ACTIVE, operator);
		
		List<String> resu = new ArrayList<>(watchedSymbols.size()+exchangeSymbols.size());
		
		resu.addAll(watchedSymbols);
		resu.addAll(exchangeSymbols);
		
		return resu;
	}
	
	
	
	
}
