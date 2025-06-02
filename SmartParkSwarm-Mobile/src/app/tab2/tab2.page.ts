import { Component } from '@angular/core';
import {IonHeader, IonToolbar, IonTitle, IonContent, IonButtons, IonTextarea, IonButton } from '@ionic/angular/standalone';
import { ExploreContainerComponent } from '../explore-container/explore-container.component';
import { FormsModule } from '@angular/forms';
import showdown from 'showdown';

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
      FormsModule 
    ]
})
export class Tab2Page {

  question: string = '';
  answer: string = '';
  converter = new showdown.Converter(); 

async run(){
  console.log("HELLO");


  const response = await fetch("http://20.82.91.96:11434/api/chat", {
  method: "POST",
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify({
    model: "llama3",
    stream: false,
    messages: [
      {
        role: "system",
        content:
          "You are an AI assistant integrated into a parking app. You help users find the best time to park based on recent occupancy data. Here is the parking occupancy data from the last week:\nMonday: 50%\nTuesday: 44%\nWednesday: 80%\nThursday: 67%\nFriday: 23%\nSaturday: 12%\nSunday: 5%"
      },
      {
        role: "user",
        content: "When should I go to park there?"
      },
      {
        role: "assistant",
        content: "Based on the data, Sunday is the best day to go since it has the lowest occupancy at only 5%."
      },
      {
        role: "user",
        content: "Which day should I avoid to go there?"
      },
      {
        role: "assistant",
        content:
          "According to the data, you may want to avoid going on Wednesday, as it has the highest occupancy rate of 80% last week. This suggests that parking spots might be scarce, making it more challenging to find a spot."
      },
      {
        role: "user",
        content:
          "I understand, but is the parking lot more occupied in the beginning of the week or in the end of the week?"
      }
    ]
  })
});

  const data = await response.json();
  console.log(data.response); // Display model's reply
}


  // async run() {
  // this.answer = '<div id="think">Thinking...</div>';
  // const response = await fetch('http://20.82.91.96:11434/api/chat', {
  //   method: 'POST',
  //   headers: {
  //     'Content-Type': 'application/json',
  //   },
  //   body: JSON.stringify({
  //     model: 'llama3',
  //     messages: [
  //       {
  //         role: 'system',
  //         content: 'You are an AI assistant integrated into a parking app...'
  //       },
  //       {
  //         role: 'user',
  //         content: this.question
  //       }
  //     ],
  //     stream: false,
  //   }),
  // });

  // const reader = response.body?.getReader();
  // const decoder = new TextDecoder();
  // let compiledResponse = '';
  // let buffer = '';

//   while (true) {
//     const { done, value } = await reader!.read();
//     if (done) break;

//     const chunk = decoder.decode(value, { stream: true });
//     buffer += chunk;

//     const lines = buffer.split('\n');
//     buffer = lines.pop() || '';

//     for (const line of lines) {
//       if (!line.trim()) continue;

//       try {
//         const chunkJson = JSON.parse(line);
//         compiledResponse += chunkJson.response;
//         this.answer = this.converter.makeHtml(compiledResponse);
//       } catch (e) {
//         console.error('JSON parse error on line:', line, e);
//       }
//     }
//   }
// }


}
