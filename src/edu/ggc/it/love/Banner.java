package edu.ggc.it.love;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;

import edu.ggc.it.R;

import android.content.Context;
import android.util.Log;

public class Banner {
	private static final String BANNER_URL = "https://ggc.gabest.usg.edu";
	private static final String TAG = "BannerInterface";
	// for class data
	private static final String DATE_RANGE = "Date Range";
	private static final String TIME = "Time";
	private static final String DAYS = "Days";
	/*private final BannerForm P_GetCrse =
			new BannerForm("/pls/B400/bwskfcls.P_GetCrse", true,
					new NameValuePair[]{
					new BasicNameValuePair("rsts", "dummy"),
					new BasicNameValuePair("crn", "dummy"),
					new BasicNameValuePair("term_in", "201302"),
					new BasicNameValuePair("sel_subj", "dummy"),
					new BasicNameValuePair("sel_day", "dummy"),
					new BasicNameValuePair("sel_schd", "dummy"),
					new BasicNameValuePair("sel_insm", "dummy"),
					new BasicNameValuePair("sel_camp", "dummy"),
					new BasicNameValuePair("sel_levl", "dummy"),
					new BasicNameValuePair("sel_sess", "dummy"),
					new BasicNameValuePair("sel_instr", "dummy"),
					new BasicNameValuePair("sel_attr", "dummy"),
					new BasicNameValuePair("sel_crse", ""),
					new BasicNameValuePair("sel_title", ""),
					new BasicNameValuePair("sel_from_cred", ""),
					new BasicNameValuePair("sel_to_cred", ""),
					new BasicNameValuePair("sel_ptrm", "%"),
					new BasicNameValuePair("begin_hh", "0"),
					new BasicNameValuePair("begin_mi", "0"),
					new BasicNameValuePair("end_hh", "0"),
					new BasicNameValuePair("end_mi", "0"),
					new BasicNameValuePair("begin_ap", "x"),
					new BasicNameValuePair("end_ap", "y"),
					new BasicNameValuePair("path", "1"),
					new BasicNameValuePair("SUB_BTN", "Course Search")
			});*/
	private final BannerForm p_display_courses =
			new BannerForm("/pls/B400/bwckctlg.p_display_courses", false,
					new NameValuePair[]{
					new BasicNameValuePair("term_in", "201302"),
					new BasicNameValuePair("one_subj", ""),
					new BasicNameValuePair("sel_crse_strt", "0000"),
					new BasicNameValuePair("sel_crse_end", "9999"),
					new BasicNameValuePair("sel_subj", ""),
					new BasicNameValuePair("sel_coll", ""),
					new BasicNameValuePair("sel_schd", ""),
					new BasicNameValuePair("sel_divs", ""),
					new BasicNameValuePair("sel_dept", ""),
					new BasicNameValuePair("sel_levl", ""),
					new BasicNameValuePair("sel_attr", ""),
			});
	private final BannerForm p_disp_listcrse =
			new BannerForm("/pls/B400/bwckctlg.p_disp_listcrse", false,
					new NameValuePair[]{
					new BasicNameValuePair("term_in", "201302"),
					new BasicNameValuePair("subj_in", ""),
					new BasicNameValuePair("crse_in", ""),
					new BasicNameValuePair("schd_in", ""),
					new BasicNameValuePair("crn_in", "")
			});
	private Context context;
	
	public Banner(Context context){
		this.context = context;
	}
	
	public Map<String, String> getCourseNumbers(String subject){
		p_display_courses.set("one_subj", subject);
		String response = p_display_courses.request();
		Map<String, String> courses = new HashMap<String, String>();
		List<String> titles = scrapeInner(response, "A", "/pls/B400/bwckctlg.p_disp_course_detail?");
		
		for (int i = 0; i < titles.size(); i++){
			String href = titles.get(i);
			int numStart = href.indexOf(" ")+1;
			int numEnd = href.indexOf(" ", numStart);
			String number = href.substring(numStart, numEnd);
			int nameStart = href.indexOf(" ", numEnd+1)+1;
			String name = href.substring(nameStart);
			courses.put(number, name);
		}
		
		return courses;
	}
	
	public Map<String, String> getSections(String subject, String course){
		final String separator = " - ";
		
		p_disp_listcrse.set("subj_in", subject);
		p_disp_listcrse.set("crse_in", course);
		String response = p_disp_listcrse.request();
		Map<String, String> sections = new HashMap<String, String>();
		List<String> titles = scrapeInner(response, "A", "/pls/B400/bwckschd.p_disp_detail_sched?");
		
		for (int i = 0; i < titles.size(); i++){
			String href = titles.get(i);
			int crnStart = href.indexOf(separator)+separator.length();
			int crnEnd = href.indexOf(separator, crnStart);
			String crn = href.substring(crnStart, crnEnd);
			int sectStart = href.indexOf(separator, crnEnd+separator.length())+separator.length();
			String sectionId = href.substring(sectStart);
			sections.put(sectionId, crn);
		}
		
		return sections;
	}
	
	public Course getCourse(String subject, String course, String crn){
		final String sectionSummary = "This layout table is used to present the sections found";
		final String summary = "This table lists the scheduled meeting times and assigned instructors for this class..";
		final String separator = " - ";
		
		Course result = null;
		
		p_disp_listcrse.set("subj_in", subject);
		p_disp_listcrse.set("crse_in", course);
		String response = p_disp_listcrse.request();
		// filter for just this section
		List<String> sectionTables = scrapeInner(response, "TABLE", sectionSummary);
		String sectionTable = null;
		for (String table: sectionTables){
			if (table.indexOf(crn) != -1){
				sectionTable = table;
				break;
			}
		}
		
		List<Map<String, String>> courseData = scrapeTable(sectionTable, summary);
		Map<String, List<Course.TimeRange>> classTimes = new HashMap<String, List<Course.TimeRange>>();
		
		for (Map<String, String> meeting: courseData){
			String dateRange = meeting.get(DATE_RANGE);
			String[] dates = dateRange.split(separator);
		}
		
		return result;
	}
	
	private List<Map<String, String>> scrapeTable(String html, String... matches){
		List<Map<String, String>> results = new ArrayList<Map<String, String>>();
		List<String> headers = null;
		
		// TODO: this method assumes uppercase tag names. This is what Banner uses,
		// but it could change in the future
		String tableData = scrapeInner(html, "TABLE", matches).get(0);
		headers = scrapeInner(tableData, "TH");
		
		Map<String, String> tableValues = new HashMap<String, String>(headers.size());
		List<String> data = scrapeInner(tableData, "TD");
		
		for (int i = 0; i < data.size(); i++){
			int headerIndex = i % tableValues.size();
			if (headerIndex == 0 && i != 0){
				results.add(tableValues);
				tableValues = new HashMap<String, String>(headers.size());
			}
			
			tableValues.put(headers.get(headerIndex), data.get(i));
		}
		
		results.add(tableValues);
		
		return results;
	}
	
	private List<String> scrapeInner(String html, String tag, String... matches){
		List<String> results = new ArrayList<String>();
		String matched = null, line = null;
		int begin = 0, end = -1;
		boolean inTag = false;
		
		searchHTML:
		do{
			begin = html.indexOf("<" + tag, begin);
			if (begin != -1){
				end = html.indexOf(">", begin);
				if (end != -1){
					matched = html.substring(begin, end);
					begin = end+1;
					
					for (String match: matches){
						if (matched.indexOf(match) == -1)
							continue searchHTML;
					}
					
					// this tag matches; grab the inner HTML
					end = html.indexOf("</" + tag, begin);
					line = html.substring(begin, end);
					results.add(line);
					begin = end;
				} else{
					begin++;
				}
			}
		} while (begin != -1);
		
		return results;
	}
	
	/*private List<String> scrapeAttr(String html, String tag, String attr,
			String... matches){
		List<String> results = new ArrayList<String>();
		String matched = null, line = null;
		int begin = -1, end = -1;
		
		// TODO: this will skip multiple matching tags on the same line
		try{
			searchHTML:
			while ((line = html.readLine()) != null){
				if (matched == null){
					begin = line.indexOf("<" + tag);
					if (begin != -1){
						end = line.indexOf(">", begin);
						if (end != -1)
							matched = line.substring(begin, end);
						else
							matched = line.substring(begin);
					}
				} else{
					end = line.indexOf(">");
					if (end == -1)
						matched += line;
					else
						matched += line.substring(0, end);
				}
				
				if (begin != -1 && end != -1){
					// we found a complete tag
					begin = -1;
					end = -1;
					
					for (String match: matches){
						if (matched.indexOf(match) == -1){
							matched = null;
							continue searchHTML;
						}
					}
					
					// it matched the requirements; grab the attribute value
					int attrLoc = matched.indexOf(attr);
					if (attrLoc == -1){
						matched = null;
						continue;
					}
					int eqLoc = matched.indexOf("=", attrLoc);
					if (eqLoc == -1){
						matched = null;
						Log.w(TAG, "Malformed attribute: " + matched);
						continue;
					}
					// attributes can start with single quotes or double quotes, so search for both;
					// whichever is first after the equals sign is the one they're using
					int sqLoc = matched.indexOf("'", eqLoc);
					int dqLoc = matched.indexOf("\"", eqLoc);
					int valStart = 0;
					
					if (sqLoc < dqLoc && sqLoc != -1){
						valStart = sqLoc+1;
					} else if (dqLoc < sqLoc && dqLoc != -1){
						valStart = dqLoc+1;
					} else{
						matched = null;
						Log.w(TAG, "Malformed attribute: " + matched);
						continue;
					}
					
					int valEnd = matched.indexOf("'", valStart)-1;
					String value = matched.substring(valStart, valEnd);
					results.add(value);
				}
			}
		} catch (IOException ioe){
			Log.e(TAG, "Failed to read response stream", ioe);
			return null;
		}
		
		return results;
	}*/
	
	private class BannerForm{
		private String path;
		private NameValuePair[] defaultParms;
		private Map<String, String> currentParms;
		private boolean isPOST;
		
		public BannerForm(String path, boolean isPOST, NameValuePair... parms){
			this.path = path;
			this.isPOST = isPOST;
			defaultParms = parms;
			currentParms = new HashMap<String, String>();
			for (NameValuePair parm: parms)
				currentParms.put(parm.getName(), parm.getValue());
		}
		
		public boolean set(String name, String value){
			if (!currentParms.containsKey(name))
				return false;
			currentParms.put(name, value);
			return true;
		}
		
		public boolean set(NameValuePair parm){
			if (!currentParms.containsKey(parm.getName()))
				return false;
			currentParms.put(parm.getName(), parm.getValue());
			return true;
		}
		
		public String request(){
			HttpClient client = new DefaultHttpClient();//BannerHttpClient(context);
			HttpUriRequest request = null;
			BufferedReader reader = null;
			StringBuffer ret = new StringBuffer();
			
			try{
				if (isPOST){
					HttpPost post = new HttpPost(BANNER_URL + path);
					List<NameValuePair> parms = new ArrayList<NameValuePair>(defaultParms.length);
					for (Map.Entry<String, String> parm: currentParms.entrySet())
						parms.add(new BasicNameValuePair(parm.getKey(), parm.getValue()));
					post.setEntity(new UrlEncodedFormEntity(parms));
					request = post;
				} else{
					String requestPath = BANNER_URL + path + "?";
					for (Map.Entry<String, String> parm: currentParms.entrySet())
						requestPath += parm.getKey() + "=" + parm.getValue() + "&";
					HttpGet get = new HttpGet(requestPath);
					request = get;
				}
				HttpResponse response = null;
				response = client.execute(request);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				Header encoding = entity.getContentEncoding();
				reader = new BufferedReader(new InputStreamReader(stream,
						(encoding == null? "iso-8859-1": encoding.getValue())));
				for (String line = reader.readLine(); line != null; line = reader.readLine())
					ret.append(line);
				reader.close();
			} catch (UnsupportedEncodingException uee){
				Log.e(TAG, "Failed to send Banner request", uee);
			} catch (ClientProtocolException cpe) {
				Log.e(TAG, "Failed to send Banner request", cpe);
			} catch (IOException ioe) {
				Log.e(TAG, "Failed to send Banner request", ioe);
			} finally{
				for (NameValuePair parm: defaultParms)
					currentParms.put(parm.getName(), parm.getValue());
			}
			
			return ret.toString();
		}
	}
}
