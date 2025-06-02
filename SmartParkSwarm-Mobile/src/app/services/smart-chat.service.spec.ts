import { TestBed } from '@angular/core/testing';

import { SmartChatService } from './smart-chat.service';

describe('SmartChatService', () => {
  let service: SmartChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SmartChatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
