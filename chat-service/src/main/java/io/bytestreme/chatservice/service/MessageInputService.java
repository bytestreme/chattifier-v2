package io.bytestreme.chatservice.service;

import io.bytestreme.chatservice.domain.room.ChatRoomsByIdRepository;
import io.bytestreme.chatservice.domain.room.ChatRoomsByIdTable;
import io.bytestreme.data.pulsar.event.input.PulsarMessageInputEvent;
import io.bytestreme.data.pulsar.event.output.PulsarMessageOutputEvent;
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
    private Producer<PulsarMessageOutputEvent> messageOutEventProducer;

    @Autowired
    private ChatRoomsByIdRepository chatRoomsById;

    @Autowired
    private ConnectedUsersService connectedUsersService;

    @Autowired
    private PersistMessagesService persistMessagesService;

    public MessageListener<PulsarMessageInputEvent> handleMessages() {
        return (consumer, message) -> {
            log.info("MessageInputService::handle => message.content: " + message.getValue().getContent());
            var payload = message.getValue();
            chatRoomsById.findByRoomId(payload.getRoom())
                    .map(ChatRoomsByIdTable::getParticipants)
                    .flatMapMany(Flux::fromIterable)
                    .doOnNext((user) -> {
                        log.info("trying to persist messages " + user);
                        persistMessagesService.requestMessagesToPersist(payload);
                    })
                    .filter(x -> !x.equals(message.getValue().getSender()))
                    .map(target -> new PulsarMessageOutputEvent(
                            target,
                            payload.getSender(),
                            payload.getRoom(),
                            payload.getContent(),
                            payload.getTimestamp()
                    ))
                    .flatMap(connectedUsersService::isConnected)
                    .map(messageOutEventProducer::sendAsync)
                    .flatMap(Mono::fromFuture)
                    .subscribe();
        };
    }


}
