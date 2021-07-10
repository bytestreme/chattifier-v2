package io.bytestreme.cloudgateway.fallback;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class FallBackMethodController {

    @GetMapping("/userServiceFallBack")
    public Map<String, String> userServiceFallBackMethod() {
        return Collections.emptyMap();
    }

}
