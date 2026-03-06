package com.jkefbq.gymentry.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class EnvelopeRedisSerializer implements RedisSerializer<@NonNull Object> {

    private record Envelope(String t, JsonNode v) {}

    private final ObjectMapper mapper;
    private final ObjectMapper envelopeMapper = new ObjectMapper();

    public EnvelopeRedisSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public byte [] serialize(Object value) throws SerializationException {
        if (value == null) return null;
        try {
            String typeName = mapper.constructType(value.getClass()).toCanonical();
            JsonNode node = mapper.valueToTree(value);
            return envelopeMapper.writeValueAsBytes(new Envelope(typeName, node));
        } catch (JsonProcessingException e) {
            throw new SerializationException("Cannot serialize", e);
        }
    }

    @Override
    public Object deserialize(byte @NonNull [] bytes ) throws SerializationException {
        try {
            Envelope env = envelopeMapper.readValue(bytes, Envelope.class);
            JavaType javaType = mapper.getTypeFactory().constructFromCanonical(env.t());
            return mapper.convertValue(env.v(), javaType);
        } catch (Exception e) {
            throw new SerializationException("Cannot deserialize", e);
        }
    }
}