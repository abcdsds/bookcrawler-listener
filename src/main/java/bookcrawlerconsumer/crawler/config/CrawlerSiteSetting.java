package bookcrawlerconsumer.crawler.config;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class CrawlerSiteSetting {

	private int pages;
	private int lastPages;
	private CrawlerType type;
	private String url;
	private CrawlerSiteType siteType;
}
