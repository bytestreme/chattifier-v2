package io.bytestreme.chatservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class ConnectedUsersService {

    public boolean isUserConnected(UUID uuid) {
        log.info("ConnectedUsersService::isUserConnected => uuid: " + uuid);
        return true;
    }

}
