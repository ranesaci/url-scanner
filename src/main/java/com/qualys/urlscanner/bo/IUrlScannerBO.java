package com.qualys.urlscanner.bo;

import java.util.List;

import com.qualys.urlscanner.bean.UrlData;

public interface IUrlScannerBO {
	
	List<UrlData> getUrlDataList();

	UrlData scanUrl(String url);

	UrlData getUrlData(Integer id);

	List<UrlData> getLastAccessedTenRecords();

	UrlData findUrlDataByUrlId(Integer urlId);

	List<UrlData> getDateBetweenRecords(String sdate, String edate);
	

}
