package com.dumas.sso.oauth.gateway.service.impl;

import com.dumas.sso.oauth.gateway.service.IpWhiteService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author dumas
 * @desc TODO
 * @date 2023/05/18 23:37
 */
@Service
public class IpWhiteServiceImpl implements IpWhiteService {
    @Override
    public List<String> getIpWhiteList() {
        return Arrays.asList("192.168.1.1");
    }
}
