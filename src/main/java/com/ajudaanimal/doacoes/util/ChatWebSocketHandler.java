package com.ajudaanimal.doacoes.util;

import com.ajudaanimal.doacoes.entity.chat.Mensagem;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.service.impl.ConversaService;
import com.ajudaanimal.doacoes.service.TokenService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ConversaService conversationService;
    @Autowired
    private UsuarioRepository userRepository;

    private final ObjectMapper mapper;

    public ChatWebSocketHandler() {
        ObjectMapper m = new ObjectMapper();
        // support java.time types (Instant, LocalDateTime, etc.)
        m.registerModule(new JavaTimeModule());
        // serialize Instants/JavaTime as ISO-8601 strings rather than timestamps
        m.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.mapper = m;
    }

    // map userId -> sessions (support multiple tabs)
    private final Map<String, List<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = extractToken(session.getUri());
        String userId = tokenService.extractUserId(token);
        if (userId == null){
            session.close(); return;
        }
        sessions.computeIfAbsent(userId, k-> Collections.synchronizedList(new ArrayList<>())).add(session);
        // send list
        List<Map<String,Object>> list = conversationService.listConversasForUser(userId);
        Map<String,Object> out = Map.of("type","list","conversations", list);
        session.sendMessage(new TextMessage(mapper.writeValueAsString(out)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        Map<String,Object> in = mapper.readValue(payload, new TypeReference<Map<String,Object>>(){});
        String type = (String) in.get("type");
        String token = extractToken(session.getUri());
        String userId = tokenService.extractUserId(token);
        if (userId == null){ session.close(); return; }
        switch (type){
            case "list":
                List<Map<String,Object>> list = conversationService.listConversasForUser(userId);
                session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("type","list","conversations",list))));
                break;
            case "open":
                String withUserId = (String) in.get("withUserId");
                // donationId expected to be a numeric id referencing Usuario.id or null
                Object donationObj = in.get("donationId");
                Long donationIdLong = null;
                if (donationObj != null) {
                    if (donationObj instanceof Number) donationIdLong = ((Number) donationObj).longValue();
                    else { try { donationIdLong = Long.parseLong(donationObj.toString()); } catch(Exception ignored){} }
                }
                var convo = conversationService.openOrCreateConversa(userId, withUserId, donationIdLong);
                session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("type","opened","conversation", Map.of("id", convo.getId(), "donationId", convo.getDonationId())))));
                break;
            case "history":
                String conversationId = (String) in.get("conversationId");
                var msgs = conversationService.getMessages(conversationId);
                List<Map<String,Object>> dto = new ArrayList<>();
                for (Mensagem m : msgs){
                    Map<String,Object> item = new HashMap<>();
                    item.put("id", m.getId());
                    item.put("conversationId", conversationId);
                    item.put("from", m.getFromUserId());
                    item.put("fromName", resolveUserName(m.getFromUserId()));
                    item.put("text", m.getText());
                    item.put("ts", m.getTs().toEpochMilli());
                    item.put("fromMe", m.getFromUserId().equals(userId));
                    dto.add(item);
                }
                session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("type","history","conversationId", conversationId, "messages", dto))));
                break;
            case "message":
                String convId = (String) in.get("conversationId");
                String text = (String) in.get("text");
                Mensagem m = conversationService.createMessage(convId, userId, text);
                Map<String,Object> messageDto = Map.of(
                        "id", m.getId(),
                        "conversationId", convId,
                        "from", m.getFromUserId(),
                        "fromName", resolveUserName(m.getFromUserId()),
                        "text", m.getText(),
                        "ts", m.getTs().toEpochMilli(),
                        "fromMe", true
                );

                // send back to sender immediately
                session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("type","message","message", messageDto))));

                // notify participants
                var participantIds = conversationService.getParticipanteUserIds(convId);
                for (String pid : participantIds){
                    List<WebSocketSession> listSessions = sessions.get(pid);
                    if (listSessions!=null){
                        for (WebSocketSession s : listSessions){
                            boolean isSender = pid.equals(userId);
                            Map<String,Object> sendObj = Map.of("type","message","message", Map.of(
                                    "id", m.getId(),
                                    "conversationId", convId,
                                    "from", m.getFromUserId(),
                                    "fromName", resolveUserName(m.getFromUserId()),
                                    "text", m.getText(),
                                    "ts", m.getTs().toEpochMilli(),
                                    "fromMe", isSender
                            ));
                            try { s.sendMessage(new TextMessage(mapper.writeValueAsString(sendObj))); } catch(IOException e){ /* ignore */ }
                        }
                    }
                }
                break;
            case "read":
                String conversationIdRead = (String) in.get("conversationId");
                conversationService.markAsRead(conversationIdRead, userId);
                break;
            default:
                session.sendMessage(new TextMessage(mapper.writeValueAsString(Map.of("type","error","message","unknown type"))));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        // remove from sessions map
        sessions.values().forEach(list -> list.removeIf(s -> s.getId().equals(session.getId())));
    }

    private String extractToken(URI uri){
        if (uri==null) return null;
        String q = uri.getQuery();
        if (q==null) return null;
        for (String part : q.split("&")){
            if (part.startsWith("token=")) return part.substring(6);
        }
        return null;
    }

    // helper to resolve a user id (stored as String in messages) to the Usuario.nome, falling back to the raw id
    private String resolveUserName(String idStr){
        if (idStr == null) return null;
        try {
            Long id = Long.parseLong(idStr);
            Optional<Usuario> u = userRepository.findById(id);
            return u.map(Usuario::getNome).orElse(idStr);
        } catch (NumberFormatException e){
            return idStr;
        }
    }
}
