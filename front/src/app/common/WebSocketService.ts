import { Injectable } from '@angular/core';
import { Client, over, Message } from 'stompjs';
import * as SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient: Client | null = null;

  constructor() {}

  connect(
    username: string,
    onMessage: (message: any) => void,
    onUsers: (users: string[]) => void,
    onError: (error: any) => void
  ) {
    const socket = new SockJS('http://localhost:8080/gs-guide-websocket');
    this.stompClient = over(socket);

    this.stompClient.connect(
      {},
      (frame) => {

        this.stompClient.subscribe(
          '/user/' + username + '/queue/reply',
          (message: Message) => {
            onMessage(JSON.parse(message.body));
          }
        );

        this.stompClient.subscribe(
          '/topic/users',
          (message: Message) => {
            const users = JSON.parse(message.body);
            // Nettoyer les guillemets échappés
            const cleanedUsers = users.map((user: string) => user.replace(/"/g, ''));
            onUsers(cleanedUsers.filter((user: string) => user !== username));
          }
        );

        this.stompClient.send('/app/connect', {}, JSON.stringify(username));
      },
      (error) => {
        console.error('WebSocket error: ' + error);
        onError(error);
      }
    );
  }

  sendPrivateMessage(recipient: string, message: string, sender: string) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.send(
        '/app/private-message',
        {},
        JSON.stringify({ recipient, name: message, sender })
      );
    }
  }

  disconnect(username: string) {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.send('/app/disconnect', {}, JSON.stringify(username));
      this.stompClient.disconnect(() => {
        this.stompClient = null;
      });
    }
  }
}
