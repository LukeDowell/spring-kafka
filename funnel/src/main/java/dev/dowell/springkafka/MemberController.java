package dev.dowell.springkafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MemberController {

    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;

    public MemberController(StreamBridge streamBridge, ObjectMapper objectMapper) {
        this.streamBridge = streamBridge;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/member")
    public ResponseEntity<Void> newMember(@RequestBody NewMember newMember) {
        try {
            streamBridge.send("receiveMemberInformation-in-0", objectMapper.writeValueAsString(newMember));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
