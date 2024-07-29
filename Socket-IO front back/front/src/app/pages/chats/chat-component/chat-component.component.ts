import { Component, OnDestroy, OnInit } from '@angular/core';
import { WebSocketService } from 'src/app/common/WebSocketService';

@Component({
  selector: 'app-chat-component',
  templateUrl: './chat-component.component.html',
  styleUrls: ['./chat-component.component.scss']
})
export class ChatComponentComponent implements OnInit, OnDestroy {
  messages: any[] = [];
  messageToSend: string = '';
  username: string = '';
  selectedRecipient: string = '';
  isConnected: boolean = false;
  connectedUsers: string[] = [];
  currentStep: number = 1; // 1: Login, 2: User list, 3: Chat

  constructor(private webSocketService: WebSocketService) {}

  ngOnInit(): void {}

  connect() {
    if (this.username.trim() !== '') {
      this.webSocketService.connect(
        this.username,
        (msg: any) => {
          this.messages.push(msg);
        },
        (users: string[]) => {

          // Utilisation d'une boucle for pour exclure l'utilisateur connecté
          this.connectedUsers = [];
          for (const user of users) {
            if (user !== this.username) {
              this.connectedUsers.push(user);
            }
          }

          this.currentStep = 2;
        },
        (error: any) => {
        }
      );
      this.isConnected = true;
    }
  }

  selectUser(recipient: string) {
    this.selectedRecipient = recipient;
    this.currentStep = 3;
  }

  sendMessage() {
    if (this.messageToSend.trim() !== '') {
      this.webSocketService.sendPrivateMessage(this.selectedRecipient, this.messageToSend, this.username);
      this.messageToSend = '';
    }
  }

  disconnect() {
    this.webSocketService.disconnect(this.username);
    this.isConnected = false;
    this.currentStep = 1;
  }

  ngOnDestroy() {
    this.webSocketService.disconnect(this.username);
  }
}
