import {Component, OnInit} from '@angular/core';
import {
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonButtons,
  IonTextarea,
  IonButton,
  IonFooter, IonIcon
} from '@ionic/angular/standalone';
import { ExploreContainerComponent } from '../explore-container/explore-container.component';
import { FormsModule } from '@angular/forms';
import showdown from 'showdown';
import {SmartChatService} from "../services/smart-chat.service";
import {IChatMessage} from "../models/i-chat-message";
import {NgClass, NgForOf, NgIf} from "@angular/common";
import {addIcons} from "ionicons";
import {paperPlaneOutline} from "ionicons/icons";

@Component({
  selector: 'app-tab2',
  templateUrl: 'tab2.page.html',
  styleUrls: ['tab2.page.scss'],
  imports: [
    IonButton,
    IonTextarea,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    ExploreContainerComponent,
    IonButtons,
    FormsModule,
    IonFooter,
    NgClass,
    NgForOf,
    NgIf,
    IonIcon,
  ]
})

export class Tab2Page implements OnInit {
  context: any;
  question: string = '';
  answer: string = '';
  history: IChatMessage[] = [];
  converter = new showdown.Converter();

  constructor(private smartChatService: SmartChatService) {
    addIcons({ paperPlaneOutline });
  }

  ngOnInit() {
    this.getChatContext();
  }

  async run() {
    this.answer = '<div id="think">Thinking...</div>';
    this.history.push({ role: 'user', content: this.question });

    const response = await fetch('http://20.82.91.96:11434/api/chat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        model: 'llama3',
        messages: [
          {
            role: 'system',
            content: 'You are an AI assistant integrated into a parking app called SmartParkSwarm, which manages real-time parking information across multiple store locations. Your purpose is to help users navigate parking availability, provide occupancy data, and answer questions about parking spots at different stores.\\\\nThis is the parking data context for the stores:\\\\n ' + this.context
          },
        ].concat(this.history),
        stream: true,
      }),
    });

    const reader = response.body?.getReader();
    const decoder = new TextDecoder();
    let compiledResponse = '';
    let buffer = '';

    this.answer = ''; // Start fresh for streamed content

    while (true) {
      const { done, value } = await reader!.read();
      if (done) break;

      const chunk = decoder.decode(value, { stream: true });
      buffer += chunk;

      const lines = buffer.split('\n');
      buffer = lines.pop() || '';

      for (const line of lines) {
        if (!line.trim()) continue;

        try {
          const chunkJson = JSON.parse(line);
          compiledResponse += chunkJson.message.content;
          this.answer = this.converter.makeHtml(compiledResponse); // live update
        } catch (e) {
          console.error('JSON parse error on line:', line, e);
        }
      }
    }

    this.history.push({ role: 'assistant', content: compiledResponse });

    this.answer = '';
    this.question = '';
  }

  getChatContext() {
    this.smartChatService.getChatContext().subscribe({
      next: (context: any) => {
        console.log(context);
        this.context = context.key;
      },
      error: (error: any) => {
        console.error('Error fetching chat context:', error);
      }
    });
  }
}
