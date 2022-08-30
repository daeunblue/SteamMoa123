package com.ssafy.backend.api.service;

import java.util.Map;

public interface IntegratedSearchService {
    Map<String,Object> getIntegratedSearch(String type, String keyword);
}
