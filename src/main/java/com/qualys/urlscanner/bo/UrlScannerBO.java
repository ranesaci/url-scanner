package com.qualys.urlscanner.bo;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.qualys.urlscanner.bean.ContentDetailEnum;
import com.qualys.urlscanner.bean.UrlData;
import com.qualys.urlscanner.dao.UrlDataRepository;
import com.qualys.urlscanner.entity.UrlDataEntity;

@Controller
public class UrlScannerBO implements IUrlScannerBO{
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlScannerBO.class);
	private static final String DATE_FORMAT= "yyyy-MM-dd";
	
	 //private static final Pattern TITLE_TAG = Pattern.compile("\\<title>(.*)\\</title>", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
	
	@Autowired
	private UrlDataRepository urlDataRepository;
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public List<UrlData> getUrlDataList() {
		List<UrlData> data = getTestData();
		return data;
	}

	

	@Override
	public UrlData scanUrl(String url) {
		//check whether URL available or not, if not available then add new record else update after scan
		//when record is new , after scan, update status 1
		UrlDataEntity urlDataUpdatedEntity;
		UrlDataEntity urlDataEntity = urlDataRepository.findByUrl(url);
		if(null != urlDataEntity) {
			UrlDataEntity scannedData = doUrlScan(url);
			urlDataUpdatedEntity = updateUrlData(urlDataEntity,scannedData);
		}else {
			//add record
			urlDataEntity = new UrlDataEntity();
			urlDataEntity.setUrl(url);
			urlDataEntity.setStatus(false);
			urlDataEntity.setSubmittedOn(new Date());
			UrlDataEntity savedEntity = urlDataRepository.save(urlDataEntity);
			UrlDataEntity scannedData = doUrlScan(url);
			urlDataUpdatedEntity = updateUrlData(savedEntity,scannedData);
			
			
			
		}
		 
		return modelMapper.map(urlDataUpdatedEntity, UrlData.class);
	}
	
	private UrlDataEntity updateUrlData(UrlDataEntity urlDataEntity, UrlDataEntity scannedData) {
		UrlDataEntity freshData =  modelMapper.map(scannedData, UrlDataEntity.class);
		if(null != freshData) {
			freshData.setUrlId(urlDataEntity.getUrlId());
			freshData = urlDataRepository.save(freshData);
		}
		
		
		return freshData;
		
	}



	private UrlDataEntity doUrlScan(String url) {
		int imageCount=0, linkCount=0;
		String title=null,body=null;
		UrlDataEntity urlDataEntity = new UrlDataEntity();
		
		urlDataEntity.setUrl(url);
		
		
		String redirectUrl = getRedirectUrl(url);
		String ipAddress = getIpAddress(url);
		
		urlDataEntity.setRedirectUrl(redirectUrl);
		urlDataEntity.setIpAddress(ipAddress);
		
		Map<String, String> webSiteContentDertails = getWebsiteContentDetails(url);
		if(null != webSiteContentDertails && webSiteContentDertails.size()>0) {
			title= webSiteContentDertails.get(ContentDetailEnum.TITLE.toString());
			body= webSiteContentDertails.get(ContentDetailEnum.BODY.toString());
			imageCount= getStringToInteger(webSiteContentDertails.get(ContentDetailEnum.IMAGE_COUNT.toString()));
			linkCount= getStringToInteger(webSiteContentDertails.get(ContentDetailEnum.LINK_COUNT.toString()));
			
		}
		urlDataEntity.setWebsiteTitle(title);
		urlDataEntity.setImageCount(imageCount);
		urlDataEntity.setLinkCount(linkCount);
		urlDataEntity.setWebsiteBody(body);
		urlDataEntity.setStatus(true);//set this to tell processing done
		urlDataEntity.setSubmittedOn(new Date());
		
		
		
		
		return urlDataEntity;
	}
	private int getStringToInteger(String str) {
		if(null != str && !str.isEmpty()) {
			return Integer.parseInt(str);
			
		}else {
			return 0;
		}
		
		
	}



	private String getIpAddress(String url) {
		
		InetAddress address=null;
		String ipAddress = null;
		try {
		address = InetAddress.getByName(new URL(url).getHost());
		}catch (Exception e) {
			ipAddress = "ERROR";
		}
		if(null != address && address.toString().contains("/")) {
			ipAddress = address.toString().substring(address.toString().lastIndexOf("/")+1);
		}
		
		return ipAddress;
	}



	private String getRedirectUrl(String url) {
		String redirectUrl=null;
		
		try {
		URLConnection con = new URL( url ).openConnection();
		System.out.println( "orignal url: " + con.getURL() );
		con.connect();
		System.out.println( "connected url: " + con.getURL() );
		InputStream is = con.getInputStream();
		redirectUrl = con.getURL().toString();
		if(url.equalsIgnoreCase(redirectUrl)) {
			redirectUrl="";
		}
		is.close();
		}catch (Exception e) {
			e.printStackTrace();
			redirectUrl="ERROR";
		}
		
		return redirectUrl;
	}



	private List<UrlData> getTestData() {
		List<UrlData> list = new ArrayList<>();
		
		UrlData data1 = new UrlData();
		UrlData data2 = new UrlData();
		
		data1.setUrl("www.google.com");
		data1.setRedirectUrl("AAAA");
		data1.setStatus(true);
		data1.setIpAddress("32.324.32.432");
		data1.setWebsiteTitle("AtitleT");
		data1.setSubmittedOn(new Date());
		
		data2.setUrl("www.yahoo.com");
		data2.setRedirectUrl("BBB");
		data2.setStatus(true);
		data2.setIpAddress("212.212.212.323");
		data2.setWebsiteTitle("Btitle");
		data2.setSubmittedOn(new Date());
		
		list.add(data1);
		list.add(data2);
		
		return list;
	}



	@Override
	public UrlData getUrlData(Integer id) {
		UrlDataEntity urlDataEntity  = urlDataRepository.findOne(id);
		return modelMapper.map(urlDataEntity, UrlData.class);
	}



	@Override
	public List<UrlData> getLastAccessedTenRecords() {
		List<UrlDataEntity> urlDataEntityList  = urlDataRepository.findLast10ByOrderBySubmittedOnDesc();
		Type listType = new TypeToken<List<UrlData>>() {}.getType();
		return modelMapper.map(urlDataEntityList, listType);
	}
	
	/**
     * @param url the HTML page
     * @return title text (null if document isn't HTML or lacks a title tag)
     * @throws IOException
     */
   /* public  String getPageTitle(String url) throws IOException {
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
 
        // ContentType is an inner class defined below
        ContentType contentType = getContentTypeHeader(conn);
        if (!contentType.contentType.equals("text/html"))
            return null; // don't continue if not HTML
        else {
            // determine the charset, or use the default
            Charset charset = getCharset(contentType);
            if (charset == null)
                charset = Charset.defaultCharset();
 
            // read the response body, using BufferedReader for performance
            InputStream in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
            int n = 0, totalRead = 0;
            char[] buf = new char[1024];
            StringBuilder content = new StringBuilder();
 
            // read until EOF or first 8192 characters
            while (totalRead < 8192 && (n = reader.read(buf, 0, buf.length)) != -1) {
                content.append(buf, 0, n);
                totalRead += n;
            }
            reader.close();
 
            // extract the title
            Matcher matcher = TITLE_TAG.matcher(content);
            if (matcher.find()) {
                 replace any occurrences of whitespace (which may
                 * include line feeds and other uglies) as well
                 * as HTML brackets with a space 
                return matcher.group(1).replaceAll("[\\s\\<>]+", " ").trim();
            }
            else
                return null;
        }
    }*/
    
    /**
     * Loops through response headers until Content-Type is found.
     * @param conn
     * @return ContentType object representing the value of
     * the Content-Type header
     */
    private  ContentType getContentTypeHeader(URLConnection conn) {
        int i = 0;
        boolean moreHeaders = true;
        do {
            String headerName = conn.getHeaderFieldKey(i);
            String headerValue = conn.getHeaderField(i);
            if (headerName != null && headerName.equals("Content-Type"))
                return new ContentType(headerValue);
 
            i++;
            moreHeaders = headerName != null || headerValue != null;
        } while (moreHeaders);
 
        return null;
    }
 
    private  Charset getCharset(ContentType contentType) {
        if (contentType != null && contentType.charsetName != null && Charset.isSupported(contentType.charsetName))
            return Charset.forName(contentType.charsetName);
        else
            return null;
    }
 
    /**
     * Class holds the content type and charset (if present)
     */
    private   class ContentType {
        private  final Pattern CHARSET_HEADER = Pattern.compile("charset=([-_a-zA-Z0-9]+)", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
 
        private String contentType;
        private String charsetName;
        private ContentType(String headerValue) {
            if (headerValue == null)
                throw new IllegalArgumentException("ContentType must be constructed with a not-null headerValue");
            int n = headerValue.indexOf(";");
            if (n != -1) {
                contentType = headerValue.substring(0, n);
                Matcher matcher = CHARSET_HEADER.matcher(headerValue);
                if (matcher.find())
                    charsetName = matcher.group(1);
            }
            else
                contentType = headerValue;
        }
    }
    
    public Map<String, String> getWebsiteContentDetails(String url){
    	Map<String, String> map = new HashMap<>();
    	int linksCount=0, imageCount=0;
    	if(null !=url && !url.isEmpty()) {
    		
    	
    	LOGGER.info("Fetching details for URL:"+url);
    	
    	
    	try {
    		
    		Document doc = Jsoup.connect(url)
    			    .header("Accept-Encoding", "gzip, deflate")
    			    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
    			    .maxBodySize(0)
    			    .timeout(600000)
    			    .get();
    	/////////////////////////////////////////////////////////////////	
    	//Document doc = Jsoup.connect(url).get();
    	String title = html2text(doc.title());
    	Element bodyElement = doc.body();
    	String body = html2text(bodyElement.data());
        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
       
        
        
        if(null != media) {
        	imageCount=media.size();
        print("\nImages: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img")) {
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            }else {
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
            }
        }
        }

        if(null != links) {
        	linksCount=links.size();
        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
        }
    	
    	
    	map.put(ContentDetailEnum.TITLE.toString(), title);
    	map.put(ContentDetailEnum.BODY.toString(), body);
    	map.put(ContentDetailEnum.IMAGE_COUNT.toString(), String.valueOf(imageCount));
    	map.put(ContentDetailEnum.LINK_COUNT.toString(), String.valueOf(linksCount));
    	
    	}catch (Exception e) {
			LOGGER.error("Eception Occured while fetching details for URL:"+url, e);
		}
    	
    	}
    	
    	return map;
    	
    }
    
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }



	@Override
	public UrlData findUrlDataByUrlId(Integer urlId) {
		UrlData urlData = null;
		UrlDataEntity urlDataEntity = urlDataRepository.findByUrlId(urlId);
		if(null != urlDataEntity) {
			urlData = modelMapper.map(urlDataEntity, UrlData.class);
		}
		return urlData;
	}



	@Override
	public List<UrlData> getDateBetweenRecords(String sdate, String edate) {
		Date d1 = convertStringToDate(sdate,DATE_FORMAT);
		Date d2 = convertStringToDate(edate,DATE_FORMAT);
		Calendar c = Calendar.getInstance(); 
		c.setTime(d2); 
		c.add(Calendar.DATE, 1);//add one to get the data of last day also
		d2 = c.getTime();
		
		List<UrlDataEntity> urlDataEntityList  = urlDataRepository.findDateBetweenRecords(d1,d2);
		Type listType = new TypeToken<List<UrlData>>() {}.getType();
		return modelMapper.map(urlDataEntityList, listType);
		
	}



	private Date convertStringToDate(String dateString, String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		Date dateObj=null;
	    try {
			 dateObj = sdf.parse(dateString);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}
		return dateObj;
	}
	
	//for converting html to text
	public static String html2text(String html) {
	    return Jsoup.parse(html).text();
	}
 
 
	
	

}
