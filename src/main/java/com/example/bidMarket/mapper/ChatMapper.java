package com.example.bidMarket.mapper;

import com.example.bidMarket.dto.ChatMessageDto;
import com.example.bidMarket.dto.ChatRoomDto;
import com.example.bidMarket.dto.UserDto;
import com.example.bidMarket.model.ChatMessage;
import com.example.bidMarket.model.ChatRoom;
import com.example.bidMarket.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class ChatMapper {

    @Autowired
    protected UserMapper userMapper;

    @Mapping(target = "senderName", source = "sender.email")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "senderId", source = "sender.id")
    public abstract ChatMessageDto toDto(ChatMessage message);

    @Mapping(target = "otherUser", expression = "java(getOtherUser(room, currentUserId))")
    @Mapping(target = "lastMessage", expression = "java(getLastMessage(room))")
    public abstract ChatRoomDto toDto(ChatRoom room, UUID currentUserId);

    protected UserDto getOtherUser(ChatRoom room, UUID currentUserId) {
        User otherUser = room.getUser1().getId().equals(currentUserId)
                ? room.getUser2() : room.getUser1();
        return userMapper.userToUserDto(otherUser);
    }

    protected ChatMessageDto getLastMessage(ChatRoom room) {
        return room.getMessages().isEmpty() ? null
                : toDto(room.getMessages().get(0));
    }
}