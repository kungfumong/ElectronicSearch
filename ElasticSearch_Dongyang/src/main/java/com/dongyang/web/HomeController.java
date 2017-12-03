package com.dongyang.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	private HttpURLConnection conn;
	private StringBuilder builder;
	private BufferedReader reader;
	private JSONObject json;
	private JSONArray jsonArr;
	String key = "07dafbd9b50bc4ddc340a453f1476f58";
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);

		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("serverTime", formattedDate);

		return "home";
	}
	
	
	@RequestMapping(value = "/asdf", method = sRequestMethod.GET)
	public String asdf(Locale locale, Model model) throws IOException {

		Header[] headers = { new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
				new BasicHeader("Authorization", "Basic YWRtaW46ZG9uZ3lhbmc=") };
		
		RestClient r = RestClient
				.builder(new HttpHost("dfe676fded9f79feafed7b9ac3eb4149.ap-northeast-1.aws.found.io",9243,"https"))
				.setDefaultHeaders(headers).build();
		
		Map<String, String> paramMap = Collections.singletonMap("pretty", "true");

		Response response = r.performRequest(
		    "GET",
		    "/",
		    paramMap
		);

		System.out.println(response.toString());

		return "home";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(Locale locale, Model model) {

		String line;
		try {
			URL url = new URL("http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieList.json?key="
					+ key + "&itemPerPage=5");
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			conn.setDoOutput(false);
			conn.setDoInput(true);
			conn.setRequestProperty("Content-Type", "application/json");
			// BufferedOutputStream out = new BufferedOutputStream(conn.getOutputStream());

			builder = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			System.out.println(builder);
			json = (JSONObject) JSONValue.parse(builder.toString());
			json = (JSONObject) json.get("movieListResult");
			jsonArr = (JSONArray) json.get("movieList");
			json.clear();

			for (int i = 0; i < jsonArr.size(); i++) {
				json = (JSONObject) jsonArr.get(i);
				System.out.println(json.get("movieCd"));
				System.out.println(json.get("prdtYear"));
				System.out.println(json.get("genreAlt"));
				System.out.println(json.get("repGenreNm"));
				System.out.println(json.get("directors"));
			}
			json.clear();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "list";
	}
}
