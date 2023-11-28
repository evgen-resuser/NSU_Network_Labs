package com.evgen.dns;

import org.xbill.DNS.*;
import org.xbill.DNS.Record;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class DnsResolver {
    public static final int DOMAIN_PORT = 53;

    private final DatagramChannel channel;

    private int senderID;
    private final HashMap<Integer, Map.Entry<Integer, SelectionKey>> clientsMatch = new HashMap<>();
    public final HashMap<Integer, byte[]> senderIDMap = new HashMap<>();

    public HashMap<Integer, Map.Entry<Integer, SelectionKey>> getClientsMatch() {
        return clientsMatch;
    }

    public DnsResolver(DatagramChannel channel){
        this.channel = channel;
        this.senderID = 0;
    }

    public void resolve(byte[] address, int port, SelectionKey key){
        try {
            Record rcrd = Record.newRecord(
              Name.fromString(new String(address, StandardCharsets.UTF_8)+'.'),
                    Type.A, DClass.IN
            );
            Message msg = new Message();
            msg.addRecord(rcrd, Section.QUESTION);

            Header header = msg.getHeader();
            header.setID(senderID); //данное поле используется как уникальный идентификатор транзакции. Указывает на то, что пакет принадлежит одной и той же сессии “запросов-ответов” и занимает 16 бит.
            header.setFlag(Flags.AD); //The AD (authentic data) bit indicates in a response that the data included has been verified by the server providing it.
            header.setFlag(Flags.RD); //Если он флаг устанавливается в запросе — это значит, что клиент просит сервер не сообщать ему промежуточных ответов, а вернуть только IP-адрес.

            senderIDMap.put(senderID, address);
            clientsMatch.put(senderID, new AbstractMap.SimpleEntry<>(port, key));
            senderID++;

            channel.write(ByteBuffer.wrap(msg.toWire()));

        } catch (TextParseException exc) {
            throw new IllegalArgumentException("resolve: ", exc);
        } catch (IOException exc) {
            throw new IllegalArgumentException("channel write: ", exc);

        }
    }

}
