package io.bytestreme.chatservice.service;

import io.bytestreme.chatservice.domain.room.ChatRoomsByIdRepository;
import io.bytestreme.chatservice.domain.room.ChatRoomsByIdTable;
import io.bytestreme.data.pulsar.event.PulsarMessageInputEvent;
import io.bytestreme.data.pulsar.event.PulsarMessageOutEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MessageInputService {

    @Autowired
    private Producer<PulsarMessageOutEvent> messageOutEventProducer;

    @Autowired
    private ChatRoomsByIdRepository chatRoomsById;

    @Autowired
    private ConnectedUsersService connectedUsersService;

    public MessageListener<PulsarMessageInputEvent> handleMessages() {
        return (consumer, message) -> {
            log.info("MessageInputService::handle => message.content: " + message.getValue().getContent());
            var payload = message.getValue();
            chatRoomsById.findByRoomId(payload.getRoom())
                    .map(ChatRoomsByIdTable::getParticipants)
                    .flatMapMany(Flux::fromIterable)
                    .filter(connectedUsersService::isUserConnected)
                    .flatMap(
                            userId -> Mono.fromFuture(
                                    messageOutEventProducer.sendAsync(
                                            new PulsarMessageOutEvent(
                                                    userId,
                                                    payload.getSender(),
                                                    payload.getRoom(),
                                                    payload.getContent(),
                                                    payload.getTimestamp()
                                            )
                                    )
                            )
                    )
                    .doOnNext((e) -> {
                        log.info("messageId " + e.toString());
                    })
                    .log("MessageInputService::handle reactor logger")
                    .subscribe();
        };
    }


}
