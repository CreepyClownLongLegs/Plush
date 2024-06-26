// typewriter.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TypewriterService {

  constructor() { }

  typeWriterEffect(elementId: string, text: string, speed: number, repeatInterval: number) {
    let i = 0;
    const element = document.getElementById(elementId);
    if (!element) return;

    const typeWriterInterval = setInterval(() => {
      if (i < text.length) {
        element.textContent += text.charAt(i);
        i++;
      } else {
        clearInterval(typeWriterInterval); // Clear current interval
        setTimeout(() => {
          element.textContent = ""; // Clear existing text
          this.typeWriterEffect(elementId, text, speed, repeatInterval); // Start typing again
        }, repeatInterval);
      }
    }, speed);
  }
}

