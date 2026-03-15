import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MediaButton } from './media-button';

describe('MediaButton', () => {
  let component: MediaButton;
  let fixture: ComponentFixture<MediaButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MediaButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MediaButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
