package bookcrawlerconsumer.crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.SerializationUtils;
import bookcrawlerconsumer.common.message.ScrapingMessage;
import bookcrawlerconsumer.domain.Scraping;
import bookcrawlerconsumer.domain.ScrapingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class KafkaCrawlerListener {

	private final ScrapingRepository scrapingRepository;
	

	@KafkaListener(topics = "crawler-topic", containerFactory = "crawlerKafkaListenerContainerFactory")
	public void listenMessage(List<byte[]> datas) throws IOException, InterruptedException {
		
		List<ScrapingMessage> messages = new ArrayList<>();
		List<Scraping> scrapings = new ArrayList<>();
		
		log.info("null check : {}", datas.get(0) == null);
		log.info("type check : {}", datas.get(0) instanceof byte[]);
		
		for (byte[] data : datas) {
			messages.add((ScrapingMessage)SerializationUtils.deserialize(data));
		}
		
		for (ScrapingMessage scrapingMessage : messages) {
			Scraping scraping = Scraping.builder().type(scrapingMessage.getType()).domain(scrapingMessage.getDomain()).link(scrapingMessage.getLink()).build();
			scraping.parseStart();
			scrapings.add(scraping);
			Thread.sleep(1500);
		}

		scrapingRepository.saveAll(scrapings);

	}

}
