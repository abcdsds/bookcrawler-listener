package bookcrawlerconsumer.scraping;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class ScrapingTest {

	private String regex = "\\D+";
	
	@Test
	void munpiaScrapingDataTest() throws IOException {
		
		String url = "https://novel.munpia.com/191149";
		
		Document doc = Jsoup.connect(url).get();
		
		String title = doc.select("div.detail-box").select("h2").select("a").text().replaceAll("선독점 ", "");
		String author = doc.select("div.detail-box").select("dl.meta-author").select("a").text().trim();
		Integer episodeCounts = Integer.valueOf(doc.select("div.detail-box").select("dl.meta-etc").get(1).select("dd").get(0).text().replaceAll(regex, "").trim());
		String description = doc.select("div#STORY-BOX").select("p.story").text();
		Boolean complete = !doc.select("span.xui-finish").isEmpty();
		
		assertNotNull(title);
		assertNotNull(description);
		assertNotNull(episodeCounts);
		assertNotNull(author);
		assertNotNull(complete);
		
		log.info("title = {} " , title);
		log.info("author = {} " , author);
		log.info("episodeCounts = {} " , episodeCounts);
		log.info("description = {} " , description);
		log.info("complete = {} " , complete);
		
	}
	
	@Test
	void mootoonScrapingDataTest() throws IOException {
		
		String url = "https://www.mootoon.co.kr/nov/nov_list.mg?tcode=h6ub";
		
		Document doc = Jsoup.connect(url).get();
		
		String title = doc.select("h2.fiction_title").text();
		String description = doc.select("h3.fiction_summary").text();
		Integer episodeCounts = Integer.valueOf(doc.select("div.t_left_total_num").text().replaceAll(regex, "").trim());
		String author = doc.select("h2.fiction_writer").text().replaceAll("작가 : ", "").trim();
		Boolean complete = doc.select("div.t_left_total").get(1).text().contains("완결");
		

		assertNotNull(title);
		assertNotNull(description);
		assertNotNull(episodeCounts);
		assertNotNull(author);
		assertNotNull(complete);
		
		log.info("title = {} " , title);
		log.info("author = {} " , author);
		log.info("episodeCounts = {} " , episodeCounts);
		log.info("description = {} " , description);
		log.info("complete = {} " , complete);
		
	}
	
	@Test
	void ridibooksScrapingDataTest() throws IOException {
		
		String url = "https://ridibooks.com/books/3214013466";
		
		Document doc = Jsoup.connect(url).get();
		
		String title = doc.select("h3.info_title_wrap").text();
		String description = doc.select("p.introduce_paragraph").text();
		Integer episodeCounts = Integer.valueOf(doc.select("span.book_count").text().replaceAll(regex, "").trim());
		String author = doc.select("a.js_author_detail_link").text().trim();
		Boolean complete = !doc.select("span.complete").isEmpty();
		
		assertNotNull(title);
		assertNotNull(description);
		assertNotNull(episodeCounts);
		assertNotNull(author);
		assertNotNull(complete);
		
		log.info("title = {} " , title);
		log.info("author = {} " , author);
		log.info("episodeCounts = {} " , episodeCounts);
		log.info("description = {} " , description);
		log.info("complete = {} " , complete);
	}
}
