package bookcrawlerconsumer.common.message;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ScrapingMessage implements Serializable {


	private String domain;
	
	private String link;

	@Enumerated(EnumType.STRING)
	private SiteType type;
	
}
