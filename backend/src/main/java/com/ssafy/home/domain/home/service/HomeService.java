package com.ssafy.home.domain.home.service;

import com.ssafy.home.domain.home.dto.request.HomeParam;
import com.ssafy.home.domain.home.dto.response.HomeResponseDto;
import com.ssafy.home.domain.home.entity.Home;
import com.ssafy.home.domain.home.repository.HomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeService {
    private final HomeRepository homeRepository;

    @Cacheable(cacheNames = "home", key = "1", cacheManager = "cacheManager")
    public List<HomeResponseDto> getHomeList(HomeParam homeParam) {
        return homeRepository.findAll().stream()
                .filter(home ->
                        (homeParam.getLongitude() == null || Math.abs(Float.parseFloat(home.getLng()) - homeParam.getLongitude()) <= 0.01f)
                        && (homeParam.getLatitude() == null || Math.abs(Float.parseFloat(home.getLat()) - homeParam.getLatitude()) <= 0.01f)
                )
                .map(HomeResponseDto::from)
                .toList();
    }

    public HomeResponseDto getHome(Long homeId) {
        Home home = homeRepository.findById(homeId).orElseThrow(RuntimeException::new);
        return HomeResponseDto.from(home);
    }
}
