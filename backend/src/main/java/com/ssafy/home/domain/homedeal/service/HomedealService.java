package com.ssafy.home.domain.homedeal.service;

import com.ssafy.home.domain.homedeal.dto.response.HomedealResponseDto;
import com.ssafy.home.domain.homedeal.entity.HomeDeal;
import com.ssafy.home.domain.homedeal.repository.HomedealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomedealService {
    private final HomedealRepository homedealRepository;

    public List<HomedealResponseDto> getHomedealList(Long homeId) {
        return homedealRepository.findAllByHomeId(homeId).stream()
                .map(HomedealResponseDto::from)
                .toList();
    }

    public HomedealResponseDto getHomedeal(Long homeId, Long homedealId) {
        //TODO : 오류처리 해줘야 함.
        HomeDeal homeDeal = homedealRepository.findById(homedealId).orElse(null);
        if(homeDeal.getHome().getAptCode()!= homeId){
            return null;
        }
        return HomedealResponseDto.from(homeDeal);
    }
}
