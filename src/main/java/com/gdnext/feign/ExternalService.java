package com.gdnext.feign;

import com.gdnext.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "externalServiceClient", url = "${external.api.url}")
public interface ExternalService {

    @PostMapping("/submit")
    String callExternalService(User user);
}
