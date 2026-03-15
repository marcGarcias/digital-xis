import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TextPrivacyPolicy } from './text-privacy-policy';

describe('TextPrivacyPolicy', () => {
  let component: TextPrivacyPolicy;
  let fixture: ComponentFixture<TextPrivacyPolicy>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TextPrivacyPolicy]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TextPrivacyPolicy);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
