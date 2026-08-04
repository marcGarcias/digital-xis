import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TitlePrivacyPolicy } from './title-privacy-policy';

describe('TitlePrivacyPolicy', () => {
  let component: TitlePrivacyPolicy;
  let fixture: ComponentFixture<TitlePrivacyPolicy>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TitlePrivacyPolicy]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TitlePrivacyPolicy);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
