package com.myanimelist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.myanimelist.rest.dto.AnimeListResponse;
import com.myanimelist.rest.dto.AnimeResponse;
import com.myanimelist.rest.dto.AnimeResponse.Anime;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JikanApiServiceImpl implements JikanApiService {

	@Autowired
	private Environment env;

	private RestTemplate restTemplate = new RestTemplate();

	@Override
	public AnimeListResponse findSearched(String title, String genres, int pageId) {
		String url = 
				env.getProperty("find.all") + 
				env.getProperty("param.page") + pageId + 
				env.getProperty("param.limit") + 
				env.getProperty("param.order_by.score") + 
				env.getProperty("param.sort.desc");

		if (title != null && !title.isBlank()) {
			url += env.getProperty("param.title") + title;
		}

		if (genres != null && !genres.isBlank()) {
			url += env.getProperty("param.genres") + genres;
		}

		log.info(url);

		AnimeListResponse animeListResponse = restTemplate.getForObject(url, AnimeListResponse.class);

		return animeListResponse;
	}

	@Override
	public AnimeListResponse findTop(int pageId) {
		String url = 
				env.getProperty("find.top") + 
				env.getProperty("param.page") + pageId + 
				env.getProperty("param.limit");

		log.info(url);

		AnimeListResponse animeListResponse = restTemplate.getForObject(url, AnimeListResponse.class);

		return animeListResponse;
	}

	@Override
	public Anime findAnime(int animeId) {
		String url = 
				env.getProperty("find.id") + animeId;

		log.info(url);

		AnimeResponse animeResponse = restTemplate.getForObject(url, AnimeResponse.class);

		return animeResponse.getAnime();
	}

	@Override
	public Anime findRandomAnime() {
		String url = 
				env.getProperty("find.rand");

		log.info(url);

		AnimeResponse animeResponse = restTemplate.getForObject(url, AnimeResponse.class);

		return animeResponse.getAnime();
	}
}
