package com.ajudaanimal.doacoes.service.impl;

import com.ajudaanimal.doacoes.entity.chat.Conversa;
import com.ajudaanimal.doacoes.entity.chat.Mensagem;
import com.ajudaanimal.doacoes.entity.chat.Participante;
import com.ajudaanimal.doacoes.repository.chat.ConversaRepository;
import com.ajudaanimal.doacoes.repository.chat.MensagemRepository;
import com.ajudaanimal.doacoes.repository.chat.ParticipanteRepository;
import com.ajudaanimal.doacoes.repository.usuario_ong.UsuarioRepository;
import com.ajudaanimal.doacoes.entity.usuario_ong.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConversaService {

    @Autowired
    private ConversaRepository convoRepo;
    @Autowired
    private ParticipanteRepository ParticipanteRepo;
    @Autowired
    private MensagemRepository messageRepo;
    @Autowired
    private UsuarioRepository userRepo;

    @Transactional(readOnly = true)
    public List<Map<String,Object>> listConversasForUser(String userId){
        List<Participante> parts = ParticipanteRepo.findByUserId(userId);
        List<Map<String,Object>> out = new ArrayList<>();
        for (Participante p : parts){
            Conversa c = p.getConversation();
            Map<String,Object> m = new HashMap<>();
            m.put("id", c.getId());
            // pick a name: other Participante
            Optional<Participante> other = c.getParticipants().stream().filter(x->!x.getUserId().equals(userId)).findFirst();
            String name = other.map(x-> resolveUserName(x.getUserId())).orElse("Conversa");
            m.put("name", name);
            m.put("donationId", c.getDonationId());
            m.put("lastMessage", c.getLastMessage());
            m.put("lastMessageTs", c.getLastMessageTs());
            m.put("unreadCount", p.getUnreadCount());
            out.add(m);
        }
        // sort by lastMessageTs desc
        out.sort((a,b)->{
            Instant ta = a.get("lastMessageTs") == null ? Instant.EPOCH : (Instant)a.get("lastMessageTs");
            Instant tb = b.get("lastMessageTs") == null ? Instant.EPOCH : (Instant)b.get("lastMessageTs");
            return tb.compareTo(ta);
        });
        return out;
    }

    @Transactional
    public Conversa openOrCreateConversa(String userA, String userB, Long donationId){
        // naive: search all Conversas of userA and check Participante userB and donationId
        List<Participante> parts = ParticipanteRepo.findByUserId(userA);
        for (Participante p : parts){
            Conversa c = p.getConversation();
            boolean hasB = c.getParticipants().stream().anyMatch(x->x.getUserId().equals(userB));
            boolean sameDonation = Objects.equals(c.getDonationId(), donationId);
            if (hasB && sameDonation) return c;
        }
        String id = UUID.randomUUID().toString();
        Conversa c = new Conversa(id);
        c.setDonationId(donationId);
        c.setLastMessage(null);
        c.setLastMessageTs(Instant.now());
        convoRepo.save(c);
        Participante pa = new Participante(c, userA);
        Participante pb = new Participante(c, userB);
        ParticipanteRepo.save(pa);
        ParticipanteRepo.save(pb);
        c.getParticipants().add(pa); c.getParticipants().add(pb);
        convoRepo.save(c);
        return c;
    }

    @Transactional
    public Mensagem createMessage(String ConversaId, String fromUserId, String text){
        Conversa c = convoRepo.findById(ConversaId).orElseThrow(() -> new RuntimeException("Conversa not found"));
        Mensagem m = new Mensagem(UUID.randomUUID().toString());
        m.setConversation(c);
        m.setFromUserId(fromUserId);
        m.setText(text);
        m.setTs(Instant.now());
        messageRepo.save(m);
        c.setLastMessage(text);
        c.setLastMessageTs(m.getTs());
        convoRepo.save(c);
        // update unread counts for other Participantes
        for (Participante p : c.getParticipants()){
            if (!p.getUserId().equals(fromUserId)){
                p.setUnreadCount(p.getUnreadCount() + 1);
                ParticipanteRepo.save(p);
            }
        }
        return m;
    }

    public List<Mensagem> getMessages(String ConversaId){
        return messageRepo.findByConversa_IdOrderByTsAsc(ConversaId);
    }

    @Transactional
    public void markAsRead(String ConversaId, String userId){
        Conversa c = convoRepo.findById(ConversaId).orElse(null);
        if (c==null) return;
        for (Participante p : c.getParticipants()){
            if (p.getUserId().equals(userId)){
                p.setUnreadCount(0);
                ParticipanteRepo.save(p);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<String> getParticipanteUserIds(String ConversaId){
        // query participants directly to avoid initializing the lazy collection on Conversa
        List<Participante> list = ParticipanteRepo.findByConversa_Id(ConversaId);
        if (list == null || list.isEmpty()) return Collections.emptyList();
        return list.stream().map(Participante::getUserId).collect(Collectors.toList());
    }

    // helper to resolve a userId (stored as String) to name using UsuarioRepository
    private String resolveUserName(String idStr){
        if (idStr == null) return null;
        try {
            Long id = Long.parseLong(idStr);
            Optional<Usuario> u = userRepo.findById(id);
            return u.map(Usuario::getNome).orElse(idStr);
        } catch (NumberFormatException e){
            return idStr;
        }
    }
}
