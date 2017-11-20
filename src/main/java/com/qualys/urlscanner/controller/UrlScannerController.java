package com.qualys.urlscanner.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.qualys.urlscanner.bean.UrlData;
import com.qualys.urlscanner.bo.IUrlScannerBO;
	

@RestController
public class UrlScannerController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlScannerController.class);
	
	@Autowired
	private IUrlScannerBO urlScannerBO;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<UrlData> list(@RequestParam(value = "sdate", required = false) String sdate,
			@RequestParam(value = "edate", required = false) String edate) {
		
		List<UrlData> list = null;
		if(null != sdate && !sdate.isEmpty()  && null != edate && !edate.isEmpty()) {
			list= urlScannerBO.getDateBetweenRecords(sdate,edate);
			
		}else {
			list= urlScannerBO.getLastAccessedTenRecords();
		}
		
		return list;
	}
	@RequestMapping(value = "/submit/url", method = RequestMethod.POST)
	public UrlData scanUrl(@QueryParam("url") String url) throws Throwable {
		UrlData result = null;
		result = urlScannerBO.scanUrl(url);

		return result;
	}
	@RequestMapping(value = "/urlId/{urlId}", method = RequestMethod.GET)
	public UrlData getUrlData(@PathVariable(required = true) Integer urlId) {
		UrlData urlData = urlScannerBO.findUrlDataByUrlId(urlId);
		return urlData;
	}
	

	@RequestMapping("/test")
	public String greeting(@RequestParam(value = "name", required = false, defaultValue = "World") String name,
			Model model) {
		model.addAttribute("name", name);
		UrlData data = urlScannerBO.getUrlData(1);
		List<UrlData> list = urlScannerBO.getLastAccessedTenRecords();	
		String json = new Gson().toJson(list);
		
		return json;
	}
	
	
	
	
}


