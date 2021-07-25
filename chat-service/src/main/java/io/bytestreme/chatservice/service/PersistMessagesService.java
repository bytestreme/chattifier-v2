package io.bytestreme.chatservice.service;

import io.bytestreme.data.pulsar.event.input.PulsarMessageInputEvent;
import io.bytestreme.data.pulsar.event.input.PulsarMessagePersistRequestInputEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersistMessagesService {

    private final Producer<PulsarMessagePersistRequestInputEvent> messagePersistProducer;

    public Mono<MessageId> requestMessagesToPersist(PulsarMessageInputEvent inputEvent) {
        return Mono.fromFuture(messagePersistProducer.sendAsync(
                new PulsarMessagePersistRequestInputEvent(
                        inputEvent.getSender(),
                        inputEvent.getRoom(),
                        inputEvent.getContent(),
                        inputEvent.getTimestamp()
                )
        ));
    }

}
