package bookcrawlerconsumer.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapingRepository extends JpaRepository<Scraping, Long>{

}
