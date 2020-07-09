package bookcrawlerconsumer.domain;


import java.io.IOException;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import bookcrawlerconsumer.common.message.SiteType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode(of = "id")
@Entity @AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter 
public class Scraping {

	@Id @GeneratedValue
	private Long id;
	
	private String domain;
	
	private String link;

	@Enumerated(EnumType.STRING)
	private SiteType type;

	private String title;

	private String description;

	private Integer episodeCounts;

	private String author;
	
	private Boolean completed;
	
	@Builder.Default
	private String regex = "\\D+";
	

	public void init(String domain, SiteType type , String link) {
		this.domain = domain;
		this.type = type;
		this.link = link;
	}

	public void parseStart() throws IOException {

		if (this.type == null) {
			log.info("type이 null 입니다.");
		}
		
		Document doc = Jsoup.connect(link).get();
		
		switch (this.type) {
			case MOOTOON:
				parseMootoon(doc);
				break;
			case MUNPIA:
				parseMunpia(doc);
				break;
			case RIDIBOOKS:
				parseRidiBooks(doc);
				break;
		}
	}

	public void parseMootoon(Document doc) {

		this.title = doc.select("h2.fiction_title").text();
		this.description = doc.select("h3.fiction_summary").text();
		this.episodeCounts = Integer.valueOf(doc.select("div.t_left_total_num").text().replaceAll(regex, "").trim());
		this.author = doc.select("h2.fiction_writer").text().replaceAll("작가 : ", "").trim();
		this.completed = doc.select("div.t_left_total").get(1).text().contains("완결");
		
	}
	
	public void parseMunpia(Document doc) {
		
		this.title = doc.select("div.detail-box").select("h2").select("a").text().replaceAll("선독점 ", "");
		this.author = doc.select("div.detail-box").select("dl.meta-author").select("a").text().trim();
		this.episodeCounts = Integer.valueOf(doc.select("div.detail-box").select("dl.meta-etc").get(1).select("dd").get(0).text().replaceAll(regex, "").trim());
		this.description = doc.select("div#STORY-BOX").select("p.story").text();
		this.completed = !doc.select("span.xui-finish").isEmpty();

	}
	
	public void parseRidiBooks(Document doc) {
		
		this.title = doc.select("h3.info_title_wrap").text();
		this.description = doc.select("p.introduce_paragraph").text();
		this.episodeCounts = Integer.valueOf(doc.select("span.book_count").text().replaceAll(regex, "").trim());
		this.author = doc.select("a.js_author_detail_link").text().trim();
		this.completed = !doc.select("span.complete").isEmpty();

	}
}
