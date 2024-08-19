package com.ticket.monolithticketmonster.concert.application;

public interface IConcertEventProducer {
  void sendMessage(String topic, String msg);
}
