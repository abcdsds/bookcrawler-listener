package bookcrawlerconsumer.crawler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import bookcrawlerconsumer.common.message.ScrapingMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class KafkaCrawlerListener {


	@KafkaListener(topics = "crawler-topic", containerFactory = "crawlerKafkaListenerContainerFactory")
	public void listenMessage(List<byte[]> datas) {
		
		List<ScrapingMessage> messages = new ArrayList<>();
		
		log.info("null check : {}", datas.get(0) == null);
		log.info("type check : {}", datas.get(0) instanceof byte[]);
		
		for (byte[] data : datas) {
			messages.add((ScrapingMessage)SerializationUtils.deserialize(data));
		}
		
		

		
	}

}
