import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HamburguerButton } from './hamburguer-button';

describe('HamburguerButton', () => {
  let component: HamburguerButton;
  let fixture: ComponentFixture<HamburguerButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HamburguerButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HamburguerButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
