package io.bytestreme.messageservice.service;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.bytestreme.data.pulsar.event.input.PulsarMessageInputEvent;
import io.bytestreme.data.pulsar.event.input.PulsarMessagePersistRequestInputEvent;
import io.bytestreme.messageservice.domain.MessagesByRoomRepository;
import io.bytestreme.messageservice.domain.MessagesByRoomTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePersistingService {

    private final MessagesByRoomRepository byRoomRepository;

    public MessageListener<PulsarMessagePersistRequestInputEvent> handleMessages() {
        log.info("handling trying to persist");
        return (consumer, message) -> {
            var payload = message.getValue();
            var persistedMessage = new MessagesByRoomTable();
            persistedMessage.setRoomId(payload.getRoom());
            persistedMessage.setSender(payload.getSender());
            persistedMessage.setData(payload.getContent());
            persistedMessage.setMessageId(Uuids.timeBased());
            persistedMessage.setTimestamp(Instant.ofEpochMilli(payload.getTimestamp()));
            persistedMessage.setSystemMessage(false);
            byRoomRepository.save(persistedMessage)
                    .doOnNext(s -> log.info("persisted: " + s.getMessageId()))
                    .subscribe();
        };
    }
}
